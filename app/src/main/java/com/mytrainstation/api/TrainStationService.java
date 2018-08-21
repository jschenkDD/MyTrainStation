package com.mytrainstation.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Query;
import com.apollographql.apollo.exception.ApolloException;
import com.mytrainstation.model.TrainStation;
import com.mytrainstation.ui.settings.SettingsFragment;
import com.mytrainstation.util.PreferenceUtils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import queries.NearByQuery;
import queries.SearchQuery;

/**
 * Handles requests to graphql endpoint to search for {@link TrainStation}s near by provided
 * latitude, longitude and radius.
 *
 * @author JSCHENK
 */
public class TrainStationService {

	private static final Logger LOG = Logger.getLogger(TrainStationService.class);

	private static final String BASE_URL = "https://developer.deutschebahn" +
			".com/free1bahnql/graphql";
	private static final int MAX_NUMBER_STATIONS = 10;

	private ApolloClient mAppolloClient;
	private final Context mContext;

	/**
	 * Default constructor that will initialize automatically underlying {@link ApolloClient}.
	 *
	 * @param context
	 * 		The context that will be used to retrieve preferences.
	 */
	public TrainStationService(@NonNull Context context) {
		mContext = context;
		initClient();
	}

	private void initClient() {
		mAppolloClient = ApolloClient.builder()
				.serverUrl(BASE_URL)
				.okHttpClient(new OkHttpClient.Builder().build())
				.build();
	}

	/**
	 * @param latitude
	 * 		Latitude value of current location.
	 * @param longitude
	 * 		Longitude value of current location.
	 * @param radius
	 * 		{@link TrainStation}s should be located within passed radius.
	 * @return A possible list of {@link TrainStation}s that are near by passed location and
	 * within given radius.
	 */
	@WorkerThread
	@NonNull
	public List<TrainStation> searchTrainStations(double latitude, double longitude, int radius) {
		int maxDisplayedItems = PreferenceUtils.getPreferenceValue(mContext, SettingsFragment
				.PREFERENCE_KEY_MAX_DISPLAYED_ITEMS, MAX_NUMBER_STATIONS);
		NearByQuery nearByQuery = NearByQuery.builder()
				.latitude(latitude)
				.longitude(longitude)
				.radius(radius)
				.count(maxDisplayedItems)
				.build();
		return executeQuery(nearByQuery);
	}

	/**
	 * @param searchTerm
	 * 		The search term to be included in train stations name.
	 * @return A possible list of {@link TrainStation}s containing the search term.
	 */
	@WorkerThread
	@NonNull
	public List<TrainStation> searchTrainStations(@NonNull String searchTerm) {
		return executeQuery(SearchQuery.builder().searchTerm(searchTerm).build());
	}

	@NonNull
	private <D extends Operation.Data, T, V extends Operation.Variables> List<TrainStation>
	executeQuery(@NonNull Query<D, T, V> query) {
		List<TrainStation> result = new ArrayList<>();
		try {
			result = TrainStationServiceUtil.parseResponse(mAppolloClient.query(query).execute());
		} catch (ApolloException e) {
			LOG.error(String.format(Locale.getDefault(), "Failure on executing %s", query.name()));
		}
		return result;
	}

}
