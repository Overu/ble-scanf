//package com.zz.deprecated;
//
//import java.util.List;
//
//import android.app.ListActivity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.hy.ss.activities.pri.ConfigureBeaconActivity;
//import com.hy.ss.constants.AppKeys;
//import com.hy.ss.sdk.database.SSPrimitiveBeaconDBAdapter;
//import com.hy.ss.sdk.pub.data.SSMarketManager;
//import com.hy.ss.sdk.pub.entity.SSMarket;
//import com.hy.ss.sdk.pub.entity.beacon.SSBeacon;
//import com.hy.ss.shopsmartprivate.AppPreferenceManager;
//import com.hy.ss.shopsmartprivate.R;
//
//public class BeaconListForChoosing extends ListActivity {
//	private static final String TAG = (BeaconListForChoosing.class
//			.getSimpleName());
//
//	SSMarket currentMarket;
//
//	List<SSBeacon> allBeaconData;
//	SSBeacon currentBeacon;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		setContentView(R.layout.activity_beacon_list_for_choosing);
//
//		getCurrentMarket();
//		setTitle(currentMarket.getName());
//
//		getAllBeaconData();
//		PrimitiveBeaconAdapter adapter = new PrimitiveBeaconAdapter(this,
//				R.layout.list_item_show_primitive_beacon, allBeaconData);
//		setListAdapter(adapter);
//	}
//
//	private void getAllBeaconData() {
//		SSPrimitiveBeaconDBAdapter db = new SSPrimitiveBeaconDBAdapter(this,
//				currentMarket.getMarketID());
//		db.open();
//		allBeaconData = db.getAllPrimitiveBeacons();
//		db.close();
//	}
//
//	private void getCurrentMarket() {
//		AppPreferenceManager pref = new AppPreferenceManager(this);
//		String cityID = pref.getDefaultCityID();
//		String marketID = pref.getDefaultMarketID();
//
//		currentMarket = SSMarketManager.loadMarketFromFilesById(this, cityID,
//				marketID);
//	}
//
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		currentBeacon = allBeaconData.get(position);
//
//		Intent intent = new Intent(this, ConfigureBeaconActivity.class);
//		intent.putExtra(AppKeys.INTENT_KEY_BEACON_MAJOR,
//				currentBeacon.getMajor());
//		intent.putExtra(AppKeys.INTENT_KEY_BEACON_MINOR,
//				currentBeacon.getMinor());
//		intent.putExtra(AppKeys.INTENT_KEY_BEACON_TAG, currentBeacon.getTag());
//
//		setResult(RESULT_OK, intent);
//		finish();
//	}
//
//	public class PrimitiveBeaconAdapter extends ArrayAdapter<SSBeacon> {
//		private int resourceID;
//		private int index;
//
//		public PrimitiveBeaconAdapter(Context context, int viewResourceID,
//				List<SSBeacon> dataList) {
//			super(context, viewResourceID, dataList);
//			this.resourceID = viewResourceID;
//
//			index = 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			SSBeacon data = getItem(position);
//
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(resourceID, parent,
//						false);
//			}
//
//			TextView tvMajor = (TextView) convertView
//					.findViewById(R.id.field_major);
//			TextView tvMinor = (TextView) convertView
//					.findViewById(R.id.field_minor);
//			TextView tvTag = (TextView) convertView
//					.findViewById(R.id.field_tag);
//
//			tvMajor.setText("" + data.getMajor());
//			tvMinor.setText("" + data.getMinor());
//			tvTag.setText("" + data.getTag());
//
//			return convertView;
//		}
//	}
//
// }
