package de.prometheus.bildarchiv;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Properties;

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

import de.prometheus.bildarchiv.exception.NoSuchEndpointException;

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
		
		Option destinationOption = new Option("d", "data", true, "The data directory contains temporary and output files");
		destinationOption.setRequired(false);
		
		options.addOption(configOption);
		options.addOption(destinationOption);
		
		CommandLineParser parser = new DefaultParser();
		
		String dataDirectory = "/tmp";
		
		try {
			
			CommandLine cmd = parser.parse(options, args);
			
			File configDir = new File(cmd.getOptionValue("c"));

			// Logger configuration
			File log4jXml = new File(configDir, "log4j2.xml");
			System.setProperty("log4j.configurationFile", log4jXml.getAbsolutePath());
			logger = LogManager.getLogger(GentleTripleGrabber.class);
			
			// ConedaKor configuration
			File endpointProperties = new File(configDir, "endpoint.properties");
			Properties properties = GentleUtils.getProperties(endpointProperties);
			System.setProperty("apiKey", properties.getProperty("apiKey"));
			System.setProperty("baseUrl", properties.getProperty("baseUrl"));
			
			if(cmd.getOptionValue("d") != null) {
				dataDirectory =  cmd.getOptionValue("d");
			}
			
			String extendedRelsXml = DateFormatUtils.format(new Date(), "dd-MM-yyyy") + "_extended_relationships.xml";
			
			if(logger.isInfoEnabled()) { 
				logger.info("Final export file " + extendedRelsXml + " will be saved to " + dataDirectory);
			}
			
			GentleTripleGrabber gentleTripleGrabber = new GentleTripleGrabber(dataDirectory);
			gentleTripleGrabber.listRecords(Endpoint.ENTITIES);
			gentleTripleGrabber.listRecords(Endpoint.RELATIONSHIPS);

			GentleSegmentMerger gentleSegmentMerger = new GentleSegmentMerger(dataDirectory, extendedRelsXml);
			File extendedRelsFile = gentleSegmentMerger.mergeEntitiesAndRelationships();

			GentleDataExtractor gentleDataExtractor = new GentleDataExtractor(extendedRelsFile);
			gentleDataExtractor.extractData();

		} catch (JAXBException e) {
			logger.error(e.toString());
		} catch (NoSuchEndpointException e) {
			logger.error(e.toString());
		} catch (ParseException e) {
			logger.error(e.toString());
		} catch (FileNotFoundException e) {
			logger.error(e.toString());
		} finally {
			// Delete temporary created files on exit
			File tmpEnt = new File(dataDirectory, Endpoint.ENTITIES.name());
			File tmpRel = new File(dataDirectory, Endpoint.RELATIONSHIPS.name());
			tmpEnt.deleteOnExit();
			tmpRel.deleteOnExit();
		}
	}

}
