package net.ranger.plugin.gui;

import net.ranger.plugin.actions.StartSearchAction;
import net.ranger.plugin.integration.RangerComponentFactoryImpl;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;


/**
 * This is just a command handler to start a new search. This is defined only
 * because Eclipse has some separation between commands and JFace actions. In
 * Ranger, user functionality is provided through JFace actions, but binding
 * them to some of the Eclipse UI hooks - e.g., a shortcut - can sometimes only
 * be done via a command. This then allows us to handle these cases, where we
 * simply delegate execution of the command associated with a handler to the
 * appropriate JFace action defined by the plugin.
 */
public class StartSearchCommandHandler extends AbstractHandler {

	public StartSearchCommandHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		// Just delegate to the action
		new StartSearchAction(window.getShell(), RangerComponentFactoryImpl.getInstance()).run();
		// Nothing to return really
		return null;
	}
}
