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
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.namespace.QName;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.Entity;
import org.openarchives.beans.ExtendedRelationship;
import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.Prometheus;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.Relationship;

public class GentleSegmentMerger {

	private static final Logger LOG = LogManager.getLogger(GentleSegmentMerger.class);

	private final String extendedRelsXml;
	private final String dataDirectory;
	private List<Relationship> relationshipsList;
	private List<Entity> entitiesList;

	public GentleSegmentMerger(final String dataDirectory, final String extendedRelsXml) {
		this.dataDirectory = dataDirectory;
		this.extendedRelsXml = extendedRelsXml;
	}

	public File mergeEntitiesAndRelationships() throws PropertyException, JAXBException, FileNotFoundException {

		long time = System.currentTimeMillis();

		if (LOG.isInfoEnabled()) {
			LOG.info("Unmarshalling 'relationships' and 'entities' ...");
		}

		Set<RecordType> relationships = getRecords(new File(dataDirectory, "RELATIONSHIPS/"));
		getRelationships(relationships);

		Set<RecordType> entities = getRecords(new File(dataDirectory, "ENTITIES/"));
		getEntities(entities);

		List<Entity> sortedEntities = new ArrayList<>(entitiesList);
		Collections.sort(sortedEntities);
		Set<String> notRetrieved = new HashSet<>();
		Set<ExtendedRelationship> extendedRelsSet = new HashSet<>();

		for (Relationship relationship : relationshipsList) {
			ExtendedRelationship extendedRelationship = new ExtendedRelationship(relationship);

			Entity fromEntity = find(relationship.getFrom(), sortedEntities);
			if (fromEntity != null) {
				extendedRelationship.setFrom(fromEntity);
			} else {
				notRetrieved.add(relationship.getFrom());
			}

			Entity toEntity = find(relationship.getTo(), sortedEntities);
			if (toEntity != null) {
				extendedRelationship.setTo(toEntity);
			} else {
				notRetrieved.add(relationship.getTo());
			}

			extendedRelsSet.add(extendedRelationship);

		}

		if (!notRetrieved.isEmpty()) {
			getMissingRecords(notRetrieved, extendedRelsSet);
		}

		File extendedRelsXmlFile = new File(dataDirectory, extendedRelsXml);

		if (LOG.isInfoEnabled()) {
			LOG.info("Creating export file " + extendedRelsXmlFile.getAbsolutePath());
		}

		exportXml(extendedRelsSet, new File(dataDirectory, extendedRelsXml));

		if (LOG.isInfoEnabled()) {
			long duration = System.currentTimeMillis() - time;
			LOG.info("Done! ...took " + ((duration / 1000) / 60) + " min");
		}

		return extendedRelsXmlFile;

	}

	private void getEntities(Set<RecordType> entities) {
		entitiesList = new ArrayList<>();
		for (RecordType recordType : entities) {
			if (valid(recordType)) {
				entitiesList.add(recordType.getMetadata().getEntity());
			}
		}
	}

	private void getRelationships(Set<RecordType> relationships) {
		relationshipsList = new ArrayList<>();
		for (RecordType recordType : relationships) {
			if (valid(recordType)) {
				relationshipsList.add(recordType.getMetadata().getRelationship());
			}
		}
	}

	private boolean valid(RecordType recordType) {
		return recordType != null && recordType.getHeader() != null && recordType.getMetadata() != null;
	}

	private Set<RecordType> getRecords(final File dir) throws FileNotFoundException {
		File[] files = getFiles(dir, ".kor");
		Set<RecordType> records = new HashSet<>();
		for (File file : files) {
			records.addAll(readObject(file));
		}
		return records;
	}

	@SuppressWarnings("unchecked")
	private Set<RecordType> readObject(File file) {
		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			return (Set<RecordType>) ois.readObject();
		} catch (IOException e) {
			LOG.error(e.toString());
		} catch (ClassNotFoundException e) {
			LOG.error(e.toString());
		}
		return null;
	}

	private void exportXml(Set<ExtendedRelationship> toXml, File file) throws JAXBException, PropertyException {
		Prometheus prom = new Prometheus();
		prom.setRelationships(toXml);

		JAXBContext jaxbContext = JAXBContext.newInstance(Prometheus.class);
		JAXBElement<Prometheus> element = new JAXBElement<Prometheus>(
				new QName("http://www.openarchives.org/OAI/2.0/", "ffm"), Prometheus.class, prom);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(element, file);
	}

	private void getMissingRecords(final Set<String> notRetrieved, Set<ExtendedRelationship> extendedRelsSet) {

		ExecutorService executor = Executors.newFixedThreadPool(1);
		Future<Set<Entity>> submit = executor.submit(new RequestCallable(notRetrieved));

		try {
			if (LOG.isInfoEnabled()) {
				LOG.info("Retrieving missing " + notRetrieved.size() + "entities... this might take a moment.");
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
			LOG.error(e.toString());
		}
		executor.shutdown();
	}

	private static Entity find(final String id, List<Entity> orderedList) {
		int index = Collections.binarySearch(orderedList, new Entity(id));
		if (index > 0) {
			return orderedList.get(index);
		}
		return null;
	}

	private File[] getFiles(File dir, final String suffix) throws FileNotFoundException {
		if (!dir.exists())
			throw new FileNotFoundException(dir.getAbsolutePath());
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.getName().endsWith(suffix);
			}
		});
		return files;
	}

	class RequestCallable implements Callable<Set<Entity>> {

		private Set<String> entities;

		RequestCallable(final Set<String> entities) {
			this.entities = entities;
		}

		@Override
		public Set<Entity> call() throws Exception {
			
			Set<Entity> toReturn = new HashSet<>();
			
			for (String id : entities) {
				
				String url = Endpoint.ENTITIES.getRecord(id);
				HttpURLConnection c = GentleUtils.getConnectionFor(url);
				JAXBElement<OAIPMHtype> oai = GentleUtils.getElement(c, null);
				
				if (valid(oai)) {
					Entity entity = oai.getValue().getGetRecord().getRecord().getMetadata().getEntity();
					toReturn.add(entity);
				}

				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					LOG.error(e.toString());
				}
			}

			return toReturn;
		}

		private boolean valid(JAXBElement<OAIPMHtype> oai) {
			return oai != null && oai.getValue() != null && oai.getValue().getGetRecord() != null
					&& oai.getValue().getGetRecord().getRecord() != null
					&& oai.getValue().getGetRecord().getRecord().getMetadata() != null
					&& oai.getValue().getGetRecord().getRecord().getMetadata().getEntity() != null;
		}
	}

}
