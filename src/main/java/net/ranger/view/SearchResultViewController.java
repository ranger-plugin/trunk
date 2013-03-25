package net.ranger.view;

import net.ranger.search.SearchResult;

/**
 * The MVC controller part of the plugin's view GUI.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchResultViewController {

	/**
	 * Sets a new grouping mode to be used when presenting results through the
	 * view. Upon setting a new grouper, any attempt to obtain the search
	 * results should yield in them being presented grouped as per the group
	 * mode defined.
	 * 
	 * @param grouper
	 *            The grouper implementing the new grouping mode to be set on
	 *            this view model.
	 * @throws SearchViewException
	 *             If there's any error when changing the grouping at the model
	 *             or updating the view
	 */
	void changeGrouping(CallHierarchyNodeGrouper grouper) throws SearchViewException;

	/**
	 * Changes the latest result being displayed by the view. Upon having this
	 * set, the search results are to be automatically grouped as per group mode
	 * defined (e.g., via
	 * {@link SearchResultViewController#changeGrouping(CallHierarchyNodeGrouper)}
	 * .
	 * 
	 * @param result
	 *            The latest result to be set on this view model
	 * @throws SearchViewException
	 *             If there's any error when changing the grouping at the model
	 *             or updating the view
	 */
	void changeCurrentSearchResult(SearchResult result) throws SearchViewException;

	/**
	 * Sets the view associated with this controller.
	 * 
	 * @param searchView
	 *            The view to be set.
	 */
	void setSearchResultView(SearchResultView searchView);

	/**
	 * Sets the model associated with this controller.
	 * 
	 * @param searchViewModel
	 *            The model to be set.
	 */
	void setSearchResultViewModel(SearchResultViewModel searchViewModel);
}
