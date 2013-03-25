package net.ranger.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;

/**
 * @author Emerson Loureiro
 * 
 */
public class ProjectSearchViewNodeGrouper extends CompositeGrouper implements SearchViewNodeGrouper {

	ProjectSearchViewNodeGrouper() {
		this(new ClassCallHierarchyNodeGrouper());
	}

	/**
	 * Used only for unit testing, to easily mock out the behaviour of the class
	 * grouper.
	 */
	ProjectSearchViewNodeGrouper(CallHierarchyNodeGrouper classGrouper) {
		super(classGrouper);
		this.addGrouper(this);
	}

	@Override
	public List<SearchViewNode> groupViewNodes(List<SearchViewNode> viewNodes) {
		Map<String, SearchViewNode> projectTable = new HashMap<String, SearchViewNode>(viewNodes.size());

		for (SearchViewNode node : viewNodes) {
			String projectName = ((ICompilationUnit) node.getJavaElement()).getJavaProject().getElementName();
			SearchViewNodeComposite projectNode = (SearchViewNodeComposite) projectTable.get(projectName);

			if (projectNode == null) {
				projectNode = this.createSearchViewNode(((ICompilationUnit) node.getJavaElement()).getJavaProject());
				projectTable.put(projectName, projectNode);
			}

			projectNode.addViewNode(node);
		}

		return new ArrayList<SearchViewNode>(projectTable.values());
	}

	/**
	 * Creates a new view node to be grouped by this grouper. Just put this up
	 * to make it easier mocking out this behaviour.
	 */
	SearchViewNodeComposite createSearchViewNode(IJavaElement element) {
		return new SearchViewNodeComposite(element);
	}
}
