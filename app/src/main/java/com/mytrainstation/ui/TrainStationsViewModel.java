package com.mytrainstation.ui;

import android.Manifest;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;

import com.mytrainstation.api.TrainStationService;
import com.mytrainstation.api.location.LocationProvider;
import com.mytrainstation.api.location.LocationUtil;
import com.mytrainstation.data.TrainStationRepository;
import com.mytrainstation.model.TrainStation;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * View model to separate business logic from ui. Will provide all information that might be
 * displayed within ui.
 *
 * @author JSCHENK
 */
public class TrainStationsViewModel extends AndroidViewModel implements LocationListener {

	private static final Logger LOG = Logger.getLogger(TrainStationsViewModel.class);

	private final TrainStationRepository mTrainStationRepository;
	private LocationProvider mLocationProvider;
	private MutableLiveData<Integer> mSearchRadius;
	private MutableLiveData<Boolean> mGpsPositioningActive;
	private MutableLiveData<Boolean> mGpsPositioningAvailable;
	private MutableLiveData<String> mSearchTerm;
	private Location mCurrentLocation;
	private ExecutorService mExecutorService;

	/**
	 * @param application
	 * 		The application context.
	 * @param repository
	 * 		Repository class that works with local and remote data sources.
	 * @param locationProvider
	 * 		Handles requests to {@link LocationManager} and delegate location updates to registered
	 * 		{@link LocationListener}s if permission of
	 * 		{@link Manifest.permission#ACCESS_FINE_LOCATION} is granted.
	 */
	TrainStationsViewModel(
			@NonNull Application application,
			@NonNull TrainStationRepository repository,
			@NonNull LocationProvider locationProvider) {
		super(application);
		mExecutorService = Executors.newSingleThreadExecutor();
		mTrainStationRepository = repository;
		mLocationProvider = locationProvider;
		mSearchRadius = new MutableLiveData<>();
		mSearchTerm = new MutableLiveData<>();
		mGpsPositioningActive = new MutableLiveData<>();
		mGpsPositioningAvailable = new MutableLiveData<>();
	}

	/**
	 * @return True, if any request is sending via {@link TrainStationService} to webservice
	 * endpoint, otherwise false.
	 */
	public MutableLiveData<Boolean> isRequestInProgress() {
		return mTrainStationRepository.isRequestInProgress();
	}

	/**
	 * @return The current search radius that could be observed.
	 */
	public MutableLiveData<Integer> getSearchRadius() {
		return mSearchRadius;
	}

	/**
	 * @return True, if gps positioning is active, otherwise false.
	 */
	public MutableLiveData<Boolean> isGpsPositioningActive() {
		return mGpsPositioningActive;
	}

	/**
	 * @param isGpsPositioningActive
	 * 		True, if gps positioning was activated by user, otherwise false.
	 */
	@UiThread
	public void setGpsPositioningIsActive(boolean isGpsPositioningActive) {
		mGpsPositioningActive.setValue(isGpsPositioningActive);
		if (isGpsPositioningActive) {
			startListeningForLocationUpdates();
		} else {
			stopListeningForLocationUpdates();
		}
	}

	/**
	 * @return True, if gps positioning is available, otherwise false.
	 */
	public MutableLiveData<Boolean> isGpsPositioningAvailable() {
		return mGpsPositioningAvailable;
	}

	/**
	 * This method could be used within tests to manually set if gps positioning is available or
	 * not.
	 *
	 * @param gpsPositioningAvailable
	 * 		True, if gps positioning is available, otherwise false.
	 */
	@VisibleForTesting
	@UiThread
	public void setGpsPositioningAvailable(boolean gpsPositioningAvailable) {
		mGpsPositioningAvailable.setValue(gpsPositioningAvailable);
	}

	/**
	 * @return The current location term that is either provided by gps location data or by last
	 * search term.
	 */
	public MutableLiveData<String> getSearchTerm() {
		return mSearchTerm;
	}

	/**
	 * Refreshes the radius that is used to determine train stations near by current location and
	 * within given radius.
	 *
	 * @param searchRadius
	 * 		The radius that is set by user to determine train stations. Unit should be kilometre.
	 */
	@UiThread
	public void setSearchRadius(int searchRadius) {
		mSearchRadius.setValue(searchRadius);
	}

	/**
	 * @param searchTerm
	 * 		The search term to be included in train stations name.
	 */
	public void setSearchTerm(@NonNull String searchTerm) {
		mSearchTerm.setValue(searchTerm);
	}

	/**
	 * @return A list of all available {@link TrainStation}s that are near by current location and
	 * within given search radius.
	 */
	LiveData<List<TrainStation>> getTrainStations() {
		return mTrainStationRepository.getTrainStations();
	}

	@Override
	public void onLocationChanged(Location location) {
		checkProvidedLocation(location);
	}

	@Override
	public void onStatusChanged(String s, int i, Bundle bundle) {

	}

	@Override
	public void onProviderEnabled(String s) {
		if (LOG.isInfoEnabled()) {
			LOG.info(String.format(Locale.getDefault(), "Provider %s enabled.", s));
		}
	}

	private void checkProvidedLocation(@Nullable Location location) {
		if (LocationUtil.isBetterLocation(location, mCurrentLocation)) {
			if (LOG.isInfoEnabled()) {
				LOG.info(String.format(Locale.getDefault(), "New location provided with " +
						"latitude=%s and longitude=%s", location.getLatitude(), location
						.getLongitude()));
			}
			mCurrentLocation = location;
			mGpsPositioningAvailable.postValue(true);
			// we automatically enable gps positioning if available
			mGpsPositioningActive.postValue(true);
			searchTrainStationsByLocation();
		}
	}

	/**
	 * Start searching for train stations that are near by last location that was provided by
	 * {@link LocationProvider} and are within given search radius that was set with
	 * {@link #setSearchRadius(int)}.
	 */
	public void searchTrainStationsByLocation() {
		Integer searchRadius = mSearchRadius.getValue();
		if (searchRadius != null && mCurrentLocation != null) {
			if (LOG.isInfoEnabled()) {
				LOG.info(String.format(Locale.getDefault(), "Search for train stations by " +
						"location (latitude=%s, longitude=%s, search radius=%s", mCurrentLocation
						.getLatitude(), mCurrentLocation.getLongitude(), searchRadius));
			}
			mExecutorService.execute(() -> mTrainStationRepository.searchTrainStationsByLocation
					(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(),
							searchRadius));
		}
	}

	/**
	 * Start searching for train stations that includes search term that was set with
	 * {@link #setSearchTerm(String)}.
	 */
	@UiThread
	public void searchTrainStationsBySearchTerm() {
		String locationTerm = mSearchTerm.getValue();
		if (locationTerm != null) {
			if (LOG.isInfoEnabled()) {
				LOG.info(String.format(Locale.getDefault(), "Search for train stations by search" +
						" term=%s", locationTerm));
			}
			// stop searching for location updates since it would override search results by search
			// term.
			setGpsPositioningIsActive(false);
			mExecutorService.execute(() -> mTrainStationRepository.searchTrainStationsBySearchTerm
					(locationTerm));
		}
	}

	@Override
	public void onProviderDisabled(String s) {
		if (LOG.isInfoEnabled()) {
			LOG.info(String.format(Locale.getDefault(), "Provider %s disabled.", s));
		}
	}

	/**
	 * Call this to start receiving location updates. Internal calls
	 * {@link LocationProvider#startListeningForLocationUpdates(LocationListener, String...)}. If
	 * gps position is available (see {@link #onLocationChanged(Location)} then
	 * {@link #isGpsPositioningActive()} will return {@code true}.
	 */
	public void startListeningForLocationUpdates() {
		mLocationProvider.startListeningForLocationUpdates(this);
	}

	/**
	 * Call this to stop receiving location updates. After this {@link #isGpsPositioningActive()}
	 * will return {@code false}.
	 */
	private void stopListeningForLocationUpdates() {
		mLocationProvider.stopListeningForLocationUpdates(this);
		mGpsPositioningActive.setValue(false);
	}
}
