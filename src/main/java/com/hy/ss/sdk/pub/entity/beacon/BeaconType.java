package com.hy.ss.sdk.pub.entity.beacon;

public enum BeaconType {
	UNKNOWN(0, "Unknown"), PUBLIC(1, "Public"), TRIGGER(2, "Trigger"), ACTIVITY(
			3, "Activity");

	private BeaconType(int t, String description) {
		this.type = t;
		this.description = description;
	}

	private int type;
	private String description;

	public int getType() {
		return type;
	}

	public String getDescripton() {
		return description;
	}
}
