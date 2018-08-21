package com.mytrainstation.ui.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.mytrainstation.R;

/**
 * Fragment that extends {@link PreferenceFragment} to display app settings from
 * {@link R.xml#preferences} that user could be changed.
 *
 * @author JSCHENK
 */
public class SettingsFragment extends PreferenceFragment {

	/**
	 * Preference identifier for maximum number of items that should be displayed within ui.
	 */
	public static final String PREFERENCE_KEY_MAX_DISPLAYED_ITEMS =
			"pref_key_max_train_stations_displayed";

	/**
	 * Preference identifier for minimum distance between location updates (in meter).
	 */
	public static final String PREFERENCE_KEY_MIN_DISTANCE_BETWEEN_LOCATION_UPDATES =
			"pref_key_min_distance_between_location_updates_meter";

	/**
	 * Preference identifier for minimum time between location updates (in milliseconds).
	 */
	public static final String PREFERENCE_KEY_MIN_TIME_BETWEEN_LOCATION_UPDATES =
			"pref_key_min_time_between_location_updates_millis";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.preferences);
	}

}
