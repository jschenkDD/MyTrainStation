package com.mytrainstation.api.location;

import android.location.Location;
import android.location.LocationManager;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Run tests against {@link LocationUtil}.
 *
 * @author JSCHENK
 */
@RunWith(AndroidJUnit4.class)
public class LocationUtilTest {

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * false} if passed location is {@code null}.
	 */
	@Test
	public void LocationUtil_isBetterLocation_returnFalseIfPassedLocationIsNull() {
		Assert.assertThat(LocationUtil.isBetterLocation(null, null), is(false));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * true} if current best location is {@code null}.
	 */
	@Test
	public void LocationUtil_isBetterLocation_returnTrueIfCurrentBestLocationIsNull() {
		Location currentLocation = mock(Location.class);
		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, null), is(true));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * true} if passed location is significantly newer (see {@link Location#getTime()} then
	 * current best location.
	 */
	@Test
	public void LocationUtil_isBetterLocation_returnTrueIfPassedLocationTimeIsSignificantlyNewer
	() {
		Location currentLocation = mock(Location.class);
		when(currentLocation.getTime()).thenReturn(240000L);
		Location currentBestLocation = mock(Location.class);
		when(currentBestLocation.getTime()).thenReturn(60000L);

		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, currentBestLocation), is
				(true));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * false} if passed location is significantly older (see {@link Location#getTime()} then
	 * current best location.
	 */
	@Test
	public void LocationUtil_isBetterLocation_returnFalseIfPassedLocationTimeIsSignificantlyOlder
	() {
		Location currentLocation = mock(Location.class);
		when(currentLocation.getTime()).thenReturn(40000L);
		Location currentBestLocation = mock(Location.class);
		when(currentBestLocation.getTime()).thenReturn(60000L);

		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, currentBestLocation), is
				(false));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * true} if passed location is more accurate (see {@link Location#getAccuracy()} then current
	 * best location.
	 */
	@Test
	public void LocationUtil_isBetterLocation_returnTrueIfPassedLocationIsMoreAccurate() {
		Location currentLocation = mock(Location.class);
		when(currentLocation.getTime()).thenReturn(240000L);
		when(currentLocation.getAccuracy()).thenReturn(3F);
		Location currentBestLocation = mock(Location.class);
		when(currentBestLocation.getTime()).thenReturn(220000L);
		when(currentBestLocation.getAccuracy()).thenReturn(4F);

		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, currentBestLocation), is
				(true));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * true} if passed location is less accurate (see {@link Location#getAccuracy()} but newer
	 * then current best location.
	 */
	@Test
	public void LocationUtil_isBetterLocation_returnTrueIfPassedLocationTimeIsNewerButLessAccurate
	() {
		Location currentLocation = mock(Location.class);
		when(currentLocation.getTime()).thenReturn(240000L);
		when(currentLocation.getAccuracy()).thenReturn(3F);
		Location currentBestLocation = mock(Location.class);
		when(currentBestLocation.getTime()).thenReturn(220000L);
		when(currentBestLocation.getAccuracy()).thenReturn(3F);

		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, currentBestLocation), is
				(true));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * true} if passed location is newer (see {@link Location#getTime()} and not significantly
	 * less accurate (see {@link Location#getAccuracy()} and from same provider (see
	 * {@link Location#getProvider()} then current best location.
	 */
	@Test
	public void
	LocationUtil_isBetterLocation_returnTrueIfPassedLocationTimeIsNewerAndNotSignificantlyLessAccurateAndFromSameProvider
	() {
		Location currentLocation = mock(Location.class);
		when(currentLocation.getTime()).thenReturn(240000L);
		when(currentLocation.getAccuracy()).thenReturn(4F);
		when(currentLocation.getProvider()).thenReturn(LocationManager.GPS_PROVIDER);
		Location currentBestLocation = mock(Location.class);
		when(currentBestLocation.getTime()).thenReturn(220000L);
		when(currentBestLocation.getAccuracy()).thenReturn(3F);
		when(currentBestLocation.getProvider()).thenReturn(LocationManager.GPS_PROVIDER);

		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, currentBestLocation), is
				(true));
	}

	/**
	 * Tests that {@link LocationUtil#isBetterLocation(Location, Location)} will return {@code
	 * false} if passed location is newer (see {@link Location#getTime()} and not significantly
	 * less accurate (see {@link Location#getAccuracy()} and not from same provider (see
	 * {@link Location#getProvider()} then current best location.
	 */
	@Test
	public void
	LocationUtil_isBetterLocation_returnFalseIfPassedLocationTimeIsNewerAndNotSignificantlyLessAccurateAndNotFromSameProvider
	() {
		Location currentLocation = mock(Location.class);
		when(currentLocation.getTime()).thenReturn(240000L);
		when(currentLocation.getAccuracy()).thenReturn(4F);
		when(currentLocation.getProvider()).thenReturn(LocationManager.GPS_PROVIDER);
		Location currentBestLocation = mock(Location.class);
		when(currentBestLocation.getTime()).thenReturn(220000L);
		when(currentBestLocation.getAccuracy()).thenReturn(3F);
		when(currentBestLocation.getProvider()).thenReturn(LocationManager.PASSIVE_PROVIDER);

		Assert.assertThat(LocationUtil.isBetterLocation(currentLocation, currentBestLocation), is
				(false));
	}

}