package net.ranger.core;

/**
 * A tracking monitor is the one receiving updates about the progress of a
 * search.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface TrackingMonitor {

	/**
	 * Notifies this monitor that a target has been found during the search.
	 * 
	 * @param node
	 *            The node representing and wrapping the target that's been
	 *            found.
	 */
	void callFound(CallHierarchyNode node);
}
