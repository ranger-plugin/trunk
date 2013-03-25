package net.ranger.core;

import java.util.LinkedList;
import java.util.List;

/**
 * A default implementation of a {@link CallHierarchyNode}.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DefaultCallHierarchyNode implements CallHierarchyNode {

	/** @see CallHierarchyNode#getParent() */
	private CallHierarchyNode parent;

	/** @see CallHierarchyNode#getDepth() */
	private int depth;

	/** @see CallHierarchyNode#getCaller() */
	private Source caller;

	/** @see CallHierarchyNode#getChildren() */
	private List<CallHierarchyNode> children;

	/** @see CallHierarchyNode#isTarget() */
	private boolean isTarget;

	/** @see CallHierarchyNode#getSearchData(). */
	private SearchData searchData;

	/**
	 * Creates a new node.
	 * 
	 * @param parent
	 *            Parent of this node, or null if it's the root of a hierarchy.
	 * @param depth
	 *            The depth of the node in the hierarchy, or 0 if it's the root.
	 * @param caller
	 *            The method represented by this node.
	 * @see CallHierarchyNode#getCaller()
	 */
	public DefaultCallHierarchyNode(CallHierarchyNode parent, int depth, Source caller) {
		this.parent = parent;
		this.depth = depth;
		this.caller = caller;
		this.children = new LinkedList<CallHierarchyNode>();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.caller.getName();
	}

	/** {@inheritDoc} */
	@Override
	public int getDepth() {
		return this.depth;
	}

	/** {@inheritDoc} */
	@Override
	public CallHierarchyNode getParent() {
		return this.parent;
	}

	/** {@inheritDoc} */
	@Override
	public Source getCaller() {
		return this.caller;
	}

	/** {@inheritDoc} */
	@Override
	public List<CallHierarchyNode> getChildren() {
		return this.children;
	}

	/** {@inheritDoc} */
	@Override
	public void addChild(CallHierarchyNode child) {
		this.children.add(child);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isTarget() {
		return this.isTarget;
	}

	/** {@inheritDoc} */
	@Override
	public void setIsTarget(boolean isTarget) {
		this.isTarget = isTarget;
	}

	/** {@inheritDoc} */
	@Override
	public SearchData getSearchData() {
		return this.searchData;
	}

	/**
	 * Sets the search data for this node.
	 * 
	 * @param searchData
	 *            The search data to be set.
	 */
	public void setSearchData(SearchData searchData) {
		this.searchData = searchData;
	}
}
