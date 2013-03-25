package net.ranger.view;

import java.util.ArrayList;
import java.util.List;

import net.ranger.core.CallHierarchyNode;


/**
 * The default way of presenting the search results, which is by just showing
 * the call hierarchy from each target found up until the source of the search.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DefaultSearchResultGrouper implements CallHierarchyNodeGrouper {

	DefaultSearchResultGrouper() {
	}

	/** {@inheritDoc} */
	@Override
	public List<SearchViewNode> group(List<CallHierarchyNode> nodes) {
		List<SearchViewNode> viewNodes = new ArrayList<SearchViewNode>(nodes.size());

		for (CallHierarchyNode node : nodes) {
			viewNodes.add(this.createSearchViewNode(node));
		}

		return viewNodes;
	}

	/**
	 * Creates a new view node to be grouped by this grouper. Just put this up
	 * to make it easier mocking out this behaviour.
	 */
	SearchViewNode createSearchViewNode(CallHierarchyNode node) {
		return new CallerSearchViewNode(node);
	}
}
