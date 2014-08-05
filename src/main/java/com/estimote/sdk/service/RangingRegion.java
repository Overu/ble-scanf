package com.estimote.sdk.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Messenger;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.PMovingAverageTD;
import com.estimote.sdk.Region;
import com.estimote.sdk.Utils;

class RangingRegion {
	private static final String TAG = (RangingRegion.class.getSimpleName());

	private static final Comparator<Beacon> BEACON_ACCURACY_COMPARATOR = new Comparator<Beacon>() {
		public int compare(Beacon lhs, Beacon rhs) {
			return Double.compare(Utils.computeAccuracy(lhs),
					Utils.computeAccuracy(rhs));
		}

	};
	private final ConcurrentHashMap<Beacon, Long> beacons;
	private ConcurrentHashMap<Beacon, PMovingAverageTD> beaconsRssi;
	final Region region;
	final Messenger replyTo;

	RangingRegion(Region region, Messenger replyTo) {
		this.region = region;
		this.replyTo = replyTo;
		this.beacons = new ConcurrentHashMap<Beacon, Long>();
		this.beaconsRssi = new ConcurrentHashMap<Beacon, PMovingAverageTD>();
	}

	public final Collection<Beacon> getSortedBeacons() {
    ArrayList<Beacon> sortedBeacons = new ArrayList<Beacon>(
				this.beacons.keySet());
		Collections.sort(sortedBeacons, BEACON_ACCURACY_COMPARATOR);

		List<Beacon> averageBeacons = new ArrayList<Beacon>();
		for (Beacon beacon : sortedBeacons) {
			if (beaconsRssi.contains(beacon)) {
				Beacon b = new Beacon(beacon.getProximityUUID(),
						beacon.getName(), beacon.getMacAddress(),
						beacon.getMajor(), beacon.getMinor(),
						beacon.getMeasuredPower(),
						(int) (beaconsRssi.get(beacon).getAverage()));
				averageBeacons.add(b);
			} else {
				averageBeacons.add(beacon);
			}
		}
		return averageBeacons;
		// return sortedBeacons;
	}

	public final void processFoundBeacons(
			Map<Beacon, Long> beaconsFoundInScanCycle,
			ConcurrentHashMap<Beacon, PMovingAverageTD> averageRssi) {
		beaconsRssi = averageRssi;
		for (Entry<Beacon, Long> entry : beaconsFoundInScanCycle.entrySet()) {
			if (Utils.isBeaconInRegion((Beacon) entry.getKey(), this.region)) {
				this.beacons.remove(entry.getKey());
				this.beacons.put(entry.getKey(), entry.getValue());
			}
		}
	}

	public final void removeNotSeenBeacons(long currentTimeMillis) {
		Iterator<Entry<Beacon, Long>> iterator = this.beacons.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Entry<Beacon, Long> entry = (Entry<Beacon, Long>) iterator.next();
			if (currentTimeMillis - ((Long) entry.getValue()).longValue() > BeaconService.EXPIRATION_MILLIS) {
				Log.v(TAG, "Not seen lately: " + entry.getKey());
				iterator.remove();
			}
		}
	}

}
