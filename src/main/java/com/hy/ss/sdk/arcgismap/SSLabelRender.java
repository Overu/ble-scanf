package com.hy.ss.sdk.arcgismap;

import android.graphics.Color;

import com.esri.core.map.Feature;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;

public class SSLabelRender {

	public SSLabelRender() {
		super();
	}

	public Symbol symbolForShopFeature(Feature f) {
		String shopName = (String) f.getAttributeValue("name");
		TextSymbol ts = new TextSymbol(10, shopName, Color.BLACK);
		return ts;
	}
}
