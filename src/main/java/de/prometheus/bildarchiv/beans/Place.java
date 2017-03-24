package de.prometheus.bildarchiv.beans;

import org.openarchives.beans.Entity;

// Ort
public class Place extends Basic {
	
	private static final long serialVersionUID = -1740229391359003802L;

	public Place() { }

//	public Place(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
//			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
//		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
//	}

	public Place(Entity entity) {
		super(entity);
	}

	@Override
	public String toString() {
		return "Place " + super.toString();
	}
	
	

}
