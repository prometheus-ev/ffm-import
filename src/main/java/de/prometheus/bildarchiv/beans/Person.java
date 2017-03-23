package de.prometheus.bildarchiv.beans;

import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openarchives.beans.Entity.Collection;

// Person
public class Person extends Basic {

	private static final long serialVersionUID = -6197578059704943164L;
	
	private List<Person> teachers;
	private Place birthPlace;
	private Place placeOfDeath;
	
	public Person() { }
	
	public Person(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
	}

	public List<Person> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Person> teachers) {
		this.teachers = teachers;
	}

	public Place getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(Place birthPlace) {
		this.birthPlace = birthPlace;
	}

	public Place getPlaceOfDeath() {
		return placeOfDeath;
	}

	public void setPlaceOfDeath(Place placeOfDeath) {
		this.placeOfDeath = placeOfDeath;
	}

	@Override
	public String toString() {
		return "Person [teachers=" + teachers + ", birthPlace=" + birthPlace + ", placeOfDeath=" + placeOfDeath + "]" + super.toString();
	}
	
	

}
