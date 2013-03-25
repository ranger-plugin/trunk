package net.ranger.view;

import java.util.List;

interface SearchViewNodeGrouper extends CallHierarchyNodeGrouper {

	/**
	 * Groups a list of {@link SearchViewNode} nodes provided, depending on the
	 * implementation-specific grouping algorithm. If the result given is empty,
	 * or null, the contract for this method specifies that it should return a
	 * single-element list, where the element simply states that nothing is
	 * contained in the result.
	 * 
	 * @param viewNodes
	 *            A list of {@link SearchViewNode} objects to be grouped.
	 * @return A List of {@link SearchViewNode}, doesn't return null
	 */
	List<SearchViewNode> groupViewNodes(List<SearchViewNode> viewNodes);
}
