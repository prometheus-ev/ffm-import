package de.prometheus.bildarchiv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.model.RecordType;
import org.openarchives.model.ResumptionTokenType;

import de.prometheus.bildarchiv.exception.HttpRequestException;
import de.prometheus.bildarchiv.exception.HttpURLConnectionException;
import de.prometheus.bildarchiv.exception.NoSuchEndpointException;
import de.prometheus.bildarchiv.model.OAIPMHtypeWrapper;
import de.prometheus.bildarchiv.util.Endpoint;
import de.prometheus.bildarchiv.util.GentleUtils;
import de.prometheus.bildarchiv.util.ProgressBar;

public class GentleTripleGrabber {

	private Logger logger = LogManager.getLogger(GentleTripleGrabber.class);
	
	private final String timestamp;
	private final File dataDir;

	public GentleTripleGrabber(final File dataDir, final String timestamp) {
		this.dataDir = dataDir;
		this.timestamp = timestamp;
	}

	public void listRecords(Endpoint endpoint, Map<String,String> optionalArguments) throws NoSuchEndpointException, HttpURLConnectionException {
		Map<String,String> arguments = new HashMap<String,String>();
		if (optionalArguments != null) {
			arguments.putAll(optionalArguments);
		}
		
		ExecutorService executor = Executors.newFixedThreadPool(1);
		AtomicInteger index = new AtomicInteger();
		
		File recordsDir = new File(dataDir, endpoint.name() + "_" + timestamp);
		recordsDir.mkdir();

		ResumptionTokenType resumptionToken;
		String url = endpoint.getListRecordsHttpRequestURL(arguments);
		
		int recordCount = 0;
		long requestDuration = System.currentTimeMillis();
		int listSize = getListSize(url); // only estimation; total list size may change during harvesting process
		requestDuration = System.currentTimeMillis() - requestDuration;

		if (logger.isInfoEnabled()) {
			logger.info("Fetching data... endpoint=" + endpoint.name() + ", completeListSize=" + listSize
					+ ", estimatedTime~" + (requestDuration * (listSize / 50 /* records per request */) / 1000 / 60) + " min");
		}

		ProgressBar progress = new ProgressBar(listSize);
		progress.start();

		loop:do {

			try {

				HttpURLConnection connection = GentleUtils.getHttpURLConnection(url);

				OAIPMHtypeWrapper oaiWrapper = new OAIPMHtypeWrapper(GentleUtils.unmarshalOAIPMHtype(connection, url));

				List<RecordType> records = oaiWrapper.getRecords();
				if (!nullOrEmptyRecords(records)) {
					int indexPos = index.incrementAndGet();
					executor.execute(writeObject(recordsDir, endpoint, new HashSet<RecordType>(records), indexPos));
					executor.execute(writeXml(url, indexPos, endpoint, recordsDir));
					recordCount += records.size();
					progress.increment(records.size());
				}
				
				// only break after write operation
				resumptionToken = oaiWrapper.getResumptionToken();
				listSize = resumptionToken.getCompleteListSize().intValue(); // total list size may change during harvesting process
				if (resumptionToken.getValue().equals("")) { // specification conformity demands presence of resumptionToken
					break loop;
				} else {
					arguments.put("resumptionToken", resumptionToken.getValue());
					url = endpoint.getListRecordsHttpRequestURL(arguments);
				}
				try {
					Thread.sleep(300); // the gentleness :-)
				} catch (InterruptedException e) {
					ProgressBar.error();
					logger.error(e.toString());
				}

			} catch (HttpRequestException e) {
				ProgressBar.error();
				logger.error(e.toString());
				// break loop;
				try {
					Thread.sleep(5000); // some cool down time
				} catch (InterruptedException err) {
					ProgressBar.error();
					logger.error(err.toString());
				}
			}

		} while (true);

		progress.done();

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

	private int getListSize(final String url) {
		HttpURLConnection connection;
		try {
			connection = GentleUtils.getHttpURLConnection(url);
			return new OAIPMHtypeWrapper(GentleUtils.unmarshalOAIPMHtype(connection, url)).listSize();
		} catch (HttpRequestException e) {
			logger.error(e.toString());
		}
		catch (HttpURLConnectionException e){
			logger.error(e.toString());
		}

		return 0;
	}

	private boolean nullOrEmptyRecords(List<RecordType> records) {
		return records == null || records.isEmpty();
	}

	private Runnable writeObject(File parent, Endpoint endpoint, final Set<?> data, final int index) {
		return new Runnable() {
			@Override
			public void run() {
				try (ObjectOutputStream oos = new ObjectOutputStream(
						new FileOutputStream(new File(parent, endpoint.name() + "_" + index + ".kor")))) {
					oos.writeObject(data);
				} catch (IOException e) {
					logger.error(e.toString());
				}
			}
		};
	}

	private Runnable writeXml(final String url, int index, Endpoint endpoint, File parent) {
		return new Runnable() {
			@Override
			public void run() {
				try (Scanner scanner = new Scanner(new URL(url).openStream());
						BufferedWriter writer = new BufferedWriter(
								new FileWriter(new File(parent, endpoint.name() + "_" + index + ".xml")))) {
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
