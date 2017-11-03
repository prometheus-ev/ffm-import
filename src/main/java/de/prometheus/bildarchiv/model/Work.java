package de.prometheus.bildarchiv.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import org.openarchives.model.Entity;

// Werk
public class Work extends Basic implements ToJson {

	private static final long serialVersionUID = 5113927173240646123L;
	private String subtype;
	@XmlElementWrapper(name="mediums")
	@XmlElement(name = "medium")
	private List<Medium> mediums = new ArrayList<>();
	private List<Person> creators;
	private Person commissioner;
	private Person portrayal;
	private Institution locatedIn;
	private List<String> connectionsTo; // work.id
	@XmlElementWrapper(name="parts")
	@XmlElement(name = "part")
	private List<Part> parts;
	private List<Literature> illustrations;
	private List<Exhibition> exhibitions;
	@XmlElementWrapper(name="sites")
	@XmlElement(name = "site")
	private List<Place> sites;

	public Work() { }

//	public Work(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
//			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
//		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
//	}

	public Work(Entity entity) {
		super(entity);
	}

//	public List<Medium> getMediums() {
//		return mediums;
//	}
	
	public void setMediums(List<Medium> mediums) {
		this.mediums.addAll(mediums);
	}

	public void setMedium(Medium medium) {
		this.mediums.add(medium);
	}

	public List<Person> getCreators() {
		return creators;
	}

	public void setCreators(List<Person> creators) {
		this.creators = creators;
	}

	public Person getCommissioner() {
		return commissioner;
	}

	public void setCommissioner(Person commissioner) {
		this.commissioner = commissioner;
	}

	public Person getPortrayal() {
		return portrayal;
	}

	public void setPortrayal(Person portrayal) {
		this.portrayal = portrayal;
	}

	public Institution getLocatedIn() {
		return locatedIn;
	}

	public void setLocatedIn(Institution locatedIn) {
		this.locatedIn = locatedIn;
	}

	public List<String> getConnections() {
		return connectionsTo;
	}

	public void setConnectionsTo(List<String> connectionsTo) {
		this.connectionsTo = connectionsTo;
	}

//	public List<Part> getParts() {
//		return parts;
//	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	public List<Literature> getIllustrations() {
		return illustrations;
	}

	public void setIllustrations(List<Literature> illustrations) {
		this.illustrations = illustrations;
	}

	public List<Exhibition> getExhibitions() {
		return exhibitions;
	}

	public void setExhibitions(List<Exhibition> exhibitions) {
		this.exhibitions = exhibitions;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

//	public List<Place> getSites() {
//		return sites;
//	}

	public void setSites(List<Place> sites) {
		this.sites = sites;
	}

	@Override
	public String toString() {
		return "Work [subtype=" + subtype + ", mediums=" + mediums + ", creators=" + creators + ", commissioner="
				+ commissioner + ", portrayal=" + portrayal + ", locatedIn=" + locatedIn + ", connectionsTo="
				+ connectionsTo + ", parts=" + parts + ", illustrations=" + illustrations + ", exhibitions="
				+ exhibitions + ", sites=" + sites + "]";
	}

}
