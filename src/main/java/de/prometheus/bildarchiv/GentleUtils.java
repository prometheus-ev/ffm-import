package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.ObjectFactory;

public final class GentleUtils {
	
	private static final Logger logger = LogManager.getLogger(GentleUtils.class);
	
	private GentleUtils() {}
	
	/**
	 * Returns an JAXBElement of type {@link OAIPMHtype}.
	 * @param c {@link HttpURLConnection}
	 * @param URL 
	 * @return JAXBElement
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static JAXBElement<OAIPMHtype> getElement(HttpURLConnection c, String url) throws JAXBException, IOException {
		if(c == null && url != null) {
			logger.error("HttpURLConnection is null...");
			logger.error("url: " + url);
			Scanner scanner = new Scanner(new URL(url).openStream());
			StringBuffer sb = new StringBuffer();
			while(scanner.hasNext()) {
				sb.append(scanner.nextLine());
			}
			scanner.close();
			logger.error(sb.toString());
		}
		InputStream inputStream = c.getInputStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(OAIPMHtype.class, ObjectFactory.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StreamSource streamSource = new StreamSource(new BufferedInputStream(inputStream));
		return jaxbUnmarshaller.unmarshal(streamSource, OAIPMHtype.class);
	}

	/**
	 * Returns a {@link HttpURLConnection} for a given URL.
	 * @param url {@link String}
	 * @return {@link HttpURLConnection}
	 * @throws IOException
	 */
	public static HttpURLConnection getConnectionFor(String url) {
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.connect();
			int code = connection.getResponseCode();
			if (code == 200)
				return connection;
		} catch (IOException e) {
			logger.error(e.getMessage());
		} 
		return null;
	}

}
