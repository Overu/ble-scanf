package com.hy.ss.shopsmartprivate;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppPreferenceManager {
	Context context;

	public AppPreferenceManager(Context c) {
		this.context = c;
	}

	public String getDefaultCityID() {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		String string = appPrefs.getString("cityID", null);
		return string;
	}

	public String getDefaultMarketID() {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		String string = appPrefs.getString("marketID", null);
		return string;
	}

	public boolean setDefaultMarketID(String marketID) {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		Editor editor = appPrefs.edit();
		editor.putString("marketID", marketID);
		return editor.commit();
	}

	public boolean setDefaultCityID(String cityID) {
		SharedPreferences appPrefs = context.getSharedPreferences(
				"app_preference", Context.MODE_PRIVATE);
		Editor editor = appPrefs.edit();
		editor.putString("cityID", cityID);
		return editor.commit();
	}
}
