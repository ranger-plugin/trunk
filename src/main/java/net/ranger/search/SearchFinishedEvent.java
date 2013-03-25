package net.ranger.search;

/**
 * Event fired when a search is finished or terminated.
 * 
 * @author Emerson Loureiro
 * 
 */
public class SearchFinishedEvent {

	/** Result of the search. */
	private SearchResult result;

	/** The search duration. */
	private long duration;

	public SearchFinishedEvent(SearchResult result, long duration) {
		this.result = result;
		this.duration = duration;
	}

	/**
	 * Returns the result of the search to which this event refers to .
	 * 
	 * @return {@link SearchResult}
	 */
	public SearchResult getSearchResult() {
		return this.result;
	}

	/**
	 * Returns the total duration of search associated with this event.
	 * 
	 * @return long
	 */
	public long getDuration() {
		return this.duration;
	}
}
