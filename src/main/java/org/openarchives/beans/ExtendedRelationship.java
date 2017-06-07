package org.openarchives.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relationship", propOrder = { "id", "createdAt", "updatedAt", "name", "relation", "from", "to",
		"properties" })
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
	protected ExtendedRelationship.Properties properties;

	public ExtendedRelationship() {
		
	}
	
	// DeppCopy
	public ExtendedRelationship(Relationship relationship) {
		this.id = relationship.getId();
		this.createdAt = relationship.getCreatedAt();
		this.updatedAt = relationship.getUpdatedAt();
		this.name = relationship.getName();
		this.from = new Entity(relationship.getFrom());
		this.to = new Entity(relationship.getTo());
		Relation relation = new Relation();
		relation.id = relationship.getRelation().getId();
		relation.name = relationship.getRelation().getName();
		relation.reverseName = relationship.getRelation().getReverseName();
		this.relation = relation;
	}

	/**
	 * Ruft den Wert der id-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Legt den Wert der id-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Ruft den Wert der createdAt-Eigenschaft ab.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getCreatedAt() {
		return createdAt;
	}

	/**
	 * Legt den Wert der createdAt-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setCreatedAt(XMLGregorianCalendar value) {
		this.createdAt = value;
	}

	/**
	 * Ruft den Wert der updatedAt-Eigenschaft ab.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Legt den Wert der updatedAt-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setUpdatedAt(XMLGregorianCalendar value) {
		this.updatedAt = value;
	}

	/**
	 * Ruft den Wert der name-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Legt den Wert der name-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Ruft den Wert der relation-Eigenschaft ab.
	 * 
	 * @return possible object is {@link ExtendedRelationship.Relation }
	 * 
	 */
	public ExtendedRelationship.Relation getRelation() {
		return relation;
	}

	/**
	 * Legt den Wert der relation-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link ExtendedRelationship.Relation }
	 * 
	 */
	public void setRelation(ExtendedRelationship.Relation value) {
		this.relation = value;
	}

	/**
	 * Ruft den Wert der from-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public Entity getFrom() {
		return from;
	}

	/**
	 * Legt den Wert der from-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setFrom(Entity value) {
		this.from = value;
	}

	/**
	 * Ruft den Wert der to-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public Entity getTo() {
		return to;
	}

	/**
	 * Legt den Wert der to-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTo(Entity value) {
		this.to = value;
	}

	/**
	 * <p>
	 * Java-Klasse für anonymous complex type.
	 * 
	 * <p>
	 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
	 * Klasse enthalten ist.
	 * 
	 * <pre>
	 * &lt;complexType&gt;
	 *   &lt;complexContent&gt;
	 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
	 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *       &lt;attribute name="reverse-name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *     &lt;/restriction&gt;
	 *   &lt;/complexContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
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

		/**
		 * Ruft den Wert der id-Eigenschaft ab.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getId() {
			return id;
		}

		/**
		 * Legt den Wert der id-Eigenschaft fest.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setId(String value) {
			this.id = value;
		}

		/**
		 * Ruft den Wert der name-Eigenschaft ab.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getName() {
			return name;
		}

		/**
		 * Legt den Wert der name-Eigenschaft fest.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setName(String value) {
			this.name = value;
		}

		/**
		 * Ruft den Wert der reverseName-Eigenschaft ab.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getReverseName() {
			return reverseName;
		}

		/**
		 * Legt den Wert der reverseName-Eigenschaft fest.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setReverseName(String value) {
			this.reverseName = value;
		}

	}

	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Properties implements Serializable {

		private static final long serialVersionUID = -2108001315464156746L;
		protected List<ExtendedRelationship.Properties.Property> property;

		/**
		 * Gets the value of the property property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the property property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getProperty().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Entity.Properties.Property }
		 * 
		 * 
		 */
		public List<ExtendedRelationship.Properties.Property> getProperty() {
			if (property == null) {
				property = new ArrayList<ExtendedRelationship.Properties.Property>();
			}
			return this.property;
		}

		/**
		 * <p>
		 * Java-Klasse für anonymous complex type.
		 * 
		 * <p>
		 * Das folgende Schemafragment gibt den erwarteten Content an, der in
		 * dieser Klasse enthalten ist.
		 * 
		 * <pre>
		 * &lt;complexType&gt;
		 *   &lt;simpleContent&gt;
		 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
		 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
		 *     &lt;/extension&gt;
		 *   &lt;/simpleContent&gt;
		 * &lt;/complexType&gt;
		 * </pre>
		 * 
		 * 
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "value" })
		public static class Property implements Serializable {

			private static final long serialVersionUID = -6053401809134575239L;
			@XmlValue
			protected String value;
			@XmlAttribute(name = "name", required = true)
			protected String name;

			/**
			 * Ruft den Wert der value-Eigenschaft ab.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getValue() {
				return value;
			}

			/**
			 * Legt den Wert der value-Eigenschaft fest.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setValue(String value) {
				this.value = value;
			}

			/**
			 * Ruft den Wert der name-Eigenschaft ab.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getName() {
				return name;
			}

			/**
			 * Legt den Wert der name-Eigenschaft fest.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setName(String value) {
				this.name = value;
			}

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
				+ from.getId() + ", toEntity=" + to.getId() + "properties=" + properties.getProperty() + "]";
	}

}
