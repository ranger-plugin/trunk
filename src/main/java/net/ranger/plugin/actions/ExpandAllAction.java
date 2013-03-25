package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;


/**
 * A JFace action to expand all nodes currently being displayed on the search
 * result view.
 * 
 * @author Emerson Loureiro
 * 
 */
public class ExpandAllAction extends RangerAction {

	/** The Eclipse tree where the search results are laid out. */
	private TreeViewer treeViewer;

	public ExpandAllAction(Shell shell, RangerComponentFactory componentFactory, TreeViewer treeViewer) {
		super("Expand all", "Expand all nodes in the view", shell, componentFactory);
		this.treeViewer = treeViewer;
		this.setImageDescriptor(ActionUtils.getEclipseExpandAllIcon());
	}

	@Override
	public void runImpl() throws ActionException {
		this.treeViewer.expandAll();
	}
}
