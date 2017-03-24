package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.ObjectFactory;

public final class GentleUtils {
	
	private GentleUtils() {}
	
	/**
	 * Returns an JAXBElement of type {@link OAIPMHtype}.
	 * @param c {@link HttpURLConnection}
	 * @return JAXBElement
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static JAXBElement<OAIPMHtype> getElement(HttpURLConnection c) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(OAIPMHtype.class, ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StreamSource streamSource = new StreamSource(new BufferedInputStream(c.getInputStream()));
		return jaxbUnmarshaller.unmarshal(streamSource, OAIPMHtype.class);
	}

	/**
	 * Returns a {@link HttpURLConnection} for a given url.
	 * @param url {@link String}
	 * @return {@link HttpURLConnection}
	 * @throws IOException
	 */
	public static HttpURLConnection getConnectionFor(String url) throws IOException {
		HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setRequestMethod("GET");
		connection.connect();
		int code = connection.getResponseCode();
		if (code == 200)
			return connection;
		return null;
	}

}
