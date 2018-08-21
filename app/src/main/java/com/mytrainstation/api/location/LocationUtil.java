package com.mytrainstation.api.location;

import android.location.Location;
import android.support.annotation.Nullable;

/**
 * Util class that provides functionality to handle operations with {@link Location}s, such as
 * checking whether new location is better location than last provided.
 *
 * @author JSCHENK
 */
public class LocationUtil {

	private static final int TWO_MINUTES = 1000 * 60 * 2;

	/**
	 * Determines whether one Location reading is better than the current location fix.
	 *
	 * @param location
	 * 		The new Location that you want to evaluate
	 * @param currentBestLocation
	 * 		The current Location fix, to which you want to compare the new one
	 */
	public static boolean isBetterLocation(
			@Nullable Location location,
			@Nullable Location currentBestLocation) {
		if (location == null) {
			// no location will never be better then anything else
			return false;
		}
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and accuracy
		return isMoreAccurate || isNewer && !isLessAccurate || isNewer &&
				!isSignificantlyLessAccurate && isFromSameProvider;
	}

	/**
	 * Checks whether two providers are the same
	 */
	private static boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

}
