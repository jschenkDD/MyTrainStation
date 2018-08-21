package com.mytrainstation.api;

import android.support.annotation.NonNull;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Error;
import com.apollographql.apollo.api.Response;
import com.mytrainstation.model.TrainStation;
import com.mytrainstation.util.BooleanUtils;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import queries.NearByQuery;
import queries.SearchQuery;

/**
 * Service util class to parse {@link Response}s from
 * {@link com.apollographql.apollo.ApolloClient} while executing graphql queries.
 *
 * @author JSCHENK
 */
public class TrainStationServiceUtil {

	private static final Logger LOG = Logger.getLogger(TrainStationServiceUtil.class);

	/**
	 * @param response
	 * 		The response that provided by {@link ApolloCall#execute()}
	 * @param <T>
	 * 		The type of the response data.
	 * @return A possible list of {@link TrainStation}s that could be initialized with help of
	 * response data.
	 */
	@NonNull
	public static <T> List<TrainStation> parseResponse(@NonNull Response<T> response) {
		List<TrainStation> trainStations = new ArrayList<>();
		if (response.hasErrors() && response.errors().size() > 0) {
			LOG.info("Response contain errors.");
			for (Error error : response.errors()) {
				LOG.info(error.message());
			}
		}
		T data = response.data();
		if (data != null) {
			if (data instanceof NearByQuery.Data) {
				NearByQuery.Nearby nearby = ((NearByQuery.Data) data).nearby();
				for (NearByQuery.Station station : nearby.stations()) {
					Integer evaId = station.primaryEvaId();
					if (evaId != null) {
						Boolean hasSteplessAccess = BooleanUtils.tryParseYesNoString(station
								.hasSteplessAccess());
						NearByQuery.Picture picture = station.picture();
						trainStations.add(
								new TrainStation.Builder(evaId, station.name())
										.hasParking(station.hasParking())
										.hasWifi(station.hasWiFi())
										.hasSteplessAccess(hasSteplessAccess != null ?
												hasSteplessAccess : false)
										.pictureUrl(picture != null ? picture.url() : null)
										.build());
					}
				}
			} else if (data instanceof SearchQuery.Data) {
				SearchQuery.Search search = ((SearchQuery.Data) data).search();
				for (SearchQuery.Station station : search.stations()) {
					Integer evaId = station.primaryEvaId();
					if (evaId != null) {
						Boolean hasSteplessAccess = BooleanUtils.tryParseYesNoString(station
								.hasSteplessAccess());
						SearchQuery.Picture picture = station.picture();
						trainStations.add(
								new TrainStation.Builder(evaId, station.name())
										.hasParking(station.hasParking())
										.hasWifi(station.hasWiFi())
										.hasSteplessAccess(hasSteplessAccess != null ?
												hasSteplessAccess : false)
										.pictureUrl(picture != null ? picture.url() : null)
										.build());
					}
				}
			}
		}
		return trainStations;
	}

}
