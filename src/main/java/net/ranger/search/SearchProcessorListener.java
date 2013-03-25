package net.ranger.search;

/**
 * A listener for receiving search-related events.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchProcessorListener {

	/**
	 * Notifies this listener that a search has started.
	 * 
	 * @param event
	 *            The event corresponding to the started search.
	 */
	void searchStarted(SearchStartedEvent event);

	/**
	 * Notifies this listener that the last started search has finished (either
	 * because it reached the end or because it's been terminated).
	 * 
	 * @param event
	 *            The event corresponding to the stopped search.
	 */
	void searchFinished(SearchFinishedEvent event);
}
