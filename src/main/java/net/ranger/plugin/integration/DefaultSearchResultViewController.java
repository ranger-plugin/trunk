package net.ranger.plugin.integration;

import net.ranger.search.SearchResult;
import net.ranger.view.CallHierarchyNodeGrouper;
import net.ranger.view.SearchResultView;
import net.ranger.view.SearchResultViewController;
import net.ranger.view.SearchResultViewModel;
import net.ranger.view.SearchViewException;


/**
 * A default implementation of {@link SearchResultViewController}.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DefaultSearchResultViewController implements SearchResultViewController {

	private SearchResultView searchView;

	private SearchResultViewModel searchViewModel;

	/** {@inheritDoc} */
	@Override
	public void changeGrouping(CallHierarchyNodeGrouper grouper) throws SearchViewException {
		this.searchViewModel.setGrouping(grouper);
		this.searchView.updateResults();
	}

	/** {@inheritDoc} */
	@Override
	public void changeCurrentSearchResult(SearchResult result) throws SearchViewException {
		this.searchViewModel.setSearchResult(result);
		this.searchView.updateResults();
	}

	/** {@inheritDoc} */
	@Override
	public void setSearchResultView(SearchResultView searchView) {
		this.searchView = searchView;
	}

	/** {@inheritDoc} */
	@Override
	public void setSearchResultViewModel(SearchResultViewModel searchViewModel) {
		this.searchViewModel = searchViewModel;
	}
}
