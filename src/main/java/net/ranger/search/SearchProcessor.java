package net.ranger.search;

import net.ranger.core.TrackingException;

/**
 * The search processor controls how search is to be performed within Eclipse
 * platform. This includes handling the different types of searches available as
 * well as stopping them when requested by client code.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchProcessor {

	/**
	 * Processes the provided search request asynchronously. Once the search is
	 * finished, a {@link SearchFinishedEvent} event is triggered.
	 * 
	 * @param parameter
	 *            The parameter to be processed.
	 * @param listener
	 *            Listener for receiving events concerning the progress of the
	 *            search.
	 * @throws TrackingException
	 *             If client code attempts to execute concurrent searches.
	 */
	void process(Search parameter, SearchProcessorListener listener) throws TrackingException;

	/**
	 * Attempts to stop the current search.
	 */
	void stopCurrentSearch();
}
