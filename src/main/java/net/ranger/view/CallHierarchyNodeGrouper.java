package net.ranger.view;

import java.util.List;

import net.ranger.core.CallHierarchyNode;


/**
 * Takes care of grouping {@link CallHierarchyNode} objects.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface CallHierarchyNodeGrouper {

	/**
	 * Groups provided list of {@link CallHierarchyNode} nodes, depending on the
	 * implementation-specific grouping algorithm. If the result given is empty,
	 * or null, the contract for this method specifies that it should return a
	 * single-element list, where the element simple states that nothing is
	 * contained in the result.
	 * 
	 * @param nodes
	 *            A list of {@link CallHierarchyNode} objects to be grouped.
	 * @return A List of {@link SearchViewNode}, doesn't return null
	 */
	List<SearchViewNode> group(List<CallHierarchyNode> nodes);
}
