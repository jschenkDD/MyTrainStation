package com.mytrainstation.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.mytrainstation.model.TrainStation;

/**
 * Abstract database class that will used by room libary to persist any search results of
 * {@link TrainStation}s.
 */
@Database(entities = {TrainStation.class}, version = 1, exportSchema = false)
public abstract class TrainStationDatabase extends RoomDatabase {

	private static TrainStationDatabase INSTANCE;

	/**
	 * @return DAO object to insert and request a list of {@link TrainStation}s within database.
	 */
	public abstract TrainStationDao getTrainStationDao();

	/**
	 * Get singleton instance of {@link TrainStationDatabase} that will be used to persist search
	 * results of {@link TrainStation}s.
	 *
	 * @param context
	 * 		The context that will be used to initialize room database.
	 * @return A singleton instance {@link TrainStationDatabase}. Will be create an instance of
	 * {@link TrainStationDatabase} if not exists yet.
	 */
	public static TrainStationDatabase getInstance(@NonNull Context context) {
		if (INSTANCE == null) {
			synchronized (TrainStationDatabase.class) {
				if (INSTANCE == null) {
					INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
							TrainStationDatabase.class, "train_stations_database").build();
				}
			}
		}
		return INSTANCE;
	}
}
