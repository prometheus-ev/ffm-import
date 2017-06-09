package de.prometheus.bildarchiv.model;

import java.util.Set;

import org.openarchives.model.Entity;
import org.openarchives.model.RecordType;
import org.openarchives.model.Relationship;

public class RecordTypeWrapper {
	
	private Set<RecordType> records;
	private Set<Entity> entities;
	private Set<Relationship> relationships;
	
	@SuppressWarnings("unchecked")
	public RecordTypeWrapper(Set<RecordType> records) {
		
		if(!records.isEmpty()) {
			
			if (((Object) records.iterator().next()) instanceof Entity) {
				
				entities = (Set<Entity>) (Set<?>) records;
			
			} else if(((Object) records.iterator().next()) instanceof Relationship) {
			
				relationships = (Set<Relationship>) (Set<?>) records;
			
			} else {
			
				this.records = records;
		
			}
		}

	}
	
	public Set<Entity> getEntities() {
		return entities;
	}

	public Set<Relationship> getRelationships() {
		return relationships;
	}
	
	public Set<RecordType> getRecords() {
		return records;
	}
	
}
