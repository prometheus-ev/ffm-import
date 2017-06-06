package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.Entity;
import org.openarchives.beans.ExtendedRelationship;
import org.openarchives.beans.Prometheus;

import de.prometheus.bildarchiv.beans.Exhibition;
import de.prometheus.bildarchiv.beans.Institution;
import de.prometheus.bildarchiv.beans.Literature;
import de.prometheus.bildarchiv.beans.Medium;
import de.prometheus.bildarchiv.beans.Part;
import de.prometheus.bildarchiv.beans.Person;
import de.prometheus.bildarchiv.beans.Place;
import de.prometheus.bildarchiv.beans.PrometheusImport;
import de.prometheus.bildarchiv.beans.Work;

public class GentleDataExtractor {

	private static final Logger LOG = LogManager.getLogger(GentleDataExtractor.class);

	private final File importFile;

	private transient Set<ExtendedRelationship> befindetSichIn;
	private transient Set<ExtendedRelationship> hatGeschaffen;
	private transient Set<ExtendedRelationship> basisdatenZumWerkAus;
	private transient Set<ExtendedRelationship> literaturEnthaeltBilddatei;
	private transient Set<ExtendedRelationship> geburtsortVon;
	private transient Set<ExtendedRelationship> sterbeOrt;
	private transient Set<ExtendedRelationship> institutionInOrt;
	private transient Set<ExtendedRelationship> verwertungsrechtAmFoto;
	private transient Set<ExtendedRelationship> stehtInVerbindungZu;
	private transient Set<ExtendedRelationship> stelltDar;
	private transient Set<ExtendedRelationship> fotografiertVon;
	private transient Set<ExtendedRelationship> ausgestellteWerke;
	private transient Set<ExtendedRelationship> kuratiertVon;
	private transient Set<ExtendedRelationship> wurdeGezeigtIn;
	private transient Set<ExtendedRelationship> autorInVon;
	private transient Set<ExtendedRelationship> herausgeberInVon;
	private transient Set<ExtendedRelationship> erschienenIn;
	private transient Set<ExtendedRelationship> sammlungskatalog;
	private transient Set<ExtendedRelationship> bilddateiZuWerk;
	private transient Set<ExtendedRelationship> istTeilVon;
	private transient Set<ExtendedRelationship> schuelerInVon;
	private transient Set<ExtendedRelationship> auftraggeberVonWerk;
	private transient Set<ExtendedRelationship> ausstellungskatalogZu;

	public GentleDataExtractor(final File importFile) {

		super();

		this.importFile = importFile;

		try {
			
			LOG.info("init-method called... filtering relationships from endpoint \"relation\" ");

			final Set<ExtendedRelationship> relationships = getRelationships(importFile);

			this.befindetSichIn = filterRelationships(relationships, Relations.BEFINDET_SICH_IN);
			this.hatGeschaffen = filterRelationships(relationships, Relations.HAT_GESCHAFFEN);
			this.basisdatenZumWerkAus = filterRelationships(relationships, Relations.BASISDATEN_ZUM_WERK_AUS);
			this.literaturEnthaeltBilddatei = filterRelationships(relationships, Relations.LITERATUR_ENTHAELT_BILDDATEI);
			this.geburtsortVon = filterRelationships(relationships, Relations.GEBURTSORT_VON);
			this.sterbeOrt = filterRelationships(relationships, Relations.STERBE_ORT);
			this.institutionInOrt = filterRelationships(relationships, Relations.INSTITUTION_IN_ORT);
			this.verwertungsrechtAmFoto = filterRelationships(relationships, Relations.VERWERTUNGSRECHT_AM_FOTO);
			this.stehtInVerbindungZu = filterRelationships(relationships, Relations.STEHT_IN_VERBINDUNG_ZU);
			this.stelltDar = filterRelationships(relationships, Relations.STELLT_DAR);
			this.fotografiertVon = filterRelationships(relationships, Relations.FOTOGRAFIERT_VON);
			this.ausgestellteWerke = filterRelationships(relationships, Relations.AUSGESTELLTE_WERKE);
			this.kuratiertVon = filterRelationships(relationships, Relations.KURATIERT_VON);
			this.wurdeGezeigtIn = filterRelationships(relationships, Relations.WURDE_GEZEIGT_IN);
			this.autorInVon = filterRelationships(relationships, Relations.AUTORIN_VON);
			this.herausgeberInVon = filterRelationships(relationships, Relations.HERAUSGEBERIN_VON);
			this.erschienenIn = filterRelationships(relationships, Relations.ERSCHIENEN_IN);
			this.sammlungskatalog = filterRelationships(relationships, Relations.SAMMLUNGSKATALOG);
			this.bilddateiZuWerk = filterRelationships(relationships, Relations.BILDDATEI_ZU_WERK);
			this.istTeilVon = filterRelationships(relationships, Relations.IST_TEIL_VON);
			this.schuelerInVon = filterRelationships(relationships, Relations.SCHUELERIN_VON);
			this.auftraggeberVonWerk = filterRelationships(relationships, Relations.AUFTRAGGEBER_VON_WERK);
			this.ausstellungskatalogZu = filterRelationships(relationships, Relations.AUSSTELLUNGSKATALOG_ZU);

			LOG.info("init-procedure done!");

		} catch (FileNotFoundException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (InterruptedException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (ExecutionException e) {
			LOG.error(e.getLocalizedMessage());
		} catch (JAXBException e) {
			LOG.error(e.getLocalizedMessage());
		}

	}

	/**
	 * 
	 * @throws JAXBException
	 */
	public void extractData() throws JAXBException {

		final Set<Work> works = new HashSet<>();

		for (ExtendedRelationship mw : bilddateiZuWerk) {

			final Entity mediumEntity = mw.getFrom();
			final Entity workEntity = mw.getTo();

			if (workEntity == null || mediumEntity == null) {
				continue;
			}

			Work workObject = new Work(workEntity);

			if (workEntity.getSubType() != null) {
				workObject.setSubtype(workEntity.getSubType());
			}

			// #1
			List<Medium> mediumObjects = getMediums(workEntity.getId());
			workObject.setMediums(mediumObjects);

			// #2
			Set<ExtendedRelationship> personWorkRel = filterSet(hatGeschaffen, x -> x.getTo().getId().equals(workEntity.getId()));
			List<Person> creators = getCreators(personWorkRel);
			workObject.setCreators(creators);

			// #3
			Set<ExtendedRelationship> workLitRel = filterSet(basisdatenZumWerkAus, x -> x.getFrom().getId().equals(workEntity.getId()));
			List<Literature> illustrations = getLiterature(workLitRel);
			workObject.setIllustrations(illustrations);

			// #4
			List<ExtendedRelationship> werkAusRel = filterList(ausgestellteWerke, x -> x.getTo().getId().equals(workEntity.getId()));
			List<Exhibition> exhibitions = getExhibitions(werkAusRel);
			workObject.setExhibitions(exhibitions);

			// #5 (Werk) befindet sich in (Institution)
			List<ExtendedRelationship> workInstRel = filterList(befindetSichIn, x -> x.getFrom().getId().equals(workEntity.getId()));
			if (!workInstRel.isEmpty()) {
				Entity institutionEntity = workInstRel.get(0).getTo();
				Institution institutionObject = getInstitution(institutionEntity);
				workObject.setLocatedIn(institutionObject);
			}

			// #6 (Person) ist Auftraggeber von (Werk)
			List<ExtendedRelationship> comWorkRel = filterList(auftraggeberVonWerk, x -> x.getTo().getId().equals(workEntity.getId()));
			if (!comWorkRel.isEmpty()) {
				Person commissionerObject = getPerson(comWorkRel.get(0).getFrom(), 0);
				workObject.setCommissioner(commissionerObject);
			}

			// #7 (Werk) steht in Verbindung zu (Werk)
			List<ExtendedRelationship> workConRel = filterList(stehtInVerbindungZu, x -> x.getTo().getId().equals(workEntity.getId()));
			List<String> connections = getConnections(workConRel);
			workObject.setConnectionsTo(connections);

			// #8 (Werk) ist Teil von (Werk)
			List<ExtendedRelationship> workPartRel = filterList(istTeilVon, x -> x.getFrom().getId().equals(workEntity.getId()));
			List<Part> relations = getWorkRelations(workPartRel);
			workObject.setParts(relations);

			// #9 (Werk) stellt dar (Person)
			List<ExtendedRelationship> workPerRel = filterList(stelltDar, x -> x.getFrom().getId().equals(workEntity.getId()));
			List<Person> portrayals = getPortrayals(workPerRel);
			if (!portrayals.isEmpty()) {
				workObject.setPortrayal(portrayals.get(0));
			}

			// collect work object
			works.add(workObject);

			if (LOG.isInfoEnabled()) {
				LOG.info("added new work object '" + workObject.getTitle() + "' left "
						+ (bilddateiZuWerk.size() - works.size()));
			}

		}

		export(works);
	}

	private List<Person> getPortrayals(final List<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getTo).map(p -> getPerson(p, 0))
				.collect(Collectors.toList());
	}

	private List<Part> getWorkRelations(final List<ExtendedRelationship> relations) {
		final List<Part> toReturn = new ArrayList<>();
		relations.stream().map(ExtendedRelationship::getTo).map(e -> new Part(e)).forEach(p -> {
			// Add creators names
			Set<ExtendedRelationship> partOf = filterSet(hatGeschaffen, x -> x.getTo().getId().equals(p.getId()));
			List<String> creatorNames = partOf.stream().map(ExtendedRelationship::getFrom).map(e -> getPerson(e, 0))
					.map(Person::getTitle).collect(Collectors.toList());
			p.setCreators(creatorNames);

			// Add location names
			List<ExtendedRelationship> locRels = filterList(befindetSichIn, x -> x.getFrom().getId().equals(p.getId()));
			Set<String> locs = locRels.stream().map(ExtendedRelationship::getTo).map(l -> getInstitution(l))
					.map(Institution::getTitle).collect(Collectors.toSet());
			p.setLocation(locs);
			toReturn.add(p);
		});
		return toReturn;
	}

	private List<String> getConnections(final List<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getTo).map(Entity::getId).collect(Collectors.toList());
	}

	private List<Exhibition> getExhibitions(final List<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getFrom).map(e -> getExhibition(e))
				.collect(Collectors.toList());
	}

	private List<Literature> getLiterature(final Set<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getTo).map(l -> getLiterature(l))
				.collect(Collectors.toList());
	}

	private List<Person> getCreators(final Set<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getFrom).map(c -> getPerson(c, 0))
				.collect(Collectors.toList());
	}

	private void export(final Set<Work> works) throws JAXBException, PropertyException {
		PrometheusImport imports = new PrometheusImport();
		imports.setWorks(works);

		if (LOG.isInfoEnabled()) {
			LOG.info("creating file *_conedakor_dump.xml");
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(PrometheusImport.class);
		JAXBElement<PrometheusImport> element = new JAXBElement<PrometheusImport>(
				new QName("http://www.prometheus-bildarchiv.de/", "conedaKorData"), PrometheusImport.class, imports);

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(element, new File(importFile.getParent(),
				DateFormatUtils.format(new Date(), "dd-MM-yyyy") + "_conedakor_dump.xml"));

		if (LOG.isInfoEnabled()) {
			LOG.info("data extraction finished!");
		}
	}

	private Set<ExtendedRelationship> getRelationships(final File importFile) throws InterruptedException, ExecutionException, FileNotFoundException, JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(Prometheus.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StreamSource streamSource = new StreamSource(
				new BufferedInputStream(new FileInputStream(new File(importFile.getAbsolutePath()))));
		JAXBElement<Prometheus> prom = jaxbUnmarshaller.unmarshal(streamSource, Prometheus.class);
		Prometheus prometheus = prom.getValue();
		return prometheus.getRelations();
	}

	private List<ExtendedRelationship> filterList(final Set<ExtendedRelationship> relation, Predicate<ExtendedRelationship> predicte) {
		return relation.stream().filter(predicte).collect(Collectors.toList());
	}

	private Set<ExtendedRelationship> filterSet(final Set<ExtendedRelationship> relation,
			Predicate<ExtendedRelationship> predicte) {
		return relation.stream().filter(predicte).collect(Collectors.toSet());
	}

	private Set<ExtendedRelationship> filterRelationships(final Set<ExtendedRelationship> relationships, final String id) {
		return relationships.stream().filter(r -> r.getRelation().getId().equals(id)).collect(Collectors.toSet());
	}

	/**
	 * Ausstellung
	 * 
	 * @param entity
	 * @return
	 */
	private Exhibition getExhibition(final Entity entity) {

		Exhibition exhibitionObject = new Exhibition(entity);
		// TODO: Avoid instantiating empty objects
		// exhibitionObject.setCurator(new Person());
		// exhibitionObject.setExhibitionCatalogue(new Literature());
		// exhibitionObject.setExhibitionVenue(new Place());

		// (Austellung) kuratiert von (Person)
		List<ExtendedRelationship> ausstellungKuratiertVon = filterList(kuratiertVon, x -> x.getFrom().getId().equals(entity.getId()));
		if (!ausstellungKuratiertVon.isEmpty()) {
			Entity curator = ausstellungKuratiertVon.get(0).getTo();
			Person curatorObject = getPerson(curator, 0);
			exhibitionObject.setCurator(curatorObject);
		}
		// (Austellung) wurde gezeigt in (Ort)
		List<ExtendedRelationship> gezeigtIn = filterList(wurdeGezeigtIn, x -> x.getFrom().getId().equals(entity.getId()));
		if (!gezeigtIn.isEmpty()) {
			Place exhibitionVenue = new Place(gezeigtIn.get(0).getTo());
			exhibitionObject.setExhibitionVenue(exhibitionVenue);
		}
		// (Ausstellung) Ausstellungskatalog zu Ausstellung (Literatur)
		List<ExtendedRelationship> ausstellungskatalogZuLit = filterList(ausstellungskatalogZu, x -> x.getTo().getId().equals(entity.getId()));
		if (!ausstellungskatalogZuLit.isEmpty()) {
			Entity exhibitionCatalog = ausstellungskatalogZuLit.get(0).getFrom();
			Literature exhibitionCatalogObject = getLiterature(exhibitionCatalog);
			exhibitionObject.setExhibitionCatalogue(exhibitionCatalogObject);
		}

		return exhibitionObject;
	}

	/**
	 * Literatur...
	 * 
	 * @param entity
	 * @return
	 */
	private Literature getLiterature(final Entity entity) {

		Literature literatureObject = new Literature(entity);
		// TODO: Avoid instantiating empty objects
		// literatureObject.setAuthor(new Person());
		// literatureObject.setPublisher(new Person());
		// literatureObject.setPublishedIn(new Place());
		// literatureObject.setCollectionCatalog(new Institution());
		// literatureObject.setMedia(new ArrayList<>());

		// Person ist Autor/in von Literatur
		List<ExtendedRelationship> autorInVonLit = filterList(autorInVon, x -> x.getTo().getId().equals(entity.getId()));
		if (!autorInVonLit.isEmpty()) {
			Entity author = autorInVonLit.get(0).getFrom();
			literatureObject.setAuthor(getPerson(author, 0));
		}

		// Person ist Herausgeberin/in von Literatur
		List<ExtendedRelationship> herausgeberInVonLit = filterList(herausgeberInVon, x -> x.getTo().getId().equals(entity.getId()));
		if (!herausgeberInVonLit.isEmpty()) {
			Entity publisher = herausgeberInVonLit.get(0).getFrom();
			literatureObject.setPublisher(getPerson(publisher, 0));
		}

		// Literatur erschienen in Ort
		List<ExtendedRelationship> erschienenInOrt = filterList(erschienenIn, x -> x.getFrom().getId().equals(entity.getId()));
		if (!erschienenInOrt.isEmpty()) {
			Place publishedIn = new Place(erschienenInOrt.get(0).getTo());
			literatureObject.setPublishedIn(publishedIn);
		}

		// Literatur Sammlungskatalog von Institution
		List<ExtendedRelationship> sammlungskatalogInst = filterList(sammlungskatalog, x -> x.getFrom().getId().equals(entity.getId()));
		if (!sammlungskatalogInst.isEmpty()) {
			literatureObject.setCollectionCatalog(getInstitution(sammlungskatalogInst.get(0).getTo()));
		}

		// (Literatur) Literatur enthält Bilddatei (Medium)
		Set<ExtendedRelationship> media = filterSet(literaturEnthaeltBilddatei, x -> x.getFrom().getId().equals(entity.getId()));
		List<String> mediums = media.stream().map(ExtendedRelationship::getTo).map(Entity::getId)
				.collect(Collectors.toList());

		literatureObject.setMedia(mediums); // set media

		return literatureObject;
	}

	/**
	 * Mediums...
	 * 
	 * @param workId
	 * @return List
	 */
	public List<Medium> getMediums(final String workId) {

		Set<ExtendedRelationship> mediumSet = filterSet(bilddateiZuWerk, x -> x.getTo().getId().equals(workId));
		List<Medium> mediums = new ArrayList<>();

		for (ExtendedRelationship relShip : mediumSet) {

			if (relShip.getFrom().getImagePath() == null) {
				continue;
			}

			Entity medium = relShip.getFrom();
			
			Medium mediumObject = new Medium(medium);
			// TODO: Avoid instantiating empty objects
			// mediumObject.setExploitationRight(new Institution());
			// mediumObject.setPhotographers(new ArrayList<>());
			List<ExtendedRelationship> rechtAmFoto = filterList(verwertungsrechtAmFoto, x -> x.getFrom().getId().equals(medium.getId()));
			if (!rechtAmFoto.isEmpty()) {
				Entity rights = rechtAmFoto.get(0).getTo();
				mediumObject.setExploitationRight(getInstitution(rights));
			}
			List<Person> photographers = new ArrayList<>();
			List<ExtendedRelationship> mediumPerson = filterList(fotografiertVon, x -> x.getFrom().getId().equals(medium.getId()));
			mediumPerson.forEach(mp -> {
				Person photographerObject = getPerson(mp.getTo(), 0);
				photographers.add(photographerObject);
			});
			
			mediumObject.setPhotographers(photographers);
			mediums.add(mediumObject);
		}

		return mediums;
	}

	/**
	 * Institution
	 * 
	 * @param entity
	 * @param institutionInOrt
	 * @return
	 */
	private Institution getInstitution(final Entity entity) {
		Institution institution = new Institution(entity);
//		institution.setLocation(new Place());
		List<ExtendedRelationship> locations = filterList(institutionInOrt, x -> x.getFrom().getId().equals(entity.getId()));
		List<Entity> locs = locations.stream().map(ExtendedRelationship::getTo).collect(Collectors.toList());
		if (!locs.isEmpty()) {
			institution.setLocation(new Place(locs.get(0)));
		}
		return institution;
	}

	/**
	 * Person
	 * 
	 * @param entity
	 * @param depth
	 * @return
	 */
	private Person getPerson(final Entity entity, int depth) {
		Person personObject = new Person(entity);
		personObject.setTeachers(new ArrayList<>());
		personObject.setBirthPlace(new Place());
		personObject.setPlaceOfDeath(new Place());

		// (Ort) Geburtsort von (Person)
		List<ExtendedRelationship> geborenIn = filterList(geburtsortVon, x -> x.getTo().getId().equals(entity.getId()));
		if (!geborenIn.isEmpty()) {
			Place birthPlace = new Place(geborenIn.get(0).getFrom());
			personObject.setBirthPlace(birthPlace);
		}
		// (Ort) Sterbeort von (Person)
		List<ExtendedRelationship> gestorbenIn = filterList(sterbeOrt, x -> x.getTo().getId().equals(entity.getId()));
		if (!gestorbenIn.isEmpty()) {
			Place placeOfDeath = new Place(gestorbenIn.get(0).getFrom());
			personObject.setPlaceOfDeath(placeOfDeath);
		}

		if (depth == 1) // stop recursion
			return personObject;

		// (Person) Schüler/in von (Person)
		List<Person> teachers = new ArrayList<>();
		List<ExtendedRelationship> students = filterList(schuelerInVon, x -> x.getFrom().getId().equals(entity.getId()));
		for (ExtendedRelationship s : students) {
			Entity teacherEntity = s.getTo();
			Person teacherObject = getPerson(teacherEntity, (depth + 1));
			if (teacherObject != null)
				teachers.add(teacherObject);
		}

		personObject.setTeachers(teachers);

		return personObject;
	}

}
