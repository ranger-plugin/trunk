package net.ranger.core;

import java.util.List;

/**
 * Represents a node in a call hierarchy. When searching for calls, for example,
 * the method being searched - i.e., the target - will be placed in a node at
 * the root of the hierarchy, and methods that invoke the target are placed in
 * children nodes of the root, and so on recursively.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface CallHierarchyNode {

	/**
	 * Returns the parent of this node in the hierarchy. This returns
	 * <code>null</code> for the node at the root.
	 * 
	 * @return <code>CallHierarchyNode</code>
	 */
	CallHierarchyNode getParent();

	/**
	 * Returns all child nodes of this one. If this node is a leaf, the list
	 * returned is empty; it does not return null.
	 * 
	 * @return a {@link List} of {@link CallHierarchyNode}.
	 */
	List<CallHierarchyNode> getChildren();

	/**
	 * Adds the provided node as a child of this one.
	 * 
	 * @param child
	 *            The node to be added.
	 * @throws IllegalArgumentException
	 *             If a null object is provided.
	 */
	void addChild(CallHierarchyNode child);

	/**
	 * Returns the depth of this node within the hierarchy it's placed in. The
	 * node at the root of the hierarchy has depth 0.
	 * 
	 * @return int
	 */
	int getDepth();

	/**
	 * Returns the object - e.g., method - that is invoking the object
	 * encapsulated by the parent of this node. For example, if a method
	 * <code>A</code> invokes <code>B</code>, this method would return
	 * <code>A</code> for the node at the root of the hierarchy and
	 * <code>B</code> for its child node.
	 * 
	 * @return {@link Source}
	 */
	Source getCaller();

	/**
	 * Returns whether this node is a target for the search where it's been
	 * found or not.
	 * 
	 * @return True if it's a target and false otherwise.
	 */
	boolean isTarget();

	/**
	 * Sets whether this node is a target for the search where it's been found
	 * or not.
	 * 
	 * @param isTarget
	 *            True if it's a target and false otherwise.
	 */
	void setIsTarget(boolean isTarget);

	/**
	 * Returns the {@link SearchData} associated with this node. For the node at
	 * the root of the hierarchy, this would return null.
	 * 
	 * @return {@link SearchData}
	 */
	SearchData getSearchData();
}
