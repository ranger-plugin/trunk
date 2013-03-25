package net.ranger.core;

/**
 * A tracker is responsible for tracking down method calls.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface Tracker {

	/**
	 * Searches recursively for all methods potentially having the provided
	 * invokable - e.g., a method or constructor - in their execution path, and
	 * which match the provided constraint. For example, if a method
	 * <code>test_1</code> invokes a method <code>A</code>, and a method
	 * <code>test_2</code> invokes <code>C</code>, which in turn also invokes
	 * <code>A</code>, and the constraint dictates that it should return all
	 * methods starting with "test", then both <code>test_1</code> and
	 * <code>test_2</code> should be returned.
	 * 
	 * @param source
	 *            What is to be searched.
	 * @param constraint
	 *            The constraint specifying when a method found is a target for
	 *            the search, as well as limiting properties of the search
	 *            (e.g., maximum depth).
	 * @param monitor
	 *            A listener to receive events about methods found that match
	 *            what is to be searched, as defined by the constraint.
	 * @throws TrackingException
	 *             If there's any error during the search.
	 */
	void searchCalls(Source source, TrackingConstraint constraint, TrackingMonitor monitor) throws TrackingException;

	/**
	 * Requests the current search to be stopped.
	 */
	void stopSearch() throws TrackingException;
}
