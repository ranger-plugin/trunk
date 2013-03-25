package net.ranger.constraint;

import net.ranger.core.Source;

/**
 * A test checker is responsible for checking, for a particular testing
 * framework, whether a given method, found during a search for example, is a
 * test method or not in said testing framework, as well as providing various
 * utility queries regarding said method and its relation to testing.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface TestChecker {

	/**
	 * Checks whether the given {@link Source} object provided is a test method
	 * or not.
	 * 
	 * @param source
	 *            The {@link Source} object representing the method to be
	 *            checked.
	 * @return True if it's a test and false otherwise
	 */
	boolean isTest(Source source);

	/**
	 * Checks whether the given {@link Source} object provided refers to a test
	 * method that's disabled.
	 * 
	 * @param source
	 *            The {@link Source} object representing the method to be
	 *            checked.
	 * @return True if it's a disabled test and false otherwise
	 */
	boolean isDisabled(Source source);

	/**
	 * Returns a string describing the type of tests associated with this
	 * checker (e.g., JUnit, TestNG).
	 * 
	 * @return String
	 */
	String getType();
}
