package com.hy.ss.sdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hy.ss.sdk.pub.data.SSFileManager;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapPoint;
import com.hy.ss.sdk.pub.entity.beacon.SSActivityBeacon;
import com.hy.ss.sdk.pub.entity.beacon.SSBeacon;
import com.hy.ss.sdk.pub.entity.beacon.SSPublicBeacon;
import com.hy.ss.sdk.pub.entity.beacon.SSTriggerBeacon;

public class SSBeaconDBAdapter {
	// private static final String TAG =
	// (SSBeaconDBAdapter.class.getSimpleName());

	private static final String TABLE_PUBLIC_BEACON = "SSPublicBeacon";
	private static final String TABLE_TRIGGER_BEACON = "SSTriggerBeacon";
	private static final String TABLE_ACTIVITY_BEACON = "SSActivityBeacon";

	private static final String FIELD_ROWID = "_id";

	private static final String FIELD_BEACON_X_COORD = "x";
	private static final String FIELD_BEACON_Y_COORD = "y";

	private static final String FIELD_BEACON_MAP_X_COORD = "mapx";
	private static final String FIELD_BEACON_MAP_Y_COORD = "mapy";

	private static final String FIELD_BEACON_FLOOR = "floor";

	private static final String FIELD_BEACON_MAJOR = "major";
	private static final String FIELD_BEACON_MINOR = "minor";
	private static final String FIELD_BEACON_TAG = "tag";

	private static final String FIELD_BEACON_TYPE = "type";
	private static final String FIELD_BEACON_SHOPGID = "shopGid";
	private static final String FIELD_BEACON_ACTIVITY_RANGE = "Frange";

	// private static final int DATABASE_VERSION = 1;

	private static final String PUBLIC_DATA_CREATE = "create table %s  (%s integer primary key autoincrement, %s integer not null, %s integer not null, %s text, %s real not null, %s real not null, %s integer not null, %s real not null, %s real not null, %s integer not null, %s text, %s real)";
	private static final String ACTIVITY_DATA_CREATE = "create table %s  (%s integer primary key autoincrement, %s integer not null, %s integer not null, %s text, %s real not null, %s real not null, %s integer not null, %s real not null, %s real not null, %s integer not null, %s text not null, %s real)";
	private static final String TRIGGER_DATA_CREATE = "create table %s  (%s integer primary key autoincrement, %s integer not null, %s integer not null, %s text, %s real not null, %s real not null, %s integer not null, %s real not null, %s real not null, %s integer not null, %s text, %s real)";

	private static final String SQL_PUBLIC_DATA_CREATE = String.format(
			PUBLIC_DATA_CREATE, TABLE_PUBLIC_BEACON, FIELD_ROWID,
			FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR, FIELD_BEACON_TAG,
			FIELD_BEACON_X_COORD, FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
			FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
			FIELD_BEACON_TYPE, FIELD_BEACON_SHOPGID,
			FIELD_BEACON_ACTIVITY_RANGE);

	private static final String SQL_ACTIVITY_DATA_CREATE = String.format(
			ACTIVITY_DATA_CREATE, TABLE_ACTIVITY_BEACON, FIELD_ROWID,
			FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR, FIELD_BEACON_TAG,
			FIELD_BEACON_X_COORD, FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
			FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
			FIELD_BEACON_TYPE, FIELD_BEACON_SHOPGID,
			FIELD_BEACON_ACTIVITY_RANGE);

	private static final String SQL_TRIGGER_DATA_CREATE = String.format(
			TRIGGER_DATA_CREATE, TABLE_TRIGGER_BEACON, FIELD_ROWID,
			FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR, FIELD_BEACON_TAG,
			FIELD_BEACON_X_COORD, FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
			FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
			FIELD_BEACON_TYPE, FIELD_BEACON_SHOPGID,
			FIELD_BEACON_ACTIVITY_RANGE);

	final Context context;
	private SQLiteDatabase db;
	private File dbFile;

	public SSBeaconDBAdapter(Context ctx, String mID) {
		this.context = ctx;
		dbFile = new File(SSFileManager.getBeaconsDBPath(mID));
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
		if (!existTable(TABLE_PUBLIC_BEACON)) {
			createPublicTable();
		}

		if (!existTable(TABLE_TRIGGER_BEACON)) {
			createTriggerTable();
		}

		if (!existTable(TABLE_ACTIVITY_BEACON)) {
			createActivityTable();
		}
	}

	// ======================== Public ============================

	private void createPublicTable() {
		db.execSQL(SQL_PUBLIC_DATA_CREATE);
	}

	public boolean erasePublicBeaconTable() {
		return db.delete(TABLE_PUBLIC_BEACON, null, null) > 0;
	}

	public boolean deletePublicBeacon(SSBeacon beacon) {
		return deletePublicBeacon(beacon.getMajor(), beacon.getMinor());
	}

	public boolean deletePublicBeacon(int major, int minor) {
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };
		return db.delete(TABLE_PUBLIC_BEACON, whereClause, whereArgs) > 0;
	}

	public boolean insertPublicBeacon(SSPublicBeacon beacon) {
		return insertPublicBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), beacon.getLocation().getX(), beacon
						.getLocation().getY(), beacon.getLocation().getFloor(),
				beacon.getMapPoint().getX(), beacon.getMapPoint().getY(),
				beacon.getType().getType(), beacon.getShopGid());
	}

	private boolean insertPublicBeacon(int major, int minor, String tag,
			double x, double y, int floor, double mapx, double mapy, int type,
			String shopid) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_MAJOR, major);
		values.put(FIELD_BEACON_MINOR, minor);
		values.put(FIELD_BEACON_TAG, tag);

		values.put(FIELD_BEACON_X_COORD, x);
		values.put(FIELD_BEACON_Y_COORD, y);
		values.put(FIELD_BEACON_FLOOR, floor);

		values.put(FIELD_BEACON_MAP_X_COORD, mapx);
		values.put(FIELD_BEACON_MAP_Y_COORD, mapy);

		values.put(FIELD_BEACON_TYPE, type);
		values.put(FIELD_BEACON_SHOPGID, shopid);

		return db.insert(TABLE_PUBLIC_BEACON, null, values) > 0;
	}

	public boolean updatePublicBeacon(SSPublicBeacon beacon) {
		return updatePublicBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), beacon.getLocation().getX(), beacon
						.getLocation().getY(), beacon.getLocation().getFloor(),
				beacon.getMapPoint().getX(), beacon.getMapPoint().getY(),
				beacon.getType().getType(), beacon.getShopGid());
	}

	private boolean updatePublicBeacon(int major, int minor, String tag,
			double x, double y, int floor, double mapx, double mapy, int type,
			String shopid) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_TAG, tag);

		values.put(FIELD_BEACON_X_COORD, x);
		values.put(FIELD_BEACON_Y_COORD, y);
		values.put(FIELD_BEACON_FLOOR, floor);

		values.put(FIELD_BEACON_MAP_X_COORD, mapx);
		values.put(FIELD_BEACON_MAP_Y_COORD, mapy);

		values.put(FIELD_BEACON_TYPE, type);
		values.put(FIELD_BEACON_SHOPGID, shopid);

		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };

		return db.update(TABLE_PUBLIC_BEACON, values, whereClause, whereArgs) > 0;
	}

	public boolean updatePublicBeacon(SSBeacon beacon, SSLocalPoint location,
			SSMapPoint mp, String sid) {
		return updatePublicBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), location.getX(), location.getY(),
				location.getFloor(), mp.getX(), mp.getY(), beacon.getType()
						.getType(), sid);
	}

	public List<SSPublicBeacon> getAllPublicBeacons() {
		List<SSPublicBeacon> allBeacons = new ArrayList<SSPublicBeacon>();

		String[] columns = new String[] { FIELD_BEACON_MAJOR,
				FIELD_BEACON_MINOR, FIELD_BEACON_TAG, FIELD_BEACON_X_COORD,
				FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
				FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
				FIELD_BEACON_SHOPGID };
		Cursor c = db.query(true, TABLE_PUBLIC_BEACON, columns, null, null,
				null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				int major = c.getInt(0);
				int minor = c.getInt(1);

				String tag = c.getString(2);
				double x = c.getDouble(3);
				double y = c.getDouble(4);
				int floor = c.getInt(5);

				double mx = c.getDouble(6);
				double my = c.getDouble(7);

				String shopid = c.getString(8);

				SSPublicBeacon beacon = new SSPublicBeacon(major, minor, tag,
						shopid);
				beacon.setLocation(new SSLocalPoint(x, y, floor));
				beacon.setMapPoint(new SSMapPoint(mx, my));

				allBeacons.add(beacon);
			} while (c.moveToNext());
		}

		return allBeacons;
	}

	public SSPublicBeacon getPublicBeacon(int major, int minor) {
		SSPublicBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_TAG,
				FIELD_BEACON_X_COORD, FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
				FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
				FIELD_BEACON_SHOPGID };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] selectionArgs = new String[] { major + "", minor + "" };

		Cursor c = db.query(true, TABLE_PUBLIC_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			String tag = c.getString(0);
			double x = c.getDouble(1);
			double y = c.getDouble(2);
			int floor = c.getInt(3);

			double mx = c.getDouble(4);
			double my = c.getDouble(5);

			String shopid = c.getString(6);

			beacon = new SSPublicBeacon(major, minor, tag, shopid);
			beacon.setLocation(new SSLocalPoint(x, y, floor));
			beacon.setMapPoint(new SSMapPoint(mx, my));
		}
		return beacon;
	}

	// ======================== Trigger ===========================

	private void createTriggerTable() {
		db.execSQL(SQL_TRIGGER_DATA_CREATE);
	}

	public boolean eraseTriggerBeaconTable() {
		return db.delete(TABLE_TRIGGER_BEACON, null, null) > 0;
	}

	public boolean deleteTriggerBeacon(SSBeacon beacon) {
		return deleteTriggerBeacon(beacon.getMajor(), beacon.getMinor());
	}

	public boolean deleteTriggerBeacon(int major, int minor) {
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };
		return db.delete(TABLE_TRIGGER_BEACON, whereClause, whereArgs) > 0;

	}

	public boolean insertTriggerBeacon(SSTriggerBeacon beacon) {
		return insertTriggerBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), beacon.getLocation().getX(), beacon
						.getLocation().getY(), beacon.getLocation().getFloor(),
				beacon.getMapPoint().getX(), beacon.getMapPoint().getY(),
				beacon.getType().getType());
	}

	private boolean insertTriggerBeacon(int major, int minor, String tag,
			double x, double y, int floor, double mapx, double mapy, int type) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_MAJOR, major);
		values.put(FIELD_BEACON_MINOR, minor);
		values.put(FIELD_BEACON_TAG, tag);

		values.put(FIELD_BEACON_X_COORD, x);
		values.put(FIELD_BEACON_Y_COORD, y);
		values.put(FIELD_BEACON_FLOOR, floor);

		values.put(FIELD_BEACON_MAP_X_COORD, mapx);
		values.put(FIELD_BEACON_MAP_Y_COORD, mapy);

		values.put(FIELD_BEACON_TYPE, type);

		return db.insert(TABLE_TRIGGER_BEACON, null, values) > 0;
	}

	public boolean updateTriggerBeacon(SSTriggerBeacon beacon) {
		return updateTriggerBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), beacon.getLocation().getX(), beacon
						.getLocation().getY(), beacon.getLocation().getFloor(),
				beacon.getMapPoint().getX(), beacon.getMapPoint().getY(),
				beacon.getType().getType());
	}

	private boolean updateTriggerBeacon(int major, int minor, String tag,
			double x, double y, int floor, double mapx, double mapy, int type) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_TAG, tag);

		values.put(FIELD_BEACON_X_COORD, x);
		values.put(FIELD_BEACON_Y_COORD, y);
		values.put(FIELD_BEACON_FLOOR, floor);

		values.put(FIELD_BEACON_MAP_X_COORD, mapx);
		values.put(FIELD_BEACON_MAP_Y_COORD, mapy);

		values.put(FIELD_BEACON_TYPE, type);

		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };

		return db.update(TABLE_TRIGGER_BEACON, values, whereClause, whereArgs) > 0;

	}

	public boolean updateTriggerBeacon(SSBeacon beacon, SSLocalPoint location,
			SSMapPoint mp) {
		return updateTriggerBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), location.getX(), location.getY(),
				location.getFloor(), mp.getX(), mp.getY(), beacon.getType()
						.getType());
	}

	public List<SSTriggerBeacon> getAllTriggerBeacons() {
		List<SSTriggerBeacon> allBeacons = new ArrayList<SSTriggerBeacon>();

		String[] columns = new String[] { FIELD_BEACON_MAJOR,
				FIELD_BEACON_MINOR, FIELD_BEACON_TAG, FIELD_BEACON_X_COORD,
				FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
				FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD };
		Cursor c = db.query(true, TABLE_TRIGGER_BEACON, columns, null, null,
				null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			do {
				int major = c.getInt(0);
				int minor = c.getInt(1);

				String tag = c.getString(2);
				double x = c.getDouble(3);
				double y = c.getDouble(4);
				int floor = c.getInt(5);

				double mx = c.getDouble(6);
				double my = c.getDouble(7);

				SSTriggerBeacon beacon = new SSTriggerBeacon(major, minor, tag);
				beacon.setLocation(new SSLocalPoint(x, y, floor));
				beacon.setMapPoint(new SSMapPoint(mx, my));
				allBeacons.add(beacon);
			} while (c.moveToNext());
		}

		return allBeacons;
	}

	public SSTriggerBeacon getTriggerBeacon(int major, int minor) {
		SSTriggerBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_TAG,
				FIELD_BEACON_X_COORD, FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
				FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] selectionArgs = new String[] { major + "", minor + "" };

		Cursor c = db.query(true, TABLE_TRIGGER_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			String tag = c.getString(0);
			double x = c.getDouble(1);
			double y = c.getDouble(2);
			int floor = c.getInt(3);

			double mx = c.getDouble(4);
			double my = c.getDouble(5);
			beacon = new SSTriggerBeacon(major, minor, tag);
			beacon.setLocation(new SSLocalPoint(x, y, floor));
			beacon.setMapPoint(new SSMapPoint(mx, my));
		}
		return beacon;
	}

	// ======================== Activity ==========================

	private void createActivityTable() {
		db.execSQL(SQL_ACTIVITY_DATA_CREATE);
	}

	public boolean eraseActivityBeaconTable() {
		return db.delete(TABLE_ACTIVITY_BEACON, null, null) > 0;
	}

	public boolean deleteActivityBeacon(SSBeacon beacon) {
		return deleteActivityBeacon(beacon.getMajor(), beacon.getMinor());
	}

	public boolean deleteActivityBeacon(int major, int minor) {
		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };
		return db.delete(TABLE_ACTIVITY_BEACON, whereClause, whereArgs) > 0;

	}

	public boolean insertActivityBeacon(SSActivityBeacon beacon) {
		return insertActivityBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), beacon.getLocation().getX(), beacon
						.getLocation().getY(), beacon.getLocation().getFloor(),
				beacon.getMapPoint().getX(), beacon.getMapPoint().getY(),
				beacon.getType().getType(), beacon.getShopGid(),
				beacon.getRange());
	}

	private boolean insertActivityBeacon(int major, int minor, String tag,
			double x, double y, int floor, double mapx, double mapy, int type,
			String shopid, double range) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_MAJOR, major);
		values.put(FIELD_BEACON_MINOR, minor);
		values.put(FIELD_BEACON_TAG, tag);

		values.put(FIELD_BEACON_X_COORD, x);
		values.put(FIELD_BEACON_Y_COORD, y);
		values.put(FIELD_BEACON_FLOOR, floor);

		values.put(FIELD_BEACON_MAP_X_COORD, mapx);
		values.put(FIELD_BEACON_MAP_Y_COORD, mapy);

		values.put(FIELD_BEACON_TYPE, type);
		values.put(FIELD_BEACON_SHOPGID, shopid);

		values.put(FIELD_BEACON_ACTIVITY_RANGE, range);

		return db.insert(TABLE_ACTIVITY_BEACON, null, values) > 0;
	}

	public boolean updateActivityBeacon(SSActivityBeacon beacon) {
		return updateActivityBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), beacon.getLocation().getX(), beacon
						.getLocation().getY(), beacon.getLocation().getFloor(),
				beacon.getMapPoint().getX(), beacon.getMapPoint().getY(),
				beacon.getType().getType(), beacon.getShopGid(),
				beacon.getRange());
	}

	public boolean updateActivityBeacon(SSBeacon beacon, SSLocalPoint location,
			SSMapPoint mp, String sid, double range) {
		return updateActivityBeacon(beacon.getMajor(), beacon.getMinor(),
				beacon.getTag(), location.getX(), location.getY(),
				location.getFloor(), mp.getX(), mp.getY(), beacon.getType()
						.getType(), sid, range);
	}

	private boolean updateActivityBeacon(int major, int minor, String tag,
			double x, double y, int floor, double mapx, double mapy, int type,
			String shopid, double range) {
		ContentValues values = new ContentValues();
		values.put(FIELD_BEACON_TAG, tag);

		values.put(FIELD_BEACON_X_COORD, x);
		values.put(FIELD_BEACON_Y_COORD, y);
		values.put(FIELD_BEACON_FLOOR, floor);

		values.put(FIELD_BEACON_MAP_X_COORD, mapx);
		values.put(FIELD_BEACON_MAP_Y_COORD, mapy);

		values.put(FIELD_BEACON_TYPE, type);
		values.put(FIELD_BEACON_SHOPGID, shopid);
		values.put(FIELD_BEACON_ACTIVITY_RANGE, range);

		String whereClause = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] whereArgs = new String[] { major + "", minor + "" };

		return db.update(TABLE_ACTIVITY_BEACON, values, whereClause, whereArgs) > 0;
	}

	public List<SSActivityBeacon> getAllActivityBeacons() {
		List<SSActivityBeacon> allBeacons = new ArrayList<SSActivityBeacon>();

		String[] columns = new String[] { FIELD_BEACON_MAJOR,
				FIELD_BEACON_MINOR, FIELD_BEACON_TAG, FIELD_BEACON_X_COORD,
				FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
				FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
				FIELD_BEACON_SHOPGID, FIELD_BEACON_ACTIVITY_RANGE };
		Cursor c = db.query(true, TABLE_ACTIVITY_BEACON, columns, null, null,
				null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				int major = c.getInt(0);
				int minor = c.getInt(1);

				String tag = c.getString(2);
				double x = c.getDouble(3);
				double y = c.getDouble(4);
				int floor = c.getInt(5);

				double mx = c.getDouble(6);
				double my = c.getDouble(7);

				String shopid = c.getString(8);
				double range = c.getDouble(9);

				SSActivityBeacon beacon;
				if (range <= 0) {
					beacon = new SSActivityBeacon(major, minor, tag, shopid);
				} else {
					beacon = new SSActivityBeacon(major, minor, tag, shopid,
							range);
				}
				beacon.setLocation(new SSLocalPoint(x, y, floor));
				beacon.setMapPoint(new SSMapPoint(mx, my));
				allBeacons.add(beacon);
			} while (c.moveToNext());
		}
		return allBeacons;
	}

	public SSActivityBeacon getActivityBeacion(int major, int minor) {
		SSActivityBeacon beacon = null;

		String[] columns = new String[] { FIELD_BEACON_TAG,
				FIELD_BEACON_X_COORD, FIELD_BEACON_Y_COORD, FIELD_BEACON_FLOOR,
				FIELD_BEACON_MAP_X_COORD, FIELD_BEACON_MAP_Y_COORD,
				FIELD_BEACON_SHOPGID, FIELD_BEACON_ACTIVITY_RANGE };
		String selection = String.format(" %s = ? and %s = ? ",
				FIELD_BEACON_MAJOR, FIELD_BEACON_MINOR);
		String[] selectionArgs = new String[] { major + "", minor + "" };

		Cursor c = db.query(true, TABLE_ACTIVITY_BEACON, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			String tag = c.getString(0);
			double x = c.getDouble(1);
			double y = c.getDouble(2);
			int floor = c.getInt(3);

			double mx = c.getDouble(4);
			double my = c.getDouble(5);

			String shopid = c.getString(6);
			double range = c.getDouble(7);

			if (range <= 0) {
				beacon = new SSActivityBeacon(major, minor, tag, shopid);
			} else {
				beacon = new SSActivityBeacon(major, minor, tag, shopid, range);
			}
			beacon.setLocation(new SSLocalPoint(x, y, floor));
			beacon.setMapPoint(new SSMapPoint(mx, my));
		}

		return beacon;
	}

	public List<SSActivityBeacon> getActivityBeaconAround(
			SSLocalPoint location, double range) {
		List<SSActivityBeacon> allBeacons = new ArrayList<SSActivityBeacon>();
		return allBeacons;
	}

}
