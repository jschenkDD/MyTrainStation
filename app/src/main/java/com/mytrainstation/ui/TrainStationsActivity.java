package com.mytrainstation.ui;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.mytrainstation.Injection;
import com.mytrainstation.R;
import com.mytrainstation.model.TrainStation;
import com.mytrainstation.permissions.PermissionRequester;
import com.mytrainstation.permissions.PermissionUtils;
import com.mytrainstation.ui.adapter.TrainStationsAdapter;
import com.mytrainstation.ui.settings.SettingsActivity;

import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Main activity to show available train stations near by current location and within given
 * search radius.
 *
 * @author JSCHENK
 */
public class TrainStationsActivity extends AppCompatActivity implements SeekBar
		.OnSeekBarChangeListener, PermissionRequester {

	private static final Logger LOG = Logger.getLogger(TrainStationsActivity.class);

	private TrainStationsViewModel mViewModel;

	private ListView mLvTrainStations;
	private TextView mTvSearchRadius;
	private TextView mTvEmptyList;
	private TextView mTvLocation;
	private SeekBar mSbSearchRadius;
	private TrainStationsAdapter mTrainStationAdapter;
	private MenuItem mGpsPositioningMenuItem;
	private ProgressBar mPbLoadingContent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.train_stations_activity);

		// set actionbar icon
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayShowHomeEnabled(true);
			actionBar.setIcon(R.mipmap.ic_launcher);
		}

		mLvTrainStations = findViewById(R.id.lv_train_stations);
		mLvTrainStations.addHeaderView(getLayoutInflater().inflate(R.layout
				.train_station_listview_header, mLvTrainStations, false));
		mSbSearchRadius = mLvTrainStations.findViewById(R.id.sb_radius);
		mSbSearchRadius.setOnSeekBarChangeListener(this);
		mSbSearchRadius.setEnabled(false);
		mTvSearchRadius = mLvTrainStations.findViewById(R.id.tv_search_radius);
		mTvEmptyList = findViewById(R.id.tv_train_station_empty_list);
		mTvLocation = mLvTrainStations.findViewById(R.id.tv_location);
		mPbLoadingContent = findViewById(R.id.pb_loading_content);

		// init view model
		mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(getApplication
				())).get(TrainStationsViewModel.class);
		mViewModel.getTrainStations().observe(this, this::submitTrainStations);
		mViewModel.getSearchRadius().observe(this, searchRadius -> mTvSearchRadius.setText
				(getString(R.string.search_radius_text, searchRadius != null ? searchRadius / 1000
						: 0)));
		mViewModel.getSearchTerm().observe(this, mTvLocation::setText);
		mViewModel.isGpsPositioningActive().observe(this, gpsPositioningActive -> {
			if (gpsPositioningActive != null) {
				if (gpsPositioningActive) {
					mTvLocation.setText(getString(R.string.gps_location_display_text));
				}
				mSbSearchRadius.setEnabled(gpsPositioningActive);
				mGpsPositioningMenuItem.setChecked(gpsPositioningActive);
				if (mGpsPositioningMenuItem != null) {
					mGpsPositioningMenuItem.setTitle(getString(R.string
							.gps_positioning_menu_item_text, gpsPositioningActive ? getString(R
							.string.active) : getString(R.string.inactive)));
				}
			}
		});
		mViewModel.isGpsPositioningAvailable().observe(this, gpsPositingAvailable -> {
			if (gpsPositingAvailable != null) {
				mGpsPositioningMenuItem.setEnabled(gpsPositingAvailable);
			}
		});
		mViewModel.isRequestInProgress().observe(this, requestInProgress -> {
			if (requestInProgress != null) {
				mPbLoadingContent.setVisibility(requestInProgress ? View.VISIBLE : View.GONE);
				mLvTrainStations.setVisibility(requestInProgress ? View.GONE : View.VISIBLE);
				mTvEmptyList.setVisibility(View.GONE);
			}
		});
		// load preferences
		loadPreferences();
		// request permission that is needed for gps positioning
		PermissionUtils.requestPermissionWithActivity(this, Manifest.permission
				.ACCESS_FINE_LOCATION, PermissionUtils.REQUEST_CODE_ACCESS_FINE_LOCATION);
	}

	private void submitTrainStations(@Nullable List<TrainStation> trainStations) {
		// if list is null we initialize an empty list while adapter could handle it instead of
		// null
		if (trainStations == null) {
			trainStations = new ArrayList<>();
		}
		if (mTrainStationAdapter == null) {
			mTrainStationAdapter = new TrainStationsAdapter(this, trainStations);
			mLvTrainStations.setAdapter(mTrainStationAdapter);
		} else {
			mTrainStationAdapter.submitList(trainStations);
		}
		mTvEmptyList.setVisibility(trainStations.size() == 0 ? View.VISIBLE : View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.train_station_activity_bar, menu);
		// add listener to action view of search menu item
		MenuItem item = menu.findItem(R.id.action_search);
		SearchView actionViewSearch = (SearchView) item.getActionView();
		actionViewSearch.setQueryHint(getString(R.string.train_station_action_bar_search_hint));
		actionViewSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				mViewModel.setSearchTerm(query);
				mViewModel.searchTrainStationsBySearchTerm();
				return true;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return true;
			}
		});
		mGpsPositioningMenuItem = menu.findItem(R.id.gps_positioning);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.gps_positioning:
				mViewModel.setGpsPositioningIsActive(!item.isChecked());
				return true;
			case R.id.app_settings:
				startActivity(new Intent(this, SettingsActivity.class));
				return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (isFinishing()) {
			savePreferences();
		}
	}

	private void savePreferences() {
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(getString(R.string.saved_search_radius), mSbSearchRadius.getProgress());
		editor.putString(getString(R.string.last_location), String.valueOf(mTvLocation.getText()));
		editor.apply();
	}

	private void loadPreferences() {
		SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
		int lastSearchRadius = preferences.getInt(getString(R.string.saved_search_radius), 0);
		// apply search radius to ui and view model
		mSbSearchRadius.setProgress(lastSearchRadius, false);
		mTvLocation.setText(preferences.getString(getString(R.string.last_location), ""));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
			@NonNull int[] grantResults) {
		PermissionUtils.forwardingToPermissionRequesterAfterPermissionDialog(this, requestCode,
				permissions, grantResults);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
		mViewModel.setSearchRadius(seekBar.getProgress() * 1000);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		mViewModel.searchTrainStationsByLocation();
	}

	@Override
	public void performPositiveUserDecision(int requestCode) {
		if (LOG.isInfoEnabled()) {
			LOG.info(String.format(Locale.getDefault(), "Permission %s granted.", Manifest
					.permission.ACCESS_FINE_LOCATION));
		}
		// by default we will use gps positioning, so we have to request location updates
		mViewModel.startListeningForLocationUpdates();
	}

	@Override
	public void performNegativeUserDecision(int requestCode) {
		LOG.warn(String.format(Locale.getDefault(), "Permission %s not granted.", Manifest
				.permission.ACCESS_FINE_LOCATION));
		// TODO: show snackbar to delegate user to app settings
	}

	/**
	 * @return The underlying {@link TrainStationsViewModel} that is used by
	 * {@link TrainStationsActivity}.
	 */
	@VisibleForTesting
	public TrainStationsViewModel getViewModel() {
		return mViewModel;
	}
}
