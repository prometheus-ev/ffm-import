package de.prometheus.bildarchiv.exception;

public class HttpRequestException extends Exception {


	private static final long serialVersionUID = -6859389302901743754L;

	public HttpRequestException() {
		super();
	}
	
	public HttpRequestException(final String message) {
		super(message);
	}
	
}
