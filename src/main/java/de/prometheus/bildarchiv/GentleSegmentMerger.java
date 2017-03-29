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
	
	private static final Logger logger = LogManager.getLogger(GentleSegmentMerger.class);
	private String exportFileName;
	private String destination;

	GentleSegmentMerger(final String destination, final String exportFileName) {
		this.destination = destination;
		this.exportFileName = exportFileName;
	}

	public File merge() {

		long time = System.currentTimeMillis();
		
		try {
			logger.info("Unmarshalling 'relationships' ...");
			File[] files = get(new File("/tmp", "RELATIONSHIPS/"), ".kor");
			Set<Relationship> relationships = new HashSet<>();
			for (File f : files) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Set<Relationship> s = (Set) readObject(f);
				relationships.addAll(s);
			}
			
			logger.info("Unmarshalling 'entities' ...");
			files = get(new File("/tmp", "ENTITIES/"), ".kor");
			Set<Entity> entities = new HashSet<>();
			for (File f : files) {
				@SuppressWarnings({ "unchecked", "rawtypes" })
				Set<Entity> s = (Set) readObject(f);
				entities.addAll(s);
			}
			
			List<Entity> sortedEntities = new ArrayList<>(entities);
			Collections.sort(sortedEntities);
			Set<String> notRetrieved = new HashSet<>();
			Set<ExtendedRelationship> asXml = new HashSet<>();
			
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

				asXml.add(rs);
				
				p.increment();
			}
			p.done();
			
			final Set<Entity> result = Collections.synchronizedSet(new HashSet<Entity>());
			if (notRetrieved.size() > 0) {
				logger.info("Count of missing records: " + notRetrieved.size());
				getMissingRecords(notRetrieved, result, asXml);
			}
			
			File desFolder = new File(destination);  
			File exportFile = new File(desFolder, exportFileName);
			
			logger.info("Creating export file " + exportFile.getAbsolutePath());
			exportXml(asXml, exportFile);
			
			long duration = System.currentTimeMillis() - time;
			logger.info("Done! ...took " + ((duration / 1000) / 60) + " min");
			
			return exportFile;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (PropertyException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
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

	private void exportXml(Set<ExtendedRelationship> asXml, File file) throws JAXBException, PropertyException {
		Prometheus prom = new Prometheus();
		prom.setRelationships(asXml);

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

		logger.info("Retrieving missing entities... this might take a moment (" + futures.size() + " threads)");
		for (Future<Set<Entity>> future : futures) {
			try {
				result.addAll(future.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
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
				JAXBElement<OAIPMHtype> e = GentleUtils.getElement(c);
				Entity entity = e.getValue().getGetRecord().getRecord().getMetadata().getEntity();
				tmp.add(entity);
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			return tmp;
		}
	}

}
