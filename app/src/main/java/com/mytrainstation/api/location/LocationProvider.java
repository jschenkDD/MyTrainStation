package com.mytrainstation.api.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mytrainstation.permissions.PermissionUtils;
import com.mytrainstation.ui.settings.SettingsFragment;
import com.mytrainstation.util.PreferenceUtils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Handles requests to {@link LocationManager} and delegate location updates to registered
 * {@link LocationListener}s if permission of {@link Manifest.permission#ACCESS_FINE_LOCATION} is
 * granted.
 *
 * @author JSCHENK
 */
public class LocationProvider {

	private static final Logger LOG = Logger.getLogger(LocationProvider.class);

	private static final String DEFAULT_LOCATION_PROVIDER = LocationManager.GPS_PROVIDER;
	private static final int MIN_TIME_BETWEEN_LOCATION_UPDATES_MILLIS = 10000;
	private static final int MIN_DISTANCE_BETWEEN_LOCATION_UPDATES_METERS = 200;

	private final Context mContext;
	private final LocationManager mLocationManager;
	private List<LocationListener> mListeners = Collections.synchronizedList(new
			ArrayList<LocationListener>());

	/**
	 * @param context
	 * 		The context where {@link LocationProvider} will be used. Need context to
	 * 		retrieve {@link LocationManager} to determine locations.
	 */
	public LocationProvider(@NonNull Context context) {
		mContext = context;
		mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	/**
	 * @param locationListener
	 * 		The listener to retrieve callbacks if new location was determined.
	 * @param locationProviders
	 * 		Specify this if you want to use specific location provider,
	 * 		otherwise {@link #DEFAULT_LOCATION_PROVIDER} will be used. Could be omit.
	 */
	@SuppressLint("MissingPermission")
	public void startListeningForLocationUpdates(@NonNull LocationListener locationListener,
			String... locationProviders) {
		if (mListeners.contains(locationListener)) {
			LOG.warn("Listener already added. Return without request location updates.");
			return;
		}
		if (!PermissionUtils.isPermissionAlreadyGranted(mContext, Manifest.permission
				.ACCESS_FINE_LOCATION)) {
			if (LOG.isInfoEnabled()) {
				LOG.info(String.format(Locale.getDefault(), "Permission %s not granted. Can't " +
						"request location updates.", Manifest.permission.ACCESS_FINE_LOCATION));
			}
			return;
		}

		int minTime = PreferenceUtils.getPreferenceValue(mContext, SettingsFragment
						.PREFERENCE_KEY_MIN_TIME_BETWEEN_LOCATION_UPDATES,
				MIN_TIME_BETWEEN_LOCATION_UPDATES_MILLIS);
		int minDistance = PreferenceUtils.getPreferenceValue(mContext, SettingsFragment
						.PREFERENCE_KEY_MIN_DISTANCE_BETWEEN_LOCATION_UPDATES,
				MIN_DISTANCE_BETWEEN_LOCATION_UPDATES_METERS);

		if (locationProviders != null && locationProviders.length > 0) {
			for (String locationProvider : locationProviders) {
				mLocationManager.requestLocationUpdates(locationProvider, minTime, minDistance,
						locationListener);
			}
		} else {
			mLocationManager.requestLocationUpdates(DEFAULT_LOCATION_PROVIDER, minTime,
					minDistance, locationListener);
		}
	}

	/**
	 * @return The last known location if available for {@link #DEFAULT_LOCATION_PROVIDER},
	 * otherwise {@code null}.
	 */
	@Nullable
	public Location getLastKnownLocation() {
		return getLastKnownLocation(DEFAULT_LOCATION_PROVIDER);
	}

	/**
	 * @param locationProvider
	 * 		The location provider that will be used to determine last known location.
	 * @return The last known location if available for the passed location provider, otherwise
	 * {@code null}.
	 */
	@SuppressLint("MissingPermission")
	@Nullable
	private Location getLastKnownLocation(@NonNull String locationProvider) {
		if (!PermissionUtils.isPermissionAlreadyGranted(mContext, Manifest.permission
				.ACCESS_FINE_LOCATION)) {
			if (LOG.isInfoEnabled()) {
				LOG.info(String.format(Locale.getDefault(), "Permission %s not granted. Can't " +
						"request location updates.", Manifest.permission.ACCESS_FINE_LOCATION));
			}
			return null;
		}
		return mLocationManager.getLastKnownLocation(locationProvider);
	}

	/**
	 * Removes the passed listener from {@link LocationManager} to stop getting location updates.
	 *
	 * @param locationListener
	 * 		The {@link LocationListener} to remove.
	 */
	public void stopListeningForLocationUpdates(@NonNull LocationListener locationListener) {
		mLocationManager.removeUpdates(locationListener);
		mListeners.remove(locationListener);
	}

}
