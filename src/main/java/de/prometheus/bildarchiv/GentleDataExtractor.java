package de.prometheus.bildarchiv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.model.Entity;
import org.openarchives.model.Entity.Properties.Property;

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

//@formatter:off

/**
 * This marvelous class is used to extract the information described in <a href=
 * "https://github.com/matana/ffm-import/blob/master/20170511_154237.jpg">the
 * truth</a>
 * 
 * @author matana
 *
 */
public class GentleDataExtractor {

	private static final int DELIMITER = 1;

	private static Logger logger;

	private File dataDirectory;

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
	private transient Set<ExtendedRelationship> standortIn;
	
	
	public static void main(String[] args) throws PropertyException, FileNotFoundException, JAXBException {
		Options options = new Options();
		Option configOption = new Option("c", "config", true, "The configuration directory");
		configOption.setRequired(true);
		Option destinationOption = new Option("d", "data", true, "The data directory contains temporary and output files");
		destinationOption.setRequired(false);
		options.addOption(configOption);
		options.addOption(destinationOption);

		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			
			File configDir = new File(cmd.getOptionValue("c"));

			// Logger configuration
			File log4jXml = new File(configDir, "log4j2.xml");
			System.setProperty("log4j.configurationFile", log4jXml.getAbsolutePath());
			logger = LogManager.getLogger(GentleDataExtractor.class);

			String dataDirectoryPath = "/tmp";
			if(cmd.getOptionValue("d") != null) {
				dataDirectoryPath = cmd.getOptionValue("d");
			}

			GentleSegmentMerger merger = new GentleSegmentMerger(dataDirectoryPath);
			Set<ExtendedRelationship> relationships = merger.mergeEntitiesAndRelationships();
			
			GentleDataExtractor exctractor = new GentleDataExtractor(relationships, dataDirectoryPath);
			exctractor.extractData();
		} catch (ParseException e) {
			logger.error(e.toString());
		}
	}
	

	public GentleDataExtractor(Set<ExtendedRelationship> relationships, String dataDirectoryPath) {

		dataDirectory = new File(dataDirectoryPath);
		logger = LogManager.getLogger(GentleDataExtractor.class);
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
		this.standortIn = filterRelationships(relationships, Relations.STANDORT_IN);

		if (logger.isInfoEnabled()) {
			
			logger.info("Filtering finished!");
			
			logger.info("bilddateiZuWerk :: " + bilddateiZuWerk.size());
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
			logger.info("standortIn :: " + standortIn.size());

		}
	}

	/**
	 * 
	 * @throws JAXBException
	 */
	public void extractData() throws JAXBException {

		final Set<Work> works = new HashSet<>();

		for (ExtendedRelationship relationship : bilddateiZuWerk) {
			final Entity mediumEntity = relationship.getFrom();
			final Entity workEntity = relationship.getTo();

			if (workEntity != null && mediumEntity != null) {
				Work work = new Work(workEntity);
				if (!works.contains(work)) {
					String identifier = workEntity.getId();
					
					Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(identifier);
					Predicate<ExtendedRelationship> predicateTo = getPredicateTo(identifier);

					if (workEntity.getSubType() != null) {
						work.setSubtype(workEntity.getSubType());
					}

					// #1
					List<ExtendedRelationship> bilddateiZuThisWerkRelationships = filterRelationshipsFrom(bilddateiZuWerk, predicateTo);
					for (ExtendedRelationship bilddateiZuThisWerkRelationship: bilddateiZuThisWerkRelationships) {
						Medium medium = getMedium(bilddateiZuThisWerkRelationship);
						if (medium != null) {
							work.setMedium(medium);
						}
					}

					// #2
					List<Person> creators = getPersons(filterFrom(hatGeschaffen, predicateTo));
					work.setCreators(creators);

					// #3
					List<Literature> illustrations = getLiteratures(filterTo(basisdatenZumWerkAus, predicateFrom));
					work.setIllustrations(illustrations);

					// #4
					List<Exhibition> exhibitions = getExhibitions(filterFrom(ausgestellteWerke, predicateTo));
					work.setExhibitions(exhibitions);

					// #5 (Werk) befindet sich in (Institution)
					List<Entity> institutions = filterTo(befindetSichIn, predicateFrom);
					if (!institutions.isEmpty()) {
						Institution institution = getInstitutionBranch(institutions.get(0));
						work.setLocatedIn(institution);
					}

					// #6 (Person) ist Auftraggeber von (Werk)
					List<Entity> commissioners = filterFrom(auftraggeberVonWerk, predicateTo);
					if (!commissioners.isEmpty()) {
						Person commissioner = getPersonBranch(commissioners.get(0), 0);
						work.setCommissioner(commissioner);
					}

					// #7 (Werk) steht in Verbindung zu (Werk)
					List<String> connections = getConnections(filterFrom(stehtInVerbindungZu, predicateTo));
					work.setConnectionsTo(connections);

					// #8 (Werk) ist Teil von (Werk)
					List<Part> relations = getWorkRelations(filterTo(istTeilVon, predicateFrom));
					work.setParts(relations);

					// #9 (Werk) stellt dar (Person)
					List<Person> portrayals = getPersons(filterTo(stelltDar, predicateFrom));
					if (!portrayals.isEmpty()) {
						work.setPortrayal(portrayals.get(0));
					}
					
					// #10 (Werk) Standort in (Ort)
					List<Place> sites = new ArrayList<Place>();
					List<Entity> places = filterTo(standortIn, predicateFrom);
					for (Entity place: places) {
						sites.add(new Place(place));
					}
					work.setSites(sites);

					// collect work object
					works.add(work);

//					if (logger.isInfoEnabled()) {
//						logger.info("Added new work object '" + work.getTitle()); // verbose
//					}
				}
			}
		}

		if(bilddateiZuWerk.isEmpty()) {
			logger.info("No Relationships for (" + Relations.BILDDATEI_ZU_WERK + ") present... ");
		} else {
			GentleUtils.finalExport(works, dataDirectory);
		}

	}

	/**
	 * @param relations
	 * @return
	 */
	private List<Part> getWorkRelations(final List<Entity> entities) {
		
		final List<Part> toReturn = new ArrayList<>();
		
		List<Part> parts = getParts(entities);
		
		for (Part part : parts) {
			
			if(part == null) {
				continue;
			}
			
			Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(part.getId());
			Predicate<ExtendedRelationship> predicateTo = getPredicateTo(part.getId());
			
			List<String> creators = getPersons(filterFrom(istTeilVon, predicateTo))
					.stream().map(Person::getTitle).collect(Collectors.toList());
			
			Set<String> locations = getLocations(filterTo(istTeilVon, predicateFrom))
					.stream().map(Institution::getTitle).collect(Collectors.toSet());
			
			part.setCreators(creators);
			part.setLocation(locations);
			
			toReturn.add(part);
		}
		
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

		Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(entity.getId());
		Predicate<ExtendedRelationship> predicateTo = getPredicateTo(entity.getId());

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

		Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(entity.getId());
		Predicate<ExtendedRelationship> predicateTo = getPredicateTo(entity.getId());

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
	
	private Medium getMedium(ExtendedRelationship bilddateiZuWerkRelationship) {
		Medium medium = null;
		Entity mediumEntity = bilddateiZuWerkRelationship.getFrom();
		if (mediumEntity.getImagePath() != null) {
			medium = new Medium(mediumEntity);

			Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(mediumEntity.getId());

			List<Entity> rights = filterTo(verwertungsrechtAmFoto, predicateFrom);
			List<Entity> persons = filterTo(fotografiertVon, predicateFrom);

			if (!rights.isEmpty()) {
				medium.setExploitationRight(getInstitutionBranch(rights.get(0)));
			}

			List<Person> photographers = new ArrayList<>();

			persons.forEach(x -> {

				Person photographer = getPersonBranch(x, 0);
				photographers.add(photographer);

			});

			medium.setPhotographers(photographers);
			
			// add bilddateiZuWerk relationship's properties to medium
			for(String property: bilddateiZuWerkRelationship.getProperties()) {
				Property titleProperty = new Entity.Properties.Property();
				titleProperty.setName("title");
				titleProperty.setValue(property);
				medium.getProperties().getProperty().add(titleProperty);
			}
		}

	return medium;
	}

	/**
	 * Mediums...
	 * 
	 * @param id
	 * @return List
	 */
//	private List<Medium> getMediumBranch(final String identifier) {
//
//		Predicate<ExtendedRelationship> predicateTo = getPredicateTo(identifier);
//		
//		List<Entity> images = filterFrom(bilddateiZuWerk, predicateTo);
//
//		List<Medium> mediums = new ArrayList<>();
//
//		for (Entity image : images) {
//
//			if (image.getImagePath() == null) {
//				continue;
//			}
//
//			Medium medium = new Medium(image);
//
//			Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(image.getId());
//
//			List<Entity> rights = filterTo(verwertungsrechtAmFoto, predicateFrom);
//			List<Entity> persons = filterTo(fotografiertVon, predicateFrom);
//
//			if (!rights.isEmpty()) {
//				medium.setExploitationRight(getInstitutionBranch(rights.get(0)));
//			}
//
//			List<Person> photographers = new ArrayList<>();
//
//			persons.forEach(x -> {
//
//				Person photographer = getPersonBranch(x, 0);
//				photographers.add(photographer);
//
//			});
//
//			medium.setPhotographers(photographers);
//			
//			mediums.add(medium);
//		}
//
//		return mediums;
//	}

	/**
	 * Institution
	 * 
	 * @param entity
	 * @param institutionInOrt
	 * @return
	 */
	private Institution getInstitutionBranch(final Entity entity) {

		Institution institution = new Institution(entity);

		Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(entity.getId());

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

		Predicate<ExtendedRelationship> predicateTo = getPredicateTo(entity.getId());
		Predicate<ExtendedRelationship> predicateFrom = getPredicateFrom(entity.getId());

		List<Entity> birthPlaces = filterFrom(geburtsortVon, predicateTo);
		List<Entity> deathPlaces = filterFrom(sterbeOrt, predicateTo);

		if (!birthPlaces.isEmpty()) {
			preson.setBirthPlace(new Place(birthPlaces.get(0)));
		}

		if (!deathPlaces.isEmpty()) {
			preson.setPlaceOfDeath(new Place(deathPlaces.get(0)));
		}

		if (depth == DELIMITER) { // stop recursion
			return preson;
		}

		List<Person> teachers = new ArrayList<>();

		filterTo(schuelerInVon, predicateFrom).forEach(x -> {

			Person teacher = getPersonBranch(x, (depth + 1));
			teachers.add(teacher);

		});

		preson.setTeachers(teachers);

		return preson;
	}
	
	private static Predicate<ExtendedRelationship> getPredicateFrom(final String identifier) {
		return x -> x.getFrom().getId().equals(identifier);
	}
	
	private static Predicate<ExtendedRelationship> getPredicateTo(final String identifier) {
		return x -> x.getTo().getId().equals(identifier);
	}

	private List<Entity> filterFrom(Collection<ExtendedRelationship> relations, Predicate<ExtendedRelationship> predicate) {
		return relations.stream().filter(predicate).map(ExtendedRelationship::getFrom).collect(Collectors.toList());
	}

	private List<Entity> filterTo(Collection<ExtendedRelationship> relations, Predicate<ExtendedRelationship> predicate) {
		return relations.stream().filter(predicate).map(ExtendedRelationship::getTo).collect(Collectors.toList());
	}
	
	private List<Institution> getLocations(final Collection<Entity> entities) {
		return entities.stream().map(x -> getInstitutionBranch(x)).collect(Collectors.toList());
	}
	
	private List<Part> getParts(final Collection<Entity> entities) {
		return entities.stream().map(x -> new Part(x)).collect(Collectors.toList());
	}
	
	private List<String> getConnections(final Collection<Entity> entities) {
		return entities.stream().map(Entity::getId).collect(Collectors.toList());
	}

	private List<Exhibition> getExhibitions(final Collection<Entity> entities) {
		return entities.stream().map(x -> getExhibitionBranch(x)).collect(Collectors.toList());
	}

	private List<Literature> getLiteratures(final Collection<Entity> entities) {
		return entities.stream().map(x -> getLiteratureBranch(x)).collect(Collectors.toList());
	}

	private List<Person> getPersons(final Collection<Entity> entities) {
		return entities.stream().map(x -> getPersonBranch(x, 0)).collect(Collectors.toList());
	}
	
	private Set<ExtendedRelationship> filterRelationships(final Set<ExtendedRelationship> relationships, final String identifier) {
		return relationships.stream().filter(x -> x.getRelation().getId().equals(identifier)).collect(Collectors.toSet());
	}
	
	private List<ExtendedRelationship> filterRelationshipsFrom(Collection<ExtendedRelationship> relations, Predicate<ExtendedRelationship> predicate) {
		return relations.stream().filter(predicate).collect(Collectors.toList());
	}

	// @formatter:off

}
