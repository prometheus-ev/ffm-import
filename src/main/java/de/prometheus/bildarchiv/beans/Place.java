package de.prometheus.bildarchiv.beans;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openarchives.beans.Entity.Collection;

// Ort
public class Place extends Basic {
	
	private static final long serialVersionUID = -1740229391359003802L;

	public Place() { }

	public Place(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
	}

	@Override
	public String toString() {
		return "Place " + super.toString();
	}
	
	

}
