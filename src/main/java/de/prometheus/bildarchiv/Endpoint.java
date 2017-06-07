package de.prometheus.bildarchiv;

import de.prometheus.bildarchiv.exception.ResumptionTokenNullException;

public enum Endpoint {

	ENTITIES("entities?"), KINDS("kinds?"), RELATIONS("relations?"), RELATIONSHIPS("relationships?");

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

	public String listRecords() {
		return baseUrl + endpoint + listRecords + prefix + apiKey;
	}

	public String listRecords(final String token) throws ResumptionTokenNullException {
		if (token == null)
			throw new ResumptionTokenNullException();
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

}