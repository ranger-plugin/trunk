package net.ranger.plugin.integration;

import java.util.ArrayList;
import java.util.List;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.Source;
import net.ranger.core.Tracker;
import net.ranger.core.TrackingException;
import net.ranger.core.TrackingMonitor;
import net.ranger.plugin.RangerPlugin;
import net.ranger.search.DefaultSearchResult;
import net.ranger.search.Search;
import net.ranger.search.SearchFinishedEvent;
import net.ranger.search.SearchProcessorListener;
import net.ranger.search.SearchResult;
import net.ranger.search.SearchStartedEvent;

import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.widgets.Display;


/**
 * A search job is an Eclipse workspace job which performs the actual search in
 * background. Not only it wraps up the actual search algorithm, hiding from
 * client code how the algorithm is defined, but it also automatically performs
 * error handling, which is reflected on the job status, collects search
 * results, as well as fires events related to the search.
 * 
 * @see WorkspaceJob
 * @author Emerson Loureiro
 * 
 */
class SearchJob extends WorkspaceJob implements TrackingMonitor {

	private Search parameter;
	private Tracker tracker;
	public List<CallHierarchyNode> targets;
	private SingleSearchSearchProcessor searchProcessor;
	private SearchProcessorListener listener;

	/**
	 * Creates a new search job.
	 * 
	 * @param parameter
	 *            The search parameter specifying the search to be performed.
	 *            This parameter determines which search algorithm will be used
	 *            and how it will be set up.
	 * @param searchProcessor
	 *            The {@link SingleSearchSearchProcessor} that started this job.
	 * @param listener
	 *            Listener for receiving search-related events.
	 * @throws TrackingException
	 *             If there's any error when trying setting up the search
	 *             algorithm given the search parameter.
	 */
	public SearchJob(Search parameter, SingleSearchSearchProcessor searchProcessor, SearchProcessorListener listener) throws TrackingException {
		super("Searching");
		this.parameter = parameter;
		this.tracker = TrackerFactory.createTracker(parameter);
		this.targets = new ArrayList<CallHierarchyNode>();
		this.searchProcessor = searchProcessor;
		this.listener = listener;
		this.setUser(true);
	}

	/** {@inheritDoc} */
	@Override
	public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
		IStatus finalStatus = Status.OK_STATUS;

		long startTime = System.currentTimeMillis();
		try {
			// Let Eclipse know that the job is starting
			monitor.beginTask("Be patient, this might take a while...", IProgressMonitor.UNKNOWN);
			// Also notify the processor listener of that
			this.triggerSearchStartedEvent(startTime);
			// Perform the search...
			this.tracker.searchCalls(this.parameter.getSource(), this.parameter.getConstraint(), this);
		} catch (TrackingException e) {
			finalStatus = new Status(Status.ERROR, RangerPlugin.PLUGIN_ID, "Error executing search: " + e.getMessage());
		} catch (Throwable e) {
			finalStatus = new Status(Status.ERROR, RangerPlugin.PLUGIN_ID, "Internal error: " + e.getMessage());
		} finally {
			// At the end, regardless of what happens, always... 
			// ...trigger the event that the search is over.
			this.triggerSearchFinishedEvent(System.currentTimeMillis() - startTime, this.parameter.getSource());
			// Let Eclipse know that the job is finished
			monitor.done();
			// and reset the current job on the processor
			// to null so that other searches can be performed
			this.searchProcessor.currentSearchJob = null;
		}

		return finalStatus;
	}

	/** {@inheritDoc} */
	@Override
	public void callFound(CallHierarchyNode call) {
		this.targets.add(call);
	}

	/** {@inheritDoc} */
	@Override
	protected void canceling() {
		if (this.tracker != null) {
			try {
				this.tracker.stopSearch();
			} catch (TrackingException e) {
				e.printStackTrace();
			}
		}
	}

	private void triggerSearchStartedEvent(long startTime) {
		// Wraps the result in an event and fire it
		// using a display execution service
		// as Eclipse doesn't like just any thread updating
		// the UI.
		final SearchStartedEvent event = new SearchStartedEvent(startTime);

		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				SearchJob.this.listener.searchStarted(event);
			}
		});
	}

	/**
	 * Utility method for notifying listeners that the search is finished,
	 * either because it did get to the end - given the constraints - or because
	 * it's been stopped by the user.
	 */
	private void triggerSearchFinishedEvent(long duration, Source source) {
		// Create a search result with whatever targets have been found.
		// This will allow the search results to be displayed even when
		// the search has been manually cancelled by the user, in this
		// case, displaying all results found up until it was cancelled.
		SearchResult result = new DefaultSearchResult(this.targets, source);

		// Wraps the result in an event and fire it
		// using a display execution service
		// as Eclipse doesn't like just any thread updating
		// the UI.
		final SearchFinishedEvent event = new SearchFinishedEvent(result, duration);
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				SearchJob.this.listener.searchFinished(event);
			}
		});
	}
}
