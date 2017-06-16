package de.prometheus.bildarchiv.exception;

/**
 * 
 * @author matana
 *
 */
public class ResumptionTokenNullException extends Exception {

	private static final long serialVersionUID = 6928774210377441154L;
	
	/**
	 * 
	 */
	public ResumptionTokenNullException() {
		super();
	}
	
	/**
	 * 
	 * @param message
	 */
	public ResumptionTokenNullException(final String message) {
		super(message);
	}

}
