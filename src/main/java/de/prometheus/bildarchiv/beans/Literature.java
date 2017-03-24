package de.prometheus.bildarchiv.beans;

import java.util.List;

import org.openarchives.beans.Entity;

// Literatur
public class Literature extends Basic {
	
	private static final long serialVersionUID = -5805575971954926098L;
	
	private Place publishedIn;
	private Person publisher;
	private Person author;
	private Institution collectionCatalog;
	private List<String> media; // mdium.id's
	
	public Literature() { }
	
//	public Literature(String id, String title, Collection collection, String distinction, String comment,
//			List<String> synonyms, XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
//		super(id, title, collection, distinction, comment, synonyms, createdAt, updatedAt);
//	}

	public Literature(Entity entity) {
		super(entity);
	}

	public Place getPublishedIn() {
		return publishedIn;
	}

	public void setPublishedIn(Place publishedIn) {
		this.publishedIn = publishedIn;
	}

	public Person getPublisher() {
		return publisher;
	}

	public void setPublisher(Person publisher) {
		this.publisher = publisher;
	}

	public Person getAuthor() {
		return author;
	}

	public void setAuthor(Person author) {
		this.author = author;
	}
	
	public Institution getCollectionCatalog() {
		return collectionCatalog;
	}
	
	public void setCollectionCatalog(Institution collectionCatalog) {
		this.collectionCatalog = collectionCatalog;
	}
	
	public List<String> getMedia() {
		return media;
	}
	
	public void setMedia(List<String> media) {
		this.media = media;
	}

	@Override
	public String toString() {
		return "Literature [publishedIn=" + publishedIn + ", publisher=" + publisher + ", author=" + author
				+ ", collectionCatalog=" + collectionCatalog + ", media=" + media + "]" + super.toString();
	}
	
	
	
}
