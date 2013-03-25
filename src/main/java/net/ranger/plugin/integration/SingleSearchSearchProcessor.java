package net.ranger.plugin.integration;

import net.ranger.core.TrackingException;
import net.ranger.search.Search;
import net.ranger.search.SearchProcessor;
import net.ranger.search.SearchProcessorListener;

import org.eclipse.core.runtime.jobs.Job;


/**
 * A search processor which only allows one search at a time to be performed.
 * 
 * @author Emerson Loureiro
 * 
 */
class SingleSearchSearchProcessor implements SearchProcessor {

	/**
	 * A mutex to synchronize reads and writes on the search job.
	 */
	private Object searchingMutex;

	/** The current search job being executed. */
	volatile Job currentSearchJob;

	/**
	 * Constructor made with friendly access for the purposes of unit testing
	 * only.
	 */
	public SingleSearchSearchProcessor() {
		this.searchingMutex = new Object();
	}

	/** {@inheritDoc} */
	@Override
	public void process(Search parameter, SearchProcessorListener listener) throws TrackingException {
		if (parameter != null) {
			// Use double checked locking to ensure synchronization
			// when checking and creating the search job, and avoiding
			// unnecessary synchronization when there's no concurrency.
			if (this.currentSearchJob == null) {
				synchronized (this.searchingMutex) {
					if (this.currentSearchJob == null) {
						this.currentSearchJob = this.createSearchJob(parameter, listener);
						this.runJob();
						return;
					}
				}
			}

			// Let clients know of the problem, concurrent searches aren't allowed!
			throw new TrackingException("Cannot execute concurrent searches");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void stopCurrentSearch() {
		// Check that we have an actual job to stop first!!
		if (this.currentSearchJob != null) {
			this.currentSearchJob.cancel();
		}
	}

	/**
	 * Schedules the current job to be run. Implemented with friendly access to
	 * make it easy mocking out this behaviour
	 */
	void runJob() {
		this.currentSearchJob.schedule();
	}

	/**
	 * Creates a search job using the Eclipse Job API. Made friendly so that
	 * it's easier to mock out this behaviour.
	 */
	Job createSearchJob(final Search parameter, SearchProcessorListener listener) throws TrackingException {
		return new SearchJob(parameter, this, listener);
	}
}
