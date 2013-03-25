package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;
import net.ranger.view.Groupers;
import net.ranger.view.SearchViewException;

import org.eclipse.swt.widgets.Shell;


/**
 * An action to group search results by the class where target methods have been
 * found.
 * 
 * @author Emerson Loureiro
 * 
 */
public class GroupByClassAction extends RangerAction {

	public GroupByClassAction(Shell shell, RangerComponentFactory componentFactory) {
		super("Group by class", "Group the results by class", shell, componentFactory, true);
		this.setImageDescriptor(ActionUtils.getEclipseClassIcon());
	}

	/** {@inheritDoc} */
	@Override
	public void runImpl() throws ActionException {
		try {
			this.componentFactory.getSearchResultViewController().changeGrouping(Groupers.CLASS_GROUPER);
		} catch (SearchViewException e) {
			throw new ActionException(e);
		}
	}
}
