package com.hy.ss.sdk.pub.entity.beacon;

import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapPoint;

public class SSBeacon {
	protected int major;
	protected int minor;

	protected String tag;
	protected BeaconType type;

	protected SSLocalPoint location;
	protected SSMapPoint mapPoint;

	public SSBeacon(int major, int minor, String tag, BeaconType type) {
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = type;
	}

	public SSBeacon(int major, int minor, String tag) {
		this.major = major;
		this.minor = minor;
		this.tag = tag;
		this.type = BeaconType.UNKNOWN;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public BeaconType getType() {
		return type;
	}

	public void setType(BeaconType type) {
		this.type = type;
	}

	public SSLocalPoint getLocation() {
		return location;
	}

	public void setLocation(SSLocalPoint location) {
		this.location = location;
	}

	public SSMapPoint getMapPoint() {
		return mapPoint;
	}

	public void setMapPoint(SSMapPoint mapPoint) {
		this.mapPoint = mapPoint;
	}

	@Override
	public String toString() {
		return "SSBeacon [major=" + major + ", minor=" + minor + ", type="
				+ type + "]";
	}

	public boolean equalToBeacon(SSBeacon beacon) {
		if (beacon == null) {
			return false;
		}

		return major == beacon.major && minor == beacon.minor
				&& type == beacon.type;
	}

}
