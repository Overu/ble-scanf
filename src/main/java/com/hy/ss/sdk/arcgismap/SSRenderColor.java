package com.hy.ss.sdk.arcgismap;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;

public class SSRenderColor {

	private static Map<Integer, Integer> shopRenderColor;
	private static Map<Integer, Integer> areaRenderColor;

	private static int outlineColor = 0;
	private static int floorColor1 = 0;
	private static int floorColor2 = 0;

	public static Map<Integer, Integer> getShopRenderColor() {
		if (shopRenderColor == null) {
			shopRenderColor = new HashMap<Integer, Integer>();

			int c0 = Color.argb(255, 184, 185, 252);
			int c1 = Color.argb(255, 252, 252, 179);
			int c2 = Color.argb(255, 179, 239, 252);

			int c3 = Color.argb(255, 212, 230, 252);
			int c4 = Color.argb(255, 252, 187, 235);
			int c5 = Color.argb(255, 225, 187, 252);

			int c6 = Color.argb(69, 126, 189, 240);
			int c7 = Color.argb(255, 252, 214, 182);
			int c8 = Color.argb(255, 207, 252, 218);

			int c9 = Color.argb(255, 245, 243, 240);

			shopRenderColor.put(0, c0);
			shopRenderColor.put(1, c1);
			shopRenderColor.put(2, c2);
			shopRenderColor.put(3, c3);
			shopRenderColor.put(4, c4);
			shopRenderColor.put(5, c5);
			shopRenderColor.put(6, c6);
			shopRenderColor.put(7, c7);
			shopRenderColor.put(8, c8);
			shopRenderColor.put(9, c9);

		}
		return shopRenderColor;
	}

	public static Map<Integer, Integer> getAreaRenderColor() {
		if (areaRenderColor == null) {
			areaRenderColor = new HashMap<Integer, Integer>();

			int c1 = Color.argb(255, 167, 167, 167);
			int c2 = Color.argb(255, 232, 188, 188);

			areaRenderColor.put(1, c1);
			areaRenderColor.put(2, c2);
		}
		return areaRenderColor;
	}

	public static int getFloorColor1() {
		if (floorColor1 == 0) {
			floorColor1 = Color.argb(255, 245, 243, 240);
		}
		return floorColor1;
	}

	public static int getFloorColor2() {
		if (floorColor2 == 0) {
			floorColor2 = Color.argb(255, 211, 211, 211);
		}
		return floorColor2;
	}

	public static int getOutlineColor() {
		if (outlineColor == 0) {
			outlineColor = Color.argb(255, 136, 136, 136);
		}
		return outlineColor;
	}
}
