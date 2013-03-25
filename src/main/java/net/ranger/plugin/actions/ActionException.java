package net.ranger.plugin.actions;

/**
 * Super-class for all exceptions thrown by the plugin's JFace actions when they
 * are executed.
 * 
 * @author eloureiro
 * 
 */
@SuppressWarnings("serial")
public class ActionException extends Exception {

	/**
	 * Creates a new exception having the provided throwable as the cause and
	 * the given message.
	 * 
	 * @param cause
	 *            The exception that caused this one to be thrown
	 * @param message
	 *            The message to be assocatiated with this exception.
	 */
	public ActionException(Throwable cause, String message) {
		super(message, cause);
	}

	/**
	 * Creates a new exception having the provided throwable as the cause. This
	 * exception's message is automatically set to the same one as the cause
	 * provided.
	 * 
	 * @param cause
	 *            The exception that caused this one to be thrown
	 */
	public ActionException(Throwable t) {
		this(t, t.getMessage());
	}

	/**
	 * Creates a new exception having the provided message and no root exception
	 * as the cause.
	 * 
	 * @param message
	 *            The message to be assocatiated with this exception.
	 */
	public ActionException(String message) {
		this(null, message);
	}

}
