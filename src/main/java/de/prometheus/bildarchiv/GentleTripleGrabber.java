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
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.bind.JAXBElement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.ResumptionTokenType;

import de.prometheus.bildarchiv.exception.NoSuchEndpointException;

public class GentleTripleGrabber {

	private static Logger LOG = LogManager.getLogger(GentleTripleGrabber.class);
	private String dataDirectory;
	
	public GentleTripleGrabber(final String dataDirectory)  {
		this.dataDirectory = dataDirectory;
	}

	public void listRecords(Endpoint endpoint) throws NoSuchEndpointException {

		ExecutorService executor = Executors.newFixedThreadPool(1);
		AtomicInteger index = new AtomicInteger();
		int recordCount = 0;
		File parent = new File(new File(dataDirectory), endpoint.name());
		parent.mkdirs();
		
		
		String listRecordsUrl = endpoint.listRecords();
		JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(GentleUtils.getConnectionFor(listRecordsUrl), null);
		ResumptionTokenType resumptionToken = oai.getValue().getListRecords().getResumptionToken();
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		final BigInteger completeListSize = resumptionToken.getCompleteListSize();

		if (LOG.isInfoEnabled()) {
			LOG.info("Fetching Data [completeListSize=" + completeListSize + "] from endpoint " + endpoint.name()
					+ " [url=" + endpoint.listRecords() + "]");
		}

		ProgressBar progressBar = new ProgressBar(Integer.valueOf(completeListSize.toString()));
		progressBar.start();

		while (resumptionToken != null) {

			if (!nullOrEmptyRecords(records)) {
				executor.execute(writeObject(parent, endpoint.name(), new HashSet<RecordType>(records), index.incrementAndGet()));
				recordCount += records.size();
				progressBar.increment();
			}

			if (resumptionToken.getValue() == null || resumptionToken.getValue().isEmpty()) {
				break;
			}
			
			listRecordsUrl = endpoint.listRecords(resumptionToken.getValue());
			HttpURLConnection connectionFor = GentleUtils.getConnectionFor(listRecordsUrl);
			oai = GentleUtils.getElement(connectionFor, endpoint.listRecords(resumptionToken.getValue()));
			resumptionToken = oai.getValue().getListRecords().getResumptionToken();
			records = oai.getValue().getListRecords().getRecord();

			try {
				Thread.sleep(200); // the gentleness :-)
			} catch (InterruptedException e) {
				LOG.error(e.toString());
			}
		}

		executor.shutdown();

		try {
			// wait until all files are written
			executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
			progressBar.done();
		} catch (InterruptedException e) {
			LOG.error(e.getLocalizedMessage());
		}

		if (LOG.isInfoEnabled()) {
			boolean b = completeListSize.intValue() == recordCount;
			LOG.info("Fetched all data? "
					+ (b ? "ok!" : completeListSize.intValue() - recordCount + " records missing!"));
		}
	}

	private boolean nullOrEmptyRecords(List<RecordType> records) {
		return records == null || records.isEmpty();
	}

	private Runnable writeObject(File parent, final String endpoint, final Set<?> data, final int index) {
		return new Runnable() {
 			@Override
			public void run() {
				try (ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(new File(parent, endpoint + "_" + index + ".kor")))) {
					oos.writeObject(data);
				} catch (IOException e) {
					LOG.error(e.toString());
				}
			}
		};
	}

	@SuppressWarnings("unused")
	private Runnable saveXml(final String url, int index, Endpoint endpoint) {
		return new Runnable() {
			@Override
			public void run() {
				try (Scanner scanner = new Scanner(new URL(url).openStream());
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(new File(new File("/tmp"), endpoint.name() + "_" + index + ".xml")))) {
					while (scanner.hasNext()) {
						writer.write(scanner.nextLine());
					}
				} catch (IOException e) {
					LOG.error(e.toString());
				}
			}
		};
	}

	@SuppressWarnings("unused")
	private void printLastRecordPage(InputStream inputStream) throws IOException {
		try (BufferedInputStream bis = new BufferedInputStream(inputStream); 
				BufferedOutputStream bos = new BufferedOutputStream(System.out)){
			int nextByte;
			while ((nextByte = bis.read()) != -1) {
				bos.write(nextByte);
			}
		} catch (IOException e) {
			LOG.error(e.toString());
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

}
