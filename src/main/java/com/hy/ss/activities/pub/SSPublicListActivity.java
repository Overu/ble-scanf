package com.hy.ss.activities.pub;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hy.ss.activities.HelperListActivity;
import com.hy.ss.activities.find.SSFindCarActivity;
import com.hy.ss.activities.find.SSFindFacilityActivity;
import com.hy.ss.activities.find.SSShopListActivity;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.sdk.pub.data.SSMarketManager;
import com.hy.ss.sdk.pub.entity.SSMarket;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSPublicListActivity extends HelperListActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle("Public");
	}

	@Override
	protected void onResume() {
		super.onResume();

		String cityID = new AppPreferenceManager(this).getDefaultCityID();
		String marketID = new AppPreferenceManager(this).getDefaultMarketID();
		SSMarket market = SSMarketManager.loadMarketFromFilesById(this, cityID,
				marketID);
		setTitle(market.getName());
	}

	@Override
	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair("找商铺",
						new Intent(this, SSShopListActivity.class)),
				new IntentPair("找设施", new Intent(this,
						SSFindFacilityActivity.class)),
				new IntentPair("找车位", new Intent(this, SSFindCarActivity.class)) };
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get(AppKeys.KEY_INTENT);
		startActivity(intent);
	}

}
