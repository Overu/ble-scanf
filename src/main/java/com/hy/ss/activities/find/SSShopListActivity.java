package com.hy.ss.activities.find;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.hy.ss.activities.HelperListActivity;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.sdk.database.SSShopInfoDBAdapter;
import com.hy.ss.sdk.pub.entity.SSShopInfo;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSShopListActivity extends HelperListActivity {
	private static final String TAG = (SSShopListActivity.class.getSimpleName());

	private List<SSShopInfo> allShops;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	protected void constructList() {
		String marketID = new AppPreferenceManager(this).getDefaultMarketID();
		SSShopInfoDBAdapter db = new SSShopInfoDBAdapter(this, marketID);
		db.open();
		allShops = db.getAllShopInfosWithName();

		List<SSShopInfo> infoList1 = db.getAllShopInfos();
		Log.i(TAG, infoList1.size() + " shops");
		Log.i(TAG, allShops.size() + " shops with name");

		db.close();

		intents = new IntentPair[allShops.size()];
		Intent intent = new Intent(this, SSFindShopActivity.class);
		for (int i = 0; i < allShops.size(); i++) {
			SSShopInfo info = allShops.get(i);
			intents[i] = new IntentPair(info.getName(), intent);
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		SSShopInfo info = allShops.get(position);
		Intent intent = (Intent) map.get(AppKeys.KEY_INTENT);

		intent.putExtra(AppKeys.INTENT_KEY_SHOPINFO, info);
		startActivity(intent);
	}
}
