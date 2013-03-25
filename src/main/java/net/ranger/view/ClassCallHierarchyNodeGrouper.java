package net.ranger.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ranger.core.CallHierarchyNode;

import org.eclipse.jdt.core.IJavaElement;


/**
 * @author Emerson Loureiro
 * 
 */
public class ClassCallHierarchyNodeGrouper implements CallHierarchyNodeGrouper {

	ClassCallHierarchyNodeGrouper() {
	}

	/** {@inheritDoc} */
	@Override
	public List<SearchViewNode> group(List<CallHierarchyNode> nodes) {
		Map<String, SearchViewNode> classTable = new HashMap<String, SearchViewNode>(nodes.size());

		for (CallHierarchyNode node : nodes) {
			String nodeJavaFileName = node.getCaller().getJavaFile().getName();
			SearchViewNodeComposite classNode = (SearchViewNodeComposite) classTable.get(nodeJavaFileName);

			if (classNode == null) {
				classNode = this.createSearchViewNodeComposite(node.getCaller().getJavaFile().getCompilationUnit());
				classTable.put(nodeJavaFileName, classNode);
			}

			classNode.addViewNode(this.createCallHierarchyViewNode(node));
		}

		return new ArrayList<SearchViewNode>(classTable.values());
	}

	SearchViewNode createCallHierarchyViewNode(CallHierarchyNode node) {
		return new CallerSearchViewNode(node);
	}

	SearchViewNodeComposite createSearchViewNodeComposite(IJavaElement element) {
		return new SearchViewNodeComposite(element);
	}
}
