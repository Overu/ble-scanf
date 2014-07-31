package com.hy.ss.sdk.pub.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class SSCity implements Parcelable {
	// private static final String TAG = (SSCity.class.getName());

	private static final String KEY_CITY_ID = "id";
	private static final String KEY_CITY_NAME = "name";
	private static final String KEY_CITY_SHORT_NAME = "sname";
	private static final String KEY_CITY_LONGITUDE = "longitude";
	private static final String KEY_CITY_LATITUDE = "latitude";
	private static final String KEY_CITY_STATUS = "status";

	private String cityID;
	private String name;
	private String sname;

	private double longitude;
	private double latitude;

	private int status;

	public SSCity() {
		super();
	}

	SSCity(Parcel in) {
		cityID = in.readString();
		name = in.readString();
		sname = in.readString();

		longitude = in.readDouble();
		latitude = in.readDouble();

		status = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(cityID);
		dest.writeString(name);
		dest.writeString(sname);

		dest.writeDouble(longitude);
		dest.writeDouble(latitude);

		dest.writeInt(status);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SSCity) {
			if (this.cityID.equals(((SSCity) obj).getCityID())) {
				return true;
			}
		}
		return false;
	}

	public static final Parcelable.Creator<SSCity> CREATOR = new Creator<SSCity>() {
		@Override
		public SSCity[] newArray(int size) {
			return new SSCity[size];
		}

		@Override
		public SSCity createFromParcel(Parcel source) {
			return new SSCity(source);
		}
	};

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_CITY_ID)) {
				setCityID(jsonObject.optString(KEY_CITY_ID));
			}
			if (!jsonObject.isNull(KEY_CITY_NAME)) {
				setName(jsonObject.optString(KEY_CITY_NAME));
			}
			if (!jsonObject.isNull(KEY_CITY_SHORT_NAME)) {
				setSname(jsonObject.optString(KEY_CITY_SHORT_NAME));
			}
			if (!jsonObject.isNull(KEY_CITY_LONGITUDE)) {
				setLongitude(jsonObject.optDouble(KEY_CITY_LONGITUDE));
			}
			if (!jsonObject.isNull(KEY_CITY_LATITUDE)) {
				setLatitude(jsonObject.optDouble(KEY_CITY_LATITUDE));
			}
			if (!jsonObject.isNull(KEY_CITY_STATUS)) {
				setStatus(jsonObject.optInt(KEY_CITY_STATUS));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_CITY_ID, cityID);
			jsonObject.put(KEY_CITY_NAME, name);
			jsonObject.put(KEY_CITY_SHORT_NAME, sname);
			jsonObject.put(KEY_CITY_LONGITUDE, longitude);
			jsonObject.put(KEY_CITY_LATITUDE, latitude);
			jsonObject.put(KEY_CITY_STATUS, status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public String getCityID() {
		return cityID;
	}

	public String getName() {
		return name;
	}

	public String getSname() {
		return sname;
	}

	public double getLongitude() {
		return longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public int getStatus() {
		return status;
	}

	protected void setCityID(String cityID) {
		this.cityID = cityID;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setSname(String sname) {
		this.sname = sname;
	}

	protected void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	protected void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	protected void setStatus(int status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "CityID = " + cityID + ", CityName = " + name;
	}
}
