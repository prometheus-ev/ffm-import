package de.prometheus.bildarchiv;

import java.io.IOException;
import java.net.HttpURLConnection;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Test;
import org.openarchives.beans.Relationship;

public class RelationsTests {

	@Test
	public void unmarshall() throws IOException, JAXBException {
		HttpURLConnection connection = GentleUtils.getConnectionFor(Endpoint.RELATIONSHIPS.getRecord("4f29aa63-4147-43a6-8297-a4caaec7aefb"));
		Relationship relationship = GentleUtils.getElement(connection).getValue().getGetRecord().getRecord().getMetadata().getRelationship();
		Assert.assertNotNull(relationship);
		Assert.assertNotNull(relationship.getProperties());
		Assert.assertNotNull(relationship.getProperties().getProperty());
		Assert.assertEquals("Detail des Gesichts", relationship.getProperties().getProperty().get(0).getValue());
	}
	
}