package com.mytrainstation.db;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.mytrainstation.model.TrainStation;

import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

/**
 * Class that handles the DAO local data source. This ensures that methods are triggered on the
 * correct executor.
 *
 * @author JSCHENK
 */
public class TrainStationLocalCache {

	private static final Logger LOG = Logger.getLogger(TrainStationLocalCache.class);

	private final TrainStationDao mTrainStationDao;
	private final Executor mExecutor;

	/**
	 * @param trainStationDao
	 * 		The DAO local data source.
	 * @param executor
	 * 		The executor to run methods on data source.
	 */
	public TrainStationLocalCache(@NonNull TrainStationDao trainStationDao,
			@NonNull Executor executor) {
		mTrainStationDao = trainStationDao;
		mExecutor = executor;
	}

	/**
	 * Insert passed train stations into database with help of {@link #mExecutor}.
	 *
	 * @param trainStations
	 * 		List of {@link TrainStation} that should be stored within database.
	 */
	public void insert(List<TrainStation> trainStations) {
		mExecutor.execute(() -> {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format(Locale.getDefault(), "Insert %s into local database.",
						TextUtils.join("|", trainStations)));
			}
			mTrainStationDao.deleteAll();
			mTrainStationDao.insert(trainStations);
		});
	}

	/**
	 * @return Snapshot of train stations that were found within last search request.
	 */
	public LiveData<List<TrainStation>> getTrainStations() {
		return mTrainStationDao.getTrainStations();
	}

}
