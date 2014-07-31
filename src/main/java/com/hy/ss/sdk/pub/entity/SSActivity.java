package com.hy.ss.sdk.pub.entity;

import org.json.JSONException;
import org.json.JSONObject;

public class SSActivity {
	private static final String KEY_ACTIVITY_ID = "activityID";
	private static final String KEY_ACTIVITY_SHOP_ID = "shopID";
	private static final String KEY_ACTIVITY_TITLE = "title";
	private static final String KEY_ACTIVITY_DETAIL = "detail";
	private static final String KEY_ACTIVITY_IMAGE = "image";
	private static final String KEY_ACTIVITY_TIME = "time";

	private String activityID;
	private String shopID;
	private String title;
	private String detail;
	private String time;
	private String image;

	public SSActivity() {
		super();
	}

	public SSActivity(String activityID, String shopID, String title,
			String detail, String time, String image) {
		this.activityID = activityID;
		this.shopID = shopID;
		this.title = title;
		this.detail = detail;
		this.time = time;
		this.image = image;
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_ACTIVITY_ID, activityID);
			jsonObject.put(KEY_ACTIVITY_SHOP_ID, shopID);
			jsonObject.put(KEY_ACTIVITY_TITLE, title);
			jsonObject.put(KEY_ACTIVITY_DETAIL, detail);
			jsonObject.put(KEY_ACTIVITY_IMAGE, image);
			jsonObject.put(KEY_ACTIVITY_TIME, time);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_ACTIVITY_ID)) {
				setActivityID(jsonObject.optString(KEY_ACTIVITY_ID));
			}
			if (!jsonObject.isNull(KEY_ACTIVITY_SHOP_ID)) {
				setShopID(jsonObject.optString(KEY_ACTIVITY_SHOP_ID));
			}
			if (!jsonObject.isNull(KEY_ACTIVITY_TITLE)) {
				setTitle(jsonObject.optString(KEY_ACTIVITY_TITLE));
			}
			if (!jsonObject.isNull(KEY_ACTIVITY_DETAIL)) {
				setDetail(jsonObject.optString(KEY_ACTIVITY_DETAIL));
			}
			if (!jsonObject.isNull(KEY_ACTIVITY_IMAGE)) {
				setImage(jsonObject.optString(KEY_ACTIVITY_IMAGE));
			}
			if (!jsonObject.isNull(KEY_ACTIVITY_TIME)) {
				setTime(jsonObject.optString(KEY_ACTIVITY_TIME));
			}
		}
	}

	public String getActivityID() {
		return activityID;
	}

	public String getShopID() {
		return shopID;
	}

	public String getTitle() {
		return title;
	}

	public String getDetail() {
		return detail;
	}

	public String getTime() {
		return time;
	}

	public String getImage() {
		return image;
	}

	protected void setActivityID(String activityID) {
		this.activityID = activityID;
	}

	protected void setShopID(String shopID) {
		this.shopID = shopID;
	}

	protected void setTitle(String title) {
		this.title = title;
	}

	protected void setDetail(String detail) {
		this.detail = detail;
	}

	protected void setTime(String time) {
		this.time = time;
	}

	protected void setImage(String image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "SSActivity [activityID=" + activityID + ", shopID=" + shopID
				+ ", title=" + title + "]";
	}

}
