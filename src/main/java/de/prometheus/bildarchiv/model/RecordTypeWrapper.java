package de.prometheus.bildarchiv.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.model.Entity;
import org.openarchives.model.MetadataType;
import org.openarchives.model.RecordType;
import org.openarchives.model.Relationship;
import org.openarchives.model.StatusType;

import de.prometheus.bildarchiv.GentleSegmentMerger;
import de.prometheus.bildarchiv.util.Endpoint;

public class RecordTypeWrapper {
	
	private Logger logger = LogManager.getLogger(GentleSegmentMerger.class);

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
		int deletedEntitiesCount = 0;
		Set<Entity> entities = new HashSet<>();
		for (RecordType recordType : records) {
			if (recordType.getHeader().getStatus() == null || !(recordType.getHeader().getStatus().equals(StatusType.DELETED))) {
				if (valid(recordType.getMetadata())) {
					Entity entity = recordType.getMetadata().getEntity();
					if (entity != null) {
						entities.add(entity);
					}
				}
			} else if (recordType.getHeader().getStatus().equals(StatusType.DELETED)) {
				deletedEntitiesCount++;
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info(deletedEntitiesCount + " entities were marked as deleted and will be ignored");
		}
		
		this.entities = entities;
	}

	private void setRelationships(Set<RecordType> records) {
		int deletedRelationshipsCount = 0;
		Set<Relationship> relationships = new HashSet<>();
		for (RecordType recordType : records) {
			if (recordType.getHeader().getStatus() == null || !(recordType.getHeader().getStatus().equals(StatusType.DELETED))) {
				if (valid(recordType.getMetadata())) {
					Relationship relationship = recordType.getMetadata().getRelationship();
					if (relationship != null) {
						relationships.add(relationship);
					}
				}
			} else if (recordType.getHeader().getStatus().equals(StatusType.DELETED)) {
				deletedRelationshipsCount++;
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info(deletedRelationshipsCount + " entities were marked as deleted and will be ignored");
		}
		
		this.relationships = relationships;
	}

	private boolean valid(MetadataType metadata) {
		return metadata != null;
	}

}
