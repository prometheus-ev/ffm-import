package de.prometheus.bildarchiv.beans;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.openarchives.beans.Entity;

import com.google.gson.Gson;

// Werk
public class Work extends Basic {

	private static final long serialVersionUID = 5113927173240646123L;
	private String subtype;
	private List<Medium> mediums = new ArrayList<>();
	private List<Person> creators;
	private Person commissioner;
	private Person portrayal;
	private Institution locatedIn;
	private List<String> connectionsTo; // work.id
	private List<Basic> partsOf; // work.id
	private List<Literature> illustrations;
	private List<Exhibition> exhibitions;

	public Work() { }

//	public Work(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
//			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
//		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
//	}

	public Work(Entity entity) {
		super(entity);
	}

	public List<Medium> getMediums() {
		return mediums;
	}
	
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

	public List<Basic> getPartsOf() {
		return partsOf;
	}

	public void setPartsOf(List<Basic> partsOf) {
		this.partsOf = partsOf;
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

	@Override
	public String toString() {
		return "Work [subtype=" + subtype + ", mediums=" + mediums + ", creators=" + creators + ", commissioner="
				+ commissioner + ", portrayal=" + portrayal + ", locatedIn=" + locatedIn + ", connectionsTo="
				+ connectionsTo + ", partsOf=" + partsOf + ", illustrations=" + illustrations + ", exhibitions="
				+ exhibitions + "]";
	}

	public String toJson() {
		return new Gson().toJson(this);
	}

}
