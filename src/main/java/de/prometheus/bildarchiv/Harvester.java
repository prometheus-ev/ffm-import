package de.prometheus.bildarchiv;

import java.io.File;
import java.util.Date;
import java.util.Properties;

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
import de.prometheus.bildarchiv.util.Endpoint;
import de.prometheus.bildarchiv.util.GentleUtils;

public class Harvester {
	
	private static Logger logger;
	
	public static void main(String[] args) {

		Options options = new Options();
		Option configOption = new Option("c", "config", true, "The configuration directory");
		configOption.setRequired(true);
		Option dataOption = new Option("d", "data", true, "The data directory contains temporary and output files");
		dataOption.setRequired(false);
		
		options.addOption(configOption);
		options.addOption(dataOption);
		
		// default values
		String dataDirectoryPath = "./data";
		String configDirectoryPath = "./conf";
		
		String timestamp = DateFormatUtils.format(new Date(), "yyyy-MM-dd-HH-mm-ss");
		
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
	
			if(cmd.getOptionValue("d") != null) {
				dataDirectoryPath =  cmd.getOptionValue("d");
			}
			if(cmd.getOptionValue("c") != null) {
				configDirectoryPath = cmd.getOptionValue("c");
			}
			
			// Logger configuration
			File configDirectory = new File(configDirectoryPath);
			File log4jXml = new File(configDirectory, "log4j2.xml");
			System.setProperty("log4j.configurationFile", log4jXml.getAbsolutePath());
			logger = LogManager.getLogger(Harvester.class);
							
			// ConedaKor configuration
			File endpointProperties = new File(configDirectory, "endpoint.properties");
			Properties properties = GentleUtils.getProperties(endpointProperties);
			System.setProperty("apiKey", properties.getProperty("apiKey"));
			System.setProperty("baseUrl", properties.getProperty("baseUrl"));
			
			File dataDir = new File(dataDirectoryPath);
			dataDir.mkdir();
			
			GentleTripleGrabber gentleTripleGrabber = new GentleTripleGrabber(dataDir, timestamp);
			try {
				gentleTripleGrabber.listRecords(Endpoint.ENTITIES, null);
				gentleTripleGrabber.listRecords(Endpoint.RELATIONSHIPS, null);
			}
			catch (HttpURLConnectionException e){
				logger.error(e.toString());
			}

		} catch (ParseException e) {
			System.err.println(e.toString()); // logger not configured
		} catch (NoSuchEndpointException e) {
			logger.error(e.toString());
		}
		
		System.exit(0);
	}

}
