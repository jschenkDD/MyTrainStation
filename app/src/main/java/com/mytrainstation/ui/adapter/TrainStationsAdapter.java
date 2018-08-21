package com.mytrainstation.ui.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mytrainstation.R;
import com.mytrainstation.model.TrainStation;

import java.util.List;

/**
 * Custom adapter that extends {@link ArrayAdapter} to display information about available
 * {@link TrainStation}s that are near by current location and within given search radius.
 *
 * @author JSCHENK
 */
public class TrainStationsAdapter extends ArrayAdapter<TrainStation> {

	private static final String HTML_MIME_TYPE = "text/html";

	/**
	 * @param context
	 * 		The context that will be used to inflate list item views.
	 * @param trainStations
	 * 		A list of available train stations. Each train station will be displayed as list view
	 * 		entry.
	 */
	public TrainStationsAdapter(@NonNull Context context,
			@NonNull List<TrainStation> trainStations) {
		super(context, 0, trainStations);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, @NonNull ViewGroup parent) {
		TrainStation trainStation = getItem(position);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout
					.train_station_list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.mTvName = convertView.findViewById(R.id.tv_train_station_name);
			viewHolder.mWvPicture = convertView.findViewById(R.id.wv_train_station_picture);
			viewHolder.mIvWifi = convertView.findViewById(R.id.iv_train_station_has_wifi);
			viewHolder.mIvParking = convertView.findViewById(R.id
					.iv_train_station_has_parking);
			viewHolder.mIvSteplessAccess = convertView.findViewById(R.id
					.iv_train_station_has_stepless_access);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (trainStation != null) {
			viewHolder.mTvName.setText(trainStation.getName());
			viewHolder.mIvWifi.setVisibility(trainStation.hasWifi() ? View.VISIBLE : View
					.INVISIBLE);
			viewHolder.mIvParking.setVisibility(trainStation.hasParking() ? View.VISIBLE : View
					.INVISIBLE);
			//DrawableCompat.setTin
			viewHolder.mIvSteplessAccess.setVisibility(trainStation.hasSteplessAccess() ? View
					.VISIBLE : View.INVISIBLE);
			String pictureUrl = trainStation.getPictureUrl();
			boolean validUrl = URLUtil.isValidUrl(pictureUrl);
			viewHolder.mWvPicture.setVisibility(validUrl ? View.VISIBLE : View.INVISIBLE);
			if (validUrl) {
				// TODO: pictures should be downloaded via AsyncTask and stored within database
				String html = "<html><body><img src=\"" + pictureUrl + "\" " +
						"width=\"100%\"\"/></body></html>";
				viewHolder.mWvPicture.loadData(html, HTML_MIME_TYPE, null);
			}
		}

		return convertView;
	}

	/**
	 * Updates the list of available train stations.
	 *
	 * @param trainStations
	 * 		The new list of available train stations.
	 */
	public void submitList(@NonNull List<TrainStation> trainStations) {
		clear();
		addAll(trainStations);
		notifyDataSetChanged();
	}

	private class ViewHolder {
		private WebView mWvPicture;
		private TextView mTvName;
		private ImageView mIvParking;
		private ImageView mIvWifi;
		private ImageView mIvSteplessAccess;
	}
}
