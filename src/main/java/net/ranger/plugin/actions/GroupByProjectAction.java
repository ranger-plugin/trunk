package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;
import net.ranger.view.Groupers;
import net.ranger.view.SearchViewException;

import org.eclipse.swt.widgets.Shell;


/**
 * An action to group search results by the project where target methods have
 * been found.
 * 
 * @author Emerson Loureiro
 * 
 */
public class GroupByProjectAction extends RangerAction {

	public GroupByProjectAction(Shell shell, RangerComponentFactory componentFactory) {
		super("Group by project", "Group the results by project", shell, componentFactory, true);
		this.setImageDescriptor(ActionUtils.getEclipseProjectIcon());
		this.setChecked(true);
	}

	/** {@inheritDoc} */
	@Override
	public void runImpl() throws ActionException {
		try {
			this.componentFactory.getSearchResultViewController().changeGrouping(Groupers.PROJECT_GROUPER);
		} catch (SearchViewException e) {
			throw new ActionException(e);
		}
	}
}
