package de.prometheus.bildarchiv;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.openarchives.beans.Entity;
import org.openarchives.beans.RecordType;
import org.openarchives.beans.Relationship;

public class RecordTypeWrapper {
	
	private Set<RecordType> records;

	public RecordTypeWrapper(Set<RecordType> records) {
		this.records = records;
	}
	
	public List<Entity> getEntities() {
		List<Entity> toReturn = new ArrayList<>();
		for (RecordType recordType : records) {
			if (valid(recordType)) {
				toReturn.add(recordType.getMetadata().getEntity());
			}
		}
		return toReturn;
	}

	public List<Relationship> getRelationships() {
		List<Relationship> toReturn = new ArrayList<>();
		for (RecordType recordType : records) {
			if(valid(recordType)) {
				toReturn.add(recordType.getMetadata().getRelationship());
			}
		}
		return toReturn;
	}
	
	private boolean valid(RecordType recordType) {
		return recordType != null && recordType.getHeader() != null && recordType.getMetadata() != null;
	}
	
}
