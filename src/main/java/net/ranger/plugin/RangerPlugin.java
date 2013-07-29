package net.ranger.plugin;

import java.util.ArrayList;
import java.util.List;

import net.ranger.extensions.TestChecker;
import net.ranger.plugin.gui.SearchView;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
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

	private static final String TEST_CHECKER_EXTENSION_ID = "net.ranger.extensions.testChecker";

	// The shared instance
	private static RangerPlugin plugin;

	/** Keeps all the test checker extensions found on the workspace. */
	private List<TestChecker> testCheckerExtensions;

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
		this.testCheckerExtensions = loadTestCheckerExtensions();
		if (this.testCheckerExtensions.isEmpty()) {
			throw new RuntimeException("No test checker extension found. Plugin won't work without at least one!");
		}
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

	/**
	 * Returns the list of test checker extensions that were available, and
	 * loaded, when the plugin was initialized.
	 * 
	 * @return {@link List}
	 */
	public List<TestChecker> getTestCheckerExtensions() {
		return this.testCheckerExtensions;
	}

	private List<TestChecker> loadTestCheckerExtensions() {
		List<TestChecker> testCheckerExtensions = new ArrayList<TestChecker>();
		IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
		IConfigurationElement[] configurations = extensionRegistry.getConfigurationElementsFor(TEST_CHECKER_EXTENSION_ID);
		for (IConfigurationElement configuration : configurations) {
			try {
				Object extension;
				extension = configuration.createExecutableExtension("class");
				if (extension instanceof TestChecker) {
					testCheckerExtensions.add((TestChecker) extension);
				}
			} catch (CoreException e) {
				log("Error loading test checker extension. Configuration: \"" + configuration.getName() + "\"");
			}
		}
		return testCheckerExtensions;
	}
}
