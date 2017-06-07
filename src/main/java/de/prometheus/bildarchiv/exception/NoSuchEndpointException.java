package de.prometheus.bildarchiv.exception;

public class NoSuchEndpointException extends Exception {

	private static final long serialVersionUID = -7466146916437734633L;
	
	public NoSuchEndpointException() {
		super();
	}
	
	public NoSuchEndpointException(String message) {
		super(message);
	}

}
