package net.ranger.core;

/**
 * A tracking constraint is what allows parts of a call hierarchy to be trimmed
 * during a search, by defining, for example, points in the search where a limit
 * has been reached, and it should no longer continue from then on.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface TrackingConstraint {

	/**
	 * Determines whether the provided node is a target of the search being
	 * constrained by this object.
	 * 
	 * @param node
	 *            The node to be checked.
	 * @return True if the node is a target and false otherwise.
	 */
	boolean isTarget(CallHierarchyNode node);

	/**
	 * Determines whether the search should keep going from the given node on,
	 * in an attempt to limit the search space.
	 * 
	 * @param node
	 *            The node to be checked.
	 * @return True if the search is to continue from the given node and false
	 *         otherwise.
	 */
	boolean keepGoing(CallHierarchyNode node);
}
