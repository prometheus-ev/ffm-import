//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.28 um 03:31:05 PM CET 
//


package org.openarchives.beans;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.openarchive.oai.beans package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _OAIPMH_QNAME = new QName("http://www.openarchives.org/OAI/2.0/", "OAI-PMH");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.openarchive.oai.beans
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Relationship }
     * 
     */
    public Relationship createRelationship() {
        return new Relationship();
    }

    /**
     * Create an instance of {@link Entity }
     * 
     */
    public Entity createEntity() {
        return new Entity();
    }

    /**
     * Create an instance of {@link Entity.Datings }
     * 
     */
    public Entity.Datings createEntityDatings() {
        return new Entity.Datings();
    }

    /**
     * Create an instance of {@link Entity.Properties }
     * 
     */
    public Entity.Properties createEntityProperties() {
        return new Entity.Properties();
    }

    /**
     * Create an instance of {@link Entity.Fields }
     * 
     */
    public Entity.Fields createEntityFields() {
        return new Entity.Fields();
    }

    /**
     * Create an instance of {@link org.openarchives.oai.beans.Relation }
     * 
     */
    public Relation createRelation() {
        return new Relation();
    }

    /**
     * Create an instance of {@link Kind }
     * 
     */
    public Kind createKind() {
        return new Kind();
    }

    /**
     * Create an instance of {@link OAIPMHtype }
     * 
     */
    public OAIPMHtype createOAIPMHtype() {
        return new OAIPMHtype();
    }

    /**
     * Create an instance of {@link Fieldtype }
     * 
     */
    public Fieldtype createFieldtype() {
        return new Fieldtype();
    }

    /**
     * Create an instance of {@link Generatortype }
     * 
     */
    public Generatortype createGeneratortype() {
        return new Generatortype();
    }

    /**
     * Create an instance of {@link Typetype }
     * 
     */
    public Typetype createTypetype() {
        return new Typetype();
    }

    /**
     * Create an instance of {@link RequestType }
     * 
     */
    public RequestType createRequestType() {
        return new RequestType();
    }

    /**
     * Create an instance of {@link OAIPMHerrorType }
     * 
     */
    public OAIPMHerrorType createOAIPMHerrorType() {
        return new OAIPMHerrorType();
    }

    /**
     * Create an instance of {@link IdentifyType }
     * 
     */
    public IdentifyType createIdentifyType() {
        return new IdentifyType();
    }

    /**
     * Create an instance of {@link ListMetadataFormatsType }
     * 
     */
    public ListMetadataFormatsType createListMetadataFormatsType() {
        return new ListMetadataFormatsType();
    }

    /**
     * Create an instance of {@link ListSetsType }
     * 
     */
    public ListSetsType createListSetsType() {
        return new ListSetsType();
    }

    /**
     * Create an instance of {@link GetRecordType }
     * 
     */
    public GetRecordType createGetRecordType() {
        return new GetRecordType();
    }

    /**
     * Create an instance of {@link ListRecordsType }
     * 
     */
    public ListRecordsType createListRecordsType() {
        return new ListRecordsType();
    }

    /**
     * Create an instance of {@link ListIdentifiersType }
     * 
     */
    public ListIdentifiersType createListIdentifiersType() {
        return new ListIdentifiersType();
    }

    /**
     * Create an instance of {@link RecordType }
     * 
     */
    public RecordType createRecordType() {
        return new RecordType();
    }

    /**
     * Create an instance of {@link HeaderType }
     * 
     */
    public HeaderType createHeaderType() {
        return new HeaderType();
    }

    /**
     * Create an instance of {@link MetadataType }
     * 
     */
    public MetadataType createMetadataType() {
        return new MetadataType();
    }

    /**
     * Create an instance of {@link AboutType }
     * 
     */
    public AboutType createAboutType() {
        return new AboutType();
    }

    /**
     * Create an instance of {@link ResumptionTokenType }
     * 
     */
    public ResumptionTokenType createResumptionTokenType() {
        return new ResumptionTokenType();
    }

    /**
     * Create an instance of {@link DescriptionType }
     * 
     */
    public DescriptionType createDescriptionType() {
        return new DescriptionType();
    }

    /**
     * Create an instance of {@link MetadataFormatType }
     * 
     */
    public MetadataFormatType createMetadataFormatType() {
        return new MetadataFormatType();
    }

    /**
     * Create an instance of {@link SetType }
     * 
     */
    public SetType createSetType() {
        return new SetType();
    }

    /**
     * Create an instance of {@link Relationship.Relation }
     * 
     */
    public Relationship.Relation createRelationshipRelation() {
        return new Relationship.Relation();
    }

    /**
     * Create an instance of {@link Entity.Collection }
     * 
     */
    public Entity.Collection createEntityCollection() {
        return new Entity.Collection();
    }

    /**
     * Create an instance of {@link Entity.Type }
     * 
     */
    public Entity.Type createEntityType() {
        return new Entity.Type();
    }

    /**
     * Create an instance of {@link Entity.ImagePath }
     * 
     */
    public Entity.ImagePath createEntityImagePath() {
        return new Entity.ImagePath();
    }

    /**
     * Create an instance of {@link Entity.Tags }
     * 
     */
    public Entity.Tags createEntityTags() {
        return new Entity.Tags();
    }

    /**
     * Create an instance of {@link Entity.Datings.Dating }
     * 
     */
    public Entity.Datings.Dating createEntityDatingsDating() {
        return new Entity.Datings.Dating();
    }
    
    
    public Relation.FromTypes.Type createRelationFromTypesType() {
        return new Relation.FromTypes.Type();
    }

    /**
     * Create an instance of {@link Entity.Properties.Property }
     * 
     */
    public Entity.Properties.Property createEntityPropertiesProperty() {
        return new Entity.Properties.Property();
    }

    /**
     * Create an instance of {@link Entity.Fields.Field }
     * 
     */
    public Entity.Fields.Field createEntityFieldsField() {
        return new Entity.Fields.Field();
    }

    /**
     * Create an instance of {@link org.openarchives.oai.beans.Relation.FromTypes }
     * 
     */
    public Relation.FromTypes createRelationFromTypes() {
        return new Relation.FromTypes();
    }

    /**
     * Create an instance of {@link org.openarchives.oai.beans.Relation.ToTypes }
     * 
     */
    public Relation.ToTypes createRelationToTypes() {
        return new Relation.ToTypes();
    }

    /**
     * Create an instance of {@link Kind.Fields }
     * 
     */
    public Kind.Fields createKindFields() {
        return new Kind.Fields();
    }

    /**
     * Create an instance of {@link Kind.Generators }
     * 
     */
    public Kind.Generators createKindGenerators() {
        return new Kind.Generators();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link OAIPMHtype }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.openarchives.org/OAI/2.0/", name = "OAI-PMH")
    public JAXBElement<OAIPMHtype> createOAIPMH(OAIPMHtype value) {
        return new JAXBElement<OAIPMHtype>(_OAIPMH_QNAME, OAIPMHtype.class, null, value);
    }

}
