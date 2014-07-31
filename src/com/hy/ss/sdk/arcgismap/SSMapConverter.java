package com.hy.ss.sdk.arcgismap;

import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Point;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.SSMapInfo;

public class SSMapConverter {

	public static Point localToMapPoint(SSLocalPoint lp, SSMapInfo info) {
		double lx = lp.getX();
		double ly = lp.getY();

		double mx = (lx / info.getScaleX()) + info.getMapExtent().getXmin();
		double my = (ly / info.getScaleY()) + info.getMapExtent().getYmin();

		return new Point(mx, my);
	}

	public static SSLocalPoint mapPointToLocalPoint(Point mp, SSMapInfo info) {
		double mx = mp.getX();
		double my = mp.getY();

		double lx = (mx - info.getMapExtent().getXmin()) * info.getScaleX();
		double ly = (my - info.getMapExtent().getYmin()) * info.getScaleY();

		return new SSLocalPoint(lx, ly, info.getFloorNumber());
	}

	public static Envelope envelopeFromMapInfo(SSMapInfo info) {
		return new Envelope(info.getMapExtent().getXmin(), info.getMapExtent()
				.getYmin(), info.getMapExtent().getXmax(), info.getMapExtent()
				.getYmax());
	}
}
