package de.prometheus.bildarchiv;

import java.io.File;
import java.io.FileNotFoundException;
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
import org.apache.commons.lang3.time.DateFormatUtils;
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
		configOption.setRequired(false);
		
		Option dataOption = new Option("d", "data", true, "The data directory contains temporary and output files");
		dataOption.setRequired(false);
		
		Option timestampOption = new Option("ts", "timestamp", true, "The timestamp for the import");
		timestampOption.setRequired(false);
		
		options.addOption(configOption);
		options.addOption(dataOption);
		options.addOption(timestampOption);
		
		
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			
			String configDirectoryPath = "./conf";
			
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
			
			String dataDirectoryPath = "./data";
			
			if(cmd.getOptionValue("d") != null) {
				dataDirectoryPath =  cmd.getOptionValue("d");
			}
			
			File dataDir = new File(dataDirectoryPath);
			dataDir.mkdir();
			
			String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
			
			if(cmd.getOptionValue("ts") != null) {
				timestamp =  cmd.getOptionValue("ts");
			}
			
			// harvest from ConedaKor and transform for Prometheus
			GentleTripleGrabber gentleTripleGrabber = new GentleTripleGrabber(dataDir, timestamp);
			try {
				// harvest entities and relationships from ConedaKor
				gentleTripleGrabber.listRecords(Endpoint.ENTITIES, null);
				gentleTripleGrabber.listRecords(Endpoint.RELATIONSHIPS, null);
				
				// merge entities and relationships to extended relationships
				GentleSegmentMerger gentleSegmentMerger = new GentleSegmentMerger(dataDir, timestamp);
				Set<ExtendedRelationship> xtendedRelationships = gentleSegmentMerger.mergeEntitiesAndRelationships();

				// transform extended relationships for Prometheus
				GentleDataExtractor gentleDataExtractor = new GentleDataExtractor(dataDir, timestamp);
				gentleDataExtractor.extractData(xtendedRelationships);
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
		
		System.exit(0);
		
	}

}
