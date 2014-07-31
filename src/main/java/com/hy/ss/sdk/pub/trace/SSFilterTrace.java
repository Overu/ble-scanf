package com.hy.ss.sdk.pub.trace;

import com.hy.ss.sdk.pub.entity.SSLocalPoint;

public class SSFilterTrace extends SSTrace {

	private static final double DEFAULT_FILTER_RANGE = 1.0;
	private static double filterRange = DEFAULT_FILTER_RANGE;

	SSFilterTrace(String marketID, long tag) {
		super(marketID, tag);
	}

	public static SSFilterTrace newFilterTrace(String marketID) {
		long now = System.currentTimeMillis() / 1000;
		return new SSFilterTrace(marketID, now);
	}

	public static SSFilterTrace newFilterTrace(String marketID, long tag) {
		return new SSFilterTrace(marketID, tag);
	}

	public boolean addTracePointToFilter(SSTracePoint tp) {
		if (getLastTracePoint() == null) {
			addTracePoint(tp);
			return true;
		}

		SSTracePoint lastPoint = getLastTracePoint();

		SSLocalPoint lastLocation = lastPoint.getLocation();
		SSLocalPoint currentLocation = tp.getLocation();

		double distance = SSLocalPoint.distanceWithPoints(lastLocation,
				currentLocation);
		if (distance <= filterRange) {
			return false;
		}

		addTracePoint(tp);
		return true;

	}

	public static void setFilterRange(double range) {
		filterRange = range;
	}

	public static double getFilterRange() {
		return filterRange;
	}
}
