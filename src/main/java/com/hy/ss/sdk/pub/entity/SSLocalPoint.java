package com.hy.ss.sdk.pub.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class SSLocalPoint {
	private static final String KEY_X = "x";
	private static final String KEY_Y = "y";
	private static final String KEY_FLOOR = "floor";

	private double x;
	private double y;
	private int floor;

	public SSLocalPoint(double x, double y) {
		this.x = x;
		this.y = y;
		this.floor = 1;
	}

	public SSLocalPoint(double x, double y, int floor) {
		this.x = x;
		this.y = y;
		this.floor = floor;
	}

	public boolean equal(SSLocalPoint p) {
		if (p == null)
			return false;

		if (this.x == p.x && this.y == p.y && this.floor == p.floor) {
			return true;
		}
		return false;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			setX(jsonObject.optDouble(KEY_X));
			setY(jsonObject.optDouble(KEY_Y));
			setFloor(jsonObject.optInt(KEY_FLOOR));
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_X, x);
			jsonObject.put(KEY_Y, y);
			jsonObject.put(KEY_FLOOR, floor);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("(").append(x).append(", ").append(y).append(")")
				.append(" in Floor: ").append(floor);
		return buffer.toString();
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	// public static double distanceBetween(PMLocalPoint p1, PMLocalPoint p2) {
	// if (!p1.getFloor().equals(p2.getFloor())) {
	// return Double.POSITIVE_INFINITY;
	// }
	//
	// double sqrt = (p1.getX() - p2.getX()) * (p1.getX() - p2.getX())
	// + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY());
	// return Math.sqrt(sqrt);
	// }
	//
	// public static Point LocalToMapPoint(PMLocalPoint localPoint, PMapInfo
	// map) {
	// double posX = map.getXMin() + (localPoint.getX() / map.getSizeX())
	// * (map.getXMax() - map.getXMin());
	// double posY = map.getYMin() + (localPoint.getY() / map.getSizeY())
	// * (map.getYMax() - map.getYMin());
	// return new Point(posX, posY);
	// }
	//
	// public static PMLocalPoint MapToLocalPoint(Point globalPoint, PMapInfo
	// map) {
	// double posX = (globalPoint.getX() - map.getXMin())
	// / (map.getXMax() - map.getXMin()) * map.getSizeX();
	// double posY = (globalPoint.getY() - map.getYMin())
	// / (map.getYMax() - map.getYMin()) * map.getSizeY();
	// return new PMLocalPoint(posX, posY);
	// }

	public double distanceWithPoint(SSLocalPoint lp) {
		if (lp == null) {
			return -1;
		}

		if (floor != lp.getFloor()) {
			return Double.POSITIVE_INFINITY;
		}

		return Math.sqrt((x - lp.getX()) * (x - lp.getX()) + (y - lp.getY())
				* (y - lp.getY()));
	}

	public static double distanceWithPoints(SSLocalPoint lp1, SSLocalPoint lp2) {
		if (lp1 == null) {
			return -1;
		}
		return lp1.distanceWithPoint(lp2);
	}
}
