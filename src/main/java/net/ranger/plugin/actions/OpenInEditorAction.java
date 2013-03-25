package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;
import net.ranger.view.SearchViewException;
import net.ranger.view.SearchViewNode;

import org.eclipse.swt.widgets.Shell;


/**
 * A JFace action to open a java element that's being displayed on the call
 * hierarchy view.
 * 
 * @author Emerson Loureiro
 * 
 */
public class OpenInEditorAction extends RangerAction {

	private SearchViewNode viewNode;

	public OpenInEditorAction(Shell shell, SearchViewNode viewNode, RangerComponentFactory componentFactory) {
		super("Open in editor", "Opens this element in editor", shell, componentFactory);
		this.viewNode = viewNode;
	}

	/** {@inheritDoc} */
	@Override
	public void runImpl() throws ActionException {
		try {
			this.viewNode.openInEditor();
		} catch (SearchViewException e) {
			throw new ActionException(e, e.getMessage());
		}
	}
}
