package net.ranger.plugin.gui;

import net.ranger.view.SearchViewNode;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


/**
 * Determines how elements in the search view of the plugin are to be labelled.
 * 
 * @author Emerson Loureiro
 * 
 */
public class SearchViewLabelProvider extends LabelProvider {

	@Override
	public void dispose() {
	}

	@Override
	public Image getImage(Object element) {
		Image image = null;

		if (element != null && element instanceof SearchViewNode) {
			return ((SearchViewNode) element).getImage();
		}

		return image;
	}

	@Override
	public String getText(Object element) {
		String label = null;

		if (element != null && element instanceof SearchViewNode) {
			label = ((SearchViewNode) element).getLabel();
		}

		return label;
	}
}
