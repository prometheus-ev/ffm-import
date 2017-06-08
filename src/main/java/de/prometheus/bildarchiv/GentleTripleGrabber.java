package de.prometheus.bildarchiv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.ResumptionTokenType;

import de.prometheus.bildarchiv.exception.HttpRequestException;
import de.prometheus.bildarchiv.exception.NoSuchEndpointException;

public class GentleTripleGrabber {

	private Logger logger = LogManager.getLogger(GentleTripleGrabber.class);

	private String dataDirectory;

	public GentleTripleGrabber(final String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}

	public void listRecords(Endpoint endpoint) throws NoSuchEndpointException, HttpRequestException {

		ExecutorService executor = Executors.newFixedThreadPool(1);
		AtomicInteger index = new AtomicInteger();
		File parent = new File(new File(dataDirectory), endpoint.name());
		parent.mkdirs();

		ResumptionTokenType resumptionToken;
		String url = endpoint.listRecords(null);
		int recordCount = 0;

		long requestDuration = System.currentTimeMillis();
		int listSize = getListSize(url);
		requestDuration = System.currentTimeMillis() - requestDuration;

		if (logger.isInfoEnabled()) {
			logger.info("Fetching data... endpoint=" + endpoint.name() + ", completeListSize=" + listSize
					+ ", estimatedTime~" + (requestDuration * (listSize / 50 /* records per request */) / 1000 / 60) + " min");
		}

		ProgressBar prgress = new ProgressBar(listSize);
		prgress.start();

		do {

			HttpURLConnection connection = GentleUtils.getConnectionFor(url);
			OAIPMHtypeWrapper oaiWrapper = new OAIPMHtypeWrapper(GentleUtils.getElement(connection, url));

			resumptionToken = oaiWrapper.getResumptionToken();
			if (resumptionToken == null || resumptionToken.getValue() == null) {
				break;
			}

			List<RecordType> records = oaiWrapper.getRecords();
			if (!nullOrEmptyRecords(records)) {
				executor.execute(writeObject(parent, endpoint.name(), new HashSet<RecordType>(records),
						index.incrementAndGet()));
				recordCount += records.size();
				prgress.increment(records.size());
			}

			url = endpoint.listRecords(resumptionToken.getValue());

			try {
				Thread.sleep(300); // the gentleness :-)
			} catch (InterruptedException e) {
				logger.error(e.toString());
			}

		} while (true);

		prgress.done();

		try {
			// wait until all files are written
			executor.shutdown();
			executor.awaitTermination(10000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			logger.error(e.toString());
		}

		if (logger.isInfoEnabled()) {
			boolean fetchedAll = listSize == recordCount;
			logger.info("Fetched all data? " + (fetchedAll ? "ok!" : listSize - recordCount + " records missing!"));
		}
	}

	private int getListSize(final String url) throws HttpRequestException {
		HttpURLConnection connection = GentleUtils.getConnectionFor(url);
		return new OAIPMHtypeWrapper(GentleUtils.getElement(connection, url)).listSize();
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
					logger.error(e.toString());
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
					logger.error(e.toString());
				}
			}
		};
	}

}
