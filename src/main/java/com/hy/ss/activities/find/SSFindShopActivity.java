package com.hy.ss.activities.find;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.TextView;

import com.esri.android.map.Callout;
import com.esri.core.geometry.Point;
import com.hy.ss.adapter.SSFloorGalleryAdapter;
import com.hy.ss.constants.AppKeys;
import com.hy.ss.sdk.arcgismap.SSMapConverter;
import com.hy.ss.sdk.arcgismap.SSMapView;
import com.hy.ss.sdk.pub.data.SSMapInfoManager;
import com.hy.ss.sdk.pub.data.SSMarketManager;
import com.hy.ss.sdk.pub.entity.SSMapInfo;
import com.hy.ss.sdk.pub.entity.SSMarket;
import com.hy.ss.sdk.pub.entity.SSShopInfo;
import com.hy.ss.shopsmartandroid.R;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSFindShopActivity extends Activity {
	private static final String TAG = (SSFindShopActivity.class.getSimpleName());

	SSMarket currentMarket;
	SSShopInfo currentShopInfo;

	SSMapView mapView;
	Gallery floorGallery;

	List<SSMapInfo> mapInfos;
	SSMapInfo currentMapInfo;
	int currentIndex;

	private int tempfloorSelected;
	private boolean tempIsFloorSelected;
	private boolean tempIsFingerUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppPreferenceManager pref = new AppPreferenceManager(this);
		String cityID = pref.getDefaultCityID();
		String marketID = pref.getDefaultMarketID();
		currentMarket = SSMarketManager.loadMarketFromFilesById(this, cityID,
				marketID);
		mapInfos = SSMapInfoManager.loadMapInfoFromFiles(this,
				currentMarket.getMarketID());

		setTitle(currentMarket.getName());
		setContentView(R.layout.activity_find_shop);

		currentShopInfo = getIntent().getParcelableExtra(
				AppKeys.INTENT_KEY_SHOPINFO);

		initLayout();

		centerToShop();
	}

	private void initLayout() {
		mapView = (SSMapView) findViewById(R.id.map);
		mapView.setEsriLogoVisible(true);

		floorGallery = (Gallery) findViewById(R.id.floor_gallery);
		floorGallery.setCallbackDuringFling(false);
		floorGallery.setBackgroundColor(Color.LTGRAY);

		if (mapInfos.size() == 0) {
			floorGallery.setVisibility(View.GONE);
		} else if (mapInfos.size() == 1) {
			floorGallery.setVisibility(View.GONE);

			currentMapInfo = mapInfos.get(0);
			currentIndex = 0;

			mapView.setFloor(currentMapInfo);

		} else if (mapInfos.size() > 1) {

			currentIndex = 0;
			currentMapInfo = mapInfos.get(0);

			for (int i = 0; i < mapInfos.size(); i++) {
				SSMapInfo info = mapInfos.get(i);
				if (info.getFloorNumber() == currentShopInfo.getLocation()
						.getFloor()) {
					currentIndex = i;
					currentMapInfo = info;
					break;
				}
			}

			SSFloorGalleryAdapter adpater = new SSFloorGalleryAdapter(this);
			adpater.setData(mapInfos);
			floorGallery.setAdapter(adpater);
			floorGallery.setSelection(currentIndex);

			floorGallery
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							tempfloorSelected = position;
							tempIsFloorSelected = true;
							if (tempIsFingerUp && tempIsFloorSelected) {
								changedToFloor(tempfloorSelected);
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> parent) {

						}
					});

			floorGallery.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						tempIsFingerUp = true;
						if (tempIsFingerUp && tempIsFloorSelected) {
							changedToFloor(tempfloorSelected);
						}
					}

					if (event.getAction() == MotionEvent.ACTION_DOWN) {
						tempIsFingerUp = false;
						tempIsFloorSelected = false;
					}
					return false;
				}
			});
			mapView.setFloor(currentMapInfo);
		}

	}

	private void changedToFloor(int index) {
		currentIndex = index;
		currentMapInfo = mapInfos.get(index);

		mapView.setFloor(currentMapInfo);
	}

	private void centerToShop() {

		Point point = SSMapConverter.localToMapPoint(
				currentShopInfo.getLocation(), currentMapInfo);
		mapView.centerAt(point, true);

		View view = LayoutInflater.from(this).inflate(
				R.layout.shopinfo_callout, null);
		final TextView nameView = (TextView) view.findViewById(R.id.shop_name);
		final TextView gidView = (TextView) view.findViewById(R.id.shop_gid);
		nameView.setText(currentShopInfo.getName());
		gidView.setText(currentShopInfo.getGid());

		Callout mapCallout = mapView.getCallout();
		mapCallout.setStyle(R.xml.shopinfo_callout_style);
		mapCallout.setContent(view);
		mapCallout.show(point);
	}
}
