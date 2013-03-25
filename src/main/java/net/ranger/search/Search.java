package net.ranger.search;

import net.ranger.core.Source;
import net.ranger.core.TrackingConstraint;
import net.ranger.core.TrackingException;

/**
 * A general representation of a search that's to be performed by the plugin.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface Search {

	/**
	 * Returns the object that is the source of the search, i.e., the method
	 * that the search will track down tests for.
	 * 
	 * @return {@link Source}
	 */
	Source getSource();

	/**
	 * Returns the constraint of the search, which determines how the search
	 * progresses, i.e., what is a target and what isn't, where to trim the
	 * search tree, and so on.
	 * 
	 * @return {@link TrackingConstraint}
	 */
	TrackingConstraint getConstraint();

	/**
	 * Accepts the provided visitor on this search object, which will process
	 * this object depending on its type.
	 * 
	 * @param visitor
	 *            The visitor that'll visit this object depending on its type.
	 * @throws TrackingException
	 *             If there's any error when the visitor is processing this
	 *             search object.
	 */
	void accept(SearchVisitor visitor) throws TrackingException;
}
