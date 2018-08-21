package com.mytrainstation.api;

import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.Response;
import com.mytrainstation.model.TrainStation;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import queries.NearByQuery;
import queries.SearchQuery;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.mockito.Mockito.mock;

/**
 * Run tests against {@link TrainStationServiceUtil}.
 *
 * @author JSCHENK
 */
public class TrainStationServiceUtilTest {

	/**
	 * Tests that {@link TrainStationServiceUtil#parseResponse(Response)} will parse all available
	 * {@link TrainStation}s from {@link Response} with {@link NearByQuery.Data}.
	 */
	@Test
	public void
	TrainStationServiceUtil_parseResponse_willParseNearByQueryResponseAndReturnListOfTrainStations
	() {
		Operation operation = mock(Operation.class);
		NearByQuery.Station station1 = new NearByQuery.Station("Station", 1, "Dresden - " +
				"Hauptbahnhof", true, true, "yes", new NearByQuery.Picture("Picture",
				"http://123.de/456.jpg"));
		NearByQuery.Station station2 = new NearByQuery.Station("Station", 2,
				"Dresden - Strehlen", false, true, "no", new NearByQuery.Picture("Picture",
				"http://123.de/789.jpg"));
		ArrayList<NearByQuery.Station> stations = new ArrayList<>();
		stations.add(station1);
		stations.add(station2);
		NearByQuery.Nearby nearBy = new NearByQuery.Nearby("Nearby", stations);
		NearByQuery.Data data = new NearByQuery.Data(nearBy);
		Response<Object> response = Response.builder(operation)
				.data(data)
				.build();

		List<TrainStation> trainStations = TrainStationServiceUtil.parseResponse(response);
		Assert.assertThat(trainStations, is(not(nullValue())));
		Assert.assertThat(trainStations.size(), is(2));

		assertTrainStationProperties(trainStations.get(0), 1, "Dresden - Hauptbahnhof", true,
				true, true, "http://123.de/456.jpg");
		assertTrainStationProperties(trainStations.get(1), 2, "Dresden - Strehlen", false, true,
				false, "http://123.de/789.jpg");
	}

	/**
	 * Tests that {@link TrainStationServiceUtil#parseResponse(Response)} will parse all available
	 * {@link TrainStation}s from {@link Response} with {@link SearchQuery.Data}.
	 */
	@Test
	public void
	TrainStationServiceUtil_parseResponse_willParseSearchQueryResponseAndReturnListOfTrainStations
	() {
		Operation operation = mock(Operation.class);
		SearchQuery.Station station1 = new SearchQuery.Station("Station", 1, "Dresden - " +
				"Hauptbahnhof", true, true, "yes", new SearchQuery.Picture("Picture",
				"http://123.de/456.jpg"));
		SearchQuery.Station station2 = new SearchQuery.Station("Station", 2,
				"Dresden - Strehlen", false, true, "no", new SearchQuery.Picture("Picture",
				"http://123.de/789.jpg"));
		ArrayList<SearchQuery.Station> stations = new ArrayList<>();
		stations.add(station1);
		stations.add(station2);
		SearchQuery.Search search = new SearchQuery.Search("Search", stations);
		SearchQuery.Data data = new SearchQuery.Data(search);
		Response<Object> response = Response.builder(operation)
				.data(data)
				.build();

		List<TrainStation> trainStations = TrainStationServiceUtil.parseResponse(response);
		Assert.assertThat(trainStations, is(not(nullValue())));
		Assert.assertThat(trainStations.size(), is(2));

		assertTrainStationProperties(trainStations.get(0), 1, "Dresden - Hauptbahnhof", true,
				true, true, "http://123.de/456.jpg");
		assertTrainStationProperties(trainStations.get(1), 2, "Dresden - Strehlen", false, true,
				false, "http://123.de/789.jpg");
	}

	private void assertTrainStationProperties(TrainStation trainStation, Integer id, String name,
			boolean hasWifi, boolean hasParking, boolean hasSteplessAccess, String pictureUrl) {
		Assert.assertThat(trainStation.getId(), is(id));
		Assert.assertThat(trainStation.getName(), is(name));
		Assert.assertThat(trainStation.hasWifi(), is(hasWifi));
		Assert.assertThat(trainStation.hasParking(), is(hasParking));
		Assert.assertThat(trainStation.hasSteplessAccess(), is(hasSteplessAccess));
		Assert.assertThat(trainStation.getPictureUrl(), is(pictureUrl));
	}
}