package de.prometheus.bildarchiv.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.model.OAIPMHtype;
import org.openarchives.model.ObjectFactory;

import de.prometheus.bildarchiv.exception.HttpRequestException;
import de.prometheus.bildarchiv.exception.HttpURLConnectionException;
import de.prometheus.bildarchiv.model.ExtendedRelationship;
import de.prometheus.bildarchiv.model.ExtendedRelationshipWrapper;
import de.prometheus.bildarchiv.model.FFMConedakorSource;
import de.prometheus.bildarchiv.model.Work;

public final class GentleUtils {

	private static Logger logger = LogManager.getLogger(GentleUtils.class);

	private GentleUtils() {
		// Static utility class
	}

	/**
	 * Returns an JAXBElement of type {@link OAIPMHtype}.
	 * 
	 * @param c
	 *            {@link HttpURLConnection}
	 * @param URL
	 * @return JAXBElement
	 * @throws HttpRequestException 
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static JAXBElement<OAIPMHtype> unmarshalOAIPMHtype(HttpURLConnection connection, final String url) throws HttpRequestException, HttpURLConnectionException {

		HttpURLConnection httpConnection = connection;

		if (connection == null) {

			if (logger.isInfoEnabled()) {
				logger.info("httpURLConnection is null... trying to reconnect to [" + url + "]");
			}

			if (url != null) {
				httpConnection = getHttpURLConnection(url);
			}

		}

		return unmarshalOAIPMHtype(httpConnection);
	}

	private static JAXBElement<OAIPMHtype> unmarshalOAIPMHtype(HttpURLConnection connection) {

		try (InputStream inputStream = connection.getInputStream()) {

			JAXBContext jaxbContext = JAXBContext.newInstance(OAIPMHtype.class, ObjectFactory.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			StreamSource streamSource = new StreamSource(new BufferedInputStream(inputStream));

			return jaxbUnmarshaller.unmarshal(streamSource, OAIPMHtype.class);

		} catch (IOException e) {
			logger.error(e.toString());
		} catch (JAXBException e) {
			logger.error(e.toString());
		}

		return null;
	}

	/**
	 * Returns a {@link HttpURLConnection} for a given URL.
	 * 
	 * @param url
	 *            {@link String}
	 * @return {@link HttpURLConnection}
	 * @throws HttpRequestException
	 * @throws IOException
	 */
	public static HttpURLConnection getHttpURLConnection(final String url) throws HttpRequestException, HttpURLConnectionException {

		HttpURLConnection connection;

		// try a maximum of three times to establish a connection
		for (int retries = 0; retries < 3; retries++) {
			try {
				connection = (HttpURLConnection) new URL(url).openConnection();
				connection.setRequestMethod("GET");
//				connection.setConnectTimeout(10000);
//				connection.setReadTimeout(10000);
				connection.setConnectTimeout(0); // infinite timeout
				connection.setReadTimeout(0); // infinite timeout
				connection.connect();
				
				int code = connection.getResponseCode();

				if (code == 200) {
					return connection;
				} else {
					throw new HttpRequestException("Response Code " + code + " url=" +url);
				}

			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
		throw new HttpURLConnectionException("Connection to url " +  url + " could not be established. Resume harvest with resumption token.");	
	}

	/**
	 * 
	 * @param propertiesFile
	 * @return
	 */
	public static Properties getProperties(final File propertiesFile) {

		try (BufferedReader reader = new BufferedReader(new FileReader(propertiesFile))) {

			Properties properties = new Properties();
			properties.load(reader);

			return properties;

		} catch (Exception e) {
			logger.error("Unable to load " + propertiesFile, e.toString());
		}

		return null;
	}

	/**
	 * 
	 * @param toXml
	 * @param file
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	public static void toXml(Set<ExtendedRelationship> relationships, File directory, String timestamp)
			throws JAXBException, PropertyException {

//		String fileName = "extended_relationships_" + getTimeStamp() + ".xml";
		String fileName = "extended_relationships_" + timestamp + ".xml";

		if (logger.isInfoEnabled()) {
			logger.info("Creating extended relationship xml file " + fileName);
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(ExtendedRelationshipWrapper.class);
		JAXBElement<ExtendedRelationshipWrapper> element = new JAXBElement<ExtendedRelationshipWrapper>(
				new QName("http://www.openarchives.org/OAI/2.0/", "ffmConedaKor"), ExtendedRelationshipWrapper.class,
				new ExtendedRelationshipWrapper(relationships));

		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(element, new File(directory, fileName));

	}

	/**
	 * 
	 * @param works
	 * @param extendedRelsFile
	 * @throws JAXBException
	 * @throws PropertyException
	 */
	public static void finalExport(final Set<Work> works, final File dir, String timestamp) throws JAXBException, PropertyException {

//		final String fileName = "conedakor_source_" + getTimeStamp() + ".xml";
		final String fileName = "conedakor_source_" + timestamp + ".xml";

		if (logger.isInfoEnabled()) {
			logger.info("Creating final source file " + fileName);
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(FFMConedakorSource.class);
		JAXBElement<FFMConedakorSource> element = new JAXBElement<FFMConedakorSource>(
				new QName("http://www.prometheus-bildarchiv.de/", "ffmConedaKor"), FFMConedakorSource.class,
				new FFMConedakorSource(works));
		
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		marshaller.marshal(element, new File(dir, fileName));

		if (logger.isInfoEnabled()) {
			logger.info("Done!");
		}

	}

	private static String getTimeStamp() {
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
	}

	/**
	 * 
	 * @param importFile
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws FileNotFoundException
	 * @throws JAXBException
	 */
	public static Set<ExtendedRelationship> unmarshalRelationships(final File file)
			throws InterruptedException, ExecutionException, FileNotFoundException, JAXBException {

		JAXBContext jaxbContext = JAXBContext.newInstance(ExtendedRelationshipWrapper.class);
		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		StreamSource streamSource = new StreamSource(
				new BufferedInputStream(new FileInputStream(new File(file.getAbsolutePath()))));
		JAXBElement<ExtendedRelationshipWrapper> prom = jaxbUnmarshaller.unmarshal(streamSource, ExtendedRelationshipWrapper.class);
		ExtendedRelationshipWrapper prometheus = prom.getValue();

		return prometheus.getRelations();
	}

}
