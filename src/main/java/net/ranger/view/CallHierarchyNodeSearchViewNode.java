package net.ranger.view;

import net.ranger.core.CallHierarchyNode;
import net.ranger.core.SearchData;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;


/**
 * A {@link SearchViewNode} which is directly associated with a
 * {@link CallHierarchyNode}.
 * 
 * @author Emerson Loureiro
 * 
 */
public abstract class CallHierarchyNodeSearchViewNode extends AbstractSearchViewNode {

	private CallHierarchyNode node;

	/** Creates a new object of this type. */
	public CallHierarchyNodeSearchViewNode(CallHierarchyNode node) {
		super(node.getCaller().getJavaElement(), new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_SMALL_ICONS | JavaElementLabelProvider.SHOW_PARAMETERS));
		this.node = node;
	}

	/** {@inheritDoc} */
	@Override
	public final void openInEditor() throws SearchViewException {
		SearchData nodeSearchData = this.node.getSearchData();

		try {
			if (nodeSearchData != null) {
				IEditorDescriptor editor = PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(this.node.getCaller().getJavaFile().getName());
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
				ICompilationUnit compilationUnit = this.node.getCaller().getJavaFile().getCompilationUnit();
				IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
				ITextEditor part = (ITextEditor) page.openEditor(new FileEditorInput(workspaceRoot.getFile(compilationUnit.getPath())), editor.getId());
				part.getSelectionProvider().setSelection(new TextSelection(nodeSearchData.getOffset(), nodeSearchData.getLength()));
			} else {
				JavaUI.openInEditor(this.node.getCaller().getJavaElement());
			}
		} catch (Exception e) {
			throw new SearchViewException("Error opening java element on the editor", e);
		}
	}

	public final CallHierarchyNode getNode() {
		return this.node;
	}
}
