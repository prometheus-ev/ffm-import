package de.prometheus.bildarchiv.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openarchives.model.Entity;
import org.openarchives.model.Relationship;
import org.openarchives.model.Relationship.Properties.Property;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationship", propOrder = { "id", "createdAt", "updatedAt", "name", "relation", "from", "to", "properties" })
public class ExtendedRelationship implements Serializable {

	private static final long serialVersionUID = -8533040673528252526L;
	@XmlElement(required = true)
	protected String id;
	@XmlElement(name = "created-at", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar createdAt;
	@XmlElement(name = "updated-at", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar updatedAt;
	@XmlElement(required = true)
	protected String name;
	@XmlElement(required = true)
	protected ExtendedRelationship.Relation relation;
	@XmlElement(required = true)
	protected Entity from;
	@XmlElement(required = true)
	protected Entity to;
	@XmlElement(required = true)
	protected List<String> properties = new ArrayList<>();

	public ExtendedRelationship() { }
	
	public ExtendedRelationship(Relationship relationship) {
		
		this.id = relationship.getId();
		this.createdAt = relationship.getCreatedAt();
		this.updatedAt = relationship.getUpdatedAt();
		this.name = relationship.getName();
		
		this.from = new Entity(relationship.getFrom());
		this.to = new Entity(relationship.getTo());
		
		setProperties(relationship);
		setRelation(relationship);
		
	}

	private void setRelation(Relationship relationship) {
		
		Relation relation = new Relation();
		
		relation.id = relationship.getRelation().getId();
		relation.name = relationship.getRelation().getName();
		relation.reverseName = relationship.getRelation().getReverseName();
		
		this.relation = relation;
		
	}

	private void setProperties(Relationship relationship) {
		
		List<Property> property = relationship.getProperties().getProperty();
		
		for (Property prop : property) {
			properties.add(prop.getValue());
		}
		
	}

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	public XMLGregorianCalendar getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(XMLGregorianCalendar value) {
		this.createdAt = value;
	}

	public XMLGregorianCalendar getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(XMLGregorianCalendar updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public ExtendedRelationship.Relation getRelation() {
		return relation;
	}

	public void setRelation(ExtendedRelationship.Relation relation) {
		this.relation = relation;
	}

	public Entity getFrom() {
		return from;
	}

	public void setFrom(Entity from) {
		this.from = from;
	}

	public Entity getTo() {
		return to;
	}

	public void setTo(Entity to) {
		this.to = to;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Relation implements Serializable {

		private static final long serialVersionUID = -5650922262898034755L;
		@XmlAttribute(name = "id", required = true)
		protected String id;
		@XmlAttribute(name = "name", required = true)
		protected String name;
		@XmlAttribute(name = "reverse-name", required = true)
		protected String reverseName;

		public String getId() {
			return id;
		}

		public void setId(final String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(final String name) {
			this.name = name;
		}

		public String getReverseName() {
			return reverseName;
		}

		public void setReverseName(final String reverseName) {
			this.reverseName = reverseName;
		}

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ExtendedRelationship other = (ExtendedRelationship) obj;
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		return "Relationship [id=" + id + ", name=" + name + ", relationId=" + relation.getId() + ", fromEntity="
				+ from.getId() + ", toEntity=" + to.getId() + "properties=" + properties + "]";
	}

}
