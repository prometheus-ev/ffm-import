package de.prometheus.bildarchiv.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;

import org.openarchives.model.Entity;
import org.openarchives.model.Entity.Datings;
import org.openarchives.model.Entity.Fields;
import org.openarchives.model.Entity.Properties;
import org.openarchives.model.Entity.Tags;


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
	private Datings datings;
	private Tags tags;
	private Properties properties;
	private Fields fields;
	private String creator;
	
	
	public Basic() { 
		super();
	}
	
	public Basic(Entity entity) {
		super();
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.collection = entity.getCollection() == null ? "unknown" : entity.getCollection().getValue();
		this.distinction = entity.getDistinction(); 
		this.comment = entity.getComment();
		this.synonyms = entity.getSynonym();
		this.createdAt = entity.getCreatedAt();
		this.updatedAt = entity.getUpdatedAt();
		this.datings = entity.getDatings();
		this.tags = entity.getTags();
		this.properties = entity.getProperties();
		this.fields = entity.getFields();
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
	
	public Datings getDatings() {
		return datings;
	}

	public void setDatings(Datings datings) {
		this.datings = datings;
	}

	public Tags getTags() {
		return tags;
	}

	public void setTags(Tags tags) {
		this.tags = tags;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Fields getFields() {
		return fields;
	}

	public void setFields(Fields fields) {
		this.fields = fields;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
				+ updatedAt + ", datings=" + datings + ", tags=" + tags + ", properties=" + properties + ", fields="
				+ fields + "]";
	}

	
	
	
}
