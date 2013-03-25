package net.ranger.core;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import net.ranger.plugin.RangerPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;


/**
 * A call tracker that performs all searches on a depth-first search mode.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DepthFirstSearchEclipseCallTracker extends SearchRequestor implements Tracker {

	/**
	 * The scope defines where the search will take place, e.g., the entire
	 * workspace, the current project, and so on.
	 */
	private IJavaSearchScope scope;

	/**
	 * The search engine that performs searches through the Eclipse workspace.
	 * See {@link SearchEngine}.
	 */
	private SearchEngine searchEngine;

	/** Monitor used throughout a single search. */
	private TrackingMonitor monitor;

	/** Constraint used throughout a single search. */
	private TrackingConstraint constraint;

	/**
	 * The current node being expanded by this tracker. It is used so that the
	 * search can be done in a recursive way, without loosing track of where in
	 * the hierarchy tree a node should be added to.
	 */
	private CallHierarchyNode currentNode;

	/**
	 * The expansion stack keeps the order of the nodes that are to be expanded
	 * next.
	 */
	private Stack<CallHierarchyNode> expansionStack;

	/**
	 * We use this flag to prevent stacking method calls at endReporting()
	 * during the recursive search.
	 */
	private volatile boolean alreadyReporting;

	/** Keeps track of all callers that have been matched during the search. */
	private Set<IJavaElement> matchedCallers;

	/**
	 * Will keep track of whether the current search has been requested to be
	 * stopped.
	 */
	private volatile boolean stopRequested;

	/**
	 * Creates a new instance of this tracker.
	 * 
	 * @param scope
	 *            The scope where the search will take place. In short, the
	 *            scope defines where the search will take place, e.g., the
	 *            entire workspace, the current project, and so on.
	 */
	public DepthFirstSearchEclipseCallTracker(IJavaSearchScope scope) {
		this.scope = scope;
		// The search engine is the one actually performing the search,
		// given all parameters defined previously.
		this.searchEngine = new SearchEngine();
	}

	/** {@inheritDoc} */
	@Override
	public final void searchCalls(Source method, TrackingConstraint constraint, TrackingMonitor monitor) throws TrackingException {
		// We use the interface method to only set up internal fields,
		// so we can recursively call an "internal version" of this
		// method - expand(...) - without worrying about setting them up again.
		this.initialize(constraint, monitor);
		// Create the root node for the hierarchy, and expand from there
		DefaultCallHierarchyNode rootNode = new DefaultCallHierarchyNode(null, 0, method);
		this.expand(rootNode);
	}

	/** {@inheritDoc} */
	@Override
	public final void acceptSearchMatch(SearchMatch match) throws CoreException {
		// We don't want any results found inside a comment block,
		// as this might bring false positives
		if (match.isInsideDocComment()) {
			return;
		}

		Source caller = SourceWrappers.create((IJavaElement) match.getElement());

		if (caller != null) {
			// Check if we haven't already found the matched method. This
			// is necessary as a method might come up
			// multiple times during a search, if either it invokes a method
			// being searched multiple times or if another method that's
			// been/will be searched is also invoked by said method.
			if (this.matchedCallers.add(caller.getJavaElement())) {
				// Create a new node for the method that's
				// been found invoking a target method and hook it up
				// on the hierarchy.
				DefaultCallHierarchyNode hierarchyNode = new DefaultCallHierarchyNode(this.currentNode, this.currentNode.getDepth() + 1, caller);
				hierarchyNode.setSearchData(new DefaultSearchData(match.getOffset(), match.getLength()));
				this.currentNode.addChild(hierarchyNode);

				if (this.constraint.isTarget(hierarchyNode)) {
					// When a target is found, notify the monitor. Also, 
					// we no long proceed with the search from that point on the call hierarchy.
					hierarchyNode.setIsTarget(true);
					this.monitor.callFound(hierarchyNode);
					RangerPlugin.log("Found [" + hierarchyNode.getCaller().getName() + "]. Node is a target. Current depth is: " + hierarchyNode.getDepth());
				} else if (this.constraint.keepGoing(hierarchyNode)) {
					// If we still have "room" to expand the hierarchy,
					// then add the hierarchy node to the stack so that'll
					// be picked up at the end of the search (endReporting()).
					RangerPlugin.log("Found [" + hierarchyNode.getCaller().getName() + "]. Will continue from here. Current depth is: " + hierarchyNode.getDepth());
					this.expansionStack.push(hierarchyNode);
				} else {
					RangerPlugin.log("Found [" + hierarchyNode.getCaller().getName() + "]. Not going any further. Current depth is: " + hierarchyNode.getDepth());
				}
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void stopSearch() throws TrackingException {
		this.stopRequested = true;
	}

	/** {@inheritDoc} */
	@Override
	public void endReporting() {
		// This will prevent recursive calls to this method from
		// stacking over and over after each search, thus avoiding
		// stack overflow.
		if (!this.alreadyReporting) {
			this.alreadyReporting = true;

			// Continue getting nodes from the stack and
			// expand each of them until the stack is
			// empty.
			while (!this.expansionStack.isEmpty()) {
				try {
					if (this.stopRequested) {
						// If the search has been requested to stop,
						// then we simply clear the stack and that'll
						// automatically stop the search at its current point
						this.expansionStack.clear();
					} else {
						this.expand(this.expansionStack.pop());
					}
				} catch (TrackingException e) {
					// TODO REFC: Change this handling
					throw new RuntimeException(e);
				}
			}
		}
	}

	/**
	 * Only used for unit testing, for easily mocking out the behaviour of a
	 * {@link SearchEngine}.
	 */
	final void setSearchEngine(SearchEngine searchEngine) {
		this.searchEngine = searchEngine;
	}

	/**
	 * Utility method only added for the purposes of unit testing, to make it
	 * easier to mock out the behaviour of
	 * {@link SearchPattern#createPattern(org.eclipse.jdt.core.IJavaElement, int)}
	 * .
	 */
	SearchPattern createSearchPattern(Source jdtInvokable) {
		// The pattern specifies what is to be searched; 
		// in this case, references to a method.
		return SearchPattern.createPattern(jdtInvokable.getJavaElement(), IJavaSearchConstants.REFERENCES);
	}

	/**
	 * Internal implementation of the interface search method. It is defined
	 * just to make it easier the recursive search.
	 */
	private void expand(CallHierarchyNode node) throws TrackingException {
		this.currentNode = node;
		SearchPattern pattern = this.createSearchPattern(node.getCaller());

		if (pattern != null) {
			try {
				RangerPlugin.log("Expanding: " + node.getCaller().getName() + ". Current depth is: " + node.getDepth());
				this.searchEngine.search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() }, this.scope, this, null);
			} catch (Throwable t) {
				throw new TrackingException(t);
			}
		}
	}

	/**
	 * Utility method for initializing class fields before a search is
	 * performed.
	 */
	private void initialize(TrackingConstraint constraint, TrackingMonitor monitor) {
		this.monitor = monitor;
		this.constraint = constraint;
		this.currentNode = null;
		this.alreadyReporting = false;
		this.expansionStack = new Stack<CallHierarchyNode>();
		// TODO REFC: Re-think on the data structure used to keep matched methods
		this.matchedCallers = new HashSet<IJavaElement>(100);
		this.stopRequested = false;
	}
}
