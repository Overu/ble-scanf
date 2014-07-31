package com.hy.ss.sdk.arcgismap;

import java.util.List;

import android.graphics.Color;

import com.esri.android.map.GraphicsLayer;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol.STYLE;

public class ArcGISDrawer {

	public static void drawPoint(Point p, GraphicsLayer layer, int color) {
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(color, 5, STYLE.CIRCLE);
		sms.setOutline(new SimpleLineSymbol(Color.BLACK, 1.0f));
		layer.addGraphic(new Graphic(p, sms));
	}

	public static void drawPoint(Point p, GraphicsLayer layer, int color,
			int size) {
		SimpleMarkerSymbol sms = new SimpleMarkerSymbol(color, size,
				STYLE.CIRCLE);
		sms.setOutline(new SimpleLineSymbol(Color.BLACK, 1.0f));
		layer.addGraphic(new Graphic(p, sms));
	}

	public static void drawLine(Point start, Point end, GraphicsLayer layer,
			int color, float width) {
		Line line = new Line();
		line.setStart(start);
		line.setEnd(end);

		SimpleLineSymbol sls = new SimpleLineSymbol(color, width);
		layer.addGraphic(new Graphic(line, sls));
	}

	public static void drawPolygon(List<Point> points, GraphicsLayer layer,
			int color) {

	}
}
