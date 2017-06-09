package de.prometheus.bildarchiv;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.model.Entity;

import de.prometheus.bildarchiv.model.Exhibition;
import de.prometheus.bildarchiv.model.ExtendedRelationship;
import de.prometheus.bildarchiv.model.Institution;
import de.prometheus.bildarchiv.model.Literature;
import de.prometheus.bildarchiv.model.Medium;
import de.prometheus.bildarchiv.model.Part;
import de.prometheus.bildarchiv.model.Person;
import de.prometheus.bildarchiv.model.Place;
import de.prometheus.bildarchiv.model.Work;
import de.prometheus.bildarchiv.util.GentleUtils;
import de.prometheus.bildarchiv.util.Relations;

/**
 * This marvelous class is used to extract the information described in <a href=
 * "https://github.com/matana/ffm-import/blob/master/20170511_154237.jpg">the
 * truth</a>
 * 
 * @author matana
 *
 */
public class GentleDataExtractor {

	private Logger logger = LogManager.getLogger(GentleDataExtractor.class);

	private File extendedRelsFile;

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

	public GentleDataExtractor(Set<ExtendedRelationship> relationships) {

		init(relationships);

	}

	private void init(Set<ExtendedRelationship> relationships) {

		this.bilddateiZuWerk = filterRelationships(relationships, Relations.BILDDATEI_ZU_WERK);
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
		this.istTeilVon = filterRelationships(relationships, Relations.IST_TEIL_VON);
		this.schuelerInVon = filterRelationships(relationships, Relations.SCHUELERIN_VON);
		this.auftraggeberVonWerk = filterRelationships(relationships, Relations.AUFTRAGGEBER_VON_WERK);
		this.ausstellungskatalogZu = filterRelationships(relationships, Relations.AUSSTELLUNGSKATALOG_ZU);

		if (logger.isInfoEnabled()) {

			logger.info("bilddateiZuWerk :: " + bilddateiZuWerk.size());
			logger.info("Filtering relationships done!");
			logger.info("befindetSichIn :: " + befindetSichIn.size());
			logger.info("hatGeschaffen :: " + hatGeschaffen.size());
			logger.info("basisdatenZumWerkAus :: " + basisdatenZumWerkAus.size());
			logger.info("literaturEnthaeltBilddatei :: " + literaturEnthaeltBilddatei.size());
			logger.info("geburtsortVon :: " + geburtsortVon.size());
			logger.info("sterbeOrt :: " + sterbeOrt.size());
			logger.info("institutionInOrt :: " + institutionInOrt.size());
			logger.info("verwertungsrechtAmFoto :: " + verwertungsrechtAmFoto.size());
			logger.info("stehtInVerbindungZu :: " + stehtInVerbindungZu.size());
			logger.info("stelltDar :: " + stelltDar.size());
			logger.info("fotografiertVon :: " + fotografiertVon.size());
			logger.info("ausgestellteWerke :: " + ausgestellteWerke.size());
			logger.info("kuratiertVon :: " + kuratiertVon.size());
			logger.info("wurdeGezeigtIn :: " + wurdeGezeigtIn.size());
			logger.info("autorInVon :: " + autorInVon.size());
			logger.info("herausgeberInVon :: " + herausgeberInVon.size());
			logger.info("erschienenIn :: " + erschienenIn.size());
			logger.info("sammlungskatalog :: " + sammlungskatalog.size());
			logger.info("istTeilVon :: " + istTeilVon.size());
			logger.info("schuelerInVon :: " + schuelerInVon.size());
			logger.info("auftraggeberVonWerk :: " + auftraggeberVonWerk.size());
			logger.info("ausstellungskatalogZu :: " + ausstellungskatalogZu.size());

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
			List<Medium> mediumObjects = getMediumBranch(workEntity.getId());
			workObject.setMediums(mediumObjects);

			// #2
			Set<ExtendedRelationship> personWorkRel = filterSet(hatGeschaffen,
					x -> x.getTo().getId().equals(workEntity.getId()));
			List<Person> creators = getCreators(personWorkRel);
			workObject.setCreators(creators);

			// #3
			Set<ExtendedRelationship> workLitRel = filterSet(basisdatenZumWerkAus,
					x -> x.getFrom().getId().equals(workEntity.getId()));
			List<Literature> illustrations = getLiterature(workLitRel);
			workObject.setIllustrations(illustrations);

			// #4
			List<ExtendedRelationship> werkAusRel = filterList(ausgestellteWerke,
					x -> x.getTo().getId().equals(workEntity.getId()));
			List<Exhibition> exhibitions = getExhibitions(werkAusRel);
			workObject.setExhibitions(exhibitions);

			// #5 (Werk) befindet sich in (Institution)
			List<ExtendedRelationship> workInstRel = filterList(befindetSichIn,
					x -> x.getFrom().getId().equals(workEntity.getId()));
			if (!workInstRel.isEmpty()) {
				Entity institutionEntity = workInstRel.get(0).getTo();
				Institution institutionObject = getInstitutionBranch(institutionEntity);
				workObject.setLocatedIn(institutionObject);
			}

			// #6 (Person) ist Auftraggeber von (Werk)
			List<ExtendedRelationship> comWorkRel = filterList(auftraggeberVonWerk,
					x -> x.getTo().getId().equals(workEntity.getId()));
			if (!comWorkRel.isEmpty()) {
				Person commissionerObject = getPersonBranch(comWorkRel.get(0).getFrom(), 0);
				workObject.setCommissioner(commissionerObject);
			}

			// #7 (Werk) steht in Verbindung zu (Werk)
			List<ExtendedRelationship> workConRel = filterList(stehtInVerbindungZu,
					x -> x.getTo().getId().equals(workEntity.getId()));
			List<String> connections = getConnections(workConRel);
			workObject.setConnectionsTo(connections);

			// #8 (Werk) ist Teil von (Werk)
			List<ExtendedRelationship> workPartRel = filterList(istTeilVon,
					x -> x.getFrom().getId().equals(workEntity.getId()));
			List<Part> relations = getWorkRelations(workPartRel);
			workObject.setParts(relations);

			// #9 (Werk) stellt dar (Person)
			List<ExtendedRelationship> workPerRel = filterList(stelltDar,
					x -> x.getFrom().getId().equals(workEntity.getId()));
			List<Person> portrayals = getPortrayals(workPerRel);
			if (!portrayals.isEmpty()) {
				workObject.setPortrayal(portrayals.get(0));
			}

			// collect work object
			works.add(workObject);

			if (logger.isInfoEnabled()) {
				logger.info("Added new work object '" + workObject.getTitle());
			}

		}

		GentleUtils.finalExport(works, extendedRelsFile.getParentFile());
	}

	private List<Person> getPortrayals(final List<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getTo).map(p -> getPersonBranch(p, 0))
				.collect(Collectors.toList());
	}

	private List<Part> getWorkRelations(final List<ExtendedRelationship> relations) {
		// TODO: wip simplifying filter routines...
		
		
		final List<Part> toReturn = new ArrayList<>();
		
//		Stream<Part> map = relations.stream().map(ExtendedRelationship::getTo).map(e -> new Part(e));
		
		relations.stream().map(ExtendedRelationship::getTo).map(e -> new Part(e)).forEach(p -> {
			
//			Predicate<ExtendedRelationship> predicateFrom = x -> x.getFrom().getId().equals(p.getId());
//			Predicate<ExtendedRelationship> predicateTo = x -> x.getTo().getId().equals(p.getId());
			
			// Add creators names
			Set<ExtendedRelationship> partOf = filterSet(hatGeschaffen, x -> x.getTo().getId().equals(p.getId()));
			List<String> creatorNames = partOf.stream().map(ExtendedRelationship::getFrom)
					.map(e -> getPersonBranch(e, 0)).map(Person::getTitle).collect(Collectors.toList());
			p.setCreators(creatorNames);

			// Add location names
			List<ExtendedRelationship> locRels = filterList(befindetSichIn, x -> x.getFrom().getId().equals(p.getId()));
			Set<String> locs = locRels.stream().map(ExtendedRelationship::getTo).map(l -> getInstitutionBranch(l))
					.map(Institution::getTitle).collect(Collectors.toSet());
			p.setLocation(locs);
			toReturn.add(p);
		});
		return toReturn;
	}

	/**
	 * Ausstellung
	 * 
	 * @param entity
	 * @return
	 */
	private Exhibition getExhibitionBranch(final Entity entity) {

		Exhibition exhibition = new Exhibition(entity);

		Predicate<ExtendedRelationship> predicateFrom = x -> x.getFrom().getId().equals(entity.getId());
		Predicate<ExtendedRelationship> predicateTo = x -> x.getTo().getId().equals(entity.getId());

		// (Austellung) kuratiert von (Person)
		List<Entity> persons = filterTo(kuratiertVon, predicateFrom);

		if (!persons.isEmpty()) {

			Person person = getPersonBranch(persons.get(0), 0);
			exhibition.setCurator(person);

		}

		// (Austellung) wurde gezeigt in (Ort)
		List<Entity> places = filterTo(wurdeGezeigtIn, predicateFrom);

		if (!places.isEmpty()) {

			Place place = new Place(places.get(0));
			exhibition.setExhibitionVenue(place);

		}

		// (Ausstellung) Ausstellungskatalog zu Ausstellung (Literatur)
		List<Entity> literatures = filterFrom(ausstellungskatalogZu, predicateTo);

		if (!literatures.isEmpty()) {

			Literature literature = getLiteratureBranch(literatures.get(0));
			exhibition.setExhibitionCatalogue(literature);

		}

		return exhibition;
	}

	/**
	 * Literatur...
	 * 
	 * @param entity
	 * @return
	 */
	private Literature getLiteratureBranch(final Entity entity) {

		Literature literature = new Literature(entity);

		Predicate<ExtendedRelationship> predicateFrom = x -> x.getFrom().getId().equals(entity.getId());
		Predicate<ExtendedRelationship> predicateTo = x -> x.getTo().getId().equals(entity.getId());

		// Person ist Autor/in von Literatur
		List<Entity> autors = filterFrom(autorInVon, predicateTo);

		if (!autors.isEmpty()) {

			literature.setAuthor(getPersonBranch(autors.get(0), 0));

		}

		// Person ist Herausgeberin/in von Literatur
		List<Entity> publishers = filterFrom(herausgeberInVon, predicateTo);

		if (!publishers.isEmpty()) {

			literature.setPublisher(getPersonBranch(publishers.get(0), 0));

		}

		// Literatur erschienen in Ort
		List<Entity> places = filterTo(erschienenIn, predicateFrom);

		if (!places.isEmpty()) {

			literature.setPublishedIn(new Place(places.get(0)));

		}

		// Literatur Sammlungskatalog von Institution
		List<Entity> catalogs = filterTo(sammlungskatalog, predicateFrom);

		if (!catalogs.isEmpty()) {

			literature.setCollectionCatalog(getInstitutionBranch(catalogs.get(0)));

		}

		// (Literatur) Literatur enthält Bilddatei (Medium)
		List<Entity> images = filterTo(literaturEnthaeltBilddatei, predicateFrom);

		List<String> mediums = images.stream().map(Entity::getId).collect(Collectors.toList());

		literature.setMedia(mediums);

		return literature;
	}

	/**
	 * Mediums...
	 * 
	 * @param id
	 * @return List
	 */
	public List<Medium> getMediumBranch(final String id) {

		Predicate<ExtendedRelationship> predicateTo = x -> x.getTo().getId().equals(id);
		List<Entity> images = filterFrom(bilddateiZuWerk, predicateTo);

		List<Medium> mediums = new ArrayList<>();

		for (Entity image : images) {

			if (image.getImagePath() == null) {

				continue;

			}

			Medium medium = new Medium(image);

			Predicate<ExtendedRelationship> predicateFrom = x -> x.getFrom().getId().equals(image.getId());

			List<Entity> rights = filterTo(verwertungsrechtAmFoto, predicateFrom);
			List<Entity> persons = filterTo(fotografiertVon, predicateFrom);

			if (!rights.isEmpty()) {

				medium.setExploitationRight(getInstitutionBranch(rights.get(0)));

			}

			List<Person> photographers = new ArrayList<>();

			persons.forEach(p -> {

				Person photographer = getPersonBranch(p, 0);
				photographers.add(photographer);

			});

			medium.setPhotographers(photographers);
			mediums.add(medium);
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
	private Institution getInstitutionBranch(final Entity entity) {

		Institution institution = new Institution(entity);

		Predicate<ExtendedRelationship> predicateFrom = x -> x.getFrom().getId().equals(entity.getId());

		List<Entity> places = filterTo(institutionInOrt, predicateFrom);

		if (!places.isEmpty()) {

			institution.setLocation(new Place(places.get(0)));

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
	private Person getPersonBranch(final Entity entity, int depth) {

		Person preson = new Person(entity);

		Predicate<ExtendedRelationship> predicateTo = x -> x.getTo().getId().equals(entity.getId());
		Predicate<ExtendedRelationship> predicateFrom = x -> x.getFrom().getId().equals(entity.getId());

		List<Entity> birthPlaces = filterFrom(geburtsortVon, predicateTo);
		List<Entity> deathPlaces = filterFrom(sterbeOrt, predicateTo);

		if (!birthPlaces.isEmpty()) {

			preson.setBirthPlace(new Place(birthPlaces.get(0)));

		}

		if (!deathPlaces.isEmpty()) {

			preson.setPlaceOfDeath(new Place(deathPlaces.get(0)));

		}

		if (depth == 1) { // stop recursion

			return preson;

		}

		List<Person> teachers = new ArrayList<>();

		filterTo(schuelerInVon, predicateFrom).forEach(t -> {

			Person teacher = getPersonBranch(t, (depth + 1));
			teachers.add(teacher);

		});

		preson.setTeachers(teachers);

		return preson;
	}

	private List<Entity> filterFrom(Set<ExtendedRelationship> relations, Predicate<ExtendedRelationship> predicate) {
		return relations.stream().filter(predicate).map(ExtendedRelationship::getFrom).collect(Collectors.toList());
	}

	private List<Entity> filterTo(Set<ExtendedRelationship> relations, Predicate<ExtendedRelationship> predicate) {
		return relations.stream().filter(predicate).map(ExtendedRelationship::getTo).collect(Collectors.toList());
	}

	private List<String> getConnections(final List<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getTo).map(Entity::getId).collect(Collectors.toList());
	}

	private List<Exhibition> getExhibitions(final List<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getFrom).map(e -> getExhibitionBranch(e))
				.collect(Collectors.toList());
	}

	private List<Literature> getLiterature(final Set<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getTo).map(l -> getLiteratureBranch(l))
				.collect(Collectors.toList());
	}

	private List<Person> getCreators(final Set<ExtendedRelationship> relations) {
		return relations.stream().map(ExtendedRelationship::getFrom).map(c -> getPersonBranch(c, 0))
				.collect(Collectors.toList());
	}

	private List<ExtendedRelationship> filterList(final Set<ExtendedRelationship> relation,
			Predicate<ExtendedRelationship> predicte) {
		return relation.stream().filter(predicte).collect(Collectors.toList());
	}

	private Set<ExtendedRelationship> filterSet(final Set<ExtendedRelationship> relation,
			Predicate<ExtendedRelationship> predicte) {
		return relation.stream().filter(predicte).collect(Collectors.toSet());
	}

	private Set<ExtendedRelationship> filterRelationships(final Set<ExtendedRelationship> relationships,
			final String id) {
		return relationships.stream().filter(r -> r.getRelation().getId().equals(id)).collect(Collectors.toSet());
	}

}
