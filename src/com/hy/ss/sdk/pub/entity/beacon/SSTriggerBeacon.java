package com.hy.ss.sdk.pub.entity.beacon;

import com.hy.ss.sdk.pub.entity.SSLocalPoint;

public class SSTriggerBeacon extends SSBeacon {

	public SSTriggerBeacon(int major, int minor, String tag) {
		super(major, minor, tag, BeaconType.TRIGGER);
	}

	public SSLocalPoint getLocation() {
		return location;
	}

	@Override
	public String toString() {
		return "SSTriggerBeacon [major=" + major + ", minor=" + minor
				+ ", type=" + type + "]";
	}

}
