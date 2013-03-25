package net.ranger.plugin.gui;

import java.util.LinkedList;
import java.util.List;

import net.ranger.view.SearchViewNode;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


/**
 * The GUI element where the search result tree is laid out.
 * 
 * @author Emerson Loureiro
 * 
 */
public class SearchViewTreeViewer extends TreeViewer {

	public SearchViewTreeViewer(Composite parent) {
		super(parent, SWT.BORDER);
		this.addTreeListener(new TreeViewerListener());
	}

	@Override
	public void refresh() {
		super.refresh();
		List<Object> expandedElementsList = new LinkedList<Object>();
		Object[] elements = ((ITreeContentProvider) this.getContentProvider()).getElements(null);

		for (Object element : elements) {
			expandedElementsList.addAll(this.getExpandedNodes((SearchViewNode) element));
		}

		this.setExpandedElements(expandedElementsList.toArray(new Object[] {}));
	}

	@Override
	public void expandAll() {
		Object[] elements = ((ITreeContentProvider) this.getContentProvider()).getElements(null);

		for (Object element : elements) {
			this.expandAll((SearchViewNode) element);
		}
	}

	@Override
	public void collapseAll() {
		Object[] elements = ((ITreeContentProvider) this.getContentProvider()).getElements(null);

		for (Object element : elements) {
			this.collapseAll((SearchViewNode) element);
		}
	}

	private void expandAll(SearchViewNode viewNode) {
		this.setExpandedState(viewNode, true);
		viewNode.setExpanded(true);

		for (SearchViewNode child : viewNode.getChildren()) {
			this.expandAll(child);
		}
	}

	private void collapseAll(SearchViewNode viewNode) {
		if (viewNode.isExpanded()) {
			this.setExpandedState(viewNode, false);
			viewNode.setExpanded(false);

			for (SearchViewNode child : viewNode.getChildren()) {
				this.collapseAll(child);
			}
		}
	}

	private List<Object> getExpandedNodes(SearchViewNode node) {
		List<Object> expandedNodes = new LinkedList<Object>();

		if (node.isExpanded()) {
			expandedNodes.add(node);

			for (SearchViewNode child : node.getChildren()) {
				expandedNodes.addAll(this.getExpandedNodes(child));
			}
		}

		return expandedNodes;
	}

	private class TreeViewerListener implements ITreeViewerListener {

		@Override
		public void treeCollapsed(TreeExpansionEvent event) {
			((SearchViewNode) event.getElement()).setExpanded(false);
		}

		@Override
		public void treeExpanded(TreeExpansionEvent event) {
			((SearchViewNode) event.getElement()).setExpanded(true);
		}
	}
}
