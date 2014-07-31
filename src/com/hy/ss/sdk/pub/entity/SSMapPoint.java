package com.hy.ss.sdk.pub.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class SSMapPoint {
	private static final String KEY_X = "x";
	private static final String KEY_Y = "y";

	private double x;
	private double y;

	public SSMapPoint() {

	}

	public SSMapPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			setX(jsonObject.optDouble(KEY_X));
			setY(jsonObject.optDouble(KEY_Y));
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_X, x);
			jsonObject.put(KEY_Y, y);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	@Override
	public String toString() {
		return "SSMapPoint [x=" + x + ", y=" + y + "]";
	}

}
