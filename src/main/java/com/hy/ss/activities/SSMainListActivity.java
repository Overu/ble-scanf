package com.hy.ss.activities;

import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.RangingListener;
import com.estimote.sdk.Region;
import com.hy.ss.activities.maphierarchy.SSMapHierarchyActivility;
import com.hy.ss.activities.pub.SSPublicListActivity;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.shopsmartandroid.R;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSMainListActivity extends HelperListActivity implements
		RangingListener {
	private static final String TAG = (SSMainListActivity.class.getSimpleName());

	// =========================================
	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region(
			"ShopSmart", "94171fa9-234c-4cf1-87da-4a9ba674561d", 2, null);
	private BeaconManager beaconManager;

	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppPreferenceManager manager = new AppPreferenceManager(this);

		if (manager.getDefaultCityID() == null
				|| manager.getDefaultMarketID() == null) {
			manager.setDefaultCityID("0027");
			manager.setDefaultMarketID("00270001");
		}

		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(this);

		// connectToService();
	};

	private void connectToService() {
		getActionBar().setSubtitle("Scanning...");
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(
							SSMainListActivity.this,
							"Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.e(TAG, "Cannot start ranging", e);
				}
			}
		});
	}

	@Override
	public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
		if (beacons.size() == 0) {
			return;
		}

		Log.i(TAG, "onBeaconsDiscovered");

		for (int i = 0; i < beacons.size(); ++i) {
			Beacon b = beacons.get(i);

			Log.i(TAG, "Minor: " + b.getMinor() + " - " + b.getRssi());
		}

	}

	protected void constructList() {
		intents = new IntentPair[] {
				new IntentPair("������������", new Intent(this,
						SSPublicListActivity.class)),
				new IntentPair("������������", new Intent(this,
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
			// startActivity(new Intent(this, SettingActivity.class));
			connectToService();
			break;

		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

}