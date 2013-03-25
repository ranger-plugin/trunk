package net.ranger.search;

import java.util.Collections;
import java.util.List;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.Source;


/**
 * A default implementation of a {@link SearchResult}.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DefaultSearchResult implements SearchResult {

	/** All results that have been found during the search. */
	private List<CallHierarchyNode> targetsFound;

	/** The source of the search associated with this result. */
	private Source source;

	/**
	 * Creates a new instance of this class with the given parameters.
	 * 
	 * @param targetsFound
	 *            All results that have been found during the search.
	 * @param source
	 *            The source of the search associated with this result.
	 */
	public DefaultSearchResult(List<CallHierarchyNode> targetsFound, Source source) {
		if (targetsFound == null) {
			this.targetsFound = Collections.emptyList();
		} else {
			this.targetsFound = targetsFound;
		}
		this.source = source;
	}

	/** {@inheritDoc} */
	@Override
	public List<CallHierarchyNode> getTargetsFound() {
		return this.targetsFound;
	}

	@Override
	public Source getTarget() {
		return this.source;
	}
}
