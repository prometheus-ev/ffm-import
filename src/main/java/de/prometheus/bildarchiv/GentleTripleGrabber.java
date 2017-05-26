package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

public class GentleTripleGrabber {

	private static final String exportFileName = "_extended_relationships.xml";

	/**
	 * <p>
	 * A gentle command line tool for harvesting OAI-PMH XML data provided by
	 * <a href="https://github.com/coneda/kor">ConedaKOR</a>
	 * </p>
	 * 
	 * @param args
	 *            -Xms1g -Xmx2g -Dlog4j.configurationFile=/path/to/log4j2.xml
	 *            -jar ffm.jar -d /path/to/data/folder/
	 */
	public static void main(String[] args) {

		String exportFile = DateFormatUtils.format(new Date(), "dd-MM-yyyy") + exportFileName;
		String destination;

		Options options = new Options();

		Option option = new Option("d", true, "destination folder");
		option.setRequired(false);
		options.addOption(option);
		CommandLineParser parser = new DefaultParser();

		try {

			CommandLine cmd = parser.parse(options, args);
			destination = cmd.getOptionValue("d") == null ? "/tmp" : cmd.getOptionValue("d");
			logger.info("Final export file " + exportFile + " will be saved to " + destination);

			GentleTripleGrabber gentleTripleGrabber = new GentleTripleGrabber(Endpoint.ENTITIES);
			gentleTripleGrabber.listRecords();

			gentleTripleGrabber = new GentleTripleGrabber(Endpoint.RELATIONSHIPS);
			gentleTripleGrabber.listRecords();

			GentleSegmentMerger gentleSegmentMerger = new GentleSegmentMerger(destination, exportFile);
			File importFile = gentleSegmentMerger.merge();

			GentleDataExtractor gentleDataExtractor = new GentleDataExtractor(importFile, true);
			gentleDataExtractor.getAndStoreData();

		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchEndpointException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (ResumptionTokenNullException e) {
			e.printStackTrace();
		} finally {
			// Delete temporary created files
			File tmpEnt = new File("/tmp", Endpoint.ENTITIES.name());
			File tmpRel = new File("/tmp", Endpoint.RELATIONSHIPS.name());
			tmpEnt.deleteOnExit();
			tmpRel.deleteOnExit();
		}

	}

	private static final Logger logger = LogManager.getLogger(GentleTripleGrabber.class);
	// private static String destination;
	private Endpoint endpoint;

	private GentleTripleGrabber(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	@SuppressWarnings("unchecked")
	private void listRecords() throws JAXBException, IOException, NoSuchEndpointException, InterruptedException, ResumptionTokenNullException {
		Integer globCounter = 0;
		Integer counter = 0;
		
		AtomicInteger index = new AtomicInteger();
		ExecutorService executor = Executors.newFixedThreadPool(1);
		
		@SuppressWarnings("rawtypes")
		Set set = new HashSet<>();
		
		JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(GentleUtils.getConnectionFor(endpoint.listRecords()), null);
		ResumptionTokenType resumptionToken = oai.getValue().getListRecords().getResumptionToken();
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		BigInteger completeListSize = resumptionToken.getCompleteListSize();

		logger.info("Fetching Data [completeListSize=" + completeListSize + "] from endpoint " + endpoint.name() + " [url=" + endpoint.listRecords() + "]");
		System.out.println("Fetching Data [completeListSize=" + completeListSize + "] from endpoint " + endpoint.name() + " [url=" + endpoint.listRecords() + "]");

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
						
					// logger.debug(recordType.getMetadata().getEntity().toJson());
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
					 // logger.debug(recordType.getMetadata().getRelationship().toJson());
					 System.out.println(recordType.getMetadata().getRelationship().toJson());
				}
				break;
			default:
				throw new NoSuchEndpointException("NoSuchEndpointException: [Endpoint.ENTITIES, Endpoint.RELATIONSHIPS] " + endpoint);
			}

			counter = set.size();
			
			if (counter >= 2500) {
				globCounter += set.size();
				executor.execute(writeObject(endpoint.name(), new HashSet<>(set), index.incrementAndGet()));
				set = new HashSet<>();
				// logger.debug("Fechted " + globCounter + " entries, missing " + (completeListSize.intValue() - globCounter) + " " + endpoint.name());
				// System.out.println("Fechted " + globCounter + " entries, missing " + (completeListSize.intValue() - globCounter) + " " + endpoint.name());
				counter = 0; 
			}

			if (resumptionToken.getValue() == null || resumptionToken.getValue().isEmpty())
				break;
			
			// System.out.println("Fetched Data "+ globCounter + " from [url=" + resumptionToken.getValue() + "]");
			
			HttpURLConnection connectionFor = GentleUtils.getConnectionFor(endpoint.listRecords(resumptionToken.getValue()));
			oai = GentleUtils.getElement(connectionFor, endpoint.listRecords(resumptionToken.getValue()));
			records = oai.getValue().getListRecords().getRecord();
			resumptionToken = oai.getValue().getListRecords().getResumptionToken();

			try {
				Thread.sleep(200); // the gentleness :-)
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		globCounter += set.size(); // last increment of global counter
		
		// save data chunk
		executor.execute(writeObject(endpoint.name(), new HashSet<>(set), index.incrementAndGet())); 
		
		executor.shutdown();
		// wait until files are written
		executor.awaitTermination(3000, TimeUnit.MILLISECONDS); 

		progressBar.done();
		
		boolean b = completeListSize.intValue() == globCounter;
		int missing = b ? 0 : (completeListSize.intValue() - globCounter);
		logger.info("Fetched all data? " + (b ? "ok!" : missing + " records missing!"));
		// System.out.println("Fetched all data? " + (b ? "ok!" : missing + " records missing!"));
	}

	private boolean nullRecord(RecordType recordType) {
		if (recordType == null)
			return true;
		else if (recordType.getMetadata() == null)
			return true;
		return false;
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
					// logger.debug("Temp file created " + file.getAbsolutePath() + " [" + file.length() + " bytes]");
					// System.out.println("Temp file created " + file.getAbsolutePath() + " [" + file.length() + " bytes]");
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
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
			e.printStackTrace();
		} finally {
			bis.close();
			bos.close();
		}
	}

	@SuppressWarnings("unused")
	private InputStream copy(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len;
		while ((len = inputStream.read(buffer)) > -1) {
			baos.write(buffer, 0, len);
		}
		baos.flush();
		ByteArrayInputStream copy1 = new ByteArrayInputStream(baos.toByteArray());
		ByteArrayInputStream copy2 = new ByteArrayInputStream(baos.toByteArray());
		return copy1;
	}

}
