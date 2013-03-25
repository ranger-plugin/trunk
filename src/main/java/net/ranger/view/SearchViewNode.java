package net.ranger.view;

import java.util.List;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.swt.graphics.Image;

/**
 * Represents a node in the search view of the plugin's GUI.
 * 
 * @author Emerson Loureiro
 * 
 */
public interface SearchViewNode {

	/**
	 * Returns the label of this node.
	 * 
	 * @return String
	 */
	String getLabel();

	/**
	 * Returns the image to be associated with this node in the view. If this
	 * node doesn't define any image associated with it, <code>null</code> can
	 * be safely returned.
	 * 
	 * @return {@link Image}
	 */
	Image getImage();

	/**
	 * Returns all nodes under this one, or an empty list if this node has no
	 * children.
	 * 
	 * @return A List of {@link SearchViewNode}, or an empty list. Doesn't
	 *         return null.
	 */
	List<SearchViewNode> getChildren();

	/**
	 * Returns the {@link IJavaElement} associated with this node.
	 * 
	 * @return {@link IJavaElement}
	 */
	IJavaElement getJavaElement();

	/**
	 * Returns whether this view node is expanded or not.
	 * 
	 * @return True if this node is expanded and false if not.
	 */
	boolean isExpanded();

	/**
	 * Sets whether this node is expanded or not.
	 * 
	 * @param expanded
	 *            True if this node is to be expanded and false if not.
	 */
	void setExpanded(boolean expanded);

	/**
	 * Opens the java element corresponding to this node in the Eclipse editor.
	 * 
	 * @throws SearchViewException
	 *             If there's any error while trying to open the element on
	 *             Eclipse.
	 */
	void openInEditor() throws SearchViewException;
}
