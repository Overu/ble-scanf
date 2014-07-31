package com.hy.ss.sdk.pub.trace;

import java.util.ArrayList;
import java.util.List;

public class SSTrace {

	protected String marketID;
	protected long tag;
	protected List<SSTracePoint> tracePointList;

	SSTrace(String marketID, long tag) {
		this.marketID = marketID;
		this.tag = tag;
		tracePointList = new ArrayList<SSTracePoint>();
	}

	public static SSTrace newTrace(String marketID) {
		long now = System.currentTimeMillis() / 1000;
		return new SSTrace(marketID, now);
	}

	public static SSTrace newTrace(String marketID, long tag) {
		return new SSTrace(marketID, tag);
	}

	public void addTracePoint(SSTracePoint tp) {
		tracePointList.add(tp);
	}

	public SSTracePoint getTracePoint(int index) {
		if (index < 0 || index >= tracePointList.size()) {
			return null;
		}
		return tracePointList.get(index);
	}

	public SSTracePoint getLastTracePoint() {
		if (tracePointList.size() == 0) {
			return null;
		}
		return tracePointList.get(tracePointList.size() - 1);
	}

	public int getCount() {
		return tracePointList.size();
	}

	public long getTag() {
		return tag;
	}

	public String getMarketID() {
		return marketID;
	}

	@Override
	public String toString() {
		return "SSTrace [marketID=" + marketID + ", tag=" + tag + "]";
	}

}
