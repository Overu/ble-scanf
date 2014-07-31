package com.hy.ss.sdk.pub.entity;

import com.esri.core.geometry.Polygon;

public class SSArea {

	enum TYPE {
		UNKNOWN, SHOP_AREA, MARKET_AREA
	}

	private String marketID;
	private int floor;

	private String areaID;
	private String shopGid;
	private Polygon polygon;
	private TYPE type;

	public String getMarketID() {
		return marketID;
	}

	public void setMarketID(String marketID) {
		this.marketID = marketID;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public String getAreaID() {
		return areaID;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getShopGid() {
		return shopGid;
	}

	public void setShopGid(String shopGid) {
		this.shopGid = shopGid;
	}

	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
	}

	public TYPE getType() {
		return type;
	}

	public void setType(TYPE type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SSArea [marketID=" + marketID + ", floor=" + floor
				+ ", areaID=" + areaID + ", shopGid=" + shopGid + ", type="
				+ type + "]";
	}

}
