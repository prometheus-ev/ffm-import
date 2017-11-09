package de.prometheus.bildarchiv.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public enum Endpoint {

	ENTITIES("entities?"), KINDS("kinds?"), RELATIONS("relations?"), RELATIONSHIPS("relationships?");
	
	private static final String EQUAL = "=";
	private static final String AMPERSAND = "&";

	private final String prefix = "&metadataPrefix=kor";
	private final String listRecords = "verb=ListRecords";
	private final String listIdentifiers = "verb=ListIdentifiers";
	private final String getRecord = "verb=GetRecord&identifier=";
	private final String resumptionToken = "&resumptionToken=";
	private final String endpoint;

	private final String baseUrl = System.getProperty("baseUrl");
	private final String apiKey = System.getProperty("apiKey");

	Endpoint(final String endpoint) {
		this.endpoint = endpoint;
	}

	public String getRecord(final String identifier) {
		if (identifier == null)
			return null;
		return baseUrl + endpoint + getRecord + identifier + prefix + apiKey;
	}

	public String listRecords(final String token) {
		if (token == null)
			return baseUrl + endpoint + listRecords + prefix + apiKey;
		else
			return baseUrl + endpoint + listRecords + prefix + apiKey + resumptionToken + token;
	}

	public String listIdentifiers() {
		return baseUrl + endpoint + listIdentifiers + prefix + apiKey;
	}

	public String listIdentifiers(final String token) {
		if (token == null)
			return null;
		return baseUrl + endpoint + listIdentifiers + prefix + apiKey + resumptionToken + token;
	}
	
	public String getListIdentifiersHttpRequestURL(Map<String, String> optionalArguments) {
		Map<String,String> arguments = new HashMap<String, String>();
		arguments.put("verb", "ListIdentifiers");
		arguments.put("metadataPrefix", "kor");
		arguments.put("api_Key", apiKey.replace("api_key=", "")); //ugly fix
		arguments.putAll(optionalArguments); //TODO resumptionToken should be exclusive argument
		return buildHttpRequestURL(arguments);
	}
	
	public String getListRecordsHttpRequestURL(Map<String, String> optionalArguments) {
		Map<String,String> arguments = new HashMap<String, String>();
		arguments.put("verb", "ListRecords");
		arguments.put("metadataPrefix", "kor");
		arguments.put("api_key", apiKey.replace("&api_key=", "")); //ugly fix
		arguments.putAll(optionalArguments); //TODO resumptionToken should be exclusive argument
		return buildHttpRequestURL(arguments);
	}
	
	private String buildHttpRequestURL(Map<String, String> arguments) {
		StringBuilder httpRequestURLBuilder = new StringBuilder();
		httpRequestURLBuilder.append(baseUrl).append(endpoint);
		Set<String> keySet = arguments.keySet();
		Iterator<String> iterator = keySet.iterator();
		while(iterator.hasNext()) {
			String key = iterator.next(); 
			httpRequestURLBuilder.append(key).append(EQUAL).append(arguments.get(key));
			if(iterator.hasNext()) {
				httpRequestURLBuilder.append(AMPERSAND);
			}
		}
		return httpRequestURLBuilder.toString();		
	}

}