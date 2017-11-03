package de.prometheus.bildarchiv;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.prometheus.bildarchiv.exception.HttpURLConnectionException;
import de.prometheus.bildarchiv.exception.NoSuchEndpointException;
import de.prometheus.bildarchiv.model.ExtendedRelationship;
import de.prometheus.bildarchiv.util.Endpoint;
import de.prometheus.bildarchiv.util.GentleUtils;

public class Application {

	private static Logger logger;

	/**
	 * <p> A gentle command line tool for harvesting OAI-PMH XML data provided by
	 * <a href="https://github.com/coneda/kor">ConedaKOR</a></p>
	 * <code>-Xms1g -Xmx2g -jar ffm.jar -c ./config -d ./data </code>
	 * @param args
	 */
	public static void main(String[] args) {
			
		Options options = new Options();
		
		Option configOption = new Option("c", "config", true, "The configuration directory");
		configOption.setRequired(true);
		
		Option dataOption = new Option("d", "data", true, "The data directory contains temporary and output files");
		dataOption.setRequired(false);
		
		options.addOption(configOption);
		options.addOption(dataOption);
		
		String dataDirectoryPath = "/data";
		String configDirectoryPath = "/conf";
		
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.getOptionValue("c") != null) {
				configDirectoryPath =  cmd.getOptionValue("c");
			}
			File configDir = new File(configDirectoryPath);

			// Logger configuration
			File log4jXml = new File(configDir, "log4j2.xml");
			System.setProperty("log4j.configurationFile", log4jXml.getAbsolutePath());
			logger = LogManager.getLogger(Application.class);
								
			// ConedaKor configuration
			File endpointProperties = new File(configDir, "endpoint.properties");
			Properties properties = GentleUtils.getProperties(endpointProperties);
			System.setProperty("apiKey", properties.getProperty("apiKey"));
			System.setProperty("baseUrl", properties.getProperty("baseUrl"));
			
			if(cmd.getOptionValue("d") != null) {
				dataDirectoryPath =  cmd.getOptionValue("d");
			}
			
			// clean data directories
			File dataEnt = new File(dataDirectoryPath, Endpoint.ENTITIES.name());
			if (dataEnt.exists() && dataEnt.isDirectory()){
				try {
					FileUtils.deleteDirectory(dataEnt);
				} catch (IOException e) {
					logger.error(e.toString());
				}
			}
			File dataRel = new File(dataDirectoryPath, Endpoint.RELATIONSHIPS.name());
			if (dataRel.exists() && dataRel.isDirectory()){
				try {
					FileUtils.deleteDirectory(dataRel);
				} catch (IOException e) {
					logger.error(e.toString());
				}
			}
			
			// harvest from ConedaKor and transform for Prometheus
			GentleTripleGrabber gentleTripleGrabber = new GentleTripleGrabber(dataDirectoryPath);
			try {
				// harvest entities and relationships from ConedaKor
				gentleTripleGrabber.listRecords(Endpoint.ENTITIES, null);
				gentleTripleGrabber.listRecords(Endpoint.RELATIONSHIPS, null);
				
				// merge entities and relationships to extended relationships
				GentleSegmentMerger gentleSegmentMerger = new GentleSegmentMerger(dataDirectoryPath);
				Set<ExtendedRelationship> xtendedRelationships = gentleSegmentMerger.mergeEntitiesAndRelationships();

				// transform extended relationships for Prometheus
				GentleDataExtractor gentleDataExtractor = new GentleDataExtractor(xtendedRelationships, dataDirectoryPath);
				gentleDataExtractor.extractData();
			}
			catch (HttpURLConnectionException e){
				logger.error(e.toString());
			}

		} catch (ParseException e) {
			System.err.println(e.toString()); // logger not configured
		} catch (FileNotFoundException e) {
			logger.error(e.toString());
		} catch (JAXBException e) {
			logger.error(e.toString());
		} catch (NoSuchEndpointException e) {
			logger.error(e.toString());
		}
		
	}

}
