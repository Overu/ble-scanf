package com.hy.ss.sdk.pub.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class SSShopInfo implements Parcelable {

	// private static final String KEY_NAME = "name";
	// private static final String KEY_GID = "gid";
	// private static final String KEY_SID = "sid";
	// private static final String KEY_X = "x";
	// private static final String KEY_Y = "y";
	// private static final String KEY_FLOOR = "floor";

	private String name;
	private String gid;
	private String sid;
	private SSLocalPoint location;

	public SSShopInfo(String name, String gid, String sid, SSLocalPoint location) {
		this.name = name;
		this.gid = gid;
		this.sid = sid;
		this.location = location;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	SSShopInfo(Parcel in) {
		name = in.readString();
		gid = in.readString();
		sid = in.readString();

		double x = in.readDouble();
		double y = in.readDouble();
		int floor = in.readInt();

		location = new SSLocalPoint(x, y, floor);
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(gid);
		dest.writeString(sid);

		dest.writeDouble(location.getX());
		dest.writeDouble(location.getY());

		dest.writeInt(location.getFloor());
	}

	public static final Parcelable.Creator<SSShopInfo> CREATOR = new Creator<SSShopInfo>() {
		@Override
		public SSShopInfo[] newArray(int size) {
			return new SSShopInfo[size];
		}

		@Override
		public SSShopInfo createFromParcel(Parcel source) {
			return new SSShopInfo(source);
		}
	};

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getSid() {
		return sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public SSLocalPoint getLocation() {
		return location;
	}

	public void setLocation(SSLocalPoint location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "SSShopInfo [name=" + name + ", gid=" + gid + ", sid=" + sid
				+ "]";
	}

}
