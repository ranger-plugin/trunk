package net.ranger.view;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaUI;

/**
 * @author Emerson Loureiro
 * 
 */
public class SearchViewNodeComposite extends AbstractSearchViewNode {

	private List<SearchViewNode> viewNodes;

	public SearchViewNodeComposite(IJavaElement element) throws IllegalArgumentException {
		super(element);
		this.viewNodes = new LinkedList<SearchViewNode>();
	}

	public final void addViewNode(SearchViewNode node) {
		this.viewNodes.add(node);
	}

	/** {@inheritDoc} */
	@Override
	public final List<SearchViewNode> getChildren() {
		return this.viewNodes;
	}

	@Override
	public final void openInEditor() throws SearchViewException {
		try {
			JavaUI.openInEditor(this.getJavaElement());
		} catch (Exception e) {
			throw new SearchViewException("Error opening java element on the editor", e);
		}
	}
}
