package com.hy.ss.sdk.arcgismap;
//package com.hy.ss.arcgismap;
//
//import java.util.List;
//import java.util.Map;
//
//import android.content.Context;
//import android.util.AttributeSet;
//import android.util.Log;
//
//import com.esri.android.map.MapView;
//import com.esri.core.map.Graphic;
//
//public class PMapView extends MapView {
//	private static final String TAG = "PMapView";
//
//	private PMBaseLayer baseLayer;
//	private PMCalibrationLayer calibrationLayer;
//	private PMLabelLayer labelLayer;
//
//	private List<PMapInfo> mapInfoList;
//	private List<PMFloor> floorList;
//	private PMapInfo mCurrentMapInfo;
//	private PMFloor mCurrentFloor;
//	private int index = 0;
//
//	private PMRenderScheme mRenderScheme;
//
//	public PMapView(Context context) {
//		super(context);
//	}
//
//	public PMapView(Context context, AttributeSet attrs) {
//		super(context, attrs);
//	}
//
//	public void loadMap(String placeId, String floor, PMRenderScheme scheme) {
//		this.mRenderScheme = scheme;
//		mapInfoList = PMapManager
//				.loadMapFromFilesByPlace(getContext(), placeId);
//		floorList = PMFloorManager.loadFloorFromFileByPlace(getContext(),
//				placeId);
//
//		if (mapInfoList.size() > 0) {
//			mCurrentMapInfo = mapInfoList.get(0);
//			index = 0;
//
//			String initFloor = floor;
//			if (initFloor == null) {
//				initFloor = "101";
//			}
//
//			for (PMapInfo info : mapInfoList) {
//				if (info.getFloor().equals(initFloor)) {
//					index = mapInfoList.indexOf(info);
//					mCurrentMapInfo = info;
//					mCurrentFloor = floorList.get(index);
//					break;
//				}
//			}
//
//			initLayers();
//		}
//	}
//
//	private void initLayers() {
//		baseLayer = new PMBaseLayer(getContext(), mCurrentMapInfo, this,
//				mRenderScheme);
//		addLayer(baseLayer, 0);
//
//		calibrationLayer = new PMCalibrationLayer(getContext(),
//				mCurrentMapInfo, this);
//		addLayer(calibrationLayer, 1);
//
//		labelLayer = new PMLabelLayer(getContext(), mCurrentMapInfo, this,
//				mRenderScheme);
//		addLayer(labelLayer, 2);
//	}
//
//	public void changeToFloor(String floor) {
//		if (mCurrentMapInfo.getFloor().equals(floor))
//			return;
//
//		int newIndex = 0;
//		for (PMapInfo info : mapInfoList) {
//			if (info.getFloor().equals(floor)) {
//				newIndex = mapInfoList.indexOf(info);
//				break;
//			}
//		}
//
//		changeToFloor(newIndex);
//	}
//
//	public void changeToFloor(int floorIndex) {
//		if (index == floorIndex) {
//			return;
//		}
//
//		index = floorIndex;
//		mCurrentFloor = floorList.get(index);
//		mCurrentMapInfo = mapInfoList.get(index);
//
//		baseLayer.clear();
//		calibrationLayer.clear();
//		labelLayer.clear();
//
//		labelLayer.changeToFloor(mCurrentMapInfo);
//		baseLayer.changeToFloor(mCurrentMapInfo);
//		calibrationLayer.changeToFloor(mCurrentMapInfo);
//
//		labelLayer.setLastResolution(labelLayer.getLastResolution() * 1.0001);
//
//		updateLabels();
//
//	}
//
//	public void changeToFloor(PMapInfo info) {
//		changeToFloor(info.getFloor());
//	}
//
//	public void updateLabels() {
//		if (labelLayer != null) {
//			labelLayer.updateLabels();
//		}
//	}
//
//	public List<PMapInfo> getMapInfos() {
//		return this.mapInfoList;
//	}
//
//	public List<PMFloor> getFloors() {
//		return floorList;
//	}
//
//	public PMapInfo getCurrentMapInfo() {
//		return mCurrentMapInfo;
//	}
//
//	public PMFloor getCurrentFloor() {
//		return mCurrentFloor;
//	}
//
//	// public List<String> getFloorIds() {
//	// List<String> list = new ArrayList<String>();
//	// for (PMapInfo info : mapInfoList) {
//	// list.add(info.getId());
//	// }
//	//
//	// return list;
//	// }
//
//	public int getFloorIndex() {
//		return index;
//	}
//
//	// ====================== overload ===================================
//
//	// @Override
//	// public Layer getLayer(int index) {
//	// return null;
//	// }
//	//
//	// @Override
//	// public Layer getLayerByID(long layerID) {
//	// return null;
//	// }
//	//
//	// @Override
//	// public Layer getLayerByURL(String url) {
//	// return null;
//	// }
//	//
//	// @Override
//	// public Layer[] getLayers() {
//	// return null;
//	// }
//
//	public Graphic getUnreachableGraphic(float x, float y, int tolerance) {
//		return calibrationLayer.getUnreachableGraphic(x, y, tolerance);
//	}
//
//	// public Graphic getCalibrationPathGraphic(float x, float y, int tolerance)
//	// {
//	// return calibrationLayer.getCalibrationPathGraphic(x, y, tolerance);
//	// }
//
//	public Map<Integer, Graphic> getAllCalibrationPaths() {
//		return calibrationLayer.getAllCalibrationPaths();
//	}
//
//	public Graphic getShopFeature(float x, float y, int tolerance) {
//		return baseLayer.getFeature(x, y, tolerance);
//	}
//
//	public void highlightFeature(Graphic g) {
//		Log.i(TAG, "highlightFeature called");
//		baseLayer.highlightFeature(g);
//	}
//
//	// public void highlightGraphics(Graphic[] gs) {
//	// baseLayer.highlightFeatures(gs);
//	// }
//
//	public void clearHighlights() {
//		baseLayer.clearHighlights();
//	}
//
//	public void resetHightlight() {
//		baseLayer.resetHightlight();
//	}
//
//	// public void highlightFeatureForType(int type) {
//	// List<Graphic> gList = baseLayer.getFeaturesForType(type);
//	// Graphic[] gs = new Graphic[gList.size()];
//	// for (int i = 0; i < gList.size(); i++) {
//	// gs[i] = gList.get(i);
//	// }
//	// Log.i(TAG, gs.length + " shops highlighted!");
//	//
//	// baseLayer.highlightFeatures(gs);
//	// }
//	public void highlightFeatureForType(int type) {
//		baseLayer.highlightFeaturesForType(type);
//	}
// }
