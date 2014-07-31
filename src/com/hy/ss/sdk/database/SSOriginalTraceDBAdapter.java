package com.hy.ss.sdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hy.ss.sdk.pub.data.SSFileManager;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapPoint;
import com.hy.ss.sdk.pub.entity.beacon.SSBeacon;
import com.hy.ss.sdk.pub.trace.SSTrace;
import com.hy.ss.sdk.pub.trace.SSTracePoint;

public class SSOriginalTraceDBAdapter {
	private static final String TAG = (SSOriginalTraceDBAdapter.class
			.getSimpleName());

	private static final String TABLE_ORIGINAL_TRACE = "Original_Trace";

	private static final String FIELD_ROWID = "_id";

	private static final String FIELD_X_COORD = "x";
	private static final String FIELD_Y_COORD = "y";
	private static final String FIELD_FLOOR = "floor";

	private static final String FIELD_MAP_X_COORD = "mapx";
	private static final String FIELD_MAP_Y_COORD = "mapy";

	private static final String FIELD_TIMESTAMP = "timestamp";
	private static final String FIELD_TAG = "tag";
	private static final String FIELD_MARKET_ID = "marketID";
	private static final String FIELD_TRACE_INDEX = "trace_index";

	private static final String FIELD_NEAREST_MAJOR = "nearest_major";
	private static final String FIELD_NEAREST_MINOR = "nearest_minor";

	private static final String DATA_CREATE = "create table %s ( %s integer primary key autoincrement, %s real not null, %s real not null, %s integer not null, %s real not null, %s real not null, %s real not null, %s integer not null, %s integer not null, %s text not null, %s integer not null, %s integer not null)";
	private static final String SQL_DATA_CREATE = String.format(DATA_CREATE,
			TABLE_ORIGINAL_TRACE, FIELD_ROWID, FIELD_X_COORD, FIELD_Y_COORD,
			FIELD_FLOOR, FIELD_MAP_X_COORD, FIELD_MAP_Y_COORD, FIELD_TIMESTAMP,
			FIELD_TRACE_INDEX, FIELD_TAG, FIELD_MARKET_ID, FIELD_NEAREST_MAJOR,
			FIELD_NEAREST_MINOR);
	static final int DATABASE_VERSION = 1;

	final Context context;
	SQLiteDatabase db;
	File dbFile;

	public SSOriginalTraceDBAdapter(Context ctx) {
		this.context = ctx;
		dbFile = new File(SSFileManager.getOriginalTraceDBPath());
		db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);

		checkDatabase();
	}

	private boolean existTable(String table) {
		boolean result = false;
		if (table == null) {
			return false;
		}

		Cursor cursor = null;
		try {
			String sql = "select count(*) as c from sqlite_master where type = 'table' and name = '"
					+ table.trim() + "'";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return result;
	}

	public void open() {
		if (!db.isOpen()) {
			db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
		}
	}

	public void close() {
		if (db.isOpen()) {
			db.close();
		}
	}

	private void checkDatabase() {
		if (!existTable(TABLE_ORIGINAL_TRACE)) {
			createOriginalTraceTable();
		}
	}

	private void createOriginalTraceTable() {
		db.execSQL(SQL_DATA_CREATE);
	}

	public boolean deleteTrace(SSTrace trace) {
		return deleteTrace(trace.getTag(), trace.getMarketID());
	}

	public boolean deleteTrace(long tag) {
		String whereClause = String.format(" %s = ? ", FIELD_TAG);
		String[] whereArgs = new String[] { tag + "" };
		return db.delete(TABLE_ORIGINAL_TRACE, whereClause, whereArgs) > 0;
	}

	public boolean deleteTrace(String marketID) {
		String whereClause = String.format(" %s = ? ", FIELD_MARKET_ID);
		String[] whereArgs = new String[] { marketID };
		return db.delete(TABLE_ORIGINAL_TRACE, whereClause, whereArgs) > 0;
	}

	public boolean deleteTrace(long tag, String marketID) {
		String whereClause = String.format(" %s = ? and %s = ? ", FIELD_TAG,
				FIELD_MARKET_ID);
		String[] whereArgs = new String[] { tag + "", marketID };
		return db.delete(TABLE_ORIGINAL_TRACE, whereClause, whereArgs) > 0;
	}

	public boolean eraseTraceTable() {
		return db.delete(TABLE_ORIGINAL_TRACE, null, null) > 0;
	}

	public void insertTrace(SSTrace trace) {
		for (int i = 0; i < trace.getCount(); ++i) {
			SSTracePoint p = trace.getTracePoint(i);
			insertTracePoint(p, i, trace.getTag(), trace.getMarketID());
		}
	}

	public boolean insertTracePoint(SSTracePoint tp, int index, long tag,
			String marketID) {
		return insertTracePoint(tp.getLocation().getX(), tp.getLocation()
				.getY(), tp.getLocation().getFloor(), tp.getMapPoint().getX(),
				tp.getMapPoint().getY(), tp.getTimestamp(), index, tag,
				marketID, tp.getNearestBeacon());
	}

	private boolean insertTracePoint(double x, double y, int floor, double mx,
			double my, double timestamp, int index, long tag, String marketID,
			SSBeacon nb) {
		ContentValues values = new ContentValues();

		values.put(FIELD_X_COORD, x);
		values.put(FIELD_Y_COORD, y);
		values.put(FIELD_FLOOR, floor);
		values.put(FIELD_MAP_X_COORD, mx);
		values.put(FIELD_MAP_Y_COORD, my);
		values.put(FIELD_TIMESTAMP, timestamp);
		values.put(FIELD_TRACE_INDEX, index);
		values.put(FIELD_TAG, tag);
		values.put(FIELD_MARKET_ID, marketID);
		values.put(FIELD_NEAREST_MAJOR, nb.getMajor());
		values.put(FIELD_MAP_Y_COORD, nb.getMinor());

		return db.insert(TABLE_ORIGINAL_TRACE, null, values) > 0;
	}

	public List<SSTrace> getAllTraces() {
		Map<Long, SSTrace> traceDict = new HashMap<Long, SSTrace>();
		List<SSTrace> allTraces = new ArrayList<SSTrace>();

		String[] columns = new String[] { FIELD_X_COORD, FIELD_Y_COORD,
				FIELD_FLOOR, FIELD_MAP_X_COORD, FIELD_MAP_Y_COORD,
				FIELD_TIMESTAMP, FIELD_TAG, FIELD_MARKET_ID,
				FIELD_NEAREST_MAJOR, FIELD_NEAREST_MINOR };
		String orderBy = String.format(" Order by %s asc, %s asc",
				FIELD_MARKET_ID, FIELD_TIMESTAMP);

		Cursor cursor = db.query(true, TABLE_ORIGINAL_TRACE, columns, null,
				null, null, null, orderBy, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				double x = cursor.getDouble(0);
				double y = cursor.getDouble(1);
				int floor = cursor.getInt(2);

				double mx = cursor.getDouble(3);
				double my = cursor.getDouble(4);

				double timestamp = cursor.getDouble(5);
				long tag = cursor.getLong(6);
				String marketID = cursor.getString(7);

				int major = cursor.getInt(8);
				int minor = cursor.getInt(9);

				if (!traceDict.containsKey(tag)) {
					SSTrace trace = SSTrace.newTrace(marketID, tag);
					traceDict.put(tag, trace);
				}

				SSTrace trace = traceDict.get(tag);
				trace.addTracePoint(SSTracePoint.newTracePoint(
						new SSLocalPoint(x, y, floor), new SSMapPoint(mx, my),
						new SSBeacon(major, minor, null), timestamp));

			} while (cursor.moveToNext());
		}

		allTraces.addAll(traceDict.values());

		return allTraces;

	}

	public List<SSTrace> getAllTraces(String marketID) {
		Map<Long, SSTrace> traceDict = new HashMap<Long, SSTrace>();
		List<SSTrace> allTraces = new ArrayList<SSTrace>();

		String[] columns = new String[] { FIELD_X_COORD, FIELD_Y_COORD,
				FIELD_FLOOR, FIELD_MAP_X_COORD, FIELD_MAP_Y_COORD,
				FIELD_TIMESTAMP, FIELD_TAG, FIELD_NEAREST_MAJOR,
				FIELD_NEAREST_MINOR };
		String selection = String.format(" %s = ? ", FIELD_MARKET_ID);
		String[] selectionArgs = new String[] { marketID };
		String orderBy = String.format(" Order by %s asc", FIELD_TIMESTAMP);

		Cursor cursor = db.query(true, TABLE_ORIGINAL_TRACE, columns,
				selection, selectionArgs, null, null, orderBy, null, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				double x = cursor.getDouble(0);
				double y = cursor.getDouble(1);
				int floor = cursor.getInt(2);

				double mx = cursor.getDouble(3);
				double my = cursor.getDouble(4);

				double timestamp = cursor.getDouble(5);
				long tag = cursor.getLong(6);

				int major = cursor.getInt(7);
				int minor = cursor.getInt(8);

				if (!traceDict.containsKey(tag)) {
					SSTrace trace = SSTrace.newTrace(marketID, tag);
					traceDict.put(tag, trace);
				}

				SSTrace trace = traceDict.get(tag);
				trace.addTracePoint(SSTracePoint.newTracePoint(
						new SSLocalPoint(x, y, floor), new SSMapPoint(mx, my),
						new SSBeacon(major, minor, null), timestamp));

			} while (cursor.moveToNext());
		}

		allTraces.addAll(traceDict.values());

		return allTraces;
	}

	public List<Integer> getAllTags() {
		List<Integer> tags = new ArrayList<Integer>();

		String[] columns = new String[] { FIELD_TAG };
		Cursor cursor = db.query(true, TABLE_ORIGINAL_TRACE, columns, null,
				null, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Integer tag = cursor.getInt(0);
				tags.add(tag);
			} while (cursor.moveToNext());
		}

		return tags;
	}

	public List<Integer> getAllTags(String marketID) {
		List<Integer> tags = new ArrayList<Integer>();

		String[] columns = new String[] { FIELD_TAG };
		String selction = String.format(" %s = ? ", FIELD_MARKET_ID);
		String[] selectionArgs = new String[] { marketID };
		Cursor cursor = db.query(true, TABLE_ORIGINAL_TRACE, columns, selction,
				selectionArgs, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Integer tag = cursor.getInt(0);
				tags.add(tag);
			} while (cursor.moveToNext());
		}

		return tags;
	}

}
