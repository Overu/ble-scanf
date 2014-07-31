package com.hy.ss.sdk.pub.entity;

public class SSFacilityInfo {

	public static final int KEY_FACILITY_TYPE_UNKNOWN = 0;
	public static final int KEY_FACILITY_TYPE_ENTRANCE = 1;
	public static final int KEY_FACILITY_TYPE_TOILET = 2;
	public static final int KEY_FACILITY_TYPE_ELEVATOR = 3;
	public static final int KEY_FACILITY_TYPE_ESCALATOR = 4;
	public static final int KEY_FACILITY_TYPE_QUERY = 5;
	public static final int KEY_FACILITY_TYPE_UNDEFINE = 6;

	private SSLocalPoint location;
	private String name;
	private String gid;
	private int type;

	public SSLocalPoint getLocation() {
		return location;
	}

	public void setLocation(SSLocalPoint location) {
		this.location = location;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SSFacilityInfo [location=" + location + ", name=" + name
				+ ", gid=" + gid + ", type=" + type + "]";
	}

}
