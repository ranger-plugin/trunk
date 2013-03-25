package net.ranger.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.DepthFirstSearchEclipseCallTracker;
import net.ranger.core.Source;
import net.ranger.core.SourceWrappers;
import net.ranger.core.TrackingConstraint;
import net.ranger.core.TrackingException;
import net.ranger.test.UnitTestHelper;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.hamcrest.Description;
import org.jmock.Expectations;
import org.jmock.api.Action;
import org.jmock.api.Invocation;


/**
 * The class <code>DepthFirstSearchEclipseCallTrackerTest</code> contains tests
 * for the class {@link DepthFirstSearchEclipseCallTracker}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro
 * 
 * @author Emerson Loureiro
 */
public class DepthFirstSearchEclipseCallTrackerTest extends UnitTestHelper {

	private DepthFirstSearchEclipseCallTracker callTracker;
	volatile boolean stopRequested = false;
	volatile boolean searchTriggered = false;
	volatile boolean searchRequested = false;

	@Override
	public void setUpImpl() {
		this.callTracker = new DepthFirstSearchEclipseCallTracker(null) {
			@Override
			SearchPattern createSearchPattern(Source jdtInvokable) {
				return new MockSearchPattern(jdtInvokable);
			}
		};
	}

	/**
	 * Tests the stopping of a search that's being performed.
	 */
	public void testStopSearch() throws Exception {
		// We'll use these mutexes to synchronize threads starting
		// and stopping the search and test them properly.
		Object stopMutex = new Object();
		Object searchTriggeredMutex = new Object();
		Object searchRequestedMutex = new Object();

		MockSearchEngine searchEngine = new MockBlockingSearchEngine(stopMutex, searchTriggeredMutex, searchRequestedMutex);
		this.callTracker.setSearchEngine(searchEngine);

		// Creating mocks and expected invokations on them
		final IMethod method_0 = this.createMockIMethod("0");
		final IMethod method_1 = this.createMockIMethod("1");
		final IMethod testMethod_1 = this.createMockIMethod("test_1");
		final IMethod testMethod_2 = this.createMockIMethod("test_2");
		final IMethod testMethod_3 = this.createMockIMethod("test_3");

		// Set up the search data on the mock search engine
		SearchMatch match_method_1 = new SearchMatch(method_1, 0, 0, 0, null, null);
		SearchMatch match_test_1 = new SearchMatch(testMethod_1, 0, 0, 0, null, null);
		SearchMatch match_test_2 = new SearchMatch(testMethod_2, 0, 0, 0, null, null);
		searchEngine.store.put(method_0.getElementName(), Arrays.asList(match_method_1, match_test_1, match_test_2));
		SearchMatch match_test_3 = new SearchMatch(testMethod_3, 0, 0, 0, null, null);
		searchEngine.store.put(method_1.getElementName(), Arrays.asList(match_test_3));

		// Objects used in the actual search
		final Source method = SourceWrappers.create(method_0);
		final MockTrackMonitor monitor = new MockTrackMonitor();

		// Perform the search on the given parameters...
		Thread thread = new Thread() {
			public void run() {
				try {
					DepthFirstSearchEclipseCallTrackerTest.this.callTracker.searchCalls(method, DepthFirstSearchEclipseCallTrackerTest.this.createMockConstraint(3), monitor);
				} catch (TrackingException e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread.start();

		// Wait until the search has been requested to start on the
		// search engine.
		synchronized (searchRequestedMutex) {
			while (!this.searchRequested) {
				searchRequestedMutex.wait();
			}
		}

		// Stop the search now
		this.callTracker.stopSearch();

		// Notify the searching thread that the stop
		// has been requested, so it'll proceed to try and
		// actually search.
		synchronized (stopMutex) {
			this.stopRequested = true;
			stopMutex.notifyAll();
		}

		// Now wait for the search engine to get queried and return results.
		// That will cause the test to fail properly if the stop functionality
		// is changed incorrectly.
		synchronized (searchTriggeredMutex) {
			while (!this.searchTriggered) {
				searchTriggeredMutex.wait();
			}
		}

		// Now check that the search monitor has been invoked only 2 times, as the search expanded
		// the root node of the hierarchy, found two matches ("test_1" and "test_2") and when
		// it was about to expand other nodes, it then realized the sop flag was on, and stopped
		// the search from that point on.
		assertEquals("Incorrect number of invocations on the tracking monitor!", 2, monitor.callFoundInvoked);
	}

	/**
	 * Tests the basic functionality of the search. In this test, we assume the
	 * following call hierarchy:
	 * 
	 * <p>
	 * 1) A method "0", which will be the input for the search
	 * <p>
	 * 2) Three test methods, "test_1", "test_2", and "test_3", which invoke
	 * method "0".
	 */
	public void testSearch_Basic() throws Exception {
		MockSearchEngine searchEngine = new MockSearchEngine();
		this.callTracker.setSearchEngine(searchEngine);

		// Mocks
		final IMethod method_0 = this.createMockIMethod("0");
		final IMethod testMethod_1 = this.createMockIMethod("test_1");
		final IMethod testMethod_2 = this.createMockIMethod("test_2");
		final IMethod testMethod_3 = this.createMockIMethod("test_3");

		// Set up the search data on the mock search engine
		SearchMatch match_test_1 = new SearchMatch(testMethod_1, 0, 0, 0, null, null);
		SearchMatch match_test_2 = new SearchMatch(testMethod_2, 0, 0, 0, null, null);
		SearchMatch match_test_3 = new SearchMatch(testMethod_3, 0, 0, 0, null, null);
		searchEngine.store.put(method_0.getElementName(), Arrays.asList(match_test_1, match_test_2, match_test_3));

		// Objects used in the actual search
		Source method = SourceWrappers.create(method_0);
		MockTrackMonitor monitor = new MockTrackMonitor();

		// Perform the search on the given parameters...
		this.callTracker.searchCalls(method, this.createMockConstraint(1), monitor);
		// ...check that the monitor has been invoked 3 times only,
		// once for each method that's calling the "0" method.
		assertEquals("Incorrect number of invocations on the tracking monitor!", 3, monitor.callFoundInvoked);
	}

	/**
	 * Tests the basic search, going more than one level of depth.
	 * 
	 * <p>
	 * 1) A method "0", which will be the input for the search
	 * <p>
	 * 2) Three methods, "1", "test_1", and "test_2", which invoke method "0".
	 * <p>
	 * 3) A method "test_3", which invokes "1"
	 * <p>
	 * Also, a maximum depth of 3 is specified.
	 */
	public void testSearch_BasicSearchFurtherDepth() throws Exception {
		MockSearchEngine searchEngine = new MockSearchEngine();
		this.callTracker.setSearchEngine(searchEngine);

		// Mocks
		final IMethod method_0 = this.createMockIMethod("0");
		final IMethod method_1 = this.createMockIMethod("1");
		final IMethod testMethod_1 = this.createMockIMethod("test_1");
		final IMethod testMethod_2 = this.createMockIMethod("test_2");
		final IMethod testMethod_3 = this.createMockIMethod("test_3");

		// Set up the search data on the mock search engine
		SearchMatch match_method_1 = new SearchMatch(method_1, 0, 0, 0, null, null);
		SearchMatch match_test_1 = new SearchMatch(testMethod_1, 0, 0, 0, null, null);
		SearchMatch match_test_2 = new SearchMatch(testMethod_2, 0, 0, 0, null, null);
		searchEngine.store.put(method_0.getElementName(), Arrays.asList(match_method_1, match_test_1, match_test_2));
		SearchMatch match_test_3 = new SearchMatch(testMethod_3, 0, 0, 0, null, null);
		searchEngine.store.put(method_1.getElementName(), Arrays.asList(match_test_3));

		// Objects used in the actual search
		Source method = SourceWrappers.create(method_0);
		MockTrackMonitor monitor = new MockTrackMonitor();

		// Perform the search on the given parameters...
		this.callTracker.searchCalls(method, this.createMockConstraint(2), monitor);
		// ...check that the monitor has been invoked 3 times,
		// once for each test method that's calling the "0" method,
		// and one for the test method that's calling the "1" method.
		assertEquals("Incorrect number of invocations on the tracking monitor!", 3, monitor.callFoundInvoked);
	}

	/**
	 * Tests that the search stops when it's going beyond the depth specified.
	 * In this test, we assume the following set up:
	 * 
	 * <p>
	 * 1) A method "0", which will be the input for the search
	 * <p>
	 * 2) Three methods, "1", "test_1", and "test_2", which invoke method "0".
	 * <p>
	 * 3) A method "test_3", which invokes "1"
	 * <p>
	 * Also, a maximum depth of 1 is specified.
	 */
	public void testSearch_SearchDepthCheck() throws Exception {
		MockSearchEngine searchEngine = new MockSearchEngine();
		this.callTracker.setSearchEngine(searchEngine);

		// Mocks
		final IMethod method_0 = this.createMockIMethod("0");
		final IMethod method_1 = this.createMockIMethod("1");
		final IMethod testMethod_1 = this.createMockIMethod("test_1");
		final IMethod testMethod_2 = this.createMockIMethod("test_2");
		final IMethod testMethod_3 = this.createMockIMethod("test_3");

		// Set up the search data on the mock search engine
		SearchMatch match_method_1 = new SearchMatch(method_1, 0, 0, 0, null, null);
		SearchMatch match_test_1 = new SearchMatch(testMethod_1, 0, 0, 0, null, null);
		SearchMatch match_test_2 = new SearchMatch(testMethod_2, 0, 0, 0, null, null);
		searchEngine.store.put(method_0.getElementName(), Arrays.asList(match_method_1, match_test_1, match_test_2));
		SearchMatch match_test_3 = new SearchMatch(testMethod_3, 0, 0, 0, null, null);
		searchEngine.store.put(method_1.getElementName(), Arrays.asList(match_test_3));

		// Objects used in the actual search
		Source method = SourceWrappers.create(method_0);
		MockTrackMonitor monitor = new MockTrackMonitor();

		// Perform the search on the given parameters...
		this.callTracker.searchCalls(method, this.createMockConstraint(1), monitor);
		// ...check that the monitor has been invoked 2 times only,
		// once for each test method that's calling the "0" method,
		// since the depth is only 1.
		assertEquals("Incorrect number of invocations on the tracking monitor!", 2, monitor.callFoundInvoked);
	}

	/**
	 * This will test the scenario where one method is invoked multiple times
	 * from another method, and such method shouldn't be duplicated on the
	 * hierarchy tree. The set up for this test is as follows:
	 * 
	 * <p>
	 * 1) We'll search for all tests on the path of a method "0";
	 * <p>
	 * 2) Method "0" is invoked by methods "1", "test_1", and "test_2";
	 * <p>
	 * 3) Method "1" invokes method "0" in more than one place;
	 * <p>
	 * 4) Method "test_1" invokes method "0" in more than one place;
	 * <p>
	 * 5) No other method invokes method "1".
	 */
	public void testSearch_MultipleCallsFromSameMethod() throws Exception {
		MockSearchEngine searchEngine = new MockSearchEngine();
		this.callTracker.setSearchEngine(searchEngine);

		// Mocks and expected invocations on them
		final IMethod method_0 = this.createMockIMethod("0");
		final IMethod method_1 = this.createMockIMethod("1");
		final IMethod testMethod_1 = this.createMockIMethod("test_1");
		final IMethod testMethod_2 = this.createMockIMethod("test_2");

		// Set up the search data on the mock search engine
		// All invocations that method "1" does on method "0"
		SearchMatch match_method_1_1 = new SearchMatch(method_1, 0, 0, 0, null, null);
		SearchMatch match_method_1_2 = new SearchMatch(method_1, 0, 0, 0, null, null);
		SearchMatch match_method_1_3 = new SearchMatch(method_1, 0, 0, 0, null, null);
		// Invocations that the test methods do on method "0";
		// method "test_1 invokes method "0" twice
		SearchMatch match_test_1_1 = new SearchMatch(testMethod_1, 0, 0, 0, null, null);
		SearchMatch match_test_1_2 = new SearchMatch(testMethod_1, 0, 0, 0, null, null);
		SearchMatch match_test_2 = new SearchMatch(testMethod_2, 0, 0, 0, null, null);
		searchEngine.store.put(method_0.getElementName(), Arrays.asList(match_method_1_1, match_method_1_2, match_method_1_3, match_test_1_1, match_test_1_2, match_test_2));

		// Objects used in the actual search
		Source method = SourceWrappers.create(method_0);
		MockTrackMonitor monitor = new MockTrackMonitor();

		// Perform the search on the given parameters...
		this.callTracker.searchCalls(method, this.createMockConstraint(1), monitor);
		// ...check that the monitor has been invoked 2 times only,
		// once for each test method that's calling the "0" method;
		// i.e., "test_1" and "test_2".
		assertEquals("Incorrect number of invocations on the tracking monitor!", 2, monitor.callFoundInvoked);
	}

	/**
	 * Tests the case when a method being tracked is found being invoked inside
	 * an anonymous block of code.
	 */
	public void search_CallFoundWithinAnonymous() throws Exception {
		// TODO TEST: Implement
	}

	/**
	 * Tests the search process when the search pattern returned for the search
	 * is null.
	 */
	public void testSearch_PatternNull() throws Exception {
		this.callTracker = new DepthFirstSearchEclipseCallTracker(null) {
			@Override
			SearchPattern createSearchPattern(Source jdtInvokable) {
				return null;
			}
		};

		// Mocks and expected invocations on them
		final IMethod method_0 = this.createMockIMethod("0");

		// Objects used in the actual search
		Source method = SourceWrappers.create(method_0);
		MockTrackMonitor monitor = new MockTrackMonitor();
		TrackingConstraint constraint = this.createMockConstraint(1);

		// Perform the search on the given parameters. Expect
		// no exception thrown and nothing really invoked on the
		// IMethod object
		this.callTracker.searchCalls(method, constraint, monitor);
	}

	// PRIVATE AND UTILITY METHODS/CLASSES

	private IMethod createMockIMethod(final String methodName) {
		final IMethod method = this.mock(IMethod.class);
		final ICompilationUnit compilationUnit = this.mock(ICompilationUnit.class);

		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(method).getElementName();
				will(returnValue(methodName));

				atLeast(0).of(method).getCompilationUnit();
				will(returnValue(compilationUnit));
			}
		});
		return method;
	}

	private TrackingConstraint createMockConstraint(final int depth) {
		final TrackingConstraint constraint = this.mock(TrackingConstraint.class);
		final Action isTargetAction = new Action() {
			@Override
			public Object invoke(Invocation invocation) throws Throwable {
				return ((CallHierarchyNode) invocation.getParameter(0)).getCaller().getName().startsWith("test");
			}

			@Override
			public void describeTo(Description arg0) {
			}
		};
		final Action keepGoingAction = new Action() {
			@Override
			public Object invoke(Invocation invocation) throws Throwable {
				return ((CallHierarchyNode) invocation.getParameter(0)).getDepth() < depth;
			}

			@Override
			public void describeTo(Description arg0) {
			}
		};
		this.getMockery().checking(new Expectations() {
			{
				atLeast(0).of(constraint).isTarget(with(aNonNull(CallHierarchyNode.class)));
				will(isTargetAction);

				atLeast(0).of(constraint).keepGoing(with(aNonNull(CallHierarchyNode.class)));
				will(keepGoingAction);
			}
		});
		return constraint;
	}

	private class MockSearchEngine extends SearchEngine {

		public Map<String, List<SearchMatch>> store = new HashMap<String, List<SearchMatch>>();

		@Override
		public void search(SearchPattern pattern, SearchParticipant[] participants, IJavaSearchScope scope, SearchRequestor requestor, IProgressMonitor monitor) throws CoreException {
			requestor.beginReporting();
			List<SearchMatch> matches = this.store.get(pattern.focus.getElementName());
			if (matches != null) {
				for (SearchMatch match : matches) {
					requestor.acceptSearchMatch(match);
				}
			}
			requestor.endReporting();
		}
	}

	private class MockBlockingSearchEngine extends MockSearchEngine {

		private Object stopMutex;
		private Object searchTriggeredMutex;
		private Object searchRequestedMutex;

		public MockBlockingSearchEngine(Object stopMutex, Object searchTriggeredMutex, Object searchRequestedMutex) {
			super();
			this.stopMutex = stopMutex;
			this.searchTriggeredMutex = searchTriggeredMutex;
			this.searchRequestedMutex = searchRequestedMutex;
		}

		@Override
		public void search(SearchPattern pattern, SearchParticipant[] participants, IJavaSearchScope scope, SearchRequestor requestor, IProgressMonitor monitor) throws CoreException {
			synchronized (this.searchRequestedMutex) {
				DepthFirstSearchEclipseCallTrackerTest.this.searchRequested = true;
				this.searchRequestedMutex.notifyAll();
			}

			synchronized (this.stopMutex) {
				while (!DepthFirstSearchEclipseCallTrackerTest.this.stopRequested) {
					try {
						this.stopMutex.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			super.search(pattern, participants, scope, requestor, monitor);

			synchronized (this.searchTriggeredMutex) {
				DepthFirstSearchEclipseCallTrackerTest.this.searchTriggered = true;
				this.searchTriggeredMutex.notifyAll();
			}
		}
	}

	private class MockSearchPattern extends SearchPattern {

		public MockSearchPattern(Source method) {
			super(IJavaElement.METHOD);
			this.focus = method.getJavaElement();
		}

		@Override
		public SearchPattern getBlankPattern() {
			return null;
		}
	}
}
