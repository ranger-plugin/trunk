package net.ranger.plugin.integration;

import net.ranger.core.TrackingException;
import net.ranger.plugin.integration.SingleSearchSearchProcessor;
import net.ranger.search.Search;
import net.ranger.search.SearchProcessorListener;
import net.ranger.test.UnitTestHelper;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;


/**
 * The class <code>SearchProcessorTest</code> contains tests for the class
 * {@link SingleSearchSearchProcessor}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro
 * 
 * @author Emerson Loureiro
 */
public class SingleSearchSearchProcessorTest extends UnitTestHelper {

	public volatile boolean searching = false;
	public Object searchingMonitor = new Object();
	public volatile boolean returnFromSearch = false;
	public Object returnFromSearchMonitor = new Object();

	@Override
	public void setUpImpl() {
	}

	/**
	 * Tests the stopCurrentSearch method, when no search has been started yet.
	 * Essentially checking that no exceptions are triggered in that case.
	 */
	public void testStopCurrentSearch_NoSearchStarted() {
		// Mock out a couple of methods on the processor that we're not really interested in for the
		// purposes of this test
		SearchProcessorMock processor = new SearchProcessorMock();

		try {
			processor.stopCurrentSearch();
		} catch (Throwable t) {
			fail("Failed with exception: \"" + t.getMessage() + "\"");
		}
	}

	/** Tests the basic processing of a search request. */
	public void testProcess() throws Exception {
		// Mock out a couple of methods on the processor that we're not really interested in for the
		// purposes of this test
		SearchProcessorMock processor = new SearchProcessorMock();
		processor.process(this.mock(Search.class), this.mock(SearchProcessorListener.class));

		assertEquals("Search job has ran more times than expected!", 1, processor.runJobInvoked);
	}

	/**
	 * This will test that, when a search is already being processed, any
	 * requests to start a new search will be blocked.
	 */
	public void testProcess_SearchAlreadyBeingProcessed() throws Exception {
		// Mock out a couple of methods on the processor that we're not really interested in for the
		// purposes of this test
		SearchProcessorMock processor = new BlockingSearchProcessorMock();

		// Create the search parameter and process it asynchronously
		processor.process(this.mock(Search.class), this.mock(SearchProcessorListener.class));

		// Wait until the searching thread reaches the searching point
		synchronized (searchingMonitor) {
			while (!searching) {
				searchingMonitor.wait();
			}
		}

		try {
			// Invoke the search again (use the same parameter, even though it doesn't really matter)
			// we simply want to mimic the triggerSingleSearchSearchProcessorTest.
			processor.process(this.mock(Search.class), this.mock(SearchProcessorListener.class));
			fail("Should've thrown an exception upon executing concurrent searches!");
		} catch (TrackingException e) {
		}

		// Notifies the search to now stop, test is finished
		synchronized (returnFromSearchMonitor) {
			returnFromSearch = true;
			returnFromSearchMonitor.notifyAll();
		}

		// In the end, check that only one search job has ever been executed
		assertEquals("Search job has ran more times than expected!", 1, processor.runJobInvoked);
	}

	// ----------------------------------------
	// PRIVATE CLASSES AND UTILITY METHODS
	// ----------------------------------------

	private class SearchProcessorMock extends SingleSearchSearchProcessor {
		public int runJobInvoked = 0;

		@Override
		Job createSearchJob(final Search parameter, SearchProcessorListener listener) {
			return new MockJob();
		}

		@Override
		void runJob() {
			this.runJobInvoked++;
		}
	}

	private class BlockingSearchProcessorMock extends SearchProcessorMock {

		@Override
		void runJob() {
			this.runJobInvoked++;
			Thread thread = new Thread() {
				public void run() {
					if (!searching) {
						searching = true;

						synchronized (searchingMonitor) {
							searchingMonitor.notifyAll();
						}

						synchronized (returnFromSearchMonitor) {
							while (!returnFromSearch) {
								try {
									returnFromSearchMonitor.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			};
			thread.start();
		}
	}

	private class MockJob extends Job {

		public MockJob() {
			super("Mock job");
		}

		@Override
		protected IStatus run(IProgressMonitor arg0) {
			return null;
		}
	}
}
