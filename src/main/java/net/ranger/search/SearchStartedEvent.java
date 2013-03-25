package net.ranger.search;

/**
 * Event fired when a search is started.
 * 
 * @author Emerson Loureiro
 * 
 */
public class SearchStartedEvent {

	private long startTime;

	public SearchStartedEvent(long startTime) {
		this.startTime = startTime;
	}

	/**
	 * Returns the time the search associated with this event started.
	 * 
	 * @return long
	 */
	public long getStartTime() {
		return this.startTime;
	}
}
