package de.prometheus.bildarchiv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openarchives.beans.OAIPMHtype;
import org.openarchives.beans.ObjectFactory;

import de.prometheus.bildarchiv.exception.HttpRequestException;

public final class GentleUtils {

	private static Logger logger = LogManager.getLogger(GentleUtils.class);

	private GentleUtils() {
		// Static utility class
	}

	/**
	 * Returns an JAXBElement of type {@link OAIPMHtype}.
	 * 
	 * @param c {@link HttpURLConnection}
	 * @param URL
	 * @return JAXBElement
	 * @throws JAXBException
	 * @throws IOException
	 */
	public static JAXBElement<OAIPMHtype> getElement(HttpURLConnection connection, final String url) {
		
		HttpURLConnection httpConnection = connection;
		
		if(connection == null) {
			
			if(logger.isInfoEnabled()) { 
				logger.info("httpURLConnection is null... trying to reconnect to [" + url + "]");
			}
			
			if (url != null) {
				try {
					httpConnection = getConnectionFor(url);
				} catch (HttpRequestException e) {
					logger.error(e.toString());
				}
			} 
			
		}
		
		return getlement(httpConnection);
	}

	private static JAXBElement<OAIPMHtype> getlement(HttpURLConnection connection) {
		
		try (InputStream inputStream = connection.getInputStream()){
			
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
	public static HttpURLConnection getConnectionFor(final String url) throws HttpRequestException {
		
		try {
			
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(10000);
			connection.setReadTimeout(10000);
			connection.connect();
			
			int code = connection.getResponseCode();
			
			if (code == 200) {
				return connection;
			} else {
				throw new HttpRequestException("ResponseCode " + code);
			}
				
			
		} catch (IOException e) {
			// interrupt progress bar...
			ProgressBar.error();
			logger.error(e.toString());
		}
		
		return null;
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
	
}
