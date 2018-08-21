package com.mytrainstation.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.apache.log4j.Logger;

/**
 * Util class to help reading preferences and cast them to given types.
 *
 * @author JSCHENK
 */
public class PreferenceUtils {

	private static final Logger LOG = Logger.getLogger(PreferenceUtils.class);

	/**
	 * Tries to parse value, that is linked to {@link android.preference.Preference} with passed
	 * key, to {@link Integer} or returns default value if preference doesn't exist, value isn't
	 * set or value couldn't parsed to {@link Integer}.
	 *
	 * @param context
	 * 		The context that will be used to retrieve {@link SharedPreferences}.
	 * @param preferenceKey
	 * 		The key of the preference where value should be returned if available.
	 * @param defaultValue
	 * 		The value that should be returned if preference value with given key doesn't exist or
	 * 		isn't set.
	 * @return The integer value of the preference with the given key, or the default value, if
	 * preference doesn't exist, value isn't set or value couldn't parse to {@link Integer}.
	 */
	@NonNull
	public static Integer getPreferenceValue(
			@NonNull Context context,
			@NonNull String preferenceKey,
			Integer defaultValue) {
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
		String preferenceValue = preferences.getString(preferenceKey, null);
		if (preferenceValue == null) {
			return defaultValue;
		}
		Integer value = null;
		try {
			value = Integer.parseInt(preferenceValue);
		} catch (NumberFormatException ex) {
			LOG.error(ex);
		}
		return value != null ? value : defaultValue;
	}
}
