package de.prometheus.bildarchiv;

import java.util.Set;

import org.openarchives.beans.Entity;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.Relationship;

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
