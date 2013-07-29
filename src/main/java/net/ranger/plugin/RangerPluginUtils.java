package net.ranger.plugin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NodeFinder;
import org.eclipse.jdt.ui.SharedASTProvider;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Provides various utility methods.
 * 
 * @author Emerson Loureiro
 * 
 */
public class RangerPluginUtils {

	/** Util class, no need for a public constructor. */
	private RangerPluginUtils() {
	}

	public static IWorkbench getWorkbench() {
		return PlatformUI.getWorkbench();
	}

	public static IJavaProject[] getWorkspaceJavaProjects() {
		List<IJavaProject> javaProjects = new ArrayList<IJavaProject>(5);

		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
					javaProjects.add(JavaCore.create(project));
				}
			} catch (CoreException e) {
				// Ignore this exception, don't even know why it's
				// thrown from isNatureEnabled (WTF?!). This way we
				// can continue to iterate through the projects.
				// If exceptions are thrown for all projects, we simply
				// return an empty array then.
			}
		}

		return javaProjects.toArray(new IJavaProject[0]);
	}

	public static IEditorPart getActiveEditor() {
		IEditorPart activeEditor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		return activeEditor;
	}

	public static CompilationUnit getCompilationUnit(ICompilationUnit compilationUnit) {
		CompilationUnit astCompilationUnit = SharedASTProvider.getAST(compilationUnit, SharedASTProvider.WAIT_NO, null);
		return astCompilationUnit;
	}

	public static boolean isMethod(ASTNode node) {
		int selectedElementType = node.getNodeType();
		return selectedElementType == ASTNode.METHOD_DECLARATION || selectedElementType == ASTNode.METHOD_INVOCATION;
	}

	public static ASTNode getCurrentlySelectedJavaElement(CompilationUnit compilationUnit) {
		ASTNode node = null;
		ITextEditor textEditor = (ITextEditor) getActiveEditor();
		ISelection selection = textEditor.getSelectionProvider().getSelection();

		if (selection != null && selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			node = NodeFinder.perform(compilationUnit, textSelection.getOffset(), textSelection.getLength());
		}

		return node;
	}
}
