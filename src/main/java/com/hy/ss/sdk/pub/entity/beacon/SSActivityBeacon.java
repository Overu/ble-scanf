package com.hy.ss.sdk.pub.entity.beacon;


public class SSActivityBeacon extends SSBeacon {
	private String shopGid;
	private double range = DEFAULT_RANGE;

	private static final double DEFAULT_RANGE = 2.0;

	public SSActivityBeacon(int major, int minor, String tag, String gid,
			double r) {
		super(major, minor, tag, BeaconType.ACTIVITY);
		this.shopGid = gid;
		this.range = r;
	}

	public SSActivityBeacon(int major, int minor, String tag, String gid) {
		super(major, minor, tag, BeaconType.ACTIVITY);
		this.shopGid = gid;
	}

	public String getShopGid() {
		return shopGid;
	}

	public void setShopGid(String shopGid) {
		this.shopGid = shopGid;
	}

	public double getRange() {
		return range;
	}

	public void setRange(double range) {
		this.range = range;
	}

	@Override
	public String toString() {
		return "SSActivityBeacon [major=" + major + ", minor=" + minor
				+ ", type=" + type + "]";
	}

}
