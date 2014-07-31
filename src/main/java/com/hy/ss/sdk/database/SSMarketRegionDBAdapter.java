package com.hy.ss.sdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hy.ss.sdk.pub.data.SSFileManager;
import com.hy.ss.sdk.pub.entity.beacon.BeaconType;

public class SSMarketRegionDBAdapter {

	private static final String TAG = (SSMarketRegionDBAdapter.class
			.getSimpleName());

	private static int REGION_TYPE_PUBLIC = BeaconType.PUBLIC.getType();
	private static int REGION_TYPE_TRIGGER = BeaconType.TRIGGER.getType();
	private static int REGION_TYPE_ACTIVITY = BeaconType.ACTIVITY.getType();

	private static final String TABLE_MARKET_REGION = "MarketRegion";
	private static final String FIELD_ROWID = "_id";
	private static final String FIELD_MARKET_ID = "marketID";
	private static final String FIELD_BEACON_MAJOR = "major";
	private static final String FIELD_BEACON_REGION_TYPE = "type";

	private static final String DATA_CREATE = "create table %s ( %s integer primary key autoincrement, %s text not null, %s integer not null, %s integer not null)";
	private static final String SQL_DATA_CREATE = String.format(DATA_CREATE,
			TABLE_MARKET_REGION, FIELD_ROWID, FIELD_MARKET_ID,
			FIELD_BEACON_MAJOR, FIELD_BEACON_REGION_TYPE);

	static final int DATABASE_VERSION = 1;

	final Context context;
	SQLiteDatabase db;
	File dbFile;

	public SSMarketRegionDBAdapter(Context ctx) {
		this.context = ctx;
		dbFile = new File(SSFileManager.getMarketRegionDBPath());
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
		if (!existTable(TABLE_MARKET_REGION)) {
			createMarketRegionTable();
		}
	}

	private void createMarketRegionTable() {
		db.execSQL(SQL_DATA_CREATE);
	}

	// =========== Database operation ======================

	public void eraseRegionTable() {
		db.delete(TABLE_MARKET_REGION, null, null);
	}

	public boolean deleteRegion(String marketID) {
		String whereClause = String.format(" %s = ? ", FIELD_MARKET_ID,
				marketID);
		String[] whereArgs = new String[] { marketID };
		return db.delete(TABLE_MARKET_REGION, whereClause, whereArgs) > 0;
	}

	public boolean deleteRegion(String marketID, int type) {
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_MARKET_ID, FIELD_BEACON_REGION_TYPE);
		String[] whereArgs = new String[] { marketID, type + "" };
		return db.delete(TABLE_MARKET_REGION, whereClause, whereArgs) > 0;
	}

	public boolean deleteRegion(int major) {
		String whereClause = String.format(" %s = ? ", FIELD_BEACON_MAJOR);
		String[] whereArgs = new String[] { major + "" };
		return db.delete(TABLE_MARKET_REGION, whereClause, whereArgs) > 0;
	}

	public boolean insertRegion(String marketID, int major, int type) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(FIELD_MARKET_ID, marketID);
		initialValues.put(FIELD_BEACON_MAJOR, major);
		initialValues.put(FIELD_BEACON_REGION_TYPE, type);

		return db.insert(TABLE_MARKET_REGION, null, initialValues) > 0;
	}

	public boolean udpateRegion(String marketID, int major, int type) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_MAJOR, major);
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_MARKET_ID, FIELD_BEACON_REGION_TYPE);
		String[] whereArgs = new String[] { marketID, type + "" };
		return db.update(TABLE_MARKET_REGION, values, whereClause, whereArgs) > 0;
	}

	public int getPublicRegion(String marketID) {
		int major = -1;

		String[] columns = new String[] { FIELD_BEACON_MAJOR };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_MARKET_ID, FIELD_BEACON_REGION_TYPE);
		String[] selectionArgs = new String[] { marketID,
				REGION_TYPE_PUBLIC + "" };

		Cursor c = db.query(true, TABLE_MARKET_REGION, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			major = c.getInt(0);
		}
		return major;
	}

	public int getTriggerRegion(String marketID) {
		int major = -1;

		String[] columns = new String[] { FIELD_BEACON_MAJOR };
		String selection = String.format(" %s = ? and %s = ?", FIELD_MARKET_ID,
				FIELD_BEACON_REGION_TYPE);
		String[] selectionArgs = new String[] { marketID,
				REGION_TYPE_TRIGGER + "" };

		Cursor c = db.query(true, TABLE_MARKET_REGION, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			major = c.getInt(0);
		}
		return major;
	}

	public int getActivityRegion(String marketID) {
		int major = -1;

		String[] columns = new String[] { FIELD_BEACON_MAJOR };
		String selection = String.format(" %s = ? and %s = ?", FIELD_MARKET_ID,
				FIELD_BEACON_REGION_TYPE);
		String[] selectionArgs = new String[] { marketID,
				REGION_TYPE_ACTIVITY + "" };

		Cursor c = db.query(true, TABLE_MARKET_REGION, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			major = c.getInt(0);
		}
		return major;
	}

	public String getMarketIDForRegion(int major) {
		String marketID = null;

		String[] columns = new String[] { FIELD_MARKET_ID };
		String selection = String.format(" %s = ?", FIELD_BEACON_MAJOR);
		String[] selectionArgs = new String[] { major + "" };

		Cursor c = db.query(true, TABLE_MARKET_REGION, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			marketID = c.getString(0);
		}
		return marketID;
	}

	public List<String> getAllMarkets() {
		List<String> markets = new ArrayList<String>();

		String[] columns = new String[] { FIELD_MARKET_ID };
		Cursor cursor = db.query(true, TABLE_MARKET_REGION, columns, null,
				null, null, null, null, null, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String mid = cursor.getString(0);
				markets.add(mid);
			} while (cursor.moveToNext());
		}
		return markets;
	}

	public Cursor getAllRecords() {
		Cursor c = db.query(true, TABLE_MARKET_REGION, null, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			return c;
		}
		return null;
	}
}
