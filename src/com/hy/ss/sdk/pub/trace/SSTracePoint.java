package com.hy.ss.sdk.pub.trace;

import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapPoint;
import com.hy.ss.sdk.pub.entity.beacon.SSBeacon;

public class SSTracePoint {
	private SSLocalPoint location;
	private SSMapPoint mapPoint;
	private SSBeacon nearestBeacon;
	private double timestamp;

	SSTracePoint(SSLocalPoint location, SSMapPoint mp, SSBeacon nb,
			double timestamp) {
		this.location = location;
		this.mapPoint = mp;
		this.nearestBeacon = nb;
		this.timestamp = timestamp;
	}

	public static SSTracePoint newTracePoint(SSLocalPoint location,
			SSMapPoint mp, SSBeacon nb) {
		double now = System.currentTimeMillis() / 1000.0;
		return new SSTracePoint(location, mp, nb, now);
	}

	public static SSTracePoint newTracePoint(SSLocalPoint location,
			SSMapPoint mp, SSBeacon nb, double timestamp) {
		return new SSTracePoint(location, mp, nb, timestamp);
	}

	public SSLocalPoint getLocation() {
		return location;
	}

	public double getTimestamp() {
		return timestamp;
	}

	public SSMapPoint getMapPoint() {
		return mapPoint;
	}

	public SSBeacon getNearestBeacon() {
		return nearestBeacon;
	}

	@Override
	public String toString() {
		return "SSTracePoint [location=" + location + ", timestamp="
				+ timestamp + "]";
	}
}
