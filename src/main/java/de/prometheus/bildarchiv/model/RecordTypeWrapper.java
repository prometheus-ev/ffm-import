package de.prometheus.bildarchiv.model;

import java.util.HashSet;
import java.util.Set;

import org.openarchives.model.Entity;
import org.openarchives.model.MetadataType;
import org.openarchives.model.RecordType;
import org.openarchives.model.Relationship;

import de.prometheus.bildarchiv.util.Endpoint;

public class RecordTypeWrapper {

	private Set<RecordType> records;
	private Set<Entity> entities;
	private Set<Relationship> relationships;

	public RecordTypeWrapper(Set<RecordType> records, Endpoint endpoint) {

		if (!records.isEmpty()) {

			switch (endpoint) {
				
			case ENTITIES:
				setEntities(records);
				break;
			case RELATIONSHIPS:
				setRelationships(records);
				break;
			default:
				this.records = records;
				break;
			
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

	private void setEntities(Set<RecordType> records) {
		System.out.println(records.size());
		Set<Entity> entities = new HashSet<>();
		for (RecordType recordType : records) {
			if (valid(recordType.getMetadata())) {
				Entity entity = recordType.getMetadata().getEntity();
				if (entity != null) {
					entities.add(entity);
				}
			}
		}
		this.entities = entities;
	}

	private void setRelationships(Set<RecordType> records) {
		Set<Relationship> relationships = new HashSet<>();
		for (RecordType recordType : records) {
			if (valid(recordType.getMetadata())) {
				Relationship relationship = recordType.getMetadata().getRelationship();
				if (relationship != null) {
					relationships.add(relationship);
				}
			}
		}
		this.relationships = relationships;
	}

	private boolean valid(MetadataType metadata) {
		return metadata != null;
	}

}
