package com.hy.ss.sdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hy.ss.sdk.pub.data.SSFileManager;
import com.hy.ss.sdk.pub.entity.beacon.SSBeacon;

public class SSPrimitiveBeaconDBAdapter {

	private static final String TABLE_PRIMITIVE_BEACON = "PrimitiveBeacon";
	private static final String FIELD_ROWID = "_id";
	private static final String FIELD_BEACON_MAJOR = "major";
	private static final String FIELD_BEACON_MINOR = "minor";
	private static final String FIELD_BEACON_TAG = "tag";

	private static final String DATA_CREATE = "create table %s ( %s integer primary key autoincrement, %s integer not null, %s integer not null, %s text not null)";
	private static final String SQL_DATA_CREATE = String.format(DATA_CREATE,
			TABLE_PRIMITIVE_BEACON, FIELD_ROWID, FIELD_BEACON_MAJOR,
			FIELD_BEACON_MINOR, FIELD_BEACON_TAG);
	// private static final int DATABASE_VERSION = 1;

	final Context context;
	private SQLiteDatabase db;
	private File dbFile;

	// private String marketID;

	public SSPrimitiveBeaconDBAdapter(Context ctx, String mID) {
		this.context = ctx;
		// this.marketID = mID;
		dbFile = new File(SSFileManager.getPrimitiveBeaconDBPath(mID));
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
		if (!existTable(TABLE_PRIMITIVE_BEACON)) {
			createPrimitiveTable();
		}
	}

	private void createPrimitiveTable() {
		db.execSQL(SQL_DATA_CREATE);
	}

	public boolean erasePrimitiveBeaconTable() {
		return db.delete(TABLE_PRIMITIVE_BEACON, null, null) > 0;
	}

	// ======================================================
	public boolean deletePrimitiveBeacon(SSBeacon beacon) {
		return deletePrimitiveBeacon(beacon.getMajor(), beacon.getMinor());
	}

	public boolean deletePrimitiveBeacon(int major, int minor) {
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };
		return db.delete(TABLE_PRIMITIVE_BEACON, whereClause, whereArgs) > 0;
	}

	public boolean insertPrimitiveBeacon(SSBeacon beacon) {
		return insertPrimitiveBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag());
	}

	public boolean insertPrimitiveBeacon(int major, int minor, String tag) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_MAJOR, major);
		values.put(FIELD_BEACON_MINOR, minor);
		values.put(FIELD_BEACON_TAG, tag);
		return db.insert(TABLE_PRIMITIVE_BEACON, null, values) > 0;
	}

	public boolean updatePrimitiveBeacon(int major, int minor, String tag) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_TAG, tag);
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };
		return db
				.update(TABLE_PRIMITIVE_BEACON, values, whereClause, whereArgs) > 0;
	}

	public boolean updatePrimitiveBeacon(SSBeacon beacon) {
		return updatePrimitiveBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag());
	}

	public List<SSBeacon> getAllPrimitiveBeacons() {
		List<SSBeacon> allBeacons = new ArrayList<SSBeacon>();

		String[] coloums = new String[] { FIELD_BEACON_MAJOR,
				FIELD_BEACON_MINOR, FIELD_BEACON_TAG };
		Cursor c = db.query(true, TABLE_PRIMITIVE_BEACON, coloums, null, null,
				null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				int major = c.getInt(0);
				int minor = c.getInt(1);
				String tag = c.getString(2);
				allBeacons.add(new SSBeacon(major, minor, tag));
			} while (c.moveToNext());
		}

		return allBeacons;
	}

	public SSBeacon getPrimitiveBeacon(int major, int minor) {
		SSBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_TAG };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] selectionArgs = new String[] { major + "", minor + "" };

		Cursor c = db.query(true, TABLE_PRIMITIVE_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			String tag = c.getString(0);
			beacon = new SSBeacon(major, minor, tag);
		}
		return beacon;
	}

	public SSBeacon getPrimitiveBeacon(String tag) {
		SSBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_MAJOR,
				FIELD_BEACON_MINOR };
		String selection = String.format(" %s = ? ", FIELD_BEACON_TAG);
		String[] selectionArgs = new String[] { tag };

		Cursor c = db.query(true, TABLE_PRIMITIVE_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			int major = c.getInt(0);
			int minor = c.getInt(1);
			beacon = new SSBeacon(major, minor, tag);
		}
		return beacon;
	}
}
