package net.ranger.plugin.actions;

import net.ranger.constraint.TestMethodTrackingConstraint;
import net.ranger.core.SourceWrappers;
import net.ranger.core.TrackingException;
import net.ranger.plugin.RangerComponentFactory;
import net.ranger.plugin.RangerPlugin;
import net.ranger.plugin.RangerPluginUtils;
import net.ranger.search.Search;
import net.ranger.search.SearchFinishedEvent;
import net.ranger.search.SearchProcessorListener;
import net.ranger.search.SearchStartedEvent;
import net.ranger.search.WorkspaceSearch;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * A JFace action to start a new search. It's encapsulated here so it can be
 * used in different GUI elements.
 * 
 * @author Emerson Loureiro
 * 
 */
public class StartSearchAction extends RangerAction {

	private static final String START_SEARCH_ACTION_DEFINITION_ID = StartSearchAction.class.toString();

	public StartSearchAction(Shell shell, RangerComponentFactory componentFactory) {
		super("Start search", "Starts a new search", shell, componentFactory);
		this.setImageDescriptor(ActionUtils.getEclipseStopIcon());
		this.setActionDefinitionId(START_SEARCH_ACTION_DEFINITION_ID);
	}

	/** {@inheritDoc} */
	@Override
	public void runImpl() throws ActionException {
		IEditorPart activeEditor = RangerPluginUtils.getActiveEditor();

		if (activeEditor != null) {
			IEditorInput editorInput = activeEditor.getEditorInput();

			if (editorInput != null) {
				IJavaElement currentJavaElement = JavaUI.getEditorInputJavaElement(editorInput);

				if (currentJavaElement instanceof ICompilationUnit) {
					ITextEditor textEditor = (ITextEditor) activeEditor;
					ISelection selection = textEditor.getSelectionProvider().getSelection();

					if (selection != null && selection instanceof TextSelection) {
						TextSelection textSelection = (TextSelection) selection;
						try {
							IJavaElement javaElementAtCursor = ((ICompilationUnit) currentJavaElement).getElementAt(textSelection.getOffset());

							if (javaElementAtCursor instanceof IMethod) {
								int maxSearchDepth = RangerPlugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.MAX_SEARCH_DEPTH_PROPERTY_NAME);
								Search search = new WorkspaceSearch(SourceWrappers.create(javaElementAtCursor), new TestMethodTrackingConstraint(maxSearchDepth, RangerPlugin
										.getDefault().getTestCheckerExtensions()), RangerPluginUtils.getWorkspaceJavaProjects());
								this.componentFactory.getSearchProcessor().process(search, new InnerSearchListener());
							} else {
								throw new ActionException("Java element at current position isn't a method");
							}
						} catch (TrackingException e) {
							throw new ActionException(e);
						} catch (JavaModelException e) {
							throw new ActionException(e, "Error during the search. Detailed message:\n" + e.getMessage());
						}
					}
				}
			}
		}
	}

	private class InnerSearchListener implements SearchProcessorListener {

		/** {@inheritDoc} */
		@Override
		public void searchStarted(SearchStartedEvent event) {
			// TODO Implement-me
		}

		/** {@inheritDoc} */
		@Override
		public void searchFinished(SearchFinishedEvent event) {
			try {
				StartSearchAction.this.componentFactory.getSearchResultViewController().changeCurrentSearchResult(event.getSearchResult());
			} catch (Throwable t) {
				MessageDialog.openError(StartSearchAction.this.getShell(), "Internal error", t.getMessage());
			}
		}
	}
}
