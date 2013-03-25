package net.ranger.search;

import java.util.List;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.Source;


/**
 * Encapsulates the results of a search that has either been finished or
 * manually stopped by the user.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchResult {

	/**
	 * Returns all targets found during the search, or an empty list in case no
	 * targets have been found. This method does not return null.
	 * 
	 * @return a List of {@link CallHierarchyNode}
	 */
	List<CallHierarchyNode> getTargetsFound();

	/**
	 * Returns the original target of the search associated with this result.
	 * 
	 * @return {@link Source}
	 */
	Source getTarget();
}
