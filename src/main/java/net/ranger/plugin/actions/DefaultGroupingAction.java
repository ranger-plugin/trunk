package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;
import net.ranger.view.Groupers;
import net.ranger.view.SearchViewException;

import org.eclipse.swt.widgets.Shell;


/**
 * Action to display search results ungrouped, which is the selected by default.
 * 
 * @author Emerson Loureiro
 * 
 */
public class DefaultGroupingAction extends RangerAction {

	public DefaultGroupingAction(Shell shell, RangerComponentFactory componentFactory) {
		super("Display all results", "Display the results ungrouped", shell, componentFactory, true);
		this.setImageDescriptor(ActionUtils.getEclipsePublicMethodIcon());
	}

	/** {@inheritDoc} */
	@Override
	public void runImpl() throws ActionException {
		try {
			this.componentFactory.getSearchResultViewController().changeGrouping(Groupers.DEFAULT_GROUPER);
		} catch (SearchViewException e) {
			throw new ActionException(e);
		}
	}
}
