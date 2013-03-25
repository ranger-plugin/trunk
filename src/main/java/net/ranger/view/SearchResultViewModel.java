package net.ranger.view;

import java.util.List;

import net.ranger.search.SearchResult;

import org.eclipse.jface.viewers.ITreeContentProvider;


/**
 * This represents the model that backs up the plugin GUI view that's used to
 * display search and manipulate search results.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchResultViewModel extends ITreeContentProvider {

	/**
	 * Sets a new grouping mode to be used when presenting results through this
	 * view model. Upon setting a new grouper, any attempt to obtain the search
	 * results should yield in them being presented grouped as per the group
	 * mode defined.
	 * 
	 * @param grouper
	 *            The grouper implementing the new grouping mode to be set on
	 *            this view model.
	 */
	void setGrouping(CallHierarchyNodeGrouper grouper);

	/**
	 * Returns all search results kept by this view model so far. When no
	 * results exist yet, an empty list is returned; this method doesn't return
	 * null.
	 * 
	 * @return A List of {@link SearchResult} objects, or an empty list.
	 */
	List<SearchResult> getHistory();

	/**
	 * Sets the latest result of this view model. Upon having this set, the
	 * search results are to be automatically grouped as per group mode defined
	 * for this view model.
	 * 
	 * @param result
	 *            The latest result to be set on this view model
	 */
	void setSearchResult(SearchResult result);

	/**
	 * Returns the latest result of this view model.
	 * 
	 * @return {@link SearchResult}
	 */
	SearchResult getSearchResult();
}
