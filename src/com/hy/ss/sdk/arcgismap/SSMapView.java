package com.hy.ss.sdk.arcgismap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.renderer.UniqueValue;
import com.esri.core.renderer.UniqueValueRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.hy.ss.sdk.pub.data.SSFileManager;
import com.hy.ss.sdk.pub.entity.SSMapInfo;

public class SSMapView extends MapView {
	// private static final String TAG = (SSMapView.class.getSimpleName());

	private GraphicsLayer floorLayer;
	private GraphicsLayer areaLayer;
	private GraphicsLayer shopLayer;
	private GraphicsLayer facilityLayer;
	private GraphicsLayer labelLayer;

	private SimpleRenderer floorRender;
	private UniqueValueRenderer areaRender;
	private UniqueValueRenderer shopRender;
	private SimpleRenderer facilityRender;
	// private SSLabelRender labelRender;

	private SSMapInfo currentMapInfo;

	public void setFloor(SSMapInfo info) {
		currentMapInfo = info;

		initLayers();

		addFloorContent(info.getMapID());

		addAreaContent(info.getMapID());

		addShopContent(info.getMapID());

		addFacilityContent(info.getMapID());
		addShopLabel();
	}

	public void setLabelLayerVisible(boolean visiable) {
	}

	public SSMapInfo getCurrentMapInfo() {
		return currentMapInfo;
	}

	public SSMapView(Context context) {
		super(context);
	}

	public SSMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private void initLayers() {
		if (floorLayer == null) {
			floorLayer = new GraphicsLayer(SpatialReference.create(4326),
					SSMapConverter.envelopeFromMapInfo(currentMapInfo));

			// super(SpatialReference.create(4326), new Envelope(info.getXMin(),
			// info.getYMin(), info.getXMax(), info.getYMax()));

			SimpleLineSymbol outlineSymbol = new SimpleLineSymbol(
					SSRenderColor.getOutlineColor(), 1);
			SimpleFillSymbol floorSFS = new SimpleFillSymbol(
					SSRenderColor.getFloorColor1());
			floorSFS.setOutline(outlineSymbol);

			floorRender = new SimpleRenderer(floorSFS);
			floorLayer.setRenderer(floorRender);
			addLayer(floorLayer);
		} else {
			floorLayer.removeAll();
		}

		if (areaLayer == null) {
			areaLayer = new GraphicsLayer();
			areaRender = new UniqueValueRenderer();

			SimpleLineSymbol outlineSymbol = new SimpleLineSymbol(
					SSRenderColor.getOutlineColor(), 1);
			List<UniqueValue> areaUVs = new ArrayList<UniqueValue>();
			Map<Integer, Integer> areaColors = SSRenderColor
					.getAreaRenderColor();
			for (Integer type : areaColors.keySet()) {
				int color = areaColors.get(type);
				SimpleFillSymbol areaSFS = new SimpleFillSymbol(color);
				areaSFS.setOutline(outlineSymbol);
				UniqueValue uv = new UniqueValue();
				uv.setSymbol(areaSFS);
				uv.setValue(new Integer[] { type });
				areaUVs.add(uv);
			}
			areaRender.setField1("type");
			areaRender.setUniqueValueInfos(areaUVs);

			areaLayer.setRenderer(areaRender);
			addLayer(areaLayer);
		} else {
			areaLayer.removeAll();
		}

		if (shopLayer == null) {
			shopLayer = new GraphicsLayer();
			shopRender = new UniqueValueRenderer();

			SimpleLineSymbol outlineSymbol = new SimpleLineSymbol(
					SSRenderColor.getOutlineColor(), 1);
			List<UniqueValue> shopUVs = new ArrayList<UniqueValue>();
			Map<Integer, Integer> shopColors = SSRenderColor
					.getShopRenderColor();
			for (Integer type : shopColors.keySet()) {
				int color = shopColors.get(type);
				SimpleFillSymbol shopSFS = new SimpleFillSymbol(color);
				shopSFS.setOutline(outlineSymbol);
				UniqueValue uv = new UniqueValue();
				uv.setSymbol(shopSFS);
				uv.setValue(new Integer[] { type });
				shopUVs.add(uv);
			}
			shopRender.setField1("type");
			shopRender.setUniqueValueInfos(shopUVs);

			shopLayer.setRenderer(shopRender);
			addLayer(shopLayer);

		} else {
			shopLayer.removeAll();
		}

		if (facilityLayer == null) {
			facilityLayer = new GraphicsLayer();

			SimpleFillSymbol sfs = new SimpleFillSymbol(Color.TRANSPARENT);
			SimpleLineSymbol sls = new SimpleLineSymbol(Color.TRANSPARENT, 0);
			sfs.setOutline(sls);

			facilityRender = new SimpleRenderer(sfs);
			facilityLayer.setRenderer(facilityRender);
			addLayer(facilityLayer);

		} else {
			facilityLayer.removeAll();
		}

		if (labelLayer == null) {
			labelLayer = new GraphicsLayer();
			// labelRender = new SSLabelRender();
			addLayer(labelLayer);
		} else {
			labelLayer.removeAll();
		}
	}

	private void addFloorContent(String floorname) {
		String filename = SSFileManager.getFloorFilePath(floorname);
		byte[] bytes = SSFileManager.readByteFromFile(filename);

		JsonFactory factory = new JsonFactory();
		try {
			JsonParser parser = factory.createJsonParser(bytes);

			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();

			if (graphics.length > 0) {
				floorLayer.addGraphic(new Graphic(graphics[0].getGeometry(),
						new SimpleFillSymbol(Color.RED)));
			}

			floorLayer.addGraphics(graphics);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void addAreaContent(String areaname) {
		String filename = SSFileManager.getShopFilePath(areaname);
		byte[] bytes = SSFileManager.readByteFromFile(filename);

		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(bytes);
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			areaLayer.addGraphics(graphics);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addShopContent(String shopname) {
		String filename = SSFileManager.getShopFilePath(shopname);
		byte[] bytes = SSFileManager.readByteFromFile(filename);

		JsonFactory factory = new JsonFactory();

		try {
			JsonParser parser = factory.createJsonParser(bytes);
			FeatureSet set = FeatureSet.fromJson(parser);

			Graphic[] graphics = set.getGraphics();
			Graphic[] shopGraphics = new Graphic[graphics.length];

			int index = 0;
			for (Graphic g : graphics) {
				int objectID = (Integer) g.getAttributeValue("OBJECTID");
				int type = objectID % 10;

				Map<String, Object> attr = new HashMap<String, Object>();
				attr.put("gid", g.getAttributeValue("gid"));
				attr.put("type", type);

				Graphic shopG = new Graphic(g.getGeometry(), null, attr);
				shopGraphics[index++] = shopG;
			}
			shopLayer.addGraphics(shopGraphics);

		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addFacilityContent(String facilityname) {

	}

	private void addShopLabel() {

	}

	public String getShopGid(float x, float y, int tolerance) {
		int[] ids = shopLayer.getGraphicIDs(x, y, tolerance);

		if (ids != null && ids.length > 0) {
			int gid = ids[0];
			Graphic g = shopLayer.getGraphic(gid);
			return (String) g.getAttributeValue("gid");
		}
		return null;
	}

}
