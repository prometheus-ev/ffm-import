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
import org.openarchives.beans.Relationship;

public class GentleSegmentMerger {
	
	private static final Logger LOG = LogManager.getLogger(GentleSegmentMerger.class);
	private final String exportFileName;
	private final String destination;

	GentleSegmentMerger(final String destination, final String exportFileName) {
		this.destination = destination;
		this.exportFileName = exportFileName;
	}
	
	/**
	 * 
	 * @return
	 */
	public File merge() {

		long time = System.currentTimeMillis();
		
		try {
			
			if(LOG.isInfoEnabled()) {
				LOG.info("Unmarshalling 'relationships' and 'entities' ...");
			}
			
			File tmpDir = new File("/tmp");
			tmpDir.mkdirs();
			
			File[] files = get(new File(tmpDir, "RELATIONSHIPS/"), ".kor");
			Set<Relationship> relationships = new HashSet<>();
			for (File f : files) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Set<Relationship> s = (Set) readObject(f);
				relationships.addAll(s);
			}
			
			files = get(new File(tmpDir, "ENTITIES/"), ".kor");
			Set<Entity> entities = new HashSet<>();
			for (File f : files) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Set<Entity> s = (Set) readObject(f);
				entities.addAll(s);
			}
			
			List<Entity> sortedEntities = new ArrayList<>(entities);
			Collections.sort(sortedEntities);
			Set<String> notRetrieved = new HashSet<>();
			Set<ExtendedRelationship> toXml = new HashSet<>();
			
			ProgressBar p = new ProgressBar(relationships.size());
			p.start();
			
			for (Relationship r : relationships) {
				ExtendedRelationship rs = new ExtendedRelationship(r);

				Entity e = find(r.getFrom(), sortedEntities);
				if (e != null) rs.setFrom(e);
				else notRetrieved.add(r.getFrom());

				e = find(r.getTo(), sortedEntities);
				if (e != null) rs.setTo(e);
				else notRetrieved.add(r.getTo());

				toXml.add(rs);
				
				p.increment();
			}
			p.done();
			
			final Set<Entity> result = Collections.synchronizedSet(new HashSet<Entity>());
			
			if (notRetrieved.size() > 0) {
				if(LOG.isInfoEnabled()) {
					LOG.info("Count of missing records: " + notRetrieved.size());
				}
				getMissingRecords(notRetrieved, result, toXml);
			}
			
			File desFolder = new File(destination);  
			File exportFile = new File(desFolder, exportFileName);
			
			if(LOG.isInfoEnabled()) {
				LOG.info("Creating export file " + exportFile.getAbsolutePath());
			}
				
			exportXml(toXml, exportFile);
			
			if(LOG.isInfoEnabled()) {
				long duration = System.currentTimeMillis() - time;
				LOG.info("Done! ...took " + ((duration / 1000) / 60) + " min");
			}
			
			
			return exportFile;
			
		} catch (FileNotFoundException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (PropertyException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (JAXBException e) {
			LOG.error(e.getLocalizedMessage());
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	private static Set<Object> readObject(File file) throws ClassNotFoundException, IOException {
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
		Set<Object> object = (Set<Object>) ois.readObject();
		ois.close();
		return object;
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

	private void getMissingRecords(final Set<String> notRetrieved, Set<Entity> result,
			Set<ExtendedRelationship> asXml) {

		// chunk missing record items to 2500 blocks
		List<Set<String>> sets = new ArrayList<>();
		Set<String> set = new HashSet<>();
		List<String> tmp = new ArrayList<>(notRetrieved);
		for (int i = 0, n = 0; i < tmp.size(); i++) {
			if (n == 2500) {
				sets.add(set);
				set = new HashSet<>();
				n = 0;
			}
			set.add(tmp.get(i));
			n++;
		}
		sets.add(set);

		ExecutorService executor = Executors.newFixedThreadPool(sets.size());
		List<Future<Set<Entity>>> futures = new ArrayList<>();

		
		for (Set<String> s : sets) {
			Future<Set<Entity>> future = executor.submit(new RequestCallable(new HashSet<>(s)));
			futures.add(future);
		}
		
		if(LOG.isInfoEnabled()) { 
			LOG.info("Retrieving missing entities... this might take a moment (" + futures.size() + " threads)");
		}
		
		for (Future<Set<Entity>> future : futures) {
			try {
				result.addAll(future.get());
			} catch (InterruptedException | ExecutionException e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
		executor.shutdown();

		// add missing records to xml export
		List<Entity> sortedEntities = new ArrayList<>(result);
		Collections.sort(sortedEntities);
		for (ExtendedRelationship rs : asXml) {
			Entity from = find(rs.getFrom().getId(), sortedEntities);
			Entity to = find(rs.getTo().getId(), sortedEntities);
			rs.setFrom(from != null ? from : rs.getFrom());
			rs.setTo(to != null ? to : rs.getTo());
		}
	}

	private static Entity find(final String id, List<Entity> list) {
		int toIndex = Collections.binarySearch(list, new Entity(id));
		if (toIndex > 0) {
			Entity e = list.get(toIndex);
			return e;
		}
		return null;
	}

	private static File[] get(File dir, final String suffix) throws FileNotFoundException {
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

		private Set<String> ids;

		RequestCallable(final Set<String> ids) {
			this.ids = ids;
		}

		@Override
		public Set<Entity> call() throws Exception {
			Set<Entity> tmp = new HashSet<>();
			for (String id : ids) {
				String url = Endpoint.ENTITIES.getRecord(id);
				HttpURLConnection c = GentleUtils.getConnectionFor(url);
				JAXBElement<OAIPMHtype> element = GentleUtils.getElement(c, null);
				Entity entity = element.getValue().getGetRecord().getRecord().getMetadata().getEntity();
				tmp.add(entity);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					LOG.error(e.getLocalizedMessage());
				}
			}

			return tmp;
		}
	}

}
