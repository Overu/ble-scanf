package com.hy.ss.sdk.database;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hy.ss.sdk.pub.data.SSFileManager;
import com.hy.ss.sdk.pub.entity.SSFacilityInfo;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSShopInfo;

public class SSShopInfoDBAdapter {

	private static final String TABLE_SHOPS = "Shops";
	private static final String TABLE_FACILITIES = "Facilities";

	// private static final String FIELD_ROWID = "_id";

	private static final String FIELD_NAME = "name";
	private static final String FIELD_X_COORD = "x";
	private static final String FIELD_Y_COORD = "y";
	private static final String FIELD_FLOOR = "floor";

	private static final String FIELD_SID = "sid";
	private static final String FIELD_GID = "gid";

	private static final String FIELD_TYPE = "type";

	final Context context;
	private SQLiteDatabase db;
	private File dbFile;

	public SSShopInfoDBAdapter(Context ctx, String mID) {
		this.context = ctx;
		dbFile = new File(SSFileManager.getShopInfoDBPath(mID));
		db = SQLiteDatabase.openOrCreateDatabase(dbFile, null);
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

	public List<SSShopInfo> getAllShopInfos() {
		List<SSShopInfo> allShopInfo = new ArrayList<SSShopInfo>();

		String[] columns = new String[] { FIELD_SID, FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_FLOOR, FIELD_GID };

		Cursor c = db.query(true, TABLE_SHOPS, columns, null, null, null, null,
				null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String sid = c.getString(0);
				String name = c.getString(1);
				double x = c.getDouble(2);
				double y = c.getDouble(3);
				int floor = c.getInt(4);
				String gid = c.getString(5);

				SSShopInfo info = new SSShopInfo(name, gid, sid,
						new SSLocalPoint(x, y, floor));
				allShopInfo.add(info);

			} while (c.moveToNext());
		}

		return allShopInfo;
	}

	public List<SSShopInfo> getAllShopInfosWithName() {
		List<SSShopInfo> allShopInfo = new ArrayList<SSShopInfo>();

		String[] columns = new String[] { FIELD_SID, FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_FLOOR, FIELD_GID };

		Cursor c = db.query(true, TABLE_SHOPS, columns, null, null, null, null,
				null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String name = c.getString(1);
				if (name == null || name.length() == 0) {
					continue;
				}

				String sid = c.getString(0);
				double x = c.getDouble(2);
				double y = c.getDouble(3);
				int floor = c.getInt(4);
				String gid = c.getString(5);

				SSShopInfo info = new SSShopInfo(name, gid, sid,
						new SSLocalPoint(x, y, floor));
				allShopInfo.add(info);

			} while (c.moveToNext());
		}

		return allShopInfo;
	}

	public List<SSShopInfo> getAllShopInfosInFloor(int floor) {
		List<SSShopInfo> allShopInfo = new ArrayList<SSShopInfo>();

		String[] columns = new String[] { FIELD_SID, FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_GID };
		String selection = String.format(" %s = ? ", FIELD_FLOOR);
		String[] selectionArgs = new String[] { floor + "" };

		Cursor c = db.query(true, TABLE_SHOPS, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String sid = c.getString(0);
				String name = c.getString(1);
				double x = c.getDouble(2);
				double y = c.getDouble(3);
				String gid = c.getString(4);

				SSShopInfo info = new SSShopInfo(name, gid, sid,
						new SSLocalPoint(x, y, floor));
				allShopInfo.add(info);

			} while (c.moveToNext());
		}

		return allShopInfo;
	}

	public SSShopInfo getShopInfoForShopId(String sid) {
		SSShopInfo info = null;

		String[] columns = new String[] { FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_FLOOR, FIELD_GID };
		String selection = String.format(" %s = ? ", FIELD_SID);
		String[] selectionArgs = new String[] { sid };

		Cursor c = db.query(true, TABLE_SHOPS, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			String name = c.getString(0);
			double x = c.getDouble(1);
			double y = c.getDouble(2);
			int floor = c.getInt(3);
			String gid = c.getString(4);

			info = new SSShopInfo(name, gid, sid, new SSLocalPoint(x, y, floor));
		}
		return info;
	}

	public SSShopInfo getShopInfoForGeoId(String gid) {
		SSShopInfo info = null;

		String[] columns = new String[] { FIELD_SID, FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_FLOOR };
		String selection = String.format(" %s = ? ", FIELD_GID);
		String[] selectionArgs = new String[] { gid };

		Cursor c = db.query(true, TABLE_SHOPS, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			String sid = c.getString(0);

			String name = c.getString(1);
			double x = c.getDouble(2);
			double y = c.getDouble(3);
			int floor = c.getInt(4);

			info = new SSShopInfo(name, gid, sid, new SSLocalPoint(x, y, floor));
		}
		return info;
	}

	public String getShopIDForGeoID(String gid) {
		String sid = null;

		String[] columns = new String[] { FIELD_SID };
		String selection = String.format(" %s = ? ", FIELD_GID);
		String[] selectionArgs = new String[] { gid };

		Cursor c = db.query(true, TABLE_SHOPS, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			sid = c.getString(0);
		}
		return sid;
	}

	public List<SSFacilityInfo> getAllFacilityInfos() {
		List<SSFacilityInfo> allFacilityInfo = new ArrayList<SSFacilityInfo>();

		String[] columns = new String[] { FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_FLOOR, FIELD_GID, FIELD_TYPE };
		Cursor c = db.query(true, TABLE_FACILITIES, columns, null, null, null,
				null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String name = c.getString(0);
				double x = c.getDouble(1);
				double y = c.getDouble(2);
				int floor = c.getInt(3);
				String gid = c.getString(4);
				int type = c.getInt(5);

				SSFacilityInfo info = new SSFacilityInfo();
				info.setLocation(new SSLocalPoint(x, y, floor));
				info.setName(name);
				info.setType(type);
				info.setGid(gid);

				allFacilityInfo.add(info);
			} while (c.moveToNext());
		}

		return allFacilityInfo;
	}

	public List<SSFacilityInfo> getAllFacilityInfosWithType(int type) {
		// String[] columns = new String[] { FIELD_SID, FIELD_NAME,
		// FIELD_X_COORD,
		// FIELD_Y_COORD, FIELD_GID };
		// String selection = String.format(" %s = ? ", FIELD_FLOOR);
		// String[] selectionArgs = new String[] { floor + "" };

		List<SSFacilityInfo> allFacilityInfo = new ArrayList<SSFacilityInfo>();

		String[] columns = new String[] { FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_FLOOR, FIELD_GID };
		String selection = String.format(" %s = ? ", FIELD_TYPE);
		String[] selectionArgs = new String[] { type + "" };
		Cursor c = db.query(true, TABLE_FACILITIES, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String name = c.getString(0);
				double x = c.getDouble(1);
				double y = c.getDouble(2);
				int floor = c.getInt(3);
				String gid = c.getString(4);

				SSFacilityInfo info = new SSFacilityInfo();
				info.setLocation(new SSLocalPoint(x, y, floor));
				info.setName(name);
				info.setType(type);
				info.setGid(gid);

				allFacilityInfo.add(info);
			} while (c.moveToNext());
		}

		return allFacilityInfo;
	}

	public List<SSFacilityInfo> getAllFacilityInfosInFloor(int floor) {
		List<SSFacilityInfo> allFacilityInfo = new ArrayList<SSFacilityInfo>();

		String[] columns = new String[] { FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_GID, FIELD_TYPE };
		String selection = String.format(" %s = ? ", FIELD_FLOOR);
		String[] selectionArgs = new String[] { floor + "" };

		Cursor c = db.query(true, TABLE_FACILITIES, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String name = c.getString(0);
				double x = c.getDouble(1);
				double y = c.getDouble(2);
				String gid = c.getString(3);
				int type = c.getInt(4);

				SSFacilityInfo info = new SSFacilityInfo();
				info.setLocation(new SSLocalPoint(x, y, floor));
				info.setName(name);
				info.setType(type);
				info.setGid(gid);

				allFacilityInfo.add(info);
			} while (c.moveToNext());
		}

		return allFacilityInfo;
	}

	public List<SSFacilityInfo> getAllFacilityInfosWithTypeInFloor(int type,
			int floor) {
		List<SSFacilityInfo> allFacilityInfo = new ArrayList<SSFacilityInfo>();

		String[] columns = new String[] { FIELD_NAME, FIELD_X_COORD,
				FIELD_Y_COORD, FIELD_GID };
		String selection = String.format(" %s = ? and %s = ? ", FIELD_FLOOR,
				FIELD_TYPE);
		String[] selectionArgs = new String[] { floor + "", type + "" };

		Cursor c = db.query(true, TABLE_FACILITIES, columns, selection,
				selectionArgs, null, null, null, null, null);
		if (c != null && c.moveToFirst()) {
			do {
				String name = c.getString(0);
				double x = c.getDouble(1);
				double y = c.getDouble(2);
				String gid = c.getString(3);

				SSFacilityInfo info = new SSFacilityInfo();
				info.setLocation(new SSLocalPoint(x, y, floor));
				info.setName(name);
				info.setType(type);
				info.setGid(gid);

				allFacilityInfo.add(info);
			} while (c.moveToNext());
		}

		return allFacilityInfo;
	}
}
