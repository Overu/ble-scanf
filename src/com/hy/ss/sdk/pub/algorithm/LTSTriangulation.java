package com.hy.ss.sdk.pub.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Utils;
import com.estimote.sdk.Utils.Proximity;
import com.hy.ss.sdk.pub.entity.SSLocalPoint;
import com.hy.ss.sdk.pub.entity.beacon.SSPublicBeacon;

public class LTSTriangulation {

	private HashMap<Integer, SSPublicBeacon> beaconDictionary = new HashMap<Integer, SSPublicBeacon>();

	private List<Beacon> nearestBeacons = new ArrayList<Beacon>();

	public LTSTriangulation() {
	}

	public LTSTriangulation(HashMap<Integer, SSPublicBeacon> dict) {
		this.beaconDictionary = dict;
	}

	public SSLocalPoint calculateLocation() {
		SSLocalPoint result = null;

		if (nearestBeacons.size() == 1) {
			Beacon beacon = nearestBeacons.get(0);
			Proximity proximity = Utils.computeProximity(beacon);
			if (proximity == Proximity.IMMEDIATE || proximity == Proximity.NEAR) {
				SSPublicBeacon sb = beaconDictionary.get(beacon.getMinor());
				result = sb.getLocation();
			}
		}

		if (nearestBeacons.size() == 2) {
			Beacon b1 = nearestBeacons.get(0);
			Beacon b2 = nearestBeacons.get(1);

			double a1 = Utils.computeAccuracy(b1);
			double a2 = Utils.computeAccuracy(b2);

			SSPublicBeacon sb1 = beaconDictionary.get(b1.getMinor());
			SSPublicBeacon sb2 = beaconDictionary.get(b2.getMinor());

			SSLocalPoint lp1 = sb1.getLocation();
			SSLocalPoint lp2 = sb2.getLocation();

			result = scaleLocalPoint(lp1, a1, lp2, a2);
			result.setFloor(lp1.getFloor());
		}

		if (nearestBeacons.size() >= 3) {
			Beacon b1 = nearestBeacons.get(0);
			Beacon b2 = nearestBeacons.get(1);
			Beacon b3 = nearestBeacons.get(2);

			double a1 = Utils.computeAccuracy(b1);
			double a2 = Utils.computeAccuracy(b2);
			double a3 = Utils.computeAccuracy(b3);

			SSPublicBeacon sb1 = beaconDictionary.get(b1.getMinor());
			SSPublicBeacon sb2 = beaconDictionary.get(b2.getMinor());
			SSPublicBeacon sb3 = beaconDictionary.get(b3.getMinor());

			SSLocalPoint lp1 = sb1.getLocation();
			SSLocalPoint lp2 = sb2.getLocation();
			SSLocalPoint lp3 = sb3.getLocation();

			SSLocalPoint r12 = scaleLocalPoint(lp1, a1, lp2, a2);
			SSLocalPoint r13 = scaleLocalPoint(lp1, a1, lp3, a3);

			result = scaleLocalPoint(r12, a2, r13, a3);
			result.setFloor(lp1.getFloor());
		}

		return result;
	}

	public String checkInShop() {
		if (nearestBeacons.size() < 2) {
			return null;
		}

		Beacon b1 = nearestBeacons.get(0);
		SSPublicBeacon nearestBeacon = beaconDictionary.get(b1.getMinor());
		if (nearestBeacon.getShopGid() == null) {
			return null;
		}

		String shopID = nearestBeacon.getShopGid();

		Beacon b2 = nearestBeacons.get(1);
		SSPublicBeacon secondNearestBeacon = beaconDictionary
				.get(b2.getMinor());

		if (secondNearestBeacon.getShopGid() == null) {
			return null;
		}

		String shopID2 = secondNearestBeacon.getShopGid();

		if (shopID.equalsIgnoreCase(shopID2)) {
			return shopID;
		}

		return null;
	}

	private static SSLocalPoint scaleLocalPoint(SSLocalPoint p1, double a1,
			SSLocalPoint p2, double a2) {
		double sum = a1 + a2;
		double x = (a1 * p2.getX() + a2 * p1.getX()) / sum;
		double y = (a1 * p2.getY() + a2 * p1.getY()) / sum;
		return new SSLocalPoint(x, y, p1.getFloor());
	}

	public HashMap<Integer, SSPublicBeacon> getBeaconDictionary() {
		return beaconDictionary;
	}

	public void setBeaconDictionary(
			HashMap<Integer, SSPublicBeacon> beaconDictionary) {
		this.beaconDictionary = beaconDictionary;
	}

	public void setNearestBeacons(List<Beacon> nearestBeacons) {
		this.nearestBeacons = nearestBeacons;
	}

}
