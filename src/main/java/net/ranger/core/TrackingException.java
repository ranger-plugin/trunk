package net.ranger.core;

/**
 * Super class of exceptions that can be thrown during tracking down elements.
 * 
 * @author Emerson Loureiro
 * 
 */
@SuppressWarnings("serial")
public class TrackingException extends Exception {

	/**
	 * Creates a new exception of this type, which has been caused by the given
	 * exception.
	 * 
	 * @param cause
	 *            The cause of this exception.
	 */
	public TrackingException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new exception of this type, having the provided message.
	 * 
	 * @param message
	 *            The message of this exception.
	 */
	public TrackingException(String message) {
		super(message);
	}
}
