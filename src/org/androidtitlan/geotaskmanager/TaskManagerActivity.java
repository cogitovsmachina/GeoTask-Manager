package org.androidtitlan.geotaskmanager;

import android.app.Activity;
import android.os.Bundle;

public class TaskManagerActivity extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Put your code here
	}

	protected GeoTaskManagerApplication getGeoTaskManagerApplication() {
		GeoTaskManagerApplication gtma = (GeoTaskManagerApplication) getApplication();
		return gtma;
	}
}
