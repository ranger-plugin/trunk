package net.ranger.view;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.ranger.core.CallHierarchyNode;


public class CompositeGrouper implements CallHierarchyNodeGrouper {

	private List<SearchViewNodeGrouper> otherGroupers;

	private CallHierarchyNodeGrouper leafGrouper;

	public CompositeGrouper(CallHierarchyNodeGrouper leafGrouper) {
		this.otherGroupers = new LinkedList<SearchViewNodeGrouper>();
		this.leafGrouper = leafGrouper;
	}

	final void addGrouper(SearchViewNodeGrouper grouper) {
		this.otherGroupers.add(grouper);
	}

	@Override
	public final List<SearchViewNode> group(List<CallHierarchyNode> nodes) {
		// The first grouper of a composite always start from
		// the "raw" nodes, which means we're grouping nodes
		// at the bottom of the grouping tree.
		List<SearchViewNode> viewNodes = this.leafGrouper.group(nodes);

		Iterator<SearchViewNodeGrouper> grouperIterator = this.otherGroupers.iterator();

		// For every grouper still composed here,
		// keep on passing down grouped SearchViewNode objects
		// to be grouped
		while (grouperIterator.hasNext()) {
			viewNodes = grouperIterator.next().groupViewNodes(viewNodes);
		}

		return viewNodes;
	}
}
