package com.mytrainstation.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Locale;

/**
 * POJO class for train stations.
 *
 * @author JSCHENK
 */
@Entity(tableName = "train_stations")
public class TrainStation {

	@PrimaryKey
	@ColumnInfo(name = "id")
	private int mId;
	@ColumnInfo(name = "name")
	private String mName;
	@ColumnInfo(name = "has_wifi")
	private boolean mHasWifi;
	@ColumnInfo(name = "has_parking")
	private boolean mHasParking;
	@ColumnInfo(name = "has_stepless_access")
	private boolean mHasSteplessAccess;
	@ColumnInfo(name = "picture_url")
	private String mPictureUrl;

	/**
	 * Default constructor that will be used by room library.
	 *
	 * Be aware to use {@link Builder} from code instead of this constructor!
	 */
	public TrainStation() {
	}

	private TrainStation(Builder builder) {
		mId = builder.mId;
		mName = builder.mName;
		mHasWifi = builder.mHasWifi;
		mHasParking = builder.mHasParking;
		mHasSteplessAccess = builder.mHasSteplessAccess;
		mPictureUrl = builder.mPictureUrl;
	}

	/**
	 * Gets the id of the {@link TrainStation}.
	 *
	 * @return The unique id of the {@link TrainStation}.
	 */
	public int getId() {
		return mId;
	}

	/**
	 * Sets the id of the {@link TrainStation}.
	 *
	 * @param id
	 * 		The unique id of the {@link TrainStation}.
	 */
	public void setId(int id) {
		this.mId = id;
	}

	/**
	 * Gets the name of the {@link TrainStation}.
	 *
	 * @return The name of the {@link TrainStation}.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Sets the name of the {@link TrainStation}.
	 *
	 * @param name
	 * 		The name of the {@link TrainStation}.
	 */
	public void setName(String name) {
		this.mName = name;
	}

	/**
	 * Gets whether {@link TrainStation} has wifi or not.
	 *
	 * @return True, if {@link TrainStation} has wifi, otherwise false.
	 */
	public boolean hasWifi() {
		return mHasWifi;
	}

	/**
	 * Sets whether {@link TrainStation} has wifi or not.
	 *
	 * @param hasWifi
	 * 		True, if {@link TrainStation} has wifi, otherwise false.
	 */
	public void setHasWifi(boolean hasWifi) {
		mHasWifi = hasWifi;
	}

	/**
	 * Gets whether {@link TrainStation} has parking or not.
	 *
	 * @return True, if {@link TrainStation} has parking, otherwise false.
	 */
	public boolean hasParking() {
		return mHasParking;
	}

	/**
	 * Sets whether {@link TrainStation} has parking or not.
	 *
	 * @param hasParking
	 * 		True, if {@link TrainStation} has parking, otherwise false.
	 */
	public void setHasParking(boolean hasParking) {
		mHasParking = hasParking;
	}

	/**
	 * Gets whether {@link TrainStation} has stepless access or not.
	 *
	 * @return True, if {@link TrainStation} has stepless access, otherwise false.
	 */
	public boolean hasSteplessAccess() {
		return mHasSteplessAccess;
	}

	/**
	 * Sets whether {@link TrainStation} has stepless access or not.
	 *
	 * @param hasSteplessAccess
	 * 		True, if {@link TrainStation} has stepless access, otherwise false.
	 */
	public void setHasSteplessAccess(boolean hasSteplessAccess) {
		mHasSteplessAccess = hasSteplessAccess;
	}

	/**
	 * Gets the url of train stations picture.
	 *
	 * @return The url of the train stations picture.
	 */
	public String getPictureUrl() {
		return mPictureUrl;
	}

	/**
	 * Sets the url of train stations picture.
	 *
	 * @param pictureUrl
	 * 		The url of the train stations picture.
	 */
	public void setPictureUrl(String pictureUrl) {
		this.mPictureUrl = pictureUrl;
	}

	@Override
	public String toString() {
		return String.format(Locale.getDefault(), "Trainstation [id=%s, name=%s, wifi=%s, " +
						"parking=%s, steplessAccess=%s", mId, mName, mHasWifi, mHasParking,
				mHasSteplessAccess);
	}

	/**
	 * Builder class for {@link TrainStation}
	 */
	public static class Builder {

		private final int mId;
		private final String mName;
		private boolean mHasWifi;
		private boolean mHasParking;
		private boolean mHasSteplessAccess;
		private String mPictureUrl;

		/**
		 * @param id
		 * 		The unique identifier of the train station.
		 * @param name
		 * 		The name of the train station.
		 */
		public Builder(int id, @NonNull String name) {
			mId = id;
			mName = name;
		}

		/**
		 * @param hasWifi
		 * 		True, if train station provides wifi, otherwise false.
		 * @return This instance of {@link Builder}.
		 */
		public Builder hasWifi(boolean hasWifi) {
			mHasWifi = hasWifi;
			return this;
		}

		/**
		 * @param hasParking
		 * 		True, if train station provides parking, otherwise false.
		 * @return This instance of {@link Builder}.
		 */
		public Builder hasParking(boolean hasParking) {
			mHasParking = hasParking;
			return this;
		}

		/**
		 * @param hasSteplessAccess
		 * 		True, if train station has stepless access, otherwise false.
		 * @return This instance of {@link Builder}.
		 */
		public Builder hasSteplessAccess(boolean hasSteplessAccess) {
			mHasSteplessAccess = hasSteplessAccess;
			return this;
		}

		/**
		 * @param pictureUrl
		 * 		The url of the train station picture.
		 * @return This instance of {@link Builder}.
		 */
		public Builder pictureUrl(String pictureUrl) {
			mPictureUrl = pictureUrl;
			return this;
		}

		/**
		 * @return A new instance of {@link TrainStation} with help of {@link Builder} instance.
		 */
		public TrainStation build() {
			return new TrainStation(this);
		}

	}
}
