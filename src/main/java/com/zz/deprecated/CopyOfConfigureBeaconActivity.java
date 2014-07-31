package com.zz.deprecated;
//package com.hy.ss.activities.pri;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.View.OnTouchListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.Gallery;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.esri.android.map.GraphicsLayer;
//import com.esri.android.map.event.OnSingleTapListener;
//import com.esri.core.geometry.Point;
//import com.esri.core.map.Graphic;
//import com.esri.core.renderer.SimpleRenderer;
//import com.esri.core.symbol.SimpleMarkerSymbol;
//import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;
//import com.hy.ss.adapter.SSFloorGalleryAdapter;
//import com.hy.ss.constants.AppKeys;
//import com.hy.ss.sdk.arcgismap.SSMapConverter;
//import com.hy.ss.sdk.arcgismap.SSMapView;
//import com.hy.ss.sdk.database.SSBeaconDBAdapter;
//import com.hy.ss.sdk.pub.data.SSMapInfoManager;
//import com.hy.ss.sdk.pub.data.SSMarketManager;
//import com.hy.ss.sdk.pub.entity.SSLocalPoint;
//import com.hy.ss.sdk.pub.entity.SSMapInfo;
//import com.hy.ss.sdk.pub.entity.SSMarket;
//import com.hy.ss.sdk.pub.entity.beacon.BeaconType;
//import com.hy.ss.sdk.pub.entity.beacon.SSActivityBeacon;
//import com.hy.ss.sdk.pub.entity.beacon.SSBeacon;
//import com.hy.ss.sdk.pub.entity.beacon.SSPublicBeacon;
//import com.hy.ss.sdk.pub.entity.beacon.SSTriggerBeacon;
//import com.hy.ss.shopsmartprivate.AppPreferenceManager;
//import com.hy.ss.shopsmartprivate.R;
//import com.zz.deprecated.BeaconListForChoosing;
//
//public class CopyOfConfigureBeaconActivity extends Activity implements
//		OnClickListener, OnItemClickListener {
//	private static final String TAG = (CopyOfConfigureBeaconActivity.class
//			.getSimpleName());
//
//	SSMarket currentMarket;
//
//	SSMapView mapView;
//	GraphicsLayer hintGraphicsLayer;
//
//	Gallery floorGallery;
//
//	List<BeaconType> beaconTypeList = new ArrayList<BeaconType>();
//	ListView beaconTypeListView;
//
//	private Button btnChooseBeaconType;
//	private Button btnChooseBeacon;
//	private Button btnAddCurrentBeacon;
//	private Button btnShowBeaconInfo;
//
//	List<SSMapInfo> mapInfos;
//	SSMapInfo currentMapInfo;
//	int currentIndex;
//
//	private int currentBeaconType;
//	private SSBeacon currentBeacon;
//	private SSLocalPoint currentLocation;
//	private String currentShopGid;
//
//	private int tempfloorSelected;
//	private boolean tempIsFloorSelected;
//	private boolean tempIsFingerUp;
//
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		AppPreferenceManager pref = new AppPreferenceManager(this);
//		String cityID = pref.getDefaultCityID();
//		String marketID = pref.getDefaultMarketID();
//
//		beaconTypeList.add(BeaconType.UNKNOWN);
//		beaconTypeList.add(BeaconType.PUBLIC);
//		beaconTypeList.add(BeaconType.TRIGGER);
//		beaconTypeList.add(BeaconType.ACTIVITY);
//
//		currentMarket = SSMarketManager.loadMarketFromFilesById(this, cityID,
//				marketID);
//
//		mapInfos = SSMapInfoManager.loadMapInfoFromFiles(this,
//				currentMarket.getMarketID());
//
//		setTitle(currentMarket.getName());
//		setContentView(R.layout.activity_configure_beacon);
//
//		initLayout();
//	}
//
//	private void singleTapOnMap(float x, float y) {
//		Point p = mapView.toMapPoint(x, y);
//
//		currentLocation = SSMapConverter
//				.mapPointToLocalPoint(p, currentMapInfo);
//		currentShopGid = mapView.getShopGid(x, y, 5);
//
//		hintGraphicsLayer.removeAll();
//		hintGraphicsLayer.addGraphic(new Graphic(p, null));
//
//		Log.i(TAG, p.getX() + ", " + p.getY());
//	}
//
//	private void initLayout() {
//		mapView = (SSMapView) findViewById(R.id.map);
//		mapView.setEsriLogoVisible(true);
//
//		mapView.setOnSingleTapListener(new OnSingleTapListener() {
//			private static final long serialVersionUID = -8177778115592046790L;
//
//			@Override
//			public void onSingleTap(float x, float y) {
//				if (beaconTypeListView.getVisibility() == View.VISIBLE) {
//					beaconTypeListView.setVisibility(View.GONE);
//				}
//				singleTapOnMap(x, y);
//			}
//		});
//
//		beaconTypeListView = (ListView) findViewById(R.id.list_beacon_type);
//		BeaconTypeAdapter typeAdapter = new BeaconTypeAdapter(this,
//				R.layout.list_item_beacon_type, beaconTypeList);
//		beaconTypeListView.setAdapter(typeAdapter);
//		beaconTypeListView.setOnItemClickListener(this);
//
//		btnChooseBeaconType = (Button) findViewById(R.id.btn_choose_beacon_type);
//		btnChooseBeaconType.setOnClickListener(this);
//
//		btnChooseBeacon = (Button) findViewById(R.id.btn_choose_beacon);
//		btnChooseBeacon.setOnClickListener(this);
//
//		btnAddCurrentBeacon = (Button) findViewById(R.id.btn_add_current_beacon);
//		btnAddCurrentBeacon.setOnClickListener(this);
//
//		btnShowBeaconInfo = (Button) findViewById(R.id.btn_show_beacon_info);
//		btnShowBeaconInfo.setOnClickListener(this);
//
//		floorGallery = (Gallery) findViewById(R.id.floor_gallery);
//		floorGallery.setCallbackDuringFling(false);
//		floorGallery.setBackgroundColor(Color.LTGRAY);
//
//		if (mapInfos.size() == 0) {
//			floorGallery.setVisibility(View.GONE);
//		} else if (mapInfos.size() == 1) {
//			floorGallery.setVisibility(View.GONE);
//
//			currentMapInfo = mapInfos.get(0);
//			currentIndex = 0;
//			setTitle(String.format("%s-%s", currentMarket.getName(),
//					currentMapInfo.getFloorString()));
//
//			// mapView.setFloor(currentMapInfo);
//		} else if (mapInfos.size() > 1) {
//
//			currentIndex = 0;
//			currentMapInfo = mapInfos.get(0);
//
//			for (int i = 0; i < mapInfos.size(); i++) {
//				SSMapInfo info = mapInfos.get(i);
//				if (info.getFloorString().equalsIgnoreCase("F1")) {
//					currentIndex = i;
//					currentMapInfo = info;
//					break;
//				}
//			}
//
//			setTitle(String.format("%s-%s", currentMarket.getName(),
//					currentMapInfo.getFloorString()));
//
//			// mapView.setFloor(currentMapInfo);
//
//			SSFloorGalleryAdapter adpater = new SSFloorGalleryAdapter(this);
//			adpater.setData(mapInfos);
//			floorGallery.setAdapter(adpater);
//			floorGallery.setSelection(currentIndex);
//
//			floorGallery
//					.setOnItemSelectedListener(new OnItemSelectedListener() {
//						@Override
//						public void onItemSelected(AdapterView<?> parent,
//								View view, int position, long id) {
//							tempfloorSelected = position;
//							tempIsFloorSelected = true;
//							if (tempIsFingerUp && tempIsFloorSelected) {
//								changedToFloor(tempfloorSelected);
//							}
//						}
//
//						@Override
//						public void onNothingSelected(AdapterView<?> parent) {
//						}
//					});
//
//			floorGallery.setOnTouchListener(new OnTouchListener() {
//
//				@Override
//				public boolean onTouch(View v, MotionEvent event) {
//					if (event.getAction() == MotionEvent.ACTION_UP) {
//						tempIsFingerUp = true;
//						if (tempIsFingerUp && tempIsFloorSelected) {
//							changedToFloor(tempfloorSelected);
//						}
//					}
//
//					if (event.getAction() == MotionEvent.ACTION_DOWN) {
//						tempIsFingerUp = false;
//						tempIsFloorSelected = false;
//					}
//					return false;
//				}
//			});
//		}
//
//		mapView.setFloor(currentMapInfo);
//
//		hintGraphicsLayer = new GraphicsLayer();
//		mapView.addLayer(hintGraphicsLayer);
//		hintGraphicsLayer.setRenderer(new SimpleRenderer(
//				new SimpleMarkerSymbol(Color.GREEN, 5, STYLE.CIRCLE)));
//
//	}
//
//	private void changedToFloor(int index) {
//		currentIndex = index;
//		currentMapInfo = mapInfos.get(index);
//
//		setTitle(String.format("%s-%s", currentMarket.getName(),
//				currentMapInfo.getFloorString()));
//
//		mapView.setFloor(currentMapInfo);
//	}
//
//	private class BeaconTypeAdapter extends ArrayAdapter<BeaconType> {
//		private int resourceID;
//
//		public BeaconTypeAdapter(Context context, int viewResourceID,
//				List<BeaconType> dataList) {
//			super(context, viewResourceID, dataList);
//			this.resourceID = viewResourceID;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			BeaconType type = getItem(position);
//
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(resourceID, parent,
//						false);
//			}
//
//			TextView tvType = (TextView) convertView
//					.findViewById(R.id.list_item_beacon_type);
//			tvType.setText(type.getDescripton() + "(" + type.getType() + ")");
//			return convertView;
//		}
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		currentBeaconType = beaconTypeList.get(position).getType();
//		beaconTypeListView.setVisibility(View.GONE);
//		btnChooseBeaconType.setText(beaconTypeList.get(position)
//				.getDescripton());
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.btn_choose_beacon_type:
//			chooseBeaconType();
//			break;
//
//		case R.id.btn_choose_beacon:
//			chooseBeacon();
//			break;
//
//		case R.id.btn_add_current_beacon:
//			addCurrentBeacon();
//			break;
//
//		case R.id.btn_show_beacon_info:
//			showBeaconInfo();
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	private void chooseBeaconType() {
//		beaconTypeListView.setVisibility(View.VISIBLE);
//	}
//
//	static int REQUEST_CODE_FOR_BEACON = 1000;
//
//	private void chooseBeacon() {
//		Intent intent = new Intent(this, BeaconListForChoosing.class);
//		startActivityForResult(intent, REQUEST_CODE_FOR_BEACON);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		switch (resultCode) {
//		case RESULT_OK:
//			if (requestCode == REQUEST_CODE_FOR_BEACON) {
//				int major = data.getIntExtra(AppKeys.INTENT_KEY_BEACON_MAJOR,
//						-1000);
//				int minor = data.getIntExtra(AppKeys.INTENT_KEY_BEACON_MINOR,
//						-1000);
//				String tag = data.getStringExtra(AppKeys.INTENT_KEY_BEACON_TAG);
//				currentBeacon = new SSBeacon(major, minor, tag);
//				btnChooseBeacon.setText("(" + major + ", " + minor + ")");
//			}
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	private void addCurrentBeacon() {
//		if (currentBeaconType == BeaconType.UNKNOWN.getType()) {
//			displayToast("Beacon Type cannot be UNKNOWN");
//			return;
//		}
//
//		if (currentBeacon == null) {
//			displayToast("Beacon cannot be null");
//			return;
//		}
//
//		if (currentLocation == null) {
//			displayToast("Beacon Location cannot be null");
//			return;
//		}
//
//		if (currentBeaconType == BeaconType.ACTIVITY.getType()
//				&& currentShopGid == null) {
//			displayToast("ShopGid cannot be null for Activity Beacon");
//			return;
//		}
//
//		SSBeaconDBAdapter db = new SSBeaconDBAdapter(this,
//				currentMarket.getMarketID());
//		db.open();
//
//		int major = currentBeacon.getMajor();
//		int minor = currentBeacon.getMinor();
//
//		boolean success = false;
//		boolean isUpdate = false;
//
//		if (currentBeaconType == BeaconType.PUBLIC.getType()) {
//			SSPublicBeacon oldPB = db.getPublicBeacon(major, minor);
//			SSPublicBeacon pb = new SSPublicBeacon(currentBeacon.getMajor(),
//					currentBeacon.getMinor(), currentBeacon.getTag(),
//					currentLocation, currentShopGid);
//			if (oldPB == null) {
//				isUpdate = false;
//				success = db.insertPublicBeacon(pb);
//			} else {
//				isUpdate = true;
//				success = db.updatePublicBeacon(pb);
//			}
//		}
//
//		if (currentBeaconType == BeaconType.TRIGGER.getType()) {
//			SSTriggerBeacon oldTB = db.getTriggerBeacon(major, minor);
//			SSTriggerBeacon tb = new SSTriggerBeacon(currentBeacon.getMajor(),
//					currentBeacon.getMinor(), currentBeacon.getTag(),
//					currentLocation);
//
//			if (oldTB == null) {
//				isUpdate = false;
//				success = db.insertTriggerBeacon(tb);
//			} else {
//				isUpdate = true;
//				success = db.updateTriggerBeacon(tb);
//			}
//		}
//
//		if (currentBeaconType == BeaconType.ACTIVITY.getType()) {
//			SSActivityBeacon oldAB = db.getActivityBeacion(major, minor);
//			SSActivityBeacon ab = new SSActivityBeacon(
//					currentBeacon.getMajor(), currentBeacon.getMinor(),
//					currentBeacon.getTag(), currentLocation, currentShopGid);
//			if (oldAB == null) {
//				isUpdate = false;
//				success = db.insertActivityBeacon(ab);
//			} else {
//				isUpdate = true;
//				success = db.updateActivityBeacon(ab);
//			}
//		}
//
//		String hintString = (isUpdate ? "Update " : "Insert ")
//				+ (success ? "Success " : "Failed ") + currentBeaconType;
//		displayToast(hintString);
//		db.close();
//	}
//
//	private void showBeaconInfo() {
//		startActivity(new Intent(this, ShowConfiguredBeacons.class));
//	}
//
//	private void displayToast(String msg) {
//		Toast.makeText(getBaseContext(), msg, Toast.LENGTH_SHORT).show();
//	}
// }
