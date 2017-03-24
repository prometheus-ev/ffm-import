package de.prometheus.bildarchiv;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public enum Endpoint {
	
	ENTITIES("entities?"), KINDS("kinds?"), RELATIONS("relations?"), RELATIONSHIPS("relationships?");

	private final String name;
	private final String prefix = "&metadataPrefix=kor";
	private final String listRecords = "verb=ListRecords";
	private final String listIdentifiers = "verb=ListIdentifiers";
	private final String getRecord = "verb=GetRecord&identifier=";
	private final String resumptionToken = "&resumptionToken=";
	
	private String baseUrl = "";
	private String apiKey = "&api_key=";

	private static final Logger logger = LogManager.getLogger(Endpoint.class);
	private static Properties properties;
	
	Endpoint(String name) {
		this.name = name;
		init();
	}

	public String getRecord(final String identifier) {
		if (identifier == null)
			return null;
		return baseUrl + name + getRecord + identifier + prefix + apiKey;
	}

	public String listRecords() {
		return baseUrl + name + listRecords + prefix + apiKey;
	}

	public String listRecords(final String token) {
		if (token == null)
			return null;
		return baseUrl + name + listRecords + prefix + apiKey + resumptionToken + token;
	}

	public String listIdentifiers() {
		return baseUrl + name + listIdentifiers + prefix + apiKey;
	}

	public String listIdentifiers(final String token) {
		if (token == null)
			return null;
		return baseUrl + name + listIdentifiers + prefix + apiKey + resumptionToken + token;
	}
	
	private void init() {
		final String propertiesFile = "/de/prometheus/bildarchiv/properties/endpoint.properties";
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(Endpoint.class.getResourceAsStream(propertiesFile));
            }
            catch (Exception e) {
                logger.error("Unable to load api key from " + propertiesFile , e);
                System.exit(1);
            }
        }
        this.apiKey += (String) properties.get("apiKey");
        this.baseUrl = (String) properties.get("baseUrl");
    }

}