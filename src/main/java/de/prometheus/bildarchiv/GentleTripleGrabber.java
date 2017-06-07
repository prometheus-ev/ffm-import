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

	private static final int CHUNK_SIZE = 2500;

	private static Logger LOG = LogManager.getLogger(GentleTripleGrabber.class);;

	@SuppressWarnings("unchecked")
	public void listRecords(Endpoint endpoint) throws NoSuchEndpointException {

		int recordCount = 0;
		int chunkCount = 0;

		AtomicInteger index = new AtomicInteger();
		ExecutorService executor = Executors.newFixedThreadPool(1);

		@SuppressWarnings("rawtypes")
		Set set = new HashSet<>();
		String url = endpoint.listRecords();
		JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(GentleUtils.getConnectionFor(url), null);
		ResumptionTokenType resumptionToken = oai.getValue().getListRecords().getResumptionToken();
		List<RecordType> records = oai.getValue().getListRecords().getRecord();
		BigInteger completeListSize = resumptionToken.getCompleteListSize();

		if (LOG.isInfoEnabled()) {
			LOG.info("Fetching Data [completeListSize=" + completeListSize + "] from endpoint " + endpoint.name()
					+ " [url=" + endpoint.listRecords() + "]");
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
				throw new NoSuchEndpointException(
						"NoSuchEndpointException: [Endpoint.ENTITIES, Endpoint.RELATIONSHIPS] " + endpoint);
			}

			chunkCount = set.size();

			if (chunkCount >= CHUNK_SIZE) {
				recordCount += set.size();
				executor.execute(writeObject(endpoint.name(), new HashSet<>(set), index.incrementAndGet()));
				set = new HashSet<>();
			}

			if (resumptionToken.getValue() == null || resumptionToken.getValue().isEmpty()) {
				break;
			}

			url = endpoint.listRecords(resumptionToken.getValue());

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

		recordCount += set.size(); // last increment of global counter

		// save data chunk
		executor.execute(writeObject(endpoint.name(), new HashSet<>(set), index.incrementAndGet()));

		executor.shutdown();

		try {
			// wait until files are written
			executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		progressBar.done();

		if (LOG.isInfoEnabled()) {
			boolean b = completeListSize.intValue() == recordCount;
			LOG.info("Fetched all data? "
					+ (b ? "ok!" : completeListSize.intValue() - recordCount + " records missing!"));
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
					File parent = new File(new File("/tmp"), type.toLowerCase());
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

}
