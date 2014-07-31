package com.hy.ss.sdk.pub.entity.beacon;

public class SSPublicBeacon extends SSBeacon {

	private String shopGid;

	public SSPublicBeacon(int major, int minor, String tag, String gid) {
		super(major, minor, tag, BeaconType.PUBLIC);
		this.shopGid = gid;
	}

	public String getShopGid() {
		return shopGid;
	}

	public void setShopGid(String shopGid) {
		this.shopGid = shopGid;
	}

	@Override
	public String toString() {
		return "SSPublicBeacon [major=" + major + ", minor=" + minor
				+ ", type=" + type + "]";
	}

}
