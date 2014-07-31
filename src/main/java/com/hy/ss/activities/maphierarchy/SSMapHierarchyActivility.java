package com.hy.ss.activities.maphierarchy;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hy.ss.activities.HelperListActivity;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.sdk.pub.data.SSCityManager;
import com.hy.ss.sdk.pub.entity.SSCity;

public class SSMapHierarchyActivility extends HelperListActivity {
	// private static final String TAG =
	// (SSMapHierarchyActivility.class.getName());

	private List<SSCity> allCities;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Map Hierarchy");
	}

	protected void constructList() {
		allCities = SSCityManager.loadCityFromFiles(this);
		intents = new IntentPair[allCities.size()];

		Intent intent = new Intent(this, SSCityMarketListActivity.class);

		for (int i = 0; i < allCities.size(); i++) {
			SSCity city = allCities.get(i);
			intents[i] = new IntentPair(city.getName() + "(" + city.getCityID()
					+ ")", intent);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		SSCity city = allCities.get(position);
		Intent intent = (Intent) map.get(AppKeys.KEY_INTENT);

		intent.putExtra(AppKeys.INTENT_KEY_CITY, city);
		startActivity(intent);
	}
}
