package net.ranger.view;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * A default abstract implementation of a {@link SearchViewNode}.
 * 
 * @author Emerson Loureiro
 * 
 */
public abstract class AbstractSearchViewNode implements SearchViewNode {

	/** The label of this view node in the GUI. */
	protected String label;

	/** The icon image representing this node in the GUI. */
	protected Image image;

	/**
	 * This essentially allows us to use the Eclipse default settings for labels
	 * and images - e.g., a small green circle for public methods - of standard
	 * java elements - e.g., project, method.
	 */
	protected ILabelProvider labelProvider;

	/** {@link IJavaElement} associated with this node. */
	private IJavaElement javaElement;

	/** Whether this node is expanded or not. */
	private boolean expanded;

	/**
	 * Default {@link ILabelProvider} used by the view nodes. Put here to avoid
	 * creating multiple instances of this type, as one is enough across
	 * multiple nodes.
	 */
	private static ILabelProvider DEFAULT_LABEL_PROVIDER;

	/**
	 * Creates a new object of this type. The provided java element is used to
	 * automatically set up the label and icon of this view node using Eclipse
	 * standards for that.
	 * 
	 * @param javaElement
	 *            The java element wrapped by this view node.
	 */
	public AbstractSearchViewNode(IJavaElement javaElement) {
		this.javaElement = javaElement;
		this.labelProvider = this.getDefaultLabelProvider();
		this.label = this.labelProvider.getText(javaElement);
		this.image = this.labelProvider.getImage(javaElement);
	}

	/**
	 * Creates a new object of this type. The provided java element is used to
	 * automatically set up the label and icon of this view node using the
	 * provided {@link ILabelProvider}.
	 * 
	 * @param javaElement
	 *            The java element wrapped by this view node.
	 * @param labelProvider
	 *            Responsible for determining the label and icon images used to
	 *            represent this view node on the GUI.
	 */
	public AbstractSearchViewNode(IJavaElement javaElement, ILabelProvider labelProvider) {
		this.javaElement = javaElement;
		this.labelProvider = labelProvider;
		this.label = this.labelProvider.getText(javaElement);
		this.image = this.labelProvider.getImage(javaElement);
	}

	/** {@inheritDoc} */
	@Override
	public String getLabel() {
		return this.label;
	}

	/** {@inheritDoc} */
	@Override
	public Image getImage() {
		return this.image;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return this.label;
	}

	/** {@inheritDoc} */
	@Override
	public IJavaElement getJavaElement() {
		return this.javaElement;
	}

	/** {@inheritDoc} */
	@Override
	public final void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isExpanded() {
		return this.expanded;
	}

	/** Wrapped this in a method to make it easier mocking out this behaviour. */
	ILabelProvider getDefaultLabelProvider() {
		if (DEFAULT_LABEL_PROVIDER == null) {
			DEFAULT_LABEL_PROVIDER = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_SMALL_ICONS | JavaElementLabelProvider.SHOW_OVERLAY_ICONS);
		}

		return DEFAULT_LABEL_PROVIDER;
	}
}
