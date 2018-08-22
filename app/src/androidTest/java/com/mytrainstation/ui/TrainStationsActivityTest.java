package com.mytrainstation.ui;

import android.Manifest;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;
import android.support.test.runner.AndroidJUnit4;

import com.mytrainstation.R;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;

/**
 * Run tests against {@link TrainStationsActivity}.
 *
 * @author JSCHENK
 */
@RunWith(AndroidJUnit4.class)
public class TrainStationsActivityTest {

	private TrainStationsViewModel mViewModel;
	private TrainStationsActivity mActivity;

	/**
	 * Define {@link ActivityTestRule} to automatically start {@link TrainStationsActivity} under
	 * test.
	 */
	@Rule
	public ActivityTestRule<TrainStationsActivity> mActivityRule = new ActivityTestRule<>(
			TrainStationsActivity.class, false, true);

	@Rule
	public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest
			.permission.ACCESS_FINE_LOCATION);

	/**
	 * Set up test environment. Will fill {@link #mViewModel} and {@link #mActivity} so that they
	 * could be used directly within test methods.
	 */
	@Before
	public void setUp() {
		mActivity = mActivityRule.getActivity();
		mViewModel = mActivity.getViewModel();
		Assert.assertThat(mViewModel, is(not(nullValue())));
	}

	/**
	 * Tests that the text of {@link R.id#tv_search_radius} within {@link TrainStationsActivity}
	 * will be updated after {@link TrainStationsViewModel#setSearchRadius(int)} was called.
	 */
	@Test
	public void TrainStationViewModel_searchRadiusChanged_willShowSearchRadiusWithinHeader() {
		mActivity.runOnUiThread(() -> mViewModel.setSearchRadius(10000));
		String expectedText = mActivity.getString(R.string.search_radius_text, 10);
		InstrumentationRegistry.getInstrumentation().waitForIdleSync();
		onView(withId(R.id.tv_search_radius)).check(matches(withText(expectedText)));
	}

	/**
	 * Tests that the text of {@link R.id#tv_location} within {@link TrainStationsActivity} will be
	 * updated after {@link TrainStationsViewModel#setSearchTerm(String)} was called.
	 */
	@Test
	public void TrainStationViewModel_searchTermChanged_willShowSearchTermWithinHeader() {
		String location = "Dresden";
		mActivity.runOnUiThread(() -> mViewModel.setSearchTerm(location));
		InstrumentationRegistry.getInstrumentation().waitForIdleSync();
		onView(withId(R.id.tv_location)).check(matches(withText(location)));
	}

	/**
	 * Tests that {@link R.id#sb_radius} will be enabled and text of menu item with id
	 * {@link R.id#gps_positioning} will be updated after
	 * {@link TrainStationsViewModel#setGpsPositioningIsActive(boolean)} was called.
	 */
	@Test
	public void
	TrainStationViewModel_isGpsPositioningActiveChanged_willEnableSeekbarAndSetMyLocationText() {
		mActivity.runOnUiThread(() -> mViewModel.setGpsPositioningIsActive(true));
		InstrumentationRegistry.getInstrumentation().waitForIdleSync();
		onView(withId(R.id.sb_radius)).check(matches(isEnabled()));
		openActionBarOverflowOrOptionsMenu(mActivity);
		InstrumentationRegistry.getInstrumentation().waitForIdleSync();
		onView(withText(mActivity.getString(R.string.gps_positioning_menu_item_text, mActivity
				.getString(R.string.active)))).check(matches(withEffectiveVisibility(ViewMatchers
				.Visibility.VISIBLE)));
	}

	/**
	 * Tests that {@link android.view.MenuItem} with id {@link R.id#gps_positioning} will be
	 * enabled after {@link TrainStationsViewModel#setGpsPositioningAvailable(boolean)} was called.
	 */
	@Test
	public void
	TrainStationViewModel_isGpsPositioningAvailableChanged_willEnableMenuItem() {
		mActivity.runOnUiThread(() -> mViewModel.setGpsPositioningAvailable(true));
		openActionBarOverflowOrOptionsMenu(mActivity);
		InstrumentationRegistry.getInstrumentation().waitForIdleSync();
		onView(withText(mActivity.getString(R.string.gps_positioning_menu_item_text, mActivity
				.getString(R.string.inactive)))).check(matches(isEnabled()));
	}

	/**
	 * Tests that TextView with id {@link R.id#tv_train_station_empty_list} will be displayed with
	 * text {@link R.string#train_station_empty_list_text} if no train stations will be provided
	 * via {@link TrainStationsViewModel#getTrainStations()}.
	 */
	@Test
	public void TrainStationViewModel_emptyListItems_willShowHint() {
		String expectedText = mActivity.getString(R.string.train_station_empty_list_text);
		onView(withId(R.id.tv_train_station_empty_list)).check(matches(allOf
				(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE), withText(expectedText)
				)));
	}

}