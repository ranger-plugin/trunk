package net.ranger.plugin.integration;

import net.ranger.search.DefaultSearchResult;

import junit.framework.TestCase;

/**
 * The class <code>DefaultSearchResultTest</code> contains tests for the class
 * {@link <code>DefaultSearchResult</code>}
 * 
 * @pattern JUnit Test Case
 * 
 * @generatedBy CodePro
 * 
 * @author Emerson Loureiro
 */
public class DefaultSearchResultTest extends TestCase {

	/**
	 * Tests that {@link DefaultSearchResult#getTargetsFound()} does not return
	 * a null list.
	 */
	public void testGetTargetsDoesNotReturnNull() {
		DefaultSearchResult searchResult = new DefaultSearchResult(null, null);
		assertNotNull("List of targets should not be null!", searchResult.getTargetsFound());
	}
}