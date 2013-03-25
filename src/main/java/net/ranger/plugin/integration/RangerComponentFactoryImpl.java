package net.ranger.plugin.integration;

import net.ranger.plugin.RangerComponentFactory;
import net.ranger.search.SearchProcessor;
import net.ranger.view.SearchResultViewController;


/**
 * @author Emerson Loureiro
 * 
 */
public class RangerComponentFactoryImpl implements RangerComponentFactory {

	private SearchProcessor searchProcessor;

	private SearchResultViewController viewController;

	private static final RangerComponentFactory instance = new RangerComponentFactoryImpl();

	private RangerComponentFactoryImpl() {
		this.searchProcessor = new SingleSearchSearchProcessor();
		this.viewController = new DefaultSearchResultViewController();
	}

	public static RangerComponentFactory getInstance() {
		return instance;
	}

	/** {@inheritDoc} */
	@Override
	public SearchProcessor getSearchProcessor() {
		return this.searchProcessor;
	}

	/** {@inheritDoc} */
	@Override
	public SearchResultViewController getSearchResultViewController() {
		return this.viewController;
	}
}
