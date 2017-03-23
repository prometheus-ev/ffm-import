package de.prometheus.bildarchiv.beans;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openarchives.beans.Entity.Collection;

public class Institution extends Basic {
	
	private static final long serialVersionUID = 1881478228895488369L;
	
	private Place location;
	
	public Institution() { }

	public Institution(String id, String title, Collection collection, String distinction, String comment,
			List<String> synonyms, XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
	}
	
	public Place getLocation() {
		return location;
	}
	
	public void setLocation(Place location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "Institution [location=" + location + "]" + super.toString();
	}
	
	

}
