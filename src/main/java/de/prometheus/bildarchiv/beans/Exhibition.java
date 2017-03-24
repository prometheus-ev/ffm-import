package de.prometheus.bildarchiv.beans;

import org.openarchives.beans.Entity;

// Ausstellung
public class Exhibition extends Basic {
	
	private static final long serialVersionUID = -6533763056301964638L;
	
	private Person curator;
	private Literature exhibitionCatalogue;
	private Place exhibitionVenue;
	
	public Exhibition() { }
	
//	public Exhibition(String id, String title, Collection collection, String distinction, String comment,
//			List<String> synonyms, XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
//		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
//	}
	
	public Exhibition(Entity entity) {
		super(entity);
	}

	public Person getCurator() {
		return curator;
	}

	public void setCurator(Person curator) {
		this.curator = curator;
	}

	public Literature getExhibitionCatalogue() {
		return exhibitionCatalogue;
	}

	public void setExhibitionCatalogue(Literature exhibitionCatalogue) {
		this.exhibitionCatalogue = exhibitionCatalogue;
	}

	public Place getExhibitionVenue() {
		return exhibitionVenue;
	}

	public void setExhibitionVenue(Place exhibitionVenue) {
		this.exhibitionVenue = exhibitionVenue;
	}

	@Override
	public String toString() {
		return "Exhibition [curator=" + curator + ", exhibitionCatalogue=" + exhibitionCatalogue + ", exhibitionVenue="
				+ exhibitionVenue + "]" + super.toString();
	}
	
	
	

}
