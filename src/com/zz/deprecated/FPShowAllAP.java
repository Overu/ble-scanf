package com.zz.deprecated;
//package com.hy.ss.activities.pri;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.ListActivity;
//import android.content.Context;
//import android.content.Intent;
//import android.database.Cursor;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.ListView;
//import android.widget.TextView;
//
//public class FPShowAllAP extends ListActivity {
//	private static final String TAG = "FPShowAllAP";
//
//	private String mPlaceID;
//	private String mTableName;
//
//	private DBAdapterForComparing db;
//	private DBHelperForComparing dbHelper;
//
//	private List<APData> allAPDatas;
//
//	private List<String> allAPs;
//	private List<String> apF1;
//	private List<String> apF2;
//
//	TextView tv_TitleF1;
//	TextView tv_TitleF2;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//
//		mPlaceID = getIntent().getStringExtra("placeID");
//		mTableName = "WifiDatabase_" + mPlaceID;
//
//		setTitle("All AP at " + mPlaceID);
//		setContentView(R.layout.fp_show_all_ap);
//
//		tv_TitleF1 = (TextView) findViewById(R.id.title_f1);
//		tv_TitleF2 = (TextView) findViewById(R.id.title_f2);
//
//		allAPDatas = new ArrayList<APData>();
//		allAPs = new ArrayList<String>();
//		apF1 = new ArrayList<String>();
//		apF2 = new ArrayList<String>();
//
//		boolean dbExist = checkDatabase();
//		if (dbExist) {
//			getBSSID();
//			getAPData();
//
//			MyAdapter adapter = new MyAdapter(this,
//					R.layout.fp_show_all_ap_row, allAPDatas);
//			setListAdapter(adapter);
//		}
//
//		tv_TitleF1.setText("F1(" + apF1.size() + ")");
//		tv_TitleF2.setText("F2(" + apF2.size() + ")");
//
//		db.close();
//	}
//
//	@Override
//	protected void onListItemClick(ListView l, View v, int position, long id) {
//		APData data = allAPDatas.get(position);
//
//		String bssid = data.bssid;
//
//		Intent intent = new Intent(FPShowAllAP.this, FingerprintAnalysis.class);
//		intent.putExtra("bssid", bssid);
//
//		setResult(RESULT_OK, intent);
//		finish();
//
//	}
//
//	private void getAPData() {
//		for (String bssid : allAPs) {
//			APData data = new APData();
//			data.bssid = bssid;
//
//			if (apF1.contains(bssid)) {
//				data.existF1 = "Yes";
//			} else {
//				data.existF1 = "";
//			}
//
//			if (apF2.contains(bssid)) {
//				data.existF2 = "Yes";
//			} else {
//				data.existF2 = "";
//			}
//
//			allAPDatas.add(data);
//		}
//	}
//
//	private void getBSSID() {
//		{
//			Cursor c = db.getAllBssid(mTableName);
//			if (c.moveToFirst()) {
//				do {
//					String bssid = c.getString(0);
//					allAPs.add(bssid);
//				} while (c.moveToNext());
//			}
//		}
//
//		{
//			Cursor c = db.getAllBssidAtFloor(mTableName, "101");
//			if (c.moveToFirst()) {
//				do {
//					String bssid = c.getString(0);
//					apF1.add(bssid);
//				} while (c.moveToNext());
//			}
//		}
//
//		{
//			Cursor c = db.getAllBssidAtFloor(mTableName, "102");
//			if (c.moveToFirst()) {
//				do {
//					String bssid = c.getString(0);
//					apF2.add(bssid);
//				} while (c.moveToNext());
//			}
//		}
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add(0, 0, 0, "Write to Excel");
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		switch (item.getItemId()) {
//		case 0:
//			try {
//				writeToFile();
//			} catch (RowsExceededException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (WriteException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			break;
//
//		default:
//			break;
//		}
//
//		return super.onOptionsItemSelected(item);
//	}
//
//	private void writeToFile() throws IOException, RowsExceededException,
//			WriteException {
//		String DIR = Environment.getExternalStorageDirectory() + "/Palmall";
//		File ap_file = new File(DIR, "FP_all_AP.excel");
//
//		WritableWorkbook wWorkBook = Workbook.createWorkbook(ap_file);
//		WritableSheet wSheet = wWorkBook.createSheet("All AP", 0);
//
//		Label[] titleLabels = new Label[3];
//		titleLabels[0] = new Label(0, 0, "BSSID");
//		wSheet.addCell(titleLabels[0]);
//		titleLabels[1] = new Label(1, 0, "F1");
//		wSheet.addCell(titleLabels[1]);
//		titleLabels[2] = new Label(2, 0, "F2");
//		wSheet.addCell(titleLabels[2]);
//
//		int row = 1;
//		for (int i = 0; i < allAPDatas.size(); i++) {
//			APData data = allAPDatas.get(i);
//
//			String bssid = data.bssid;
//			wSheet.addCell(new Label(0, row + i, bssid));
//
//			String f1 = data.existF1;
//			wSheet.addCell(new Label(1, row + i, f1));
//
//			String f2 = data.existF2;
//			wSheet.addCell(new Label(2, row + i, f2));
//
//		}
//
//		wWorkBook.write();
//		wWorkBook.close();
//	}
//
//	private boolean checkDatabase() {
//
//		File file = new File(PMWifiConfig.Sampling_DIR, "PMWifiDatabase_"
//				+ mPlaceID + ".db");
//
//		if (file.exists()) {
//			db = new DBAdapterForComparing(this, mPlaceID, true);
//			dbHelper = new DBHelperForComparing(db);
//
//			if (!db.existTable(mTableName))
//				return false;
//
//		} else {
//			return false;
//		}
//
//		return true;
//	}
//
//	public class MyAdapter extends ArrayAdapter<APData> {
//		private int resourceId;
//
//		public MyAdapter(Context context, int textViewResourceID,
//				List<APData> objects) {
//			super(context, textViewResourceID, objects);
//			this.resourceId = textViewResourceID;
//		}
//
//		public View getView(int position, View convertView, ViewGroup parent) {
//			APData data = getItem(position);
//
//			if (convertView == null) {
//				convertView = getLayoutInflater().inflate(resourceId, parent,
//						false);
//			}
//
//			TextView tvBssid = (TextView) convertView.findViewById(R.id.bssid);
//			TextView tvExistF1 = (TextView) convertView
//					.findViewById(R.id.exist_f1);
//			TextView tvExistF2 = (TextView) convertView
//					.findViewById(R.id.exist_f2);
//
//			tvBssid.setText(data.bssid);
//			tvExistF1.setText(data.existF1);
//			tvExistF2.setText(data.existF2);
//
//			return convertView;
//		};
//
//	}
// }
