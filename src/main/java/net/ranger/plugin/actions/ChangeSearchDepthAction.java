package net.ranger.plugin.actions;

import net.ranger.plugin.RangerComponentFactory;
import net.ranger.plugin.RangerPlugin;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


/**
 * @author Emerson Loureiro
 * 
 */
public class ChangeSearchDepthAction extends RangerAction {

	public ChangeSearchDepthAction(Shell shell, RangerComponentFactory componentFactory) {
		super("Max Search Depth", "Defines the maximum search depth used by the algorithm", shell, componentFactory);
	}

	/** {@inheritDoc} */
	@Override
	public void runImpl() throws ActionException {
		RangerPlugin.getDefault().getPreferenceStore().setValue(PreferenceConstants.MAX_SEARCH_DEPTH_PROPERTY_NAME, this.getNewSearchDepth());
	}

	/** Returns the user's input for the new maximum search depth. */
	int getNewSearchDepth() {
		SetMaxSearchDepthDialog dialog = new SetMaxSearchDepthDialog(this.getShell(), RangerPlugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.MAX_SEARCH_DEPTH_PROPERTY_NAME));

		if (dialog.open() == Window.OK) {
			return dialog.getSearchDepthField();
		} else {
			return RangerPlugin.getDefault().getPreferenceStore().getInt(PreferenceConstants.MAX_SEARCH_DEPTH_PROPERTY_NAME);
		}
	}

	/**
	 * Inner class for the dialog that receives the user input of the new
	 * maximum search depth.
	 */
	private class SetMaxSearchDepthDialog extends Dialog {

		private Text maxSearchDepth;
		private int initialValue;
		private Label errorLabel;
		private int parsedSearchDepth;
		private static final int MAX_SEARCH_DEPTH = 100;

		protected SetMaxSearchDepthDialog(Shell parentShell, int initialValue) {
			super(parentShell);
			this.initialValue = initialValue;
		}

		@Override
		public void create() {
			super.create();
			this.getShell().setSize(360, 135);
		}

		@Override
		protected Control createDialogArea(Composite parent) {
			parent.getShell().setText("Enter new max search depth");
			// Layout of the dialog box
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			parent.setLayout(layout);

			// Positioning the max search depth field label
			Label maxSearchDepthFieldLabel = new Label(parent, SWT.NONE);
			maxSearchDepthFieldLabel.setText("Value");

			// Positioning the max search depth field
			GridData gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = GridData.FILL;
			this.maxSearchDepth = new Text(parent, SWT.BORDER);
			this.maxSearchDepth.setText(String.valueOf(this.initialValue));
			this.maxSearchDepth.setLayoutData(gridData);

			// Positioning the error label
			gridData = new GridData();
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalAlignment = GridData.FILL;
			gridData.horizontalSpan = 2;
			this.errorLabel = new Label(parent, SWT.NONE);
			this.errorLabel.setText("");
			this.errorLabel.setLayoutData(gridData);

			return parent;
		}

		@Override
		protected void createButtonsForButtonBar(Composite parent) {
			GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			parent.setLayoutData(gridData);

			this.createButton(parent, OK, "Ok", true);
			this.createButton(parent, CANCEL, "Cancel", false);
		}

		@Override
		protected void okPressed() {
			// Validate the input when Ok is pressed
			if (this.isValidSearchDepth()) {
				super.okPressed();
			} else {
				this.errorLabel.setText("Please enter a valid integer [1 - " + MAX_SEARCH_DEPTH + "]");
			}
		}

		/**
		 * Checks whether the current value at the max search depth field is
		 * valid (i.e., an integer).
		 */
		private boolean isValidSearchDepth() {
			boolean valid = true;

			try {
				int parsedInput = Integer.parseInt(this.maxSearchDepth.getText());

				if (parsedInput > 0 && parsedInput <= MAX_SEARCH_DEPTH) {
					this.parsedSearchDepth = parsedInput;
				} else {
					valid = false;
				}
			} catch (NumberFormatException e) {
				valid = false;
			}

			return valid;
		}

		/** Returns the value of the search depth field. */
		public int getSearchDepthField() {
			return this.parsedSearchDepth;
		}
	}
}
