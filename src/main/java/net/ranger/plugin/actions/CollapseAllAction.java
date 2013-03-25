package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Shell;


/**
 * A JFace action to collapse all nodes currently being displayed on the search
 * result view.
 * 
 * @author Emerson Loureiro
 * 
 */
public class CollapseAllAction extends RangerAction {

	/** The Eclipse tree where the search results are laid out. */
	private TreeViewer treeViewer;

	public CollapseAllAction(Shell shell, RangerComponentFactory componentFactory, TreeViewer treeViewer) {
		super("Collapse all", "Collapse all nodes in the view", shell, componentFactory);
		this.treeViewer = treeViewer;
		this.setImageDescriptor(ActionUtils.getEclipseCollapseAllIcon());
	}

	@Override
	public void runImpl() throws ActionException {
		this.treeViewer.collapseAll();
	}
}
