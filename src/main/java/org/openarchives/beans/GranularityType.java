//
// Diese Datei wurde mit der JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 generiert 
// Siehe <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Änderungen an dieser Datei gehen bei einer Neukompilierung des Quellschemas verloren. 
// Generiert: 2017.02.28 um 03:31:05 PM CET 
//


package org.openarchives.beans;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für granularityType.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * <p>
 * <pre>
 * &lt;simpleType name="granularityType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="YYYY-MM-DD"/&gt;
 *     &lt;enumeration value="YYYY-MM-DDThh:mm:ssZ"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "granularityType")
@XmlEnum
public enum GranularityType {

    @XmlEnumValue("YYYY-MM-DD")
    YYYY_MM_DD("YYYY-MM-DD"),
    @XmlEnumValue("YYYY-MM-DDThh:mm:ssZ")
    YYYY_MM_DD_THH_MM_SS_Z("YYYY-MM-DDThh:mm:ssZ");
    private final String value;

    GranularityType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static GranularityType fromValue(String v) {
        for (GranularityType c: GranularityType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
