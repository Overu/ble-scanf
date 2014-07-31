package com.hy.ss.activities.find;

import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.symbol.TextSymbol.HorizontalAlignment;
import com.esri.core.symbol.TextSymbol.VerticalAlignment;
import com.hy.ss.adapter.SSFloorGalleryAdapter;
import com.hy.ss.sdk.arcgismap.SSMapConverter;
import com.hy.ss.sdk.arcgismap.SSMapView;
import com.hy.ss.sdk.database.SSShopInfoDBAdapter;
import com.hy.ss.sdk.pub.data.SSMapInfoManager;
import com.hy.ss.sdk.pub.data.SSMarketManager;
import com.hy.ss.sdk.pub.entity.SSFacilityInfo;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapInfo;
import com.hy.ss.sdk.pub.entity.SSMarket;
import com.hy.ss.shopsmartandroid.R;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSFindFacilityActivity extends Activity {
	private static final String TAG = (SSFindFacilityActivity.class
			.getSimpleName());

	SSMarket currentMarket;

	SSMapView mapView;
	Gallery floorGallery;

	List<SSMapInfo> mapInfos;
	SSMapInfo currentMapInfo;
	int currentIndex;

	private GraphicsLayer facilityGraphicsLayer;

	private int tempfloorSelected;
	private boolean tempIsFloorSelected;
	private boolean tempIsFingerUp;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AppPreferenceManager pref = new AppPreferenceManager(this);
		String cityID = pref.getDefaultCityID();
		String marketID = pref.getDefaultMarketID();
		currentMarket = SSMarketManager.loadMarketFromFilesById(this, cityID,
				marketID);
		mapInfos = SSMapInfoManager.loadMapInfoFromFiles(this,
				currentMarket.getMarketID());

		setTitle(currentMarket.getName());
		setContentView(R.layout.activity_find_facility);

		initLayout();

		if (mapInfos.size() > 0) {
			SSMapInfo info = mapInfos.get(0);
			mapView.setFloor(info);
		}

		facilityGraphicsLayer = new GraphicsLayer();
		mapView.addLayer(facilityGraphicsLayer);
		facilityGraphicsLayer.setRenderer(new SimpleRenderer(
				new SimpleMarkerSymbol(Color.BLUE, 10, STYLE.DIAMOND)));

		getFacilityOnCurrentMap();

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
				if (info.getFloorString().equalsIgnoreCase("F1")) {
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

		}

	}

	private void changedToFloor(int index) {
		currentIndex = index;
		currentMapInfo = mapInfos.get(index);

		mapView.setFloor(currentMapInfo);
	}

	private void getFacilityOnCurrentMap() {
		SSShopInfoDBAdapter db = new SSShopInfoDBAdapter(this,
				currentMarket.getMarketID());
		db.open();
		List<SSFacilityInfo> facilityInfos = db
				.getAllFacilityInfosInFloor(currentMapInfo.getFloorNumber());
		db.close();

		Log.i(TAG, facilityInfos.toString());

		facilityGraphicsLayer.removeAll();
		for (SSFacilityInfo info : facilityInfos) {
			SSLocalPoint lp = info.getLocation();
			Point p = SSMapConverter.localToMapPoint(lp, currentMapInfo);

			Log.i(TAG, info.getName());
			TextSymbol ts = new TextSymbol(10, info.getName(), Color.GREEN);
			ts.setHorizontalAlignment(HorizontalAlignment.CENTER);
			ts.setVerticalAlignment(VerticalAlignment.MIDDLE);

			facilityGraphicsLayer.addGraphic(new Graphic(p, ts));

			facilityGraphicsLayer.addGraphic(new Graphic(p, null));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Test");

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		return super.onOptionsItemSelected(item);
	}

}
