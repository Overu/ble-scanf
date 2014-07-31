package com.hy.ss.sdk.pub.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import android.os.Environment;

public class SSFileManager {
	public static final String ROOT_DIR = Environment
			.getExternalStorageDirectory() + "/ShopSmart/";

	private static String MAP_DIR = ROOT_DIR + "MapFile";

	private static String JSON_FILE_FLOOR = MAP_DIR + "/%s_floor.json";
	private static String JSON_FILE_AREA = MAP_DIR + "/%s_area.json";
	private static String JSON_FILE_SHOP = MAP_DIR + "/%s_shop.json";
	private static String JSON_FILE_FACILITY = MAP_DIR + "/%s_facility.json";

	private static String JSON_FILE_CITY = MAP_DIR + "/Cities.json";
	private static String JSON_FILE_MARKET = MAP_DIR + "/Markets_city_%s.json";
	private static String JSON_FILE_MAPINFO = MAP_DIR
			+ "/MapInfo_market_%s.json";
	private static String JSON_FILE_ACTIVITY = MAP_DIR
			+ "/%s_Activity_market.json";

	private static String DB_FILE_MARKET_REGION = MAP_DIR + "/MarketRegion.db";
	private static String DB_FILE_PRIMITIVE_BEACON = MAP_DIR
			+ "/%s_PrimitiveBeacon.db";
	private static String DB_FILE_SHOP_INFO = MAP_DIR + "/%s_ShopInfo.db";
	private static String DB_FILE_BEACONS = MAP_DIR + "/%s_SSBeacon.db";
	private static String DB_FILE_BEACON_RECORD = ROOT_DIR + "/BeaconRecord.db";
	private static String DB_FILE_PUSHING_RECORD = ROOT_DIR
			+ "/PushingRecord.db";
	private static String DB_FILE_TRACE = ROOT_DIR + "/Trace.db";
	private static String DB_FILE_ORIGINAL_TRACE = ROOT_DIR
			+ "/OriginalTrace.db";

	public static boolean fileExist(String path) {
		return new File(path).exists() ? true : false;
	}

	public static File getFileFromFilePath(String filename) {
		File file = new File(filename);
		if (!file.isAbsolute()) {
			File root = Environment.getExternalStorageDirectory();
			file = new File(root, filename);
		}
		return file;
	}

	public static String getMarketRegionDBPath() {
		return DB_FILE_MARKET_REGION;
	}

	public static String getPrimitiveBeaconDBPath(String marketID) {
		return String.format(DB_FILE_PRIMITIVE_BEACON, marketID);
	}

	public static String getShopInfoDBPath(String marketID) {
		return String.format(DB_FILE_SHOP_INFO, marketID);
	}

	public static String getBeaconsDBPath(String marketID) {
		return String.format(DB_FILE_BEACONS, marketID);
	}

	public static String getBeaconRecordDBPath() {
		return DB_FILE_BEACON_RECORD;
	}

	public static String getPushingRecordDBPath() {
		return DB_FILE_PUSHING_RECORD;
	}

	public static String getTraceDBPath() {
		return DB_FILE_TRACE;
	}

	public static String getOriginalTraceDBPath() {
		return DB_FILE_ORIGINAL_TRACE;
	}

	public static String getFloorFilePath(String floorname) {
		return String.format(JSON_FILE_FLOOR, floorname);
	}

	public static String getShopFilePath(String shopname) {
		return String.format(JSON_FILE_SHOP, shopname);
	}

	public static String getAreaFilePath(String areaname) {
		return String.format(JSON_FILE_AREA, areaname);
	}

	public static String getFacilityFilePath(String facilityname) {
		return String.format(JSON_FILE_FACILITY, facilityname);
	}

	public static String getCityJsonPath() {
		return JSON_FILE_CITY;
	}

	public static String getMarketJsonPath(String cityID) {
		return String.format(JSON_FILE_MARKET, cityID);
	}

	public static String getMapInfoJsonPath(String marketID) {
		return String.format(JSON_FILE_MAPINFO, marketID);
	}

	public static String getActivityJsonPath(String marketID) {
		return String.format(JSON_FILE_ACTIVITY, marketID);
	}

	public static byte[] readByteFromFile(String path) {
		RandomAccessFile input;
		try {
			input = new RandomAccessFile(path, "rw");
			long filelength = input.length();
			byte output[] = new byte[(int) filelength];
			byte buffer[] = new byte[1024];

			int c;
			int offset = 0;
			while ((c = input.read(buffer)) != -1) {
				System.arraycopy(buffer, 0, output, offset, c);
				offset += c;
			}

			input.close();
			return output;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
