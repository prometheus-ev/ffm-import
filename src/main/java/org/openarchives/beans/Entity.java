//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.28 um 03:31:05 PM CET 
//

package org.openarchives.beans;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.datatype.XMLGregorianCalendar;

import de.prometheus.bildarchiv.beans.ToJson;

/**
 * <p>
 * Java-Klasse für entity complex type.
 * 
 * <p>
 * Das folgende Schemafragment gibt den erwarteten Content an, der in dieser
 * Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="entity"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="collection"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="created-at" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="updated-at" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="type"&gt;
 *           &lt;complexType&gt;
 *             &lt;simpleContent&gt;
 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                 &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *               &lt;/extension&gt;
 *             &lt;/simpleContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;choice&gt;
 *           &lt;group ref="{http://www.openarchives.org/OAI/2.0/}mediumgroup"/&gt;
 *           &lt;group ref="{http://www.openarchives.org/OAI/2.0/}othergroup"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element name="tags"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="tag" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="fields"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="field" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;simpleContent&gt;
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/extension&gt;
 *                       &lt;/simpleContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="properties"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="property" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;simpleContent&gt;
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                           &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                         &lt;/extension&gt;
 *                       &lt;/simpleContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="datings"&gt;
 *           &lt;complexType&gt;
 *             &lt;complexContent&gt;
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *                 &lt;sequence&gt;
 *                   &lt;element name="dating" maxOccurs="unbounded" minOccurs="0"&gt;
 *                     &lt;complexType&gt;
 *                       &lt;simpleContent&gt;
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
 *                           &lt;attribute name="event" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *                           &lt;attribute name="from-day" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                           &lt;attribute name="to-day" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *                         &lt;/extension&gt;
 *                       &lt;/simpleContent&gt;
 *                     &lt;/complexType&gt;
 *                   &lt;/element&gt;
 *                 &lt;/sequence&gt;
 *               &lt;/restriction&gt;
 *             &lt;/complexContent&gt;
 *           &lt;/complexType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="comment" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "entity", propOrder = { "id", "collection", "createdAt", "updatedAt", "type", "imagePath", "title",
		"subType", "distinction", "synonym", "noNameSpecifier", "tags", "fields", "properties", "datings", "comment" })
public class Entity implements Serializable, Comparable<Entity>, ToJson {

	private static final long serialVersionUID = -1197501305348860684L;
	@XmlElement(required = true)
	protected String id;
	@XmlElement(required = true)
	protected Entity.Collection collection;
	@XmlElement(name = "created-at", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar createdAt;
	@XmlElement(name = "updated-at", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar updatedAt;
	@XmlElement(required = true)
	protected Entity.Type type;
	@XmlElement(name = "image-path")
	protected Entity.ImagePath imagePath;
	protected String title;
	@XmlElement(name = "sub-type")
	protected String subType;
	protected String distinction;
	protected List<String> synonym;
	@XmlElement(name = "no-name-specifier")
	protected String noNameSpecifier;
	@XmlElement(required = true)
	protected Entity.Tags tags;
	@XmlElement(required = true)
	protected Entity.Fields fields;
	@XmlElement(required = true)
	protected Entity.Properties properties;
	@XmlElement(required = true)
	protected Entity.Datings datings;
	@XmlElement(required = true)
	protected String comment;

	public Entity() {
	
	}
	
	public Entity(String id) {
		this.id = id;
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
	 * Ruft den Wert der collection-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.Collection }
	 * 
	 */
	public Entity.Collection getCollection() {
		return collection;
	}

	/**
	 * Legt den Wert der collection-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.Collection }
	 * 
	 */
	public void setCollection(Entity.Collection value) {
		this.collection = value;
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
	 * Ruft den Wert der type-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.Type }
	 * 
	 */
	public Entity.Type getType() {
		return type;
	}

	/**
	 * Legt den Wert der type-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.Type }
	 * 
	 */
	public void setType(Entity.Type value) {
		this.type = value;
	}

	/**
	 * Ruft den Wert der imagePath-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.ImagePath }
	 * 
	 */
	public Entity.ImagePath getImagePath() {
		return imagePath;
	}

	/**
	 * Legt den Wert der imagePath-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.ImagePath }
	 * 
	 */
	public void setImagePath(Entity.ImagePath value) {
		this.imagePath = value;
	}

	/**
	 * Ruft den Wert der title-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Legt den Wert der title-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setTitle(String value) {
		this.title = value;
	}

	/**
	 * Ruft den Wert der subType-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSubType() {
		return subType;
	}

	/**
	 * Legt den Wert der subType-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSubType(String value) {
		this.subType = value;
	}

	/**
	 * Ruft den Wert der distinction-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDistinction() {
		return distinction;
	}

	/**
	 * Legt den Wert der distinction-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setDistinction(String value) {
		this.distinction = value;
	}

	/**
	 * Gets the value of the synonym property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the synonym property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSynonym().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getSynonym() {
		if (synonym == null) {
			synonym = new ArrayList<String>();
		}
		return this.synonym;
	}

	/**
	 * Ruft den Wert der noNameSpecifier-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNoNameSpecifier() {
		return noNameSpecifier;
	}

	/**
	 * Legt den Wert der noNameSpecifier-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNoNameSpecifier(String value) {
		this.noNameSpecifier = value;
	}

	/**
	 * Ruft den Wert der tags-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.Tags }
	 * 
	 */
	public Entity.Tags getTags() {
		return tags;
	}

	/**
	 * Legt den Wert der tags-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.Tags }
	 * 
	 */
	public void setTags(Entity.Tags value) {
		this.tags = value;
	}

	/**
	 * Ruft den Wert der fields-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.Fields }
	 * 
	 */
	public Entity.Fields getFields() {
		return fields;
	}

	/**
	 * Legt den Wert der fields-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.Fields }
	 * 
	 */
	public void setFields(Entity.Fields value) {
		this.fields = value;
	}

	/**
	 * Ruft den Wert der properties-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.Properties }
	 * 
	 */
	public Entity.Properties getProperties() {
		return properties;
	}

	/**
	 * Legt den Wert der properties-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.Properties }
	 * 
	 */
	public void setProperties(Entity.Properties value) {
		this.properties = value;
	}

	/**
	 * Ruft den Wert der datings-Eigenschaft ab.
	 * 
	 * @return possible object is {@link Entity.Datings }
	 * 
	 */
	public Entity.Datings getDatings() {
		return datings;
	}

	/**
	 * Legt den Wert der datings-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link Entity.Datings }
	 * 
	 */
	public void setDatings(Entity.Datings value) {
		this.datings = value;
	}

	/**
	 * Ruft den Wert der comment-Eigenschaft ab.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * Legt den Wert der comment-Eigenschaft fest.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setComment(String value) {
		this.comment = value;
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
	 *   &lt;simpleContent&gt;
	 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
	 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
	 *     &lt;/extension&gt;
	 *   &lt;/simpleContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "value" })
	public static class Collection implements Serializable {

		private static final long serialVersionUID = -876988884390071458L;
		@XmlValue
		protected String value;
		@XmlAttribute(name = "id", required = true)
		protected BigInteger id;

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
		 * Ruft den Wert der id-Eigenschaft ab.
		 * 
		 * @return possible object is {@link BigInteger }
		 * 
		 */
		public BigInteger getId() {
			return id;
		}

		/**
		 * Legt den Wert der id-Eigenschaft fest.
		 * 
		 * @param value
		 *            allowed object is {@link BigInteger }
		 * 
		 */
		public void setId(BigInteger value) {
			this.id = value;
		}

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
	 *       &lt;sequence&gt;
	 *         &lt;element name="dating" maxOccurs="unbounded" minOccurs="0"&gt;
	 *           &lt;complexType&gt;
	 *             &lt;simpleContent&gt;
	 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
	 *                 &lt;attribute name="event" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *                 &lt;attribute name="from-day" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
	 *                 &lt;attribute name="to-day" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
	 *               &lt;/extension&gt;
	 *             &lt;/simpleContent&gt;
	 *           &lt;/complexType&gt;
	 *         &lt;/element&gt;
	 *       &lt;/sequence&gt;
	 *     &lt;/restriction&gt;
	 *   &lt;/complexContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
//	@XmlType(name = "", propOrder = { "dating" })
	public static class Datings implements Serializable {

		private static final long serialVersionUID = -5762240858123704606L;
		protected List<Entity.Datings.Dating> dating;

		/**
		 * Gets the value of the dating property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the dating property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getDating().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Entity.Datings.Dating }
		 * 
		 * 
		 */
		public List<Entity.Datings.Dating> getDating() {
			if (dating == null) {
				dating = new ArrayList<Entity.Datings.Dating>();
			}
			return this.dating;
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
		 *       &lt;attribute name="event" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
		 *       &lt;attribute name="from-day" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
		 *       &lt;attribute name="to-day" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
		 *     &lt;/extension&gt;
		 *   &lt;/simpleContent&gt;
		 * &lt;/complexType&gt;
		 * </pre>
		 * 
		 * 
		 */
		@XmlAccessorType(XmlAccessType.FIELD)
		@XmlType(name = "", propOrder = { "value" })
		public static class Dating implements Serializable {

			private static final long serialVersionUID = 2523446744446305686L;
			@XmlValue
			protected String value;
			@XmlAttribute(name = "event", required = true)
			protected String event;
			@XmlAttribute(name = "from-day", required = true)
			protected BigInteger fromDay;
			@XmlAttribute(name = "to-day", required = true)
			protected BigInteger toDay;

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
			 * Ruft den Wert der event-Eigenschaft ab.
			 * 
			 * @return possible object is {@link String }
			 * 
			 */
			public String getEvent() {
				return event;
			}

			/**
			 * Legt den Wert der event-Eigenschaft fest.
			 * 
			 * @param value
			 *            allowed object is {@link String }
			 * 
			 */
			public void setEvent(String value) {
				this.event = value;
			}

			/**
			 * Ruft den Wert der fromDay-Eigenschaft ab.
			 * 
			 * @return possible object is {@link BigInteger }
			 * 
			 */
			public BigInteger getFromDay() {
				return fromDay;
			}

			/**
			 * Legt den Wert der fromDay-Eigenschaft fest.
			 * 
			 * @param value
			 *            allowed object is {@link BigInteger }
			 * 
			 */
			public void setFromDay(BigInteger value) {
				this.fromDay = value;
			}

			/**
			 * Ruft den Wert der toDay-Eigenschaft ab.
			 * 
			 * @return possible object is {@link BigInteger }
			 * 
			 */
			public BigInteger getToDay() {
				return toDay;
			}

			/**
			 * Legt den Wert der toDay-Eigenschaft fest.
			 * 
			 * @param value
			 *            allowed object is {@link BigInteger }
			 * 
			 */
			public void setToDay(BigInteger value) {
				this.toDay = value;
			}

		}

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
	 *       &lt;sequence&gt;
	 *         &lt;element name="field" maxOccurs="unbounded" minOccurs="0"&gt;
	 *           &lt;complexType&gt;
	 *             &lt;simpleContent&gt;
	 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
	 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *               &lt;/extension&gt;
	 *             &lt;/simpleContent&gt;
	 *           &lt;/complexType&gt;
	 *         &lt;/element&gt;
	 *       &lt;/sequence&gt;
	 *     &lt;/restriction&gt;
	 *   &lt;/complexContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement
//	@XmlType(name = "", propOrder = { "fields" })
	public static class Fields implements Serializable {

		private static final long serialVersionUID = 2006000618568294878L;
		protected List<Entity.Fields.Field> field;

		/**
		 * Gets the value of the field property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the field property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getField().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link Entity.Fields.Field }
		 * 
		 * 
		 */
		public List<Entity.Fields.Field> getField() {
			if (field == null) {
				field = new ArrayList<Entity.Fields.Field>();
			}
			return this.field;
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
		public static class Field implements Serializable {

			private static final long serialVersionUID = 5299796800657385861L;
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

			@Override
			public String toString() {
				return "Field [name=" + name + ", value=" + value + "]";
			}

		}

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
	 *   &lt;simpleContent&gt;
	 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
	 *       &lt;attribute name="style" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *     &lt;/extension&gt;
	 *   &lt;/simpleContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "value" })
	public static class ImagePath implements Serializable {

		private static final long serialVersionUID = 6655454101673181191L;
		@XmlValue
		protected String value;
		@XmlAttribute(name = "style", required = true)
		protected String style;

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
		 * Ruft den Wert der style-Eigenschaft ab.
		 * 
		 * @return possible object is {@link String }
		 * 
		 */
		public String getStyle() {
			return style;
		}

		/**
		 * Legt den Wert der style-Eigenschaft fest.
		 * 
		 * @param value
		 *            allowed object is {@link String }
		 * 
		 */
		public void setStyle(String value) {
			this.style = value;
		}

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
	 *       &lt;sequence&gt;
	 *         &lt;element name="property" maxOccurs="unbounded" minOccurs="0"&gt;
	 *           &lt;complexType&gt;
	 *             &lt;simpleContent&gt;
	 *               &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
	 *                 &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *               &lt;/extension&gt;
	 *             &lt;/simpleContent&gt;
	 *           &lt;/complexType&gt;
	 *         &lt;/element&gt;
	 *       &lt;/sequence&gt;
	 *     &lt;/restriction&gt;
	 *   &lt;/complexContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
//	@XmlType(name = "", propOrder = { "property" })
	public static class Properties implements Serializable {

		private static final long serialVersionUID = 2317458202304162661L;
		protected List<Entity.Properties.Property> property;

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
		public List<Entity.Properties.Property> getProperty() {
			if (property == null) {
				property = new ArrayList<Entity.Properties.Property>();
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

			private static final long serialVersionUID = 6291740818789334611L;
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
	 *       &lt;sequence&gt;
	 *         &lt;element name="tag" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/&gt;
	 *       &lt;/sequence&gt;
	 *     &lt;/restriction&gt;
	 *   &lt;/complexContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
//	@XmlType(name = "", propOrder = { "tag" })
	public static class Tags implements Serializable {

		private static final long serialVersionUID = -3000681005275357270L;
		protected List<String> tag;

		/**
		 * Gets the value of the tag property.
		 * 
		 * <p>
		 * This accessor method returns a reference to the live list, not a
		 * snapshot. Therefore any modification you make to the returned list
		 * will be present inside the JAXB object. This is why there is not a
		 * <CODE>set</CODE> method for the tag property.
		 * 
		 * <p>
		 * For example, to add a new item, do as follows:
		 * 
		 * <pre>
		 * getTag().add(newItem);
		 * </pre>
		 * 
		 * 
		 * <p>
		 * Objects of the following type(s) are allowed in the list
		 * {@link String }
		 * 
		 * 
		 */
		public List<String> getTag() {
			if (tag == null) {
				tag = new ArrayList<String>();
			}
			return this.tag;
		}

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
	 *   &lt;simpleContent&gt;
	 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema&gt;string"&gt;
	 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
	 *     &lt;/extension&gt;
	 *   &lt;/simpleContent&gt;
	 * &lt;/complexType&gt;
	 * </pre>
	 * 
	 * 
	 */
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "value" })
	public static class Type implements Serializable {

		private static final long serialVersionUID = -7732299008657058205L;
		@XmlValue
		protected String value;
		@XmlAttribute(name = "id", required = true)
		protected String id;

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

	}

	@Override
	public String toString() {
		return "Entity [id=" + id + ", kind=" + type.getValue() + ", title=" + title + ", distinction=" + distinction + ", synonym=" + synonym + "]";
	}

	@Override
	public int compareTo(Entity o) {
		return this.getId().compareTo(o.getId());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Entity other = (Entity) obj;
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
	
	

}
