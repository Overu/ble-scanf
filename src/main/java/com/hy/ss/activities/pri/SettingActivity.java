package com.hy.ss.activities.pri;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.ss.adapter.SSCityListAdapter;
import com.hy.ss.adapter.SSMarketListAdapter;
import com.hy.ss.sdk.pub.data.SSCityManager;
import com.hy.ss.sdk.pub.data.SSMarketManager;
import com.hy.ss.sdk.pub.entity.SSCity;
import com.hy.ss.sdk.pub.entity.SSMarket;
import com.hy.ss.shopsmartandroid.R;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SettingActivity extends Activity implements OnClickListener {
	private static final String TAG = (SettingActivity.class.getSimpleName());

	ListView cityListView;
	ListView marketListView;

	TextView tvCurrentCity;
	TextView tvCurrentMarket;

	Button btnSetting;

	List<SSCity> cityData;
	List<SSMarket> marketData;

	SSCity currentCity;
	SSMarket currentMarket;

	SSCityListAdapter cityAdapter;
	SSMarketListAdapter marketAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		setTitle(getResources().getString(R.string.label_setting));

		tvCurrentCity = (TextView) findViewById(R.id.tv_currentCity);
		tvCurrentCity.setTextColor(Color.GREEN);
		tvCurrentMarket = (TextView) findViewById(R.id.tv_currentMarket);
		tvCurrentMarket.setTextColor(Color.GREEN);

		btnSetting = (Button) findViewById(R.id.btn_setting);

		btnSetting.setOnClickListener(this);

		cityListView = (ListView) findViewById(R.id.cityList);
		cityListView.setDividerHeight(20);
		cityData = SSCityManager.loadCityFromFiles(this);
		cityAdapter = new SSCityListAdapter(this);
		cityAdapter.setData(cityData);
		cityListView.setAdapter(cityAdapter);

		cityListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentCity = cityData.get(position);

				Log.i(TAG, currentCity.getName());
				tvCurrentCity.setText(currentCity.getName());

				marketData = SSMarketManager.loadMarketFromFiles(
						SettingActivity.this, currentCity.getCityID());
				marketAdapter.setData(marketData);
				marketAdapter.notifyDataSetChanged();

				currentMarket = null;
				tvCurrentMarket.setText("");
			}
		});

		if (cityData.size() == 0) {
			return;
		}

		currentCity = cityData.get(0);

		marketListView = (ListView) findViewById(R.id.marketList);
		marketListView.setDividerHeight(20);

		marketData = SSMarketManager.loadMarketFromFiles(this,
				currentCity.getCityID());
		marketAdapter = new SSMarketListAdapter(this);
		marketAdapter.setData(marketData);
		marketListView.setAdapter(marketAdapter);

		marketListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				currentMarket = marketData.get(position);
				Log.i(TAG, currentMarket.getName());
				tvCurrentMarket.setText(currentMarket.getName());
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (currentCity == null) {
			Toast.makeText(getBaseContext(), "City not selected",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (currentMarket == null) {
			Toast.makeText(getBaseContext(), "Market not selected",
					Toast.LENGTH_SHORT).show();
			return;
		}

		AppPreferenceManager pref = new AppPreferenceManager(this);
		pref.setDefaultCityID(currentCity.getCityID());
		pref.setDefaultMarketID(currentMarket.getMarketID());

		Toast.makeText(
				getBaseContext(),
				String.format("Set Default: %s-%s", currentCity.getName(),
						currentMarket.getName()), Toast.LENGTH_SHORT).show();
		finish();
	}
}
