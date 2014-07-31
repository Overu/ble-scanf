package com.hy.ss.activities.find;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.BeaconManager.RangingListener;
import com.estimote.sdk.Region;
import com.hy.ss.adapter.SSFloorGalleryAdapter;
import com.hy.ss.sdk.arcgismap.SSMapConverter;
import com.hy.ss.sdk.arcgismap.SSMapView;
import com.hy.ss.sdk.database.SSBeaconDBAdapter;
import com.hy.ss.sdk.pub.algorithm.LTSTriangulation;
import com.hy.ss.sdk.pub.data.SSMapInfoManager;
import com.hy.ss.sdk.pub.data.SSMarketManager;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapInfo;
import com.hy.ss.sdk.pub.entity.SSMarket;
import com.hy.ss.sdk.pub.entity.beacon.SSPublicBeacon;
import com.hy.ss.shopsmartandroid.R;
import com.hy.ss.shopsmartprivate.AppPreferenceManager;

public class SSFindCarActivity extends Activity implements OnClickListener,
		RangingListener {
	private static final String TAG = (SSFindCarActivity.class.getSimpleName());

	private static final String HINT_FOR_MARKING = "������������������������������������������������������";
	private static final String HINT_FOR_DATE = "���������";

	private TextView tvMarket;
	private Button btnMarkLocation;

	private boolean isCarLocationMarked;
	private SSLocalPoint carLocation;
	private String carLocationMarket;
	private long carLocationTime;

	private SSLocalPoint currentLocation;

	SSMarket currentMarket;
	SSMapView mapView;
	Gallery floorGallery;

	List<SSMapInfo> mapInfos;
	SSMapInfo currentMapInfo;
	int currentIndex;

	private int tempfloorSelected;
	private boolean tempIsFloorSelected;
	private boolean tempIsFingerUp;

	private GraphicsLayer hintGraphicsLayer;
	private GraphicsLayer carLocationGraphicsLayer;
	// private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region(
	// "ShopSmart", "74278BDA-B644-4520-8F0C-720EAF059935", 2, null);

	private static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region(
			"ShopSmart", "94171fa9-234c-4cf1-87da-4a9ba674561d", 2, null);

	private static final int REQUEST_ENABLE_BT = 1234;

	private BeaconManager beaconManager;
	private HashMap<Integer, SSPublicBeacon> publicBeaconMap;
	private LTSTriangulation mAlgorithm;
	private List<Beacon> scannedBeacons = new ArrayList<Beacon>();

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
		setContentView(R.layout.activity_find_car);

		CarLocationPreferenceManager manager = new CarLocationPreferenceManager(
				this);
		carLocation = manager.getCarLocation();
		carLocationTime = manager.getTime();
		carLocationMarket = manager.getCarLocationMarket();
		if (carLocation == null || carLocationMarket == null) {
			isCarLocationMarked = false;
		} else {
			isCarLocationMarked = true;
		}

		initLayout();

		initAlgorithm();

		beaconManager = new BeaconManager(this);
		beaconManager.setRangingListener(this);

	}

	private void initAlgorithm() {
		SSBeaconDBAdapter db = new SSBeaconDBAdapter(this,
				currentMarket.getMarketID());
		List<SSPublicBeacon> pList = new ArrayList<SSPublicBeacon>();
		db.open();
		pList = db.getAllPublicBeacons();
		db.close();

		publicBeaconMap = new HashMap<Integer, SSPublicBeacon>();
		for (SSPublicBeacon b : pList) {
			publicBeaconMap.put(b.getMinor(), b);
		}

		mAlgorithm = new LTSTriangulation(publicBeaconMap);
	}

	private void initLayout() {
		mapView = (SSMapView) findViewById(R.id.map);
		mapView.setEsriLogoVisible(true);

		floorGallery = (Gallery) findViewById(R.id.floor_gallery);
		floorGallery.setCallbackDuringFling(false);
		floorGallery.setBackgroundColor(Color.LTGRAY);

		tvMarket = (TextView) findViewById(R.id.tv_location_market);
		btnMarkLocation = (Button) findViewById(R.id.mark_button);
		btnMarkLocation.setOnClickListener(this);

		if (mapInfos.size() == 0) {
			floorGallery.setVisibility(View.GONE);
		} else if (mapInfos.size() == 1) {
			floorGallery.setVisibility(View.GONE);

			currentMapInfo = mapInfos.get(0);
			currentIndex = 0;
			mapView.setFloor(currentMapInfo);
			setTitle(String.format("%s-%s", currentMarket.getName(),
					currentMapInfo.getFloorString()));

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

			setTitle(String.format("%s-%s", currentMarket.getName(),
					currentMapInfo.getFloorString()));

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

		mapView.setFloor(currentMapInfo);

		carLocationGraphicsLayer = new GraphicsLayer();
		mapView.addLayer(carLocationGraphicsLayer);

		hintGraphicsLayer = new GraphicsLayer();
		mapView.addLayer(hintGraphicsLayer);
		hintGraphicsLayer.setRenderer(new SimpleRenderer(
				new SimpleMarkerSymbol(Color.GREEN, 10, STYLE.CROSS)));

		updateHintLabels();

		if (isCarLocationMarked) {
			Point point = SSMapConverter.localToMapPoint(carLocation,
					currentMapInfo);
			PictureMarkerSymbol pms = new PictureMarkerSymbol(getResources()
					.getDrawable(R.drawable.car));
			carLocationGraphicsLayer.addGraphic(new Graphic(point, pms));

		}

	}

	private void changedToFloor(int index) {
		currentIndex = index;
		currentMapInfo = mapInfos.get(index);

		setTitle(String.format("%s-%s", currentMarket.getName(),
				currentMapInfo.getFloorString()));

		mapView.setFloor(currentMapInfo);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mark_button:
			markButtonClicked();
			break;

		default:
			break;
		}
	}

	private void markButtonClicked() {
		Log.i(TAG, "markButtonClicked");

		if (isCarLocationMarked) {
			cancelMarkCarLocation();
		} else {
			markCarLocation();
		}
	}

	private void markCarLocation() {
		if (currentLocation == null) {
			Toast.makeText(getBaseContext(),
					"���������������������������������������",
					Toast.LENGTH_LONG).show();
			return;
		}

		long now = System.currentTimeMillis();

		CarLocationPreferenceManager manager = new CarLocationPreferenceManager(
				this);
		manager.setCarLocation(currentLocation);
		manager.setTime(now);
		manager.setCarLocationMarket(currentMarket.getMarketID());

		Point point = SSMapConverter.localToMapPoint(currentLocation,
				currentMapInfo);
		PictureMarkerSymbol pms = new PictureMarkerSymbol(getResources()
				.getDrawable(R.drawable.car));
		carLocationGraphicsLayer.addGraphic(new Graphic(point, pms));

		carLocation = currentLocation;
		carLocationTime = System.currentTimeMillis();

		isCarLocationMarked = true;
		updateHintLabels();
	}

	private void cancelMarkCarLocation() {
		CarLocationPreferenceManager manager = new CarLocationPreferenceManager(
				this);
		manager.clear();

		carLocationGraphicsLayer.removeAll();

		isCarLocationMarked = false;
		updateHintLabels();
	}

	private void updateHintLabels() {
		if (isCarLocationMarked) {

			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date d1 = new Date(carLocationTime);
			String t1 = format.format(d1);

			String str = currentMarket.getName() + "-"
					+ currentMapInfo.getFloorString() + "\n���������" + t1;
			tvMarket.setText(str);
			btnMarkLocation.setText("������������");
		} else {
			tvMarket.setText(HINT_FOR_MARKING);
			btnMarkLocation.setText("������������");
		}
	}

	// ====================================================

	@Override
	protected void onResume() {
		super.onResume();
		startRanging();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stopRanging();
	}

	private void startRanging() {
		if (!beaconManager.hasBluetooth()) {
			Toast.makeText(this, "Device does not have Bluetooth Low Energy",
					Toast.LENGTH_LONG).show();
			return;
		}

		if (!beaconManager.isBluetoothEnabled()) {
			Intent enableBtIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
		} else {
			connectToService();
		}
	}

	private void stopRanging() {
		try {
			beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS_REGION);
		} catch (RemoteException e) {
			Log.d(TAG, "Error while stopping ranging", e);
		}

		getActionBar().setSubtitle("");
	}

	private void connectToService() {
		getActionBar().setSubtitle("Scanning...");
		beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
			@Override
			public void onServiceReady() {
				try {
					beaconManager.startRanging(ALL_ESTIMOTE_BEACONS_REGION);
				} catch (RemoteException e) {
					Toast.makeText(
							SSFindCarActivity.this,
							"Cannot start ranging, something terrible happened",
							Toast.LENGTH_LONG).show();
					Log.e(TAG, "Cannot start ranging", e);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT) {
			if (resultCode == Activity.RESULT_OK) {
				connectToService();
			} else {
				Toast.makeText(this, "Bluetooth not enabled", Toast.LENGTH_LONG)
						.show();
				getActionBar().setSubtitle("Bluetooth not enabled");
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
		if (beacons.size() == 0) {
			return;
		}
		scannedBeacons.clear();

		Log.i(TAG, "onBeaconsDiscovered");

		for (int i = 0; i < beacons.size(); ++i) {
			Beacon b = beacons.get(i);
			if (b.getRssi() < 0) {
				scannedBeacons.add(b);
			}

			Log.i(TAG, "Minor: " + b.getMinor() + " - " + b.getRssi());
		}

		List<Beacon> toRemove = new ArrayList<Beacon>();
		for (Beacon beacon : scannedBeacons) {
			if (!publicBeaconMap.containsKey(beacon.getMinor())) {
				toRemove.add(beacon);
			}
		}
		scannedBeacons.removeAll(toRemove);

		mAlgorithm.setNearestBeacons(scannedBeacons);

		SSLocalPoint location = mAlgorithm.calculateLocation();
		currentLocation = location;

		if (location != null) {
			Point point = SSMapConverter.localToMapPoint(location,
					currentMapInfo);
			hintGraphicsLayer.removeAll();
			hintGraphicsLayer.addGraphic(new Graphic(point, null));
		}
	}

}
