package com.mytrainstation.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.mytrainstation.model.TrainStation;

import java.util.List;

/**
 * Data access object for {@link TrainStation}s.
 *
 * @author JSCHENK
 */
@Dao
public interface TrainStationDao {

	/**
	 * @param trainStations
	 * 		A list of all available {@link TrainStation} that should be stored within database.
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(List<TrainStation> trainStations);

	/**
	 * Deletes all entries within table 'train_stations'.
	 */
	@Query("DELETE FROM train_stations")
	void deleteAll();

	/**
	 * @return A list of all available {@link TrainStation} that are stored within database.
	 */
	@Query("SELECT * FROM train_stations")
	LiveData<List<TrainStation>> getTrainStations();
}
