package net.ranger.plugin.gui;

import net.ranger.plugin.RangerPlugin;
import net.ranger.plugin.actions.PreferenceConstants;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;


/**
 * Class used to initialize default preference values.
 * 
 * @author Emerson Loureiro
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/** {@inheritDoc} */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = RangerPlugin.getDefault().getPreferenceStore();
		store.setDefault(PreferenceConstants.MAX_SEARCH_DEPTH_PROPERTY_NAME, 5);
	}
}
