package org.openarchives.beans;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Prometheus {
	
	@XmlElementWrapper(name="relationships")
	@XmlElement(name = "relationship")
	protected Set<ExtendedRelationship> relationships;
	
	public Prometheus() {
	}
	
	public void setRelationships(Set<ExtendedRelationship> relationships) {
		this.relationships = relationships;
	}
	
	public Set<ExtendedRelationship> getRelations() {
		return Collections.unmodifiableSet(relationships);
	}

	@Override
	public String toString() {
		return "Prometheus [relationships=" + relationships.size() + "]";
	}
	
	
	
	

}
