package net.ranger.plugin.integration;

import net.ranger.core.DepthFirstSearchEclipseCallTracker;
import net.ranger.core.Tracker;
import net.ranger.core.TrackingException;
import net.ranger.search.ProjectSearch;
import net.ranger.search.Search;
import net.ranger.search.SearchVisitor;
import net.ranger.search.WorkspaceSearch;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;


/**
 * A factory for creating instances of {@link Tracker}.
 * 
 * @author Emerson Loureiro
 * 
 */
public class TrackerFactory implements SearchVisitor {

	private static TrackerFactory factory = new TrackerFactory();
	private Tracker tracker;

	/**
	 * Have this with friendly access so that only the static factory method
	 * will be visible for this type.
	 */
	TrackerFactory() {
	}

	/**
	 * Sets the internal instance used by the factory. Only used for unit
	 * testing.
	 */
	static void setInstance(TrackerFactory factory) {
		TrackerFactory.factory = factory;
	}

	/**
	 * Creates and returns a concrete implementation of a {@link Tracker}, based
	 * on the search parameter provided.
	 * 
	 * @param search
	 *            The search parameter specifying what is to be searched and
	 *            where.
	 * @return {@link Tracker}
	 * @throws TrackingException
	 *             If there's any error when processing the search argument
	 *             provided
	 * @throws IllegalArgumentException
	 *             If the search argument provided is null.
	 */
	public static Tracker createTracker(Search search) throws TrackingException {
		if (search != null) {
			// Always reset the last created tracker before
			// trying to create a new one.
			factory.tracker = null;
			search.accept(factory);
			return factory.tracker;
		} else {
			throw new IllegalArgumentException("Search parameter cannot be null");
		}
	}

	/** {@inheritDoc} */
	@Override
	public void visit(ProjectSearch projectSearch) throws TrackingException {
		this.visitCommon(projectSearch, new IJavaProject[] { projectSearch.getProject() });
	}

	/** {@inheritDoc} */
	@Override
	public void visit(WorkspaceSearch workspaceSearch) throws TrackingException {
		this.visitCommon(workspaceSearch, workspaceSearch.getProjects());
	}

	/**
	 * Creates a seach scope for the given projects. Put this in a separate
	 * method with friendly access so that this behaviour can be easily mocked
	 * out.
	 */
	IJavaSearchScope createSearchScope(IJavaProject[] projects) {
		return SearchEngine.createJavaSearchScope(projects, IJavaSearchScope.SOURCES);
	}

	/**
	 * Centralizes the processing of different types of search parameters,
	 * putting in here everything that's common to that.
	 */
	private void visitCommon(Search search, IJavaProject[] projects) throws TrackingException {
		IJavaSearchScope searchScope = this.createSearchScope(projects);
		this.tracker = new DepthFirstSearchEclipseCallTracker(searchScope);
	}
}
