package com.hy.ss.sdk.pub.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class SSMapInfo implements Parcelable {
	// private static final String TAG = (SSMapInfo.class.getName());

	private static final String KEY_MAP_ID = "mapID";
	private static final String KEY_FLOOR_STRING = "floor";
	private static final String KEY_FLOOR_NUMBER = "floorIndex";

	private static final String KEY_SIZE_X = "size_x";
	private static final String KEY_SIZE_Y = "size_y";

	private static final String KEY_X_MIN = "xmin";
	private static final String KEY_X_MAX = "xmax";
	private static final String KEY_Y_MIN = "ymin";
	private static final String KEY_Y_MAX = "ymax";

	private static final String KEY_INIT_ANGLE = "initAngle";

	private String mapID;

	private String floorString;
	private int floorNumber;

	private double size_x;
	private double size_y;

	private double xmin;
	private double xmax;
	private double ymin;
	private double ymax;

	private double initAngle;

	public SSMapInfo() {
		super();
	}

	SSMapInfo(Parcel in) {
		mapID = in.readString();
		floorString = in.readString();

		floorNumber = in.readInt();

		size_x = in.readDouble();
		size_y = in.readDouble();

		xmin = in.readDouble();
		xmax = in.readDouble();
		ymin = in.readDouble();
		ymax = in.readDouble();

		initAngle = in.readDouble();
	}

	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mapID);
		dest.writeString(floorString);

		dest.writeInt(floorNumber);

		dest.writeDouble(size_x);
		dest.writeDouble(size_y);

		dest.writeDouble(xmin);
		dest.writeDouble(xmax);
		dest.writeDouble(ymin);
		dest.writeDouble(ymax);

		dest.writeDouble(initAngle);
	}

	public static final Parcelable.Creator<SSMapInfo> CREATOR = new Creator<SSMapInfo>() {
		public SSMapInfo[] newArray(int size) {
			return new SSMapInfo[size];
		};

		public SSMapInfo createFromParcel(Parcel source) {
			return new SSMapInfo(source);
		};
	};

	public int describeContents() {
		return 0;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_MAP_ID)) {
				setMapID(jsonObject.optString(KEY_MAP_ID));
			}
			if (!jsonObject.isNull(KEY_FLOOR_STRING)) {
				setFloorString(jsonObject.optString(KEY_FLOOR_STRING));
			}
			if (!jsonObject.isNull(KEY_FLOOR_NUMBER)) {
				setFloorNumber(jsonObject.optInt(KEY_FLOOR_NUMBER));
			}
			if (!jsonObject.isNull(KEY_SIZE_X)) {
				setSize_x(jsonObject.optDouble(KEY_SIZE_X));
			}
			if (!jsonObject.isNull(KEY_SIZE_Y)) {
				setSize_y(jsonObject.optDouble(KEY_SIZE_Y));
			}
			if (!jsonObject.isNull(KEY_X_MIN)) {
				setXmin(jsonObject.optDouble(KEY_X_MIN));
			}
			if (!jsonObject.isNull(KEY_X_MAX)) {
				setXmax(jsonObject.optDouble(KEY_X_MAX));
			}
			if (!jsonObject.isNull(KEY_Y_MIN)) {
				setYmin(jsonObject.optDouble(KEY_Y_MIN));
			}
			if (!jsonObject.isNull(KEY_Y_MAX)) {
				setYmax(jsonObject.optDouble(KEY_Y_MAX));
			}
			if (!jsonObject.isNull(KEY_INIT_ANGLE)) {
				setInitAngle(jsonObject.optDouble(KEY_INIT_ANGLE));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_MAP_ID, mapID);
			jsonObject.put(KEY_FLOOR_STRING, floorString);
			jsonObject.put(KEY_FLOOR_NUMBER, floorNumber);
			jsonObject.put(KEY_SIZE_X, size_x);
			jsonObject.put(KEY_SIZE_Y, size_y);
			jsonObject.put(KEY_X_MIN, xmin);
			jsonObject.put(KEY_X_MAX, xmax);
			jsonObject.put(KEY_Y_MIN, ymin);
			jsonObject.put(KEY_Y_MAX, ymax);
			jsonObject.put(KEY_INIT_ANGLE, initAngle);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String getMapID() {
		return mapID;
	}

	public MapSize getMapSize() {
		return new MapSize(size_x, size_y);
	}

	public MapExtent getMapExtent() {
		return new MapExtent(xmin, ymin, xmax, ymax);
	}

	public String getFloorString() {
		return floorString;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public double getScaleX() {
		return size_x / (xmax - xmin);
	}

	public double getScaleY() {
		return size_y / (ymax - ymin);
	}

	public double getInitAngle() {
		return initAngle;
	}

	protected void setMapID(String mapID) {
		this.mapID = mapID;
	}

	protected void setFloorString(String floorString) {
		this.floorString = floorString;
	}

	protected void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
	}

	protected void setInitAngle(double initAngle) {
		this.initAngle = initAngle;
	}

	protected void setSize_x(double size_x) {
		this.size_x = size_x;
	}

	protected void setSize_y(double size_y) {
		this.size_y = size_y;
	}

	protected void setXmin(double xmin) {
		this.xmin = xmin;
	}

	protected void setXmax(double xmax) {
		this.xmax = xmax;
	}

	protected void setYmin(double ymin) {
		this.ymin = ymin;
	}

	protected void setYmax(double ymax) {
		this.ymax = ymax;
	}

	@Override
	public String toString() {
		String str = "MapID: %s, Floor:%d ,Size:(%.2f, %.2f) Extent: (%.4f, %.4f, %.4f, %.4f)";
		return String.format(str, mapID, floorNumber, size_x, size_y, xmin,
				ymin, xmax, ymax);
	}

	public class MapExtent {
		double xmin;
		double ymin;
		double xmax;
		double ymax;

		public MapExtent(double xmin, double ymin, double xmax, double ymax) {
			this.xmin = xmin;
			this.ymin = ymin;
			this.xmax = xmax;
			this.ymax = ymax;
		}

		public double getXmin() {
			return xmin;
		}

		public double getYmin() {
			return ymin;
		}

		public double getXmax() {
			return xmax;
		}

		public double getYmax() {
			return ymax;
		}
	}

	public class MapSize {
		double x;
		double y;

		public MapSize(double x, double y) {
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public double getY() {
			return y;
		}

	}
}
