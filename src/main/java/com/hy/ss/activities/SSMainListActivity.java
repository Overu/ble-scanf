package com.hy.ss.activities;

import java.util.Map;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.hy.ss.activities.maphierarchy.SSMapHierarchyActivility;
import com.hy.ss.activities.pri.SettingActivity;
import com.hy.ss.activities.pub.SSPublicListActivity;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.shopsmartandroid.R;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSMainListActivity extends HelperListActivity {
	private static final String TAG = (SSMainListActivity.class.getSimpleName());

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppPreferenceManager manager = new AppPreferenceManager(this);

		if (manager.getDefaultCityID() == null
				|| manager.getDefaultMarketID() == null) {
			manager.setDefaultCityID("0027");
			manager.setDefaultMarketID("00270001");
		}
	};

	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair("公共功能", new Intent(this,
						SSPublicListActivity.class)),
				new IntentPair("地图层级", new Intent(this,
						SSMapHierarchyActivility.class))

		};
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) l
				.getItemAtPosition(position);

		Intent intent = (Intent) map.get(AppKeys.KEY_INTENT);
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, getResources().getString(R.string.menu_setting));

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			startActivity(new Intent(this, SettingActivity.class));
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}