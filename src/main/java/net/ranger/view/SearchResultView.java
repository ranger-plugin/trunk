package net.ranger.view;

/**
 * General interface to represent the actual GUI component where search results
 * are laid out.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchResultView {

	void updateResults() throws SearchViewException;
}
