package net.ranger.plugin.integration;

import net.ranger.plugin.integration.DefaultSearchResultViewController;
import net.ranger.search.SearchResult;
import net.ranger.test.UnitTestHelper;
import net.ranger.view.CallHierarchyNodeGrouper;
import net.ranger.view.SearchResultView;
import net.ranger.view.SearchResultViewModel;

import org.jmock.Expectations;


/**
 * Unit test for {@link DefaultSearchResultViewController}.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DefaultSearchResultViewControllerTest extends UnitTestHelper {

	private DefaultSearchResultViewController controller;

	@Override
	public void setUpImpl() {
		this.controller = new DefaultSearchResultViewController();
	}

	public void testChangeGrouping() throws Exception {
		// Mock grouper
		final CallHierarchyNodeGrouper grouper = this.mock(CallHierarchyNodeGrouper.class);
		// Mock for SearchResultViewModel
		final SearchResultViewModel searchViewModel = this.mock(SearchResultViewModel.class);
		this.getMockery().checking(new Expectations() {
			{
				oneOf(searchViewModel).setGrouping(grouper);
			}
		});

		this.controller.setSearchResultView(this.mockSearchResultView());
		this.controller.setSearchResultViewModel(searchViewModel);
		this.controller.changeGrouping(grouper);
	}

	public void testChangeCurrentSearchResult() throws Exception {
		// Mock result
		final SearchResult result = this.mock(SearchResult.class);
		// Mock for SearchResultViewModel
		final SearchResultViewModel searchViewModel = this.mock(SearchResultViewModel.class);
		this.getMockery().checking(new Expectations() {
			{
				oneOf(searchViewModel).setSearchResult(result);
			}
		});

		this.controller.setSearchResultView(this.mockSearchResultView());
		this.controller.setSearchResultViewModel(searchViewModel);
		this.controller.changeCurrentSearchResult(result);
	}

	private SearchResultView mockSearchResultView() throws Exception {
		final SearchResultView searchResultView = this.mock(SearchResultView.class);

		this.getMockery().checking(new Expectations() {
			{
				oneOf(searchResultView).updateResults();
			}
		});

		return searchResultView;
	}
}
