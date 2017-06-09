//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.28 um 03:31:05 PM CET 
//


package org.openarchives.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für OAI-PMHerrorcodeType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="OAI-PMHerrorcodeType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="cannotDisseminateFormat"/&gt;
 *     &lt;enumeration value="idDoesNotExist"/&gt;
 *     &lt;enumeration value="badArgument"/&gt;
 *     &lt;enumeration value="badVerb"/&gt;
 *     &lt;enumeration value="noMetadataFormats"/&gt;
 *     &lt;enumeration value="noRecordsMatch"/&gt;
 *     &lt;enumeration value="badResumptionToken"/&gt;
 *     &lt;enumeration value="noSetHierarchy"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "OAI-PMHerrorcodeType")
@XmlEnum
public enum OAIPMHerrorcodeType {

    @XmlEnumValue("cannotDisseminateFormat")
    CANNOT_DISSEMINATE_FORMAT("cannotDisseminateFormat"),
    @XmlEnumValue("idDoesNotExist")
    ID_DOES_NOT_EXIST("idDoesNotExist"),
    @XmlEnumValue("badArgument")
    BAD_ARGUMENT("badArgument"),
    @XmlEnumValue("badVerb")
    BAD_VERB("badVerb"),
    @XmlEnumValue("noMetadataFormats")
    NO_METADATA_FORMATS("noMetadataFormats"),
    @XmlEnumValue("noRecordsMatch")
    NO_RECORDS_MATCH("noRecordsMatch"),
    @XmlEnumValue("badResumptionToken")
    BAD_RESUMPTION_TOKEN("badResumptionToken"),
    @XmlEnumValue("noSetHierarchy")
    NO_SET_HIERARCHY("noSetHierarchy");
    private final String value;

    OAIPMHerrorcodeType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static OAIPMHerrorcodeType fromValue(String v) {
        for (OAIPMHerrorcodeType c: OAIPMHerrorcodeType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
