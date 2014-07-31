package com.hy.ss.sdk.pub.entity;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class SSMarket implements Parcelable {
	// private static final String TAG = (SSMarket.class.getName());

	private static final String KEY_MARKET_ID = "id";
	private static final String KEY_MARKET_NAME = "name";
	private static final String KEY_MARKET_ADDRESS = "address";
	private static final String KEY_MARKET_LONGITUDE = "longitude";
	private static final String KEY_MARKET_LATITUDE = "latitude";
	private static final String KEY_MARKET_STATUS = "status";

	private String marketID;
	private String name;
	private String address;

	private double longitude;
	private double latitude;

	private int status;

	public SSMarket() {
		super();
	}

	SSMarket(Parcel in) {
		marketID = in.readString();
		name = in.readString();
		address = in.readString();

		longitude = in.readDouble();
		latitude = in.readDouble();

		status = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(marketID);
		dest.writeString(name);
		dest.writeString(address);

		dest.writeDouble(longitude);
		dest.writeDouble(latitude);

		dest.writeInt(status);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SSMarket) {
			if (this.marketID.equals(((SSMarket) obj).getMarketID())) {
				return true;
			}
		}
		return false;
	}

	public static final Parcelable.Creator<SSMarket> CREATOR = new Creator<SSMarket>() {
		@Override
		public SSMarket[] newArray(int size) {
			return new SSMarket[size];
		}

		@Override
		public SSMarket createFromParcel(Parcel source) {
			return new SSMarket(source);
		}
	};

	public void parseJson(JSONObject jsonObject) {
		if (jsonObject != null) {
			if (!jsonObject.isNull(KEY_MARKET_ID)) {
				setMarketID(jsonObject.optString(KEY_MARKET_ID));
			}
			if (!jsonObject.isNull(KEY_MARKET_NAME)) {
				setName(jsonObject.optString(KEY_MARKET_NAME));
			}
			if (!jsonObject.isNull(KEY_MARKET_ADDRESS)) {
				setAddress(jsonObject.optString(KEY_MARKET_ADDRESS));
			}
			if (!jsonObject.isNull(KEY_MARKET_LONGITUDE)) {
				setLongitude(jsonObject.optDouble(KEY_MARKET_LONGITUDE));
			}
			if (!jsonObject.isNull(KEY_MARKET_LATITUDE)) {
				setLatitude(jsonObject.optDouble(KEY_MARKET_LATITUDE));
			}
			if (!jsonObject.isNull(KEY_MARKET_STATUS)) {
				setStatus(jsonObject.optInt(KEY_MARKET_STATUS));
			}
		}
	}

	public JSONObject buildJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(KEY_MARKET_ID, marketID);
			jsonObject.put(KEY_MARKET_NAME, name);
			jsonObject.put(KEY_MARKET_ADDRESS, address);
			jsonObject.put(KEY_MARKET_LONGITUDE, longitude);
			jsonObject.put(KEY_MARKET_LATITUDE, latitude);
			jsonObject.put(KEY_MARKET_STATUS, status);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	protected void setMarketID(String marketID) {
		this.marketID = marketID;
	}

	protected void setName(String name) {
		this.name = name;
	}

	protected void setAddress(String address) {
		this.address = address;
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

	public String getMarketID() {
		return marketID;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
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

	@Override
	public String toString() {
		return "MarektID = " + marketID + ", MarketName = " + name;
	}

}
