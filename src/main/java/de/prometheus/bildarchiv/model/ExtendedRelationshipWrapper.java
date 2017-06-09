package de.prometheus.bildarchiv.model;

import java.util.Collections;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class ExtendedRelationshipWrapper {
	
	@XmlElementWrapper(name="relationships")
	@XmlElement(name = "relationship")
	protected Set<ExtendedRelationship> relationships;
	
	public ExtendedRelationshipWrapper() { }
	
	public ExtendedRelationshipWrapper(Set<ExtendedRelationship> relationships) { 
		this.relationships = relationships;
	}
	
	public void setRelationships(Set<ExtendedRelationship> relationships) {
		this.relationships = relationships;
	}
	
	public Set<ExtendedRelationship> getRelations() {
		return Collections.unmodifiableSet(relationships);
	}

	@Override
	public String toString() {
		return "ExtendedRelationshipWrapper [relationships=" + relationships.size() + "]";
	}
	
	
	
	

}
