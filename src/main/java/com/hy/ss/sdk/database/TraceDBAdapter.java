//package com.hy.ss.sdk.database;
//
//import java.io.File;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//
//import com.ss.ble.fp.db.AbstractDBAdapter;
//import com.ss.ble.fp.helper.BLEPathManager;
//
//public class TraceDBAdapter extends AbstractDBAdapter {
//	static final String TAG = "TraceDBAdapter";
//
//	public TraceDBAdapter(Context ctx) {
//		super(ctx, new File(BLEPathManager.ROOT_DIR,
//				BLEPathManager.TRACE_DB_FILE));
//	}
//
//	public void checkDatabase() {
//		if (!existTable(TraceDBConstants.BLE_TRACE_TABLE)) {
//			creatTraceTable();
//		}
//	}
//
//	private void creatTraceTable() {
//		String TRACE_CREATE = "create table "
//				+ TraceDBConstants.BLE_TRACE_TABLE
//				+ " (_id integer primary key autoincrement, "
//				+ "x real not null, y real not null, start_time integer not null, end_time integer not null, trace_index integer not null, tag integer not null);";
//		db.execSQL(TRACE_CREATE);
//	}
//
//	public void eraseTraceTable() {
//		eraseTable(TraceDBConstants.BLE_TRACE_TABLE);
//		creatTraceTable();
//	}
//
//	public long insertTracePoint(double x, double y, long start, long end,
//			int index, int tag) {
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(TraceDBConstants.KEY_XCOORD, x);
//		initialValues.put(TraceDBConstants.KEY_YCOORD, y);
//		initialValues.put(TraceDBConstants.KEY_START_TIME, start);
//		initialValues.put(TraceDBConstants.KEY_END_TIME, end);
//		initialValues.put(TraceDBConstants.KEY_INDEX, index);
//		initialValues.put(TraceDBConstants.KEY_TAG, tag);
//
//		return db.insert(TraceDBConstants.BLE_TRACE_TABLE, null, initialValues);
//	}
//
//	public boolean updateTracePoint(double x, double y, int index, int tag,
//			long end) {
//		ContentValues values = new ContentValues();
//		values.put(TraceDBConstants.KEY_END_TIME, end);
//
//		String whereClause = TraceDBConstants.KEY_XCOORD + "=? and "
//				+ TraceDBConstants.KEY_YCOORD + "=? and "
//				+ TraceDBConstants.KEY_INDEX + "=? and "
//				+ TraceDBConstants.KEY_TAG + "=?";
//		String[] whereArgs = new String[] { x + "", y + "", index + "",
//				tag + "" };
//		return db.update(TraceDBConstants.BLE_TRACE_TABLE, values, whereClause,
//				whereArgs) > 0;
//	}
//
//	public Cursor getAllRecords() {
//		Cursor cursor = db.query(true, TraceDBConstants.BLE_TRACE_TABLE, null,
//				null, null, null, null, null, null);
//		if (cursor != null) {
//			cursor.moveToFirst();
//		}
//		return cursor;
//	}
//
//	public Cursor getTrace(int tag) {
//		String selection = TraceDBConstants.KEY_TAG + "=?";
//		String[] selectionArgs = new String[] { tag + "" };
//		Cursor cursor = db.query(true, TraceDBConstants.BLE_TRACE_TABLE, null,
//				selection, selectionArgs, null, null, null, null, null);
//		if (cursor != null) {
//			cursor.moveToFirst();
//		}
//		return cursor;
//	}
//
//	// public Cursor getAllTracePoint(int tag) {
//	// return null;
//	// }
//	//
//	public Cursor getAllTags() {
//		String[] columns = new String[] { TraceDBConstants.KEY_TAG };
//
//		Cursor cursor = db.query(true, TraceDBConstants.BLE_TRACE_TABLE,
//				columns, null, null, null, null, null, null, null);
//
//		if (cursor != null) {
//			cursor.moveToFirst();
//		}
//		return cursor;
//	}
// }
