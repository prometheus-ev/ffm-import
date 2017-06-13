package de.prometheus.bildarchiv;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.model.Entity;
import org.openarchives.model.RecordType;
import org.openarchives.model.Relationship;

import de.prometheus.bildarchiv.model.ExtendedRelationship;
import de.prometheus.bildarchiv.model.OAIPMHtypeWrapper;
import de.prometheus.bildarchiv.model.RecordTypeWrapper;
import de.prometheus.bildarchiv.util.Endpoint;
import de.prometheus.bildarchiv.util.GentleUtils;
import de.prometheus.bildarchiv.util.ProgressBar;

public class GentleSegmentMerger {

	private Logger logger = LogManager.getLogger(GentleSegmentMerger.class);

	private final String dataDirectory;

	public GentleSegmentMerger(final String dataDirectory) {
		
		this.dataDirectory = dataDirectory;
	
	}
	
	public static void main(String[] args) throws PropertyException, FileNotFoundException, JAXBException {
		
		File log4jXml = new File("conf/", "log4j2.xml");
		System.setProperty("log4j.configurationFile", log4jXml.getAbsolutePath());
		
		File endpointProperties = new File("conf/", "endpoint.properties");
		Properties properties = GentleUtils.getProperties(endpointProperties);
		System.setProperty("apiKey", properties.getProperty("apiKey"));
		System.setProperty("baseUrl", properties.getProperty("baseUrl"));
		
		GentleSegmentMerger merger = new GentleSegmentMerger("/Users/matana/Documents/mars/workspace/ffm-import");
		merger.mergeEntitiesAndRelationships();
	}

	public Set<ExtendedRelationship> mergeEntitiesAndRelationships() throws PropertyException, JAXBException, FileNotFoundException {

		long time = System.currentTimeMillis();

		if (logger.isInfoEnabled()) {
			logger.info("Merging endpoints 'relationships' and 'entities' ...");
		}
		
		RecordTypeWrapper entityRecords = new RecordTypeWrapper(getRecords(new File(dataDirectory, "ENTITIES/")));
		
		Set<Entity> entities = entityRecords.getEntities();
		
		if (logger.isInfoEnabled()) {
			logger.info("... counting " + entities.size() + " entities");
		}
		
		RecordTypeWrapper relRecords = new RecordTypeWrapper(getRecords(new File(dataDirectory, "RELATIONSHIPS/")));
		
		Set<Relationship> relationships = relRecords.getRelationships();
		
		if (logger.isInfoEnabled()) {
			logger.info("... counting " + relationships.size() + " relationships");
		}

		List<Entity> sortedEntities = new ArrayList<>(entities);
		Collections.sort(sortedEntities);
		
		Set<String> notRetrieved = new HashSet<>();
		Set<ExtendedRelationship> xtendedRelationships = new HashSet<>();

		for (Relationship relationship : relationships) {
			
			ExtendedRelationship xtendedRelationship = new ExtendedRelationship(relationship);

			Entity fromEntity = find(relationship.getFrom(), sortedEntities);
			if (fromEntity != null) {
				xtendedRelationship.setFrom(fromEntity);
			} else {
				notRetrieved.add(relationship.getFrom());
			}

			Entity toEntity = find(relationship.getTo(), sortedEntities);
			
			if (toEntity != null) {
				xtendedRelationship.setTo(toEntity);
			} else {
				notRetrieved.add(relationship.getTo());
			}

			xtendedRelationships.add(xtendedRelationship);

		}

		if (!notRetrieved.isEmpty()) {
			getMissingRecords(notRetrieved, xtendedRelationships);
		}

		GentleUtils.toXml(xtendedRelationships, new File(dataDirectory));

		if (logger.isInfoEnabled()) {
			long duration = System.currentTimeMillis() - time;
			logger.info("Done! ...took " + ((duration / 1000) / 60) + " min");
		}

		return xtendedRelationships;
		
	}

	private Set<RecordType> getRecords(final File directory) throws FileNotFoundException {
		
		File[] files = getFiles(directory, ".kor");
		
		Set<RecordType> records = new HashSet<>();
		
		for (File file : files) {
			records.addAll(readObject(file));
		}
		
		return records;
	}
	
	private File[] getFiles(final File directory, final String suffix) throws FileNotFoundException {
		
		if (!directory.exists()) {
			throw new FileNotFoundException(directory.getAbsolutePath());
		}
		
		File[] files = directory.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
			
				return pathname.getName().endsWith(suffix);
			
			}
			
		});
		
		return files;
	}

	@SuppressWarnings("unchecked")
	private Set<RecordType> readObject(final File file) {
		
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			
			return (Set<RecordType>) ois.readObject();
		
		} catch (IOException e) {
			logger.error(e.toString());
		} catch (ClassNotFoundException e) {
			logger.error(e.toString());
		}
		
		return null;
	}

	private void getMissingRecords(final Set<String> notRetrieved, Set<ExtendedRelationship> extendedRelsSet) {

		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Set<Entity>> submit = executor.submit(new RequestCallable(notRetrieved));

		try {
			
			if (logger.isInfoEnabled()) {
				logger.info("Retrieving missing " + notRetrieved.size() + " entities... this might take a moment.");
			}

			List<Entity> missing = new ArrayList<>(submit.get());
			Collections.sort(missing);
			for (ExtendedRelationship extendedRelationship : extendedRelsSet) {

				Entity fromEntity = find(extendedRelationship.getFrom().getId(), missing);
				Entity toEntity = find(extendedRelationship.getTo().getId(), missing);

				extendedRelationship.setFrom(fromEntity != null ? fromEntity : extendedRelationship.getFrom());
				extendedRelationship.setTo(toEntity != null ? toEntity : extendedRelationship.getTo());
			}

		} catch (InterruptedException | ExecutionException e) {
			logger.error(e.toString());
		}
		
		executor.shutdown();
	}

	private static Entity find(final String identifier, List<Entity> orderedList) {
		
		int index = Collections.binarySearch(orderedList, new Entity(identifier));
		
		if (index >= 0) {
			return orderedList.get(index);
		}
		
		return null;
	}

	class RequestCallable implements Callable<Set<Entity>> {

		private Set<String> entities;

		RequestCallable(final Set<String> entities) {
			this.entities = entities;
		}

		@Override
		public Set<Entity> call() throws Exception {
			
			Set<Entity> toReturn = new HashSet<>();
			
			ProgressBar progress = new ProgressBar(entities.size());
			progress.start();
			
			for (String id : entities) {
				
				String url = Endpoint.ENTITIES.getRecord(id);
				HttpURLConnection connection = GentleUtils.getHttpURLConnection(url);
				OAIPMHtypeWrapper oaiWrapper = new OAIPMHtypeWrapper(GentleUtils.unmarshalOAIPMHtype(connection, url));
				
				if (oaiWrapper.validEntity()) {
					Entity entity = oaiWrapper.getEntity();
					toReturn.add(entity);
					progress.increment(1);
				}

				try {
					Thread.sleep(300); // and again... some nice gentleness to the stressed server :-D
				} catch (InterruptedException e) {
					logger.error(e.toString());
				}
			}
			
			progress.done();

			return toReturn;
		}

	}

}
