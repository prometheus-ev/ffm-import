package de.prometheus.bildarchiv.beans;

import java.io.Serializable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openarchives.beans.Entity.Collection;


public class Basic implements Serializable {
	
	private static final long serialVersionUID = 5846273839166399285L;
	
	private String id;
	private String title;
	private String collection;
	private String distinction;	
	private String comment;
	private List<String> synonyms;
	private XMLGregorianCalendar createdAt;
	private XMLGregorianCalendar updatedAt;
	
	public Basic() { }
	
	public Basic(String id, String title, Collection collection, String distinction, String comment, List<String> synonyms,
			XMLGregorianCalendar createdAt, XMLGregorianCalendar updatedAt) {
		super();
		
		String collectionValue = collection == null ? "unknown" : collection.getValue();
		
		this.id = id;
		this.title = title;
		this.collection = collectionValue;
		this.distinction = distinction;
		this.comment = comment;
		this.synonyms = synonyms;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getCollection() {
		return collection;
	}
	
	public void setCollection(String collection) {
		this.collection = collection;
	}
	
	public String getDistinction() {
		return distinction;
	}
	
	public void setDistinction(String distinction) {
		this.distinction = distinction;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public List<String> getSynonyms() {
		return synonyms;
	}
	public void setSynonyms(List<String> synonyms) {
		this.synonyms = synonyms;
	}
	
	public XMLGregorianCalendar getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(XMLGregorianCalendar createdAt) {
		this.createdAt = createdAt;
	}
	public XMLGregorianCalendar getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(XMLGregorianCalendar updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((collection == null) ? 0 : collection.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((distinction == null) ? 0 : distinction.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((synonyms == null) ? 0 : synonyms.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Basic other = (Basic) obj;
		if (collection == null) {
			if (other.collection != null)
				return false;
		} else if (!collection.equals(other.collection))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (distinction == null) {
			if (other.distinction != null)
				return false;
		} else if (!distinction.equals(other.distinction))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (synonyms == null) {
			if (other.synonyms != null)
				return false;
		} else if (!synonyms.equals(other.synonyms))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (updatedAt == null) {
			if (other.updatedAt != null)
				return false;
		} else if (!updatedAt.equals(other.updatedAt))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Basic [id=" + id + ", title=" + title + ", collection=" + collection + ", distinction=" + distinction
				+ ", comment=" + comment + ", synonyms=" + synonyms + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
	
	
}
