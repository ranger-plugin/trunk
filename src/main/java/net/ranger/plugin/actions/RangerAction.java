package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;


/**
 * Super-class of all actions defined by the plugin. Provides some customized
 * set-up, for the purposes of the plugin, as well as automatic error handling
 * at the GUI-level.
 * 
 * @author Emerson Loureiro
 * 
 */
public abstract class RangerAction extends Action {

	/** Allows access to GUI operations - e.g., opening message dialogs. */
	private Shell shell;

	protected RangerComponentFactory componentFactory;

	/**
	 * Creates a new action with the provided parameters.
	 * 
	 * @param text
	 *            A descriptive text associated with this action. This is the
	 *            text that'll be displayed at the GUI level, should this action
	 *            have no image icon associated with.
	 * @param tooltip
	 *            The tooltip associated with this action, at the GUI level.
	 * @param shell
	 *            Provides access to various GUI-related operations; e.g.,
	 *            opening a message dialog.
	 * @param componentFactory
	 *            The search processor component of the plugin.
	 */
	public RangerAction(String text, String tooltip, Shell shell, RangerComponentFactory componentFactory) {
		this(text, tooltip, shell, componentFactory, false);
	}

	/**
	 * Creates a new action with the provided parameters.
	 * 
	 * @param text
	 *            A descriptive text associated with this action. This is the
	 *            text that'll be displayed at the GUI level, should this action
	 *            have no image icon associated with.
	 * @param tooltip
	 *            The tooltip associated with this action, at the GUI level.
	 * @param shell
	 *            Provides access to various GUI-related operations; e.g.,
	 *            opening a message dialog.
	 * @param radioButton
	 *            Weather the button associated with this action will behave as
	 *            a radio button or not.
	 * @param componentFactory
	 *            The search processor component of the plugin.
	 */
	public RangerAction(String text, String tooltip, Shell shell, RangerComponentFactory componentFactory, boolean radioButton) {
		super(text, (radioButton ? Action.AS_RADIO_BUTTON : Action.AS_PUSH_BUTTON));
		if (tooltip != null) {
			this.setToolTipText(tooltip);
		}
		this.shell = shell;
		this.componentFactory = componentFactory;
	}

	/**
	 * Returns the object that provides access to GUI operations - e.g., opening
	 * message dialogs - to this action.
	 * 
	 * @return {@link Shell}
	 */
	public final Shell getShell() {
		return this.shell;
	}

	/** {@inheritDoc} */
	@Override
	public final void run() {
		try {
			this.runImpl();
		} catch (ActionException e) {
			MessageDialog.openError(this.getShell(), "Plugin error", e.getMessage());
		} catch (Throwable t) {
			MessageDialog.openError(this.getShell(), "Internal error", t.getMessage());
		}
	}

	/**
	 * Sub-classes specific implementation of the run() method. Sub-classes
	 * shouldn't be concerned with error handling; throwing a
	 * {@link ActionException} is the preferred way of doing so, and let
	 * {@link RangerAction#run()} handle the error appropriately. There's also
	 * no need to catch unchecked exceptions here, as that's also handled at
	 * {@link RangerAction#run()}.
	 */
	public abstract void runImpl() throws ActionException;
}
