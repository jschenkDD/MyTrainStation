package com.mytrainstation;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.mytrainstation.api.TrainStationService;
import com.mytrainstation.api.location.LocationProvider;
import com.mytrainstation.data.TrainStationRepository;
import com.mytrainstation.db.TrainStationDatabase;
import com.mytrainstation.db.TrainStationLocalCache;
import com.mytrainstation.ui.ViewModelFactory;

import java.util.concurrent.Executors;

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 *
 * @author JSCHENK
 */
public class Injection {

	private static TrainStationLocalCache provideTrainStationLocalCache(
			@NonNull Application application) {
		return new TrainStationLocalCache(TrainStationDatabase.getInstance(application)
				.getTrainStationDao(), Executors.newSingleThreadExecutor());
	}

	private static TrainStationRepository provideTrainStationRepository(
			@NonNull Application application) {
		return new TrainStationRepository(new TrainStationService(application),
				provideTrainStationLocalCache(application));
	}

	private static LocationProvider provideLocationProvider(@NonNull Application application) {
		return new LocationProvider(application);
	}

	/**
	 * Provides the {@link ViewModelProvider.Factory} that is then used to get a reference to
	 * {@link android.arch.lifecycle.ViewModel} objects.
	 *
	 * @param application
	 * 		The application context that will be used to initialize repository (see
	 * 		{@link TrainStationRepository}), local cache (see {@link TrainStationLocalCache}) and
	 * 		location provider (see {@link LocationProvider}).
	 * @return A {@link android.arch.lifecycle.ViewModelProvider.Factory} that could be used to
	 * initialize {@link android.arch.lifecycle.ViewModel} via
	 * {@link android.arch.lifecycle.ViewModelProvider#get(Class)}.
	 */
	public static ViewModelProvider.Factory provideViewModelFactory(
			@NonNull Application application) {
		return new ViewModelFactory(application, provideTrainStationRepository(application),
				provideLocationProvider(application));
	}

}
