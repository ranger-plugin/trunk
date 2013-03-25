package net.ranger.plugin.gui;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.ranger.search.SearchResult;
import net.ranger.view.CallHierarchyNodeGrouper;
import net.ranger.view.Groupers;
import net.ranger.view.SearchResultViewModel;
import net.ranger.view.SearchViewNode;

import org.eclipse.jface.viewers.Viewer;


/**
 * This class is responsible for providing objects to the view. It can wrap
 * existing objects in adapters or simply return objects as-is. These objects
 * may be sensitive to the current input of the view, or ignore it and always
 * show the same content (like Task List, for example).
 * 
 * @author Emerson Loureiro
 */
public class DefaultSearchViewModel implements SearchResultViewModel {

	private final static Object[] EMPTY_ARRAY = new Object[0];

	private SearchViewNode[] viewNodes;

	private SearchResult currentResults;

	private CallHierarchyNodeGrouper grouper;

	private Map<Integer, SearchViewNode[]> groupings;

	public DefaultSearchViewModel() {
		this.grouper = Groupers.PROJECT_GROUPER;
	}

	// -------------------------------------
	// Methods from SearchResultViewModel
	// -------------------------------------

	/** {@inheritDoc} */
	@Override
	public void setSearchResult(SearchResult result) {
		this.currentResults = result;
		// Reset the groupings already done every time new
		// results are received
		this.groupings = new TreeMap<Integer, SearchViewNode[]>();
		this.updateGroupings();
	}

	/** {@inheritDoc} */
	@Override
	public SearchResult getSearchResult() {
		return this.currentResults;
	}

	/** {@inheritDoc} */
	@Override
	public void setGrouping(CallHierarchyNodeGrouper grouper) {
		if (grouper != null) {
			this.grouper = grouper;
			this.updateGroupings();
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<SearchResult> getHistory() {
		// TODO Implement me
		return null;
	}

	// ---------------------------------
	// Methods from ITreeContentProvider
	// ---------------------------------

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Object[] getElements(Object parent) {
		// If we have a latest result, return the nodes the way
		// they have been grouped to be presented.
		if (this.viewNodes != null) {
			return this.viewNodes;
		} else {
			return EMPTY_ARRAY;
		}
	}

	@Override
	public Object[] getChildren(Object element) {
		Object[] children = null;

		if (element instanceof SearchViewNode) {
			children = ((SearchViewNode) element).getChildren().toArray(new Object[0]);
		}

		return children;
	}

	@Override
	public Object getParent(Object element) {
		// TODO CHEK: No idea what this method is for yet. Will leave it returning null for now 
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return (element instanceof SearchViewNode) && (((SearchViewNode) element).getChildren().size() != 0);
	}

	// ---------------------------------
	// PRIVATE/UTILITY METHODS
	// ---------------------------------

	private void updateGroupings() {
		if (this.currentResults != null) {
			// First, check whether we already have done a grouping
			// for the current results, for the grouping mode
			// currently set.
			this.viewNodes = this.groupings.get(this.grouper.hashCode());

			// If not, then do the grouping, and add it to the cache.
			// This will allow us to retrieve it later, thus keeping
			// the current state of the result tree in the view
			if (this.viewNodes == null) {
				this.viewNodes = this.grouper.group(this.currentResults.getTargetsFound()).toArray(new SearchViewNode[0]);
				this.groupings.put(this.grouper.hashCode(), this.viewNodes);
			}
		}
	}
}
