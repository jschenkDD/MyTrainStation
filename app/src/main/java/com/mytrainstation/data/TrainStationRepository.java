package com.mytrainstation.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.mytrainstation.api.TrainStationService;
import com.mytrainstation.db.TrainStationLocalCache;
import com.mytrainstation.model.TrainStation;

import org.apache.log4j.Logger;

import java.util.List;

/**
 * Repository class that works with local and remote data sources.
 *
 * @author JSCHENK
 */
public class TrainStationRepository {

	private static final Logger LOG = Logger.getLogger(TrainStationRepository.class);

	private final TrainStationService mService;
	private final TrainStationLocalCache mCache;

	private MutableLiveData<Boolean> mIsRequestInProgress;

	/**
	 * @param service
	 * 		The service that could be used to request train stations near by current location and
	 * 		within given search radius.
	 * @param localCache
	 * 		The local cache where to insert search results.
	 */
	public TrainStationRepository(
			@NonNull TrainStationService service,
			@NonNull TrainStationLocalCache localCache) {
		mService = service;
		mCache = localCache;
		mIsRequestInProgress = new MutableLiveData<>();
	}

	/**
	 * @return True, if any request is sending via {@link TrainStationService} to webservice
	 * endpoint, otherwise false.
	 */
	public MutableLiveData<Boolean> isRequestInProgress() {
		return mIsRequestInProgress;
	}

	/**
	 * @param latitude
	 * 		Latitude value of current location.
	 * @param longitude
	 * 		Longitude value of current location.
	 * @param radius
	 * 		{@link TrainStation}s should be located within passed radius.
	 */
	@WorkerThread
	public void searchTrainStationsByLocation(double latitude, double longitude,
			int radius) {
		requestAndSaveData(latitude, longitude, radius);
	}

	/**
	 * @param searchTerm
	 * 		The search term to be included in train stations name.
	 */
	@WorkerThread
	public void searchTrainStationsBySearchTerm(@NonNull String searchTerm) {
		requestAndSaveData(searchTerm);
	}

	/**
	 * @return A list of all available {@link TrainStation}s that are currently in cache.
	 */
	public LiveData<List<TrainStation>> getTrainStations() {
		return mCache.getTrainStations();
	}

	private synchronized void requestAndSaveData(double latitude, double longitude, int radius) {
		if (prepareRequest()) {
			finishRequest(mService.searchTrainStations(latitude, longitude, radius));
		}
	}

	private synchronized void requestAndSaveData(@NonNull String searchTerm) {
		if (prepareRequest()) {
			finishRequest(mService.searchTrainStations(searchTerm));
		}
	}

	private void finishRequest(List<TrainStation> trainStations) {
		mCache.insert(trainStations);
		mIsRequestInProgress.postValue(false);
	}

	private synchronized boolean prepareRequest() {
		Boolean requestInProgress = mIsRequestInProgress.getValue();
		if (requestInProgress != null && requestInProgress) {
			LOG.warn("Request already in progress. Return without do anything.");
			return false;
		}
		mIsRequestInProgress.postValue(true);
		return true;
	}
}
