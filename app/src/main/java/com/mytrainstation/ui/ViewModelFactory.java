package com.mytrainstation.ui;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.mytrainstation.api.location.LocationProvider;
import com.mytrainstation.data.TrainStationRepository;

/**
 * Factory class for {@link ViewModel}s.
 *
 * @author JSCHENK
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

	private Application mApplication;
	private TrainStationRepository mRepository;
	private LocationProvider mLocationProvider;

	/**
	 * @param application
	 * 		The application context.
	 * @param repository
	 * @param locationProvider
	 */
	public ViewModelFactory(
			@NonNull Application application,
			@NonNull TrainStationRepository repository,
			LocationProvider locationProvider) {
		mApplication = application;
		mRepository = repository;
		mLocationProvider = locationProvider;
	}

	@SuppressWarnings("unchecked")
	@NonNull
	@Override
	public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
		if (modelClass.isAssignableFrom(TrainStationsViewModel.class)) {
			return (T) new TrainStationsViewModel(mApplication, mRepository, mLocationProvider);
		}
		throw new IllegalArgumentException("Unknown ViewModel class");
	}
}
