package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.Entity;
import org.openarchives.beans.ExtendedRelationship;
import org.openarchives.beans.Prometheus;

import de.prometheus.bildarchiv.beans.Exhibition;
import de.prometheus.bildarchiv.beans.Institution;
import de.prometheus.bildarchiv.beans.Literature;
import de.prometheus.bildarchiv.beans.Medium;
import de.prometheus.bildarchiv.beans.Person;
import de.prometheus.bildarchiv.beans.Place;
import de.prometheus.bildarchiv.beans.PrometheusImport;
import de.prometheus.bildarchiv.beans.Work;

public class GentleDataExtractor {

	private static final Logger logger = LogManager.getLogger(GentleTripleGrabber.class);

	public static void main(String[] args)
			throws FileNotFoundException, InterruptedException, ExecutionException, JAXBException {
		
		GentleDataExtractor gentleDataExtractor = new GentleDataExtractor();
		Set<ExtendedRelationship> relationships = gentleDataExtractor.getRelationships();

		Set<ExtendedRelationship> befindetSichIn = filteredRelationships(relationships, Relations.befindetSichIn);
		Set<ExtendedRelationship> hatGeschaffen = filteredRelationships(relationships, Relations.hatGeschaffen);
		Set<ExtendedRelationship> basisdatenZumWerkAus = filteredRelationships(relationships, Relations.basisdatenZumWerkAus);
		Set<ExtendedRelationship> literaturEnthaeltBilddatei = filteredRelationships(relationships, Relations.literaturEnthaeltBilddatei);
		Set<ExtendedRelationship> geburtsortVon = filteredRelationships(relationships, Relations.geburtsortVon);
		Set<ExtendedRelationship> sterbeOrt = filteredRelationships(relationships, Relations.sterbeOrt);
		Set<ExtendedRelationship> institutionInOrt = filteredRelationships(relationships, Relations.institutionInOrt);
		Set<ExtendedRelationship> verwertungsrechtAmFoto = filteredRelationships(relationships, Relations.verwertungsrechtAmFoto);
		Set<ExtendedRelationship> stehtInVerbindungZu = filteredRelationships(relationships, Relations.stehtInVerbindungZu);
		Set<ExtendedRelationship> stelltDar = filteredRelationships(relationships, Relations.stelltDar);
		Set<ExtendedRelationship> fotografiertVon = filteredRelationships(relationships, Relations.fotografiertVon);
		Set<ExtendedRelationship> ausgestellteWerke = filteredRelationships(relationships, Relations.ausgestellteWerke);
		Set<ExtendedRelationship> kuratiertVon = filteredRelationships(relationships, Relations.kuratiertVon);
		Set<ExtendedRelationship> wurdeGezeigtIn = filteredRelationships(relationships, Relations.wurdeGezeigtIn);
		Set<ExtendedRelationship> autorInVon = filteredRelationships(relationships, Relations.autorInVon);
		Set<ExtendedRelationship> herausgeberInVon = filteredRelationships(relationships, Relations.herausgeberInVon);
		Set<ExtendedRelationship> erschienenIn = filteredRelationships(relationships, Relations.erschienenIn);
		Set<ExtendedRelationship> sammlungskatalog = filteredRelationships(relationships, Relations.sammlungskatalog);
		Set<ExtendedRelationship> bilddateiZuWerk = filteredRelationships(relationships, Relations.bilddateiZuWerk);
		Set<ExtendedRelationship> istTeilVonB = filteredRelationships(relationships, Relations.istTeilVonB);
		Set<ExtendedRelationship> schuelerInVon = filteredRelationships(relationships, Relations.schuelerInVon);
		Set<ExtendedRelationship> auftraggeberVonWerk = filteredRelationships(relationships, Relations.auftraggeberVonWerk);
		Set<ExtendedRelationship> ausstellungskatalogZu = filteredRelationships(relationships, Relations.ausstellungskatalogZu);
		
		
		final Set<Work> works = new HashSet<>();
		
		for (ExtendedRelationship mw : bilddateiZuWerk) {
			
			Entity mediumEntity = mw.getFrom();
			Entity workEntity = mw.getTo();
			
			if(workEntity == null || mediumEntity == null)
				continue;
			
			// TODO: VALIDATE NULLPOINTER_EXCEPTION
			Work workObject = new Work(workEntity.getId(), workEntity.getTitle(), workEntity.getCollection(),
					workEntity.getDistinction(), workEntity.getComment(), workEntity.getSynonym(),
					workEntity.getCreatedAt(), workEntity.getUpdatedAt());
			
			if(workEntity.getSubType() != null) {
				workObject.setSubtype(workEntity.getSubType());
			}
			
			workObject.setImage(new Medium()); // #1
			workObject.setCreators(new ArrayList<>()); // #2
			workObject.setIllustrations(new ArrayList<>()); // #3
			workObject.setExhibitions(new ArrayList<>()); // #4
			workObject.setLocatedIn(new Institution()); // #5
			workObject.setCommissioner(new Person()); // #6
			workObject.setConnectionsTo(new ArrayList<>()); // #7
			workObject.setPartsOf(new ArrayList<>()); // #8
			workObject.setPortrayal(new Person()); // #9
			
			// #1
			Medium mediumObject = getMedium(mediumEntity, geburtsortVon, sterbeOrt, institutionInOrt, verwertungsrechtAmFoto, fotografiertVon, schuelerInVon, false);
			workObject.setImage(mediumObject);
			// System.out.println("work_: " + mw.getTo().getTitle() + " (" + mediumObject.getImagePath() + ")");
			
			// #2
			List<Person> creators = new ArrayList<>();
			Set<ExtendedRelationship> personWork = hatGeschaffen.stream().filter(x -> x.getTo().getId().equals(workEntity.getId())).collect(Collectors.toSet());
			personWork.forEach(pw -> {
				Entity creatorEntity = pw.getFrom();
				Person creatorObject = getPerson(creatorEntity, geburtsortVon, sterbeOrt, schuelerInVon, false, 0);
				creators.add(creatorObject);
				// System.out.println("\tcreator_: " + creatorObject.getTitle());
			});
			workObject.setCreators(creators);
			
			// #3
			List<Literature> illustrations = new ArrayList<>();
			Set<ExtendedRelationship> literatures = basisdatenZumWerkAus.stream().filter(x -> x.getFrom().getId().equals(workEntity.getId())).collect(Collectors.toSet());
			literatures.forEach(wl -> {
				Entity literatureEntity = wl.getTo();
				Literature literatureObject = getLiterature(literatureEntity, literaturEnthaeltBilddatei, geburtsortVon, sterbeOrt, institutionInOrt,
						verwertungsrechtAmFoto, fotografiertVon, autorInVon, herausgeberInVon, erschienenIn,
						sammlungskatalog, schuelerInVon);
				illustrations.add(literatureObject);
				// System.out.println("\tliterature_: " + literatureObject.getTitle());
			});
			workObject.setIllustrations(illustrations);
			
			// #4
			List<Exhibition> exhibitions = new ArrayList<>();
			List<ExtendedRelationship> werkAusstellung = ausgestellteWerke.stream().filter(x -> x.getTo().getId().equals(workEntity.getId())).collect(Collectors.toList());
			werkAusstellung.forEach(wa -> {
				Entity exhibitionEntity  = wa.getFrom();
				Exhibition exhibitionObject = getExhibition(exhibitionEntity, literaturEnthaeltBilddatei, geburtsortVon, sterbeOrt, institutionInOrt,
						verwertungsrechtAmFoto, fotografiertVon, kuratiertVon, wurdeGezeigtIn, autorInVon,
						herausgeberInVon, erschienenIn, sammlungskatalog, schuelerInVon, ausstellungskatalogZu);
				exhibitions.add(exhibitionObject);
				// System.out.println("\texhibition_: " + exhibitionObject.getTitle());
			});
			workObject.setExhibitions(exhibitions);
			
			// (Werk) befindet sich in (Institution)
			List<ExtendedRelationship> locations = befindetSichIn.stream().filter(x -> x.getFrom().getId().equals(workEntity.getId())).collect(Collectors.toList());
			// #5
			if(locations.size() > 0) {
				Entity institutionEntity = locations.get(0).getTo();
				Institution institutionObject = getInstitution(institutionEntity, institutionInOrt);
				workObject.setLocatedIn(institutionObject);
				// System.out.println("\tinstitution_: " + institutionObject.getTitle());
			}
			// (Person) ist Auftraggeber von (Werk)
			List<ExtendedRelationship> commissioners = auftraggeberVonWerk.stream().filter(x -> x.getTo().getId().equals(workEntity.getId())).collect(Collectors.toList());
			// #6
			if(commissioners.size() > 0) {
				Entity commissionerEntity = commissioners.get(0).getFrom();
				Person commissionerObject = getPerson(commissionerEntity, geburtsortVon, sterbeOrt, schuelerInVon, false, 0);
				workObject.setCommissioner(commissionerObject);
				// System.out.println("\tcommissioner_: " + commissionerObject.getTitle());
			}
			// (Werk) steht in Verbindung zu (Werk)
			List<ExtendedRelationship> connections = stehtInVerbindungZu.stream().filter(x -> x.getTo().getId().equals(workEntity.getId())).collect(Collectors.toList());
			// #7
			if(connections.size() > 0 ) {
				List<String> relations = new ArrayList<>();
				for (ExtendedRelationship c : connections) {
					relations.add(c.getTo().getId());
				}
				workObject.setConnectionsTo(relations);
				// System.out.println("\tconnections_: " + connections.size());
			}
			// (Werk) ist Teil von (Werk)
			List<ExtendedRelationship> parts = istTeilVonB.stream().filter(x -> x.getFrom().getId().equals(workEntity.getId())).collect(Collectors.toList());
			// #8
			if(parts.size() > 0) {
				List<String> relations = new ArrayList<>();
				for (ExtendedRelationship c : parts) {
					relations.add(c.getTo().getId());
				}
				workObject.setPartsOf(relations);
				// System.out.println("\tparts_: " + parts.size());
			}
			// (Werk) stellt dar (Person)
			List<ExtendedRelationship> portrayals = stelltDar.stream().filter(x -> x.getFrom().getId().equals(workEntity.getId())).collect(Collectors.toList());
			// #9
			if(portrayals.size() > 0 ) {
				Entity portrayal = portrayals.get(0).getTo();
				Person portrayalObject = getPerson(portrayal, geburtsortVon, sterbeOrt, schuelerInVon, false, 0);
				workObject.setPortrayal(portrayalObject);
				// System.out.println("\tportrayal_: " + portrayalObject.getTitle());
			}
			
			works.add(workObject); // collect work object
			
		}
		
		PrometheusImport imports = new PrometheusImport();
		imports.setWorks(works);
		
		JAXBContext jaxbContext = JAXBContext.newInstance(PrometheusImport.class);
		JAXBElement<PrometheusImport> element = new JAXBElement<PrometheusImport>(
				new QName("http://www.prometheus-bildarchiv.de/", "ffm_import"), PrometheusImport.class, imports);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(element, new File("ffm_import.xml"));
		
	}

	// Ausstellung
	private static Exhibition getExhibition(Entity exhibitionEntity, Set<ExtendedRelationship> literaturEnthaeltBilddatei,
			Set<ExtendedRelationship> geburtsortVon, Set<ExtendedRelationship> sterbeOrt,
			Set<ExtendedRelationship> institutionInOrt, Set<ExtendedRelationship> verwertungsrechtAmFoto,
			Set<ExtendedRelationship> fotografiertVon, Set<ExtendedRelationship> kuratiertVon,
			Set<ExtendedRelationship> wurdeGezeigtIn, Set<ExtendedRelationship> autorInVon,
			Set<ExtendedRelationship> herausgeberInVon, Set<ExtendedRelationship> erschienenIn,
			Set<ExtendedRelationship> sammlungskatalog, Set<ExtendedRelationship> schuelerInVon,
			Set<ExtendedRelationship> ausstellungskatalogZu) {
		
		Exhibition exhibitionObject = new Exhibition(exhibitionEntity.getId(), exhibitionEntity.getTitle(),
				exhibitionEntity.getCollection(), exhibitionEntity.getDistinction(), exhibitionEntity.getComment(),
				exhibitionEntity.getSynonym(), exhibitionEntity.getCreatedAt(), exhibitionEntity.getUpdatedAt());
		exhibitionObject.setCurator(new Person());
		exhibitionObject.setExhibitionCatalogue(new Literature());
		exhibitionObject.setExhibitionVenue(new Place());
		
		// (Austellung) kuratiert von (Person)
		List<ExtendedRelationship> ausstellungKuratiertVon = kuratiertVon.stream().filter(x -> x.getFrom().getId().equals(exhibitionEntity.getId())).collect(Collectors.toList());
		if(ausstellungKuratiertVon.size() > 0) {
			Entity curator = ausstellungKuratiertVon.get(0).getTo();
			Person curatorObject = getPerson(curator, geburtsortVon, sterbeOrt, schuelerInVon, false, 0);
			exhibitionObject.setCurator(curatorObject);
		}
		// (Austellung) wurde gezeigt in (Ort)
		List<ExtendedRelationship> ausstellungWurdeGezeigtIn = wurdeGezeigtIn.stream().filter(x -> x.getFrom().getId().equals(exhibitionEntity.getId())).collect(Collectors.toList());
		if(ausstellungWurdeGezeigtIn.size() > 0) {
			Entity ev = ausstellungWurdeGezeigtIn.get(0).getTo();
			Place exhibitionVenueObject = new Place(ev.getId(), ev.getTitle(),
					ev.getCollection(), ev.getDistinction(), ev.getComment(), ev.getSynonym(),
					ev.getCreatedAt(), ev.getUpdatedAt());
			exhibitionObject.setExhibitionVenue(exhibitionVenueObject);;
		}
		// (Ausstellung) Ausstellungskatalog zu Ausstellung (Literatur)
		List<ExtendedRelationship> ausstellungskatalogZuLit = ausstellungskatalogZu.stream().filter(x -> x.getTo().getId().equals(exhibitionEntity.getId())).collect(Collectors.toList());
		if(ausstellungskatalogZuLit.size() > 0) {
			Entity exhibitionCatalog  = ausstellungskatalogZuLit.get(0).getFrom();
			Literature exhibitionCatalogObject = getLiterature(exhibitionCatalog, literaturEnthaeltBilddatei, geburtsortVon, sterbeOrt,
					institutionInOrt, verwertungsrechtAmFoto, fotografiertVon, autorInVon, herausgeberInVon,
					erschienenIn, sammlungskatalog, schuelerInVon);
			exhibitionObject.setExhibitionCatalogue(exhibitionCatalogObject);
		}
		return exhibitionObject;
	}
	
	// Literatur
	private static Literature getLiterature(Entity literature, Set<ExtendedRelationship> literaturEnthaeltBilddatei,
			Set<ExtendedRelationship> geburtsortVon, Set<ExtendedRelationship> sterbeOrt,
			Set<ExtendedRelationship> institutionInOrt, Set<ExtendedRelationship> verwertungsrechtAmFoto,
			Set<ExtendedRelationship> fotografiertVon, Set<ExtendedRelationship> autorInVon,
			Set<ExtendedRelationship> herausgeberInVon, Set<ExtendedRelationship> erschienenIn,
			Set<ExtendedRelationship> sammlungskatalog, Set<ExtendedRelationship> schuelerInVon) {
		
		//System.out.println("\t\tl: " + literature.getTitle()); 
		Literature literatureObject = new Literature(literature.getId(), literature.getTitle(),
				literature.getCollection(), literature.getDistinction(), literature.getComment(),
				literature.getSynonym(), literature.getCreatedAt(), literature.getUpdatedAt());
		
		literatureObject.setAuthor(new Person()); // author
		literatureObject.setPublisher(new Person()); // publisher
		literatureObject.setPublishedIn(new Place()); // published in location
		literatureObject.setCollectionCatalog(new Institution()); // collection catalog in institution
		literatureObject.setMedia(new ArrayList<>()); // related media
		
		// Person ist Autor/in von Literatur
		List<ExtendedRelationship> autorInVonLit = autorInVon.stream().filter(x -> x.getTo().getId().equals(literature.getId())).collect(Collectors.toList());
		if(autorInVonLit.size() > 0) {
			Entity author = autorInVonLit.get(0).getFrom();
			literatureObject.setAuthor(getPerson(author, geburtsortVon, sterbeOrt, schuelerInVon, false, 0));
			// System.out.println("\t\tl_: Autor/in " + author.getTitle());
		}
		// Person ist Herausgeberin/in von Literatur
		List<ExtendedRelationship> herausgeberInVonLit = herausgeberInVon.stream().filter(x -> x.getTo().getId().equals(literature.getId())).collect(Collectors.toList());
		if(herausgeberInVonLit.size() > 0) {
			Entity publisher = herausgeberInVonLit.get(0).getFrom();
			literatureObject.setPublisher(getPerson(publisher, geburtsortVon, sterbeOrt, schuelerInVon, false, 0));
			// System.out.println("\t\tl_: Herausgeber/in " + publisher.getTitle());
		}
		// Literatur erschienen in Ort
		List<ExtendedRelationship> erschienenInOrt = erschienenIn.stream().filter(x -> x.getFrom().getId().equals(literature.getId())).collect(Collectors.toList());
		if(erschienenInOrt.size() > 0)  {
			Entity pi = erschienenInOrt.get(0).getTo();
			Place publishedIn = new Place(pi.getId(), pi.getTitle(), pi.getCollection(),
					pi.getDistinction(), pi.getComment(), pi.getSynonym(), pi.getCreatedAt(), pi.getUpdatedAt());
			literatureObject.setPublishedIn(publishedIn);
			// System.out.println("\t\tl_: erschienen in " + pi.getTitle());
		}
		// Literatur Sammlungskatalog von Institution
		List<ExtendedRelationship> sammlungskatalogInst = sammlungskatalog.stream().filter(x -> x.getFrom().getId().equals(literature.getId())).collect(Collectors.toList());
		if(sammlungskatalogInst.size() > 0)  {
			Entity cc = sammlungskatalogInst.get(0).getTo();
			literatureObject.setCollectionCatalog(getInstitution(cc, institutionInOrt));
			// System.out.println("\t\tl_: Sammlungskatalog von " + cc.getTitle());
		}
		// (Literatur) Literatur enthält Bilddatei (Medium)
		Set<ExtendedRelationship> media = literaturEnthaeltBilddatei.stream().filter(x -> x.getFrom().getId().equals(literature.getId())).collect(Collectors.toSet());
		Set<String> mediums = new HashSet<>();
		for (ExtendedRelationship medium : media) {
			mediums.add(medium.getTo().getId()); // collect media
		}
		literatureObject.setMedia(new ArrayList<>(mediums)); // set media
		return literatureObject;
	}

	// Medium
	public static Medium getMedium(Entity medium, Set<ExtendedRelationship> geburtsortVon, Set<ExtendedRelationship> sterbeOrt,
			Set<ExtendedRelationship> institutionInOrt, Set<ExtendedRelationship> verwertungsrechtAmFoto,
			Set<ExtendedRelationship> fotografiertVon, Set<ExtendedRelationship> schuelerInVon, boolean p) {
		String imagePath = null;
		// TODO: java.lang.NullPointerException on getImagePath().getValue()
		// "St. Marien-Kirche" siehe: https://kor.uni-frankfurt.de/blaze#/entities/298542
		if(medium.getImagePath() == null) 
			imagePath = "unknown";
		else imagePath = medium.getImagePath().getValue();
		if(p) System.out.println("\t\t\tm:" + medium.getImagePath().getValue());
		
		Medium mediumObject = new Medium(medium.getId(), medium.getTitle(), medium.getCollection(),
				medium.getDistinction(), medium.getComment(), medium.getSynonym(), medium.getCreatedAt(), medium.getUpdatedAt(),
				imagePath); 
		mediumObject.setExploitationRight(new Institution()); // rights holder
		mediumObject.setPhotographers(new ArrayList<>()); // photographer
		// Medium Verwertungsrechte liegen bei Institution
		List<ExtendedRelationship> rechtAmFoto = verwertungsrechtAmFoto.stream().filter(x -> x.getFrom().getId().equals(medium.getId())).collect(Collectors.toList());
		if(rechtAmFoto.size() > 0) {
			Entity rights = rechtAmFoto.get(0).getTo();
			mediumObject.setExploitationRight(getInstitution(rights, institutionInOrt)); // 
			if(p) System.out.println("\t\t\tm_: Verwertungsrecht am Foto " + rights.getTitle() + ")");
		}
		// (Medium) fotografiert von (Person)
		List<Person> photographers = new ArrayList<>();
		List<ExtendedRelationship> mediumPerson = fotografiertVon.stream().filter(x -> x.getFrom().getId().equals(medium.getId())).collect(Collectors.toList());
		mediumPerson.forEach(mp -> {
			Entity photographerEntity = mp.getTo();
			Person photographerObject = getPerson(photographerEntity, geburtsortVon, sterbeOrt, schuelerInVon, false, 0);
			photographers.add(photographerObject);
		});
		mediumObject.setPhotographers(photographers); // set photographers
		
		return mediumObject;
	}

	// Institution
	private static Institution getInstitution(Entity cc, Set<ExtendedRelationship> institutionInOrt) {
		Institution institution = new Institution(cc.getId(), cc.getTitle(),
				cc.getCollection(), cc.getDistinction(), cc.getComment(), cc.getSynonym(),
				cc.getCreatedAt(), cc.getUpdatedAt());
		institution.setLocation(new Place());
		List<ExtendedRelationship> locations = institutionInOrt.stream().filter(x -> x.getFrom().getId().equals(cc.getId())).collect(Collectors.toList());
		if(locations.size() > 0) {
			Entity loc = locations.get(0).getTo();
			Place locationObject = new Place(loc.getId(), loc.getTitle(),
					loc.getCollection(), loc.getDistinction(), loc.getComment(),
					loc.getSynonym(), loc.getCreatedAt(), loc.getUpdatedAt());
			institution.setLocation(locationObject);
		}
		return institution;
	}

	// Person
	private static Person getPerson(Entity person, Set<ExtendedRelationship> geburtsortVon, Set<ExtendedRelationship> sterbeOrt, Set<ExtendedRelationship> schuelerInVon, boolean p, int depth) {
		Person personObject = new Person(person.getId(), person.getTitle(), person.getCollection(),
				person.getDistinction(), person.getComment(), person.getSynonym(), person.getCreatedAt(),
				person.getUpdatedAt());
		personObject.setTeachers(new ArrayList<>());
		personObject.setBirthPlace(new Place());
		personObject.setPlaceOfDeath(new Place());
		
		// (Ort) Geburtsort von (Person)
		List<ExtendedRelationship> geborenIn = geburtsortVon.stream().filter(x -> x.getTo().getId().equals(person.getId())).collect(Collectors.toList());
		if(geborenIn.size() > 0) {
			Entity bp = geborenIn.get(0).getFrom();
			Place birthPlace = new Place(bp.getId(), bp.getTitle(), bp.getCollection(), bp.getDistinction(), bp.getComment(), bp.getSynonym(), bp.getCreatedAt(), bp.getUpdatedAt());
			personObject.setBirthPlace(birthPlace);
		}
		// (Ort) Sterbeort von (Person)
		List<ExtendedRelationship> gestorbenIn = sterbeOrt.stream().filter(x -> x.getTo().getId().equals(person.getId())).collect(Collectors.toList());
		if(gestorbenIn.size() > 0) {
			Entity dp = gestorbenIn.get(0).getFrom();
			Place placeOfDeath = new Place(dp.getId(), dp.getTitle(), dp.getCollection(), dp.getDistinction(), dp.getComment(), dp.getSynonym(), dp.getCreatedAt(), dp.getUpdatedAt());
			personObject.setPlaceOfDeath(placeOfDeath);
		}
		
		if(depth == 1) // stop recursion
			return personObject;
		
		// (Person) Schüler/in von (Person)
		List<Person> teachers = new ArrayList<>();
		List<ExtendedRelationship> students = schuelerInVon.stream().filter(x -> x.getFrom().getId().equals(person.getId())).collect(Collectors.toList());
		for (ExtendedRelationship s : students) {
			Entity teacherEntity = s.getTo();
			Person teacherObject = getPerson(teacherEntity, geburtsortVon, sterbeOrt, schuelerInVon, false, (depth + 1));
			if(teacherObject != null)
				teachers.add(teacherObject);
			else break;
		}
		personObject.setTeachers(teachers);
		
		return personObject;
	}

	private static Set<ExtendedRelationship> filteredRelationships(Set<ExtendedRelationship> relationships,
			final String id) {
		return relationships.stream().filter(r -> r.getRelation().getId().equals(id)).collect(Collectors.toSet());
	}

	public Set<ExtendedRelationship> getRelationships()
			throws InterruptedException, ExecutionException, FileNotFoundException, JAXBException {
		Prometheus prom = load("/Users/matana/Desktop/14-03-2017_ffm_export.xml");
		return prom.getRelations();
	}

	private Prometheus load(final String xmlFile) throws JAXBException, FileNotFoundException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Prometheus.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StreamSource streamSource = new StreamSource(new BufferedInputStream(new FileInputStream(new File(xmlFile))));
		JAXBElement<Prometheus> prom = jaxbUnmarshaller.unmarshal(streamSource, Prometheus.class);
		return prom.getValue();
	}

}
