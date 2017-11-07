package de.prometheus.bildarchiv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import java.util.Set;

import javax.xml.bind.JAXBException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.prometheus.bildarchiv.model.ExtendedRelationship;
import de.prometheus.bildarchiv.util.GentleUtils;

public class Transformer {

	private static Logger logger;

	public static void main (String[] args) {

			Options options = new Options();
			Option dataOption = new Option("d", "data", true, "The data directory contains temporary and output files");
			dataOption.setRequired(false);
			options.addOption(dataOption);
			Option configOption = new Option("c", "config", true, "The configuration directory");
			configOption.setRequired(true);
			options.addOption(configOption);		

			// default values
			String dataDirectoryPath = "/data";
			String configDirectoryPath = "/conf";
		
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.getOptionValue("d") != null) {
				dataDirectoryPath =  cmd.getOptionValue("d");
			}
		
			if(cmd.getOptionValue("c") != null) {
				configDirectoryPath = cmd.getOptionValue("c");
			}
			
			File configDirectory = new File(configDirectoryPath);

			// logger configuration
			File log4jXml = new File(configDirectory, "log4j2.xml");
			System.setProperty("log4j.configurationFile", log4jXml.getAbsolutePath());
			logger = LogManager.getLogger(Transformer.class);
			
			// ConedaKor configuration; necessary in case of incomplete or selective harvest
			File endpointProperties = new File(configDirectory, "endpoint.properties");
			Properties properties = GentleUtils.getProperties(endpointProperties);
			System.setProperty("apiKey", properties.getProperty("apiKey"));
			System.setProperty("baseUrl", properties.getProperty("baseUrl"));
			
			// merging ConedaKor entities and relationships into extended relationships					
			GentleSegmentMerger gentleSegmentMerger = new GentleSegmentMerger(dataDirectoryPath);
			Set<ExtendedRelationship> xtendedRelationships = gentleSegmentMerger.mergeEntitiesAndRelationships();

			// transforming extended relationships for Prometheus 
			GentleDataExtractor gentleDataExtractor = new GentleDataExtractor(xtendedRelationships, dataDirectoryPath);
			gentleDataExtractor.extractData();

		} catch (ParseException e) {
			System.err.println(e.toString()); // logger not configured
		} catch (FileNotFoundException e) {
			logger.error(e.toString());
		} catch (JAXBException e) {
			logger.error(e.toString());
		}
		
		System.exit(0);

	}

}
