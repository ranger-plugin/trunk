package net.ranger.plugin;

import net.ranger.plugin.gui.SearchView;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;


/**
 * The actual plugin class. It controls the life cycle of the
 * <code>Ranger</code> plugin.
 * 
 * @author Emerson Loureiro
 */
public class RangerPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.im.ranger"; //$NON-NLS-1$

	// The shared instance
	private static RangerPlugin plugin;

	/**
	 * Default constructor. It's defined without any parameter to allow Eclipse
	 * to easily instantiate and initialize the plugin.
	 */
	public RangerPlugin() {
	}

	/** {@inheritDoc} */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		PlatformUI.getWorkbench().getViewRegistry().find(SearchView.ID).createView();
	}

	/** {@inheritDoc} */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 * 
	 * @return <code>RangerPlugin</code>
	 */
	public static RangerPlugin getDefault() {
		return plugin;
	}

	/**
	 * Logs the given message using the default logger provided to this plugin
	 * by the underlying Eclipse platform.
	 * 
	 * @param message
	 *            Message to be logged.
	 */
	public static void log(String message) {
		if (plugin != null) {
			IStatus status = new Status(IStatus.INFO, PLUGIN_ID, message);
			plugin.getLog().log(status);
		}
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            The path
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
