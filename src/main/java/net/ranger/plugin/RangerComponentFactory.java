package net.ranger.plugin;

import net.ranger.search.SearchProcessor;
import net.ranger.view.SearchResultViewController;


/**
 * @author Emerson Loureiro
 * 
 */
public interface RangerComponentFactory {

	SearchProcessor getSearchProcessor();

	SearchResultViewController getSearchResultViewController();
}
