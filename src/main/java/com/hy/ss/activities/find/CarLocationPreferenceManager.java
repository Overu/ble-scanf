package com.hy.ss.activities.find;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.hy.ss.sdk.pub.entity.SSLocalPoint;

public class CarLocationPreferenceManager {

	private Context context;

	public CarLocationPreferenceManager(Context ctx) {
		this.context = ctx;
	}

	// PreferenceManager.getDefaultSharedPreferences(context);

	public void setCarLocation(SSLocalPoint lp) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putFloat("x", (float) lp.getX());
		editor.putFloat("y", (float) lp.getY());
		editor.putInt("floor", lp.getFloor());
		editor.commit();
	}

	public void setCarLocationMarket(String marketID) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putString("marketID", marketID);
		editor.commit();
	}

	public String getCarLocationMarket() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		return pref.getString("marketID", null);
	}

	public void setTime(long timestamp) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.putLong("time", timestamp);
		editor.commit();
	}

	public long getTime() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		long timestamp = pref.getLong("time", 0);
		return timestamp;
	}

	public SSLocalPoint getCarLocation() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		double x = pref.getFloat("x", -1);
		double y = pref.getFloat("y", -1);
		int floor = pref.getInt("floor", -100);

		if (x == -1 || y == -1) {
			return null;
		}
		return new SSLocalPoint(x, y, floor);
	}

	public void clear() {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}

}
