package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.ResumptionTokenType;

import de.prometheus.bildarchiv.exception.ResumptionTokenNullException;

public final class GentleTripleGrabber {

	private static final String EXPORT_FILE_NAME = "_extended_relationships.xml";
	private static final Logger LOG = LogManager.getLogger(GentleTripleGrabber.class);
	private static final int CHUNK_SIZE = 2500;
	private transient Endpoint endpoint;
	
	/**
	 * <p> A gentle command line tool for harvesting OAI-PMH XML data provided by
	 * <a href="https://github.com/coneda/kor">ConedaKOR</a></p>
	 * <code>-Xms1g -Xmx2g -Dlog4j.configurationFile=/path/to/log4j2.xml -jar ffm.jar -d /path/to/data/folder/</code>
	 * @param args
	 */
	public static void main(String[] args) {

		String exportFile = getTimeStamp() + EXPORT_FILE_NAME;
		String destination;

		Options options = new Options();
		Option option = new Option("d", "dir", true, "The destination of the '*." + EXPORT_FILE_NAME + "' file");
		option.setRequired(false);
		options.addOption(option);
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			destination = cmd.getOptionValue("d") == null ? "/tmp" : cmd.getOptionValue("d");
			
			if(LOG.isInfoEnabled()) { 
				LOG.info("Final export file " + exportFile + " will be saved to " + destination);
			}

			GentleTripleGrabber gentleTripleGrabber = new GentleTripleGrabber(Endpoint.ENTITIES);
			gentleTripleGrabber.listRecords();

			gentleTripleGrabber = new GentleTripleGrabber(Endpoint.RELATIONSHIPS);
			gentleTripleGrabber.listRecords();

			GentleSegmentMerger gentleSegmentMerger = new GentleSegmentMerger(destination, exportFile);
			File importFile = gentleSegmentMerger.merge();

			GentleDataExtractor gentleDataExtractor = new GentleDataExtractor(importFile);
			gentleDataExtractor.getAndStoreData();

		} catch (JAXBException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (NoSuchEndpointException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (InterruptedException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (ParseException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (ResumptionTokenNullException e) {
			LOG.error(e.getLocalizedMessage());
		} finally {
			// Delete temporary created files
			File tmpEnt = new File("/tmp", Endpoint.ENTITIES.name());
			File tmpRel = new File("/tmp", Endpoint.RELATIONSHIPS.name());
			tmpEnt.deleteOnExit();
			tmpRel.deleteOnExit();
		}
	}

	private GentleTripleGrabber(Endpoint endpoint) {
		this.endpoint = endpoint;
	}
	
	@SuppressWarnings("unchecked")
	private void listRecords() throws JAXBException, IOException, NoSuchEndpointException, InterruptedException, ResumptionTokenNullException {
		
		int globCounter = 0;
		int counter = 0;

		AtomicInteger index = new AtomicInteger();
		AtomicInteger xmlIndex = new AtomicInteger();
		
		String url = endpoint.listRecords();
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		//executor.execute(saveXml(url, xmlIndex.incrementAndGet())); // Save also as XML
		
		@SuppressWarnings("rawtypes")
		Set set = new HashSet<>();

		JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(GentleUtils.getConnectionFor(url), null);
		ResumptionTokenType resumptionToken = oai.getValue().getListRecords().getResumptionToken();
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		BigInteger completeListSize = resumptionToken.getCompleteListSize();

		if(LOG.isInfoEnabled()) {
			LOG.info("Fetching Data [completeListSize=" + completeListSize + "] from endpoint " + endpoint.name() + " [url=" + endpoint.listRecords() + "]");
		}
		
		ProgressBar progressBar = new ProgressBar(Integer.valueOf(completeListSize.toString()));
		progressBar.start();
		
		while (resumptionToken != null) {

			switch (endpoint) {

			case ENTITIES:
				for (RecordType recordType : records) {
					if (nullRecord(recordType))
						continue;
					else {
						set.add(recordType.getMetadata().getEntity());
						progressBar.increment();
					}
				}
				break;
			case RELATIONSHIPS:
				for (RecordType recordType : records) {
					if (nullRecord(recordType))
						continue;
					else {
						set.add(recordType.getMetadata().getRelationship());
						progressBar.increment();
					}
				}
				break;
			default:
				throw new NoSuchEndpointException("NoSuchEndpointException: [Endpoint.ENTITIES, Endpoint.RELATIONSHIPS] " + endpoint);
			}

			counter = set.size();

			if (counter >= CHUNK_SIZE) {
				globCounter += set.size();
				executor.execute(writeObject(endpoint.name(), new HashSet<>(set), index.incrementAndGet()));
				set = new HashSet<>();
				//counter = 0;
			}

			if (resumptionToken.getValue() == null || resumptionToken.getValue().isEmpty())
				break;
			
			url = endpoint.listRecords(resumptionToken.getValue());
			
			// executor.execute(saveXml(url, xmlIndex.incrementAndGet())); // Save also as XML
			
			HttpURLConnection connectionFor = GentleUtils.getConnectionFor(url);
			oai = GentleUtils.getElement(connectionFor, endpoint.listRecords(resumptionToken.getValue()));
			records = oai.getValue().getListRecords().getRecord();
			resumptionToken = oai.getValue().getListRecords().getResumptionToken();

			try {
				Thread.sleep(200); // the gentleness :-)
			} catch (InterruptedException e) {
				LOG.error(e.getLocalizedMessage());
			}
		}

		globCounter += set.size(); // last increment of global counter

		// save data chunk
		executor.execute(writeObject(endpoint.name(), new HashSet<>(set), index.incrementAndGet()));

		executor.shutdown();
		// wait until files are written
		executor.awaitTermination(10000, TimeUnit.MILLISECONDS);

		progressBar.done();

		if(LOG.isInfoEnabled()) {
			boolean b = completeListSize.intValue() == globCounter;
			LOG.info("Fetched all data? " + (b ? "ok!" : completeListSize.intValue() - globCounter + " records missing!"));
		}
	}

	private boolean nullRecord(RecordType recordType) {
		return recordType == null || recordType.getMetadata() == null;
	}

	private Runnable writeObject(final String type, final Set<?> set, final int index) {
		return new Runnable() {

			@Override
			public void run() {
				try {
					File parent = new File(new File("/tmp"), type);
					parent.mkdirs();
					File file = new File(parent, type + "_" + index + ".kor");
					ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
					oos.writeObject(set);
					oos.close();
				} catch (IOException e) {
					LOG.error(e.getLocalizedMessage());
				}
			}

		};
	}

	private Runnable saveXml(final String url, int index) {
		return new Runnable() {
			@Override
			public void run() {
				try (Scanner scanner = new Scanner(new URL(url).openStream());
						BufferedWriter writer = new BufferedWriter(new FileWriter(
								new File(new File("/tmp"), endpoint.name() + "_" + index + ".xml")))) {
					while (scanner.hasNext()) {
						writer.write(scanner.nextLine());
					}
				} catch (IOException e) {
					LOG.error(e.getLocalizedMessage());
				}
			}
		};
	}

	@SuppressWarnings("unused")
	private void printLastRecordPage(InputStream inputStream) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(inputStream);
		BufferedOutputStream bos = new BufferedOutputStream(System.out);
		try {
			int i;
			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		} finally {
			bis.close();
			bos.close();
		}
	}

	@SuppressWarnings("unused")
	private InputStream copy(InputStream inputStream) throws IOException {
		int length = 0;
		byte[] buffer = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		while (length > -1) {
			length = inputStream.read(buffer);
			baos.write(buffer, 0, length);
		}
		baos.flush();
		ByteArrayInputStream copy1 = new ByteArrayInputStream(baos.toByteArray());
		ByteArrayInputStream copy2 = new ByteArrayInputStream(baos.toByteArray());
		return copy1;
	}
	
	private static String getTimeStamp() {
		return DateFormatUtils.format(new Date(), "dd-MM-yyyy");
	}

}
