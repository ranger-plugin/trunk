package net.ranger.view;

import java.util.ArrayList;
import java.util.List;

import net.ranger.core.CallHierarchyNode;


/**
 * A {@link SearchViewNode} which displays method calls found during a search,
 * from the callers perspective.
 * 
 * @author Emerson Loureiro
 * 
 */
public class CallerSearchViewNode extends CallHierarchyNodeSearchViewNode {

	private List<SearchViewNode> viewNodes;

	/** Creates a new object of this type. */
	public CallerSearchViewNode(CallHierarchyNode node) {
		super(node);
	}

	/** {@inheritDoc} */
	@Override
	public List<SearchViewNode> getChildren() {
		if (this.viewNodes == null) {
			this.viewNodes = new ArrayList<SearchViewNode>(1);

			if (this.getNode().getParent() != null) {
				this.viewNodes.add(new CallerSearchViewNode(this.getNode().getParent()));
			}
		}

		return this.viewNodes;
	}
}
