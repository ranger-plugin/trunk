package net.ranger.plugin.actions;

import org.eclipse.jdt.ui.ISharedImages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE.SharedImages;

/**
 * Provides utility methods for the action objects accessible from the
 * interface.
 * 
 * @author Emerson Loureiro
 * 
 */
public class ActionUtils {

	private ActionUtils() {
	}

	/**
	 * Returns the standard Eclipse icon to represent a collapse all action on a
	 * tree view.
	 * 
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getEclipseCollapseAllIcon() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(org.eclipse.ui.ISharedImages.IMG_ELCL_COLLAPSEALL);
	}

	/**
	 * Returns the standard Eclipse icon to represent a expand all action on a
	 * tree view.
	 * 
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getEclipseExpandAllIcon() {
		return ImageDescriptor.createFromURL(ActionUtils.class.getResource("/icons/expandall.gif"));
	}

	/**
	 * Returns the standard Eclipse icon to represent a public class.
	 * 
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getEclipseClassIcon() {
		return JavaUI.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_CLASS);
	}

	/**
	 * Returns the standard Eclipse icon to represent a java project.
	 * 
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getEclipseProjectIcon() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(SharedImages.IMG_OBJ_PROJECT);
	}

	/**
	 * Returns the standard Eclipse icon to represent a stop action.
	 * 
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getEclipseStopIcon() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(org.eclipse.ui.ISharedImages.IMG_ELCL_STOP);
	}

	/**
	 * Returns the standard Eclipse icon to represent a public method.
	 * 
	 * @return {@link ImageDescriptor}
	 */
	public static ImageDescriptor getEclipsePublicMethodIcon() {
		return JavaUI.getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_PUBLIC);
	}
}
