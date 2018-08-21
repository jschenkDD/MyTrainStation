package com.mytrainstation.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Activity that will add {@link SettingsFragment} to display app settings that user could be
 * changed.
 *
 * @author JSCHENK
 */
public class SettingsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Display the fragment as the main content.
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new SettingsFragment())
				.commit();
	}

}
