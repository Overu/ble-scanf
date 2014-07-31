package com.hy.ss.activities.maphierarchy;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hy.ss.activities.HelperListActivity;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.sdk.pub.data.SSMarketManager;
import com.hy.ss.sdk.pub.entity.SSCity;
import com.hy.ss.sdk.pub.entity.SSMarket;

public class SSCityMarketListActivity extends HelperListActivity {
	// private static final String TAG =
	// (SSCityMarketListActivity.class.getName());

	SSCity currentCity;
	List<SSMarket> allMarkets;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(currentCity.getName());
	}

	@Override
	protected void constructList() {
		currentCity = getIntent().getParcelableExtra(AppKeys.INTENT_KEY_CITY);

		allMarkets = SSMarketManager.loadMarketFromFiles(this,
				currentCity.getCityID());
		intents = new IntentPair[allMarkets.size()];

		Intent intent = new Intent(this, SSMarketMapActivity.class);

		for (int i = 0; i < allMarkets.size(); i++) {
			SSMarket market = allMarkets.get(i);
			intents[i] = new IntentPair(market.getName() + "("
					+ market.getMarketID() + ")", intent);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		SSMarket market = allMarkets.get(position);
		Intent intent = (Intent) map.get(AppKeys.KEY_INTENT);

		intent.putExtra(AppKeys.INTENT_KEY_MARKET, market);
		startActivity(intent);
	}
}