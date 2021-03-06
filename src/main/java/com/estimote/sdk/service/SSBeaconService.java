//package com.estimote.sdk.service;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map.Entry;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.TimeUnit;
//
//import android.app.AlarmManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.bluetooth.BluetoothAdapter;
//import android.bluetooth.BluetoothDevice;
//import android.bluetooth.BluetoothManager;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Handler;
//import android.os.HandlerThread;
//import android.os.IBinder;
//import android.os.Looper;
//import android.os.Message;
//import android.os.Messenger;
//import android.os.RemoteException;
//import android.os.SystemClock;
//import android.util.Log;
//
//import com.estimote.sdk.Beacon;
//import com.estimote.sdk.Region;
//import com.estimote.sdk.Utils;
//import com.estimote.sdk.internal.Preconditions;
//import com.estimote.sdk.utils.EstimoteBeacons;
//
//public class SSBeaconService extends Service {
//	private static final String TAG = (SSBeaconService.class.getSimpleName());
//	public static final int MSG_START_RANGING = 1;
//	public static final int MSG_STOP_RANGING = 2;
//	public static final int MSG_RANGING_RESPONSE = 3;
//	public static final int MSG_START_MONITORING = 4;
//	public static final int MSG_STOP_MONITORING = 5;
//	public static final int MSG_MONITORING_RESPONSE = 6;
//	public static final int MSG_REGISTER_ERROR_LISTENER = 7;
//	public static final int MSG_ERROR_RESPONSE = 8;
//	public static final int MSG_SET_FOREGROUND_SCAN_PERIOD = 9;
//	public static final int MSG_SET_BACKGROUND_SCAN_PERIOD = 10;
//
//	public static final int ERROR_COULD_NOT_START_LOW_ENERGY_SCANNING = -1;
//	static final long EXPIRATION_MILLIS = TimeUnit.SECONDS.toMillis(10L);
//
//	public static final String SCAN_START_ACTION_NAME = "startScan";
//	public static final String AFTER_SCAN_ACTION_NAME = "afterScan";
//	private static final Intent SCAN_START_INTENT = new Intent("startScan");
//	private static final Intent AFTER_SCAN_INTENT = new Intent("afterScan");
//
//	private final Messenger messenger;
//	private final BluetoothAdapter.LeScanCallback leScanCallback;
//	private final ConcurrentHashMap<Beacon, Long> beaconsFoundInScanCycle;
//	private final List<RangingRegion> rangedRegions;
//	private final List<MonitoringRegion> monitoredRegions;
//	private BluetoothAdapter adapter;
//	private AlarmManager alarmManager;
//	private HandlerThread handlerThread;
//	private Handler handler;
//	private Runnable afterScanCycleTask;
//	private boolean scanning;
//	private Messenger errorReplyTo;
//	private BroadcastReceiver bluetoothBroadcastReceiver;
//	private BroadcastReceiver scanStartBroadcastReceiver;
//	private PendingIntent scanStartBroadcastPendingIntent;
//	private BroadcastReceiver afterScanBroadcastReceiver;
//	private PendingIntent afterScanBroadcastPendingIntent;
//	private ScanPeriodData foregroundScanPeriod;
//	private ScanPeriodData backgroundScanPeriod;
//
//	public SSBeaconService() {
//		this.messenger = new Messenger(new IncomingHandler());
//		this.leScanCallback = new InternalLeScanCallback();
//
//		this.beaconsFoundInScanCycle = new ConcurrentHashMap<Beacon, Long>();
//
//		this.rangedRegions = new ArrayList<RangingRegion>();
//
//		this.monitoredRegions = new ArrayList<MonitoringRegion>();
//
//		this.foregroundScanPeriod = new ScanPeriodData(
//				TimeUnit.SECONDS.toMillis(1L), TimeUnit.SECONDS.toMillis(0L));
//
//		this.backgroundScanPeriod = new ScanPeriodData(
//				TimeUnit.SECONDS.toMillis(5L), TimeUnit.SECONDS.toMillis(30L));
//	}
//
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
//		Log.i(TAG, "Service Started");
//
//		return super.onStartCommand(intent, flags, startId);
//	}
//
//	public void onCreate() {
//		super.onCreate();
//		// L.i("Creating service");
//		Log.i(TAG, "Service Created");
//
//		this.alarmManager = ((AlarmManager) getSystemService("alarm"));
//		BluetoothManager bluetoothManager = (BluetoothManager) getSystemService("bluetooth");
//		this.adapter = bluetoothManager.getAdapter();
//		this.afterScanCycleTask = new AfterScanCycleTask();
//
//		this.handlerThread = new HandlerThread("BeaconServiceThread", 10);
//		this.handlerThread.start();
//		this.handler = new Handler(this.handlerThread.getLooper());
//
//		this.bluetoothBroadcastReceiver = createBluetoothBroadcastReceiver();
//		this.scanStartBroadcastReceiver = createScanStartBroadcastReceiver();
//		this.afterScanBroadcastReceiver = createAfterScanBroadcastReceiver();
//		registerReceiver(this.bluetoothBroadcastReceiver, new IntentFilter(
//				"android.bluetooth.adapter.action.STATE_CHANGED"));
//		registerReceiver(this.scanStartBroadcastReceiver, new IntentFilter(
//				"startScan"));
//		registerReceiver(this.afterScanBroadcastReceiver, new IntentFilter(
//				"afterScan"));
//		this.afterScanBroadcastPendingIntent = PendingIntent.getBroadcast(
//				getApplicationContext(), 0, AFTER_SCAN_INTENT, 0);
//		this.scanStartBroadcastPendingIntent = PendingIntent.getBroadcast(
//				getApplicationContext(), 0, SCAN_START_INTENT, 0);
//	}
//
//	public void onDestroy() {
//		// L.i("Service destroyed");
//		Log.i(TAG, "Service Destroyed");
//		unregisterReceiver(this.bluetoothBroadcastReceiver);
//		unregisterReceiver(this.scanStartBroadcastReceiver);
//		unregisterReceiver(this.afterScanBroadcastReceiver);
//
//		if (this.adapter != null) {
//			stopScanning();
//		}
//
//		removeAfterScanCycleCallback();
//		this.handlerThread.quit();
//
//		super.onDestroy();
//	}
//
//	public IBinder onBind(Intent intent) {
//		Log.i(TAG, "onBind");
//		return this.messenger.getBinder();
//	}
//
//	private void startRanging(RangingRegion rangingRegion) {
//		checkNotOnUiThread();
//		Log.v(TAG, "Start ranging: " + rangingRegion.region);
//		Preconditions.checkNotNull(this.adapter,
//				"Bluetooth adapter cannot be null");
//		this.rangedRegions.add(rangingRegion);
//		startScanning();
//	}
//
//	private void stopRanging(String regionId) {
//		Log.v(TAG, "Stopping ranging: " + regionId);
//		checkNotOnUiThread();
//		Iterator<RangingRegion> iterator = this.rangedRegions.iterator();
//		while (iterator.hasNext()) {
//			RangingRegion rangingRegion = (RangingRegion) iterator.next();
//			if (regionId.equals(rangingRegion.region.getIdentifier())) {
//				iterator.remove();
//			}
//		}
//		if ((this.rangedRegions.isEmpty()) && (this.monitoredRegions.isEmpty())) {
//			removeAfterScanCycleCallback();
//			stopScanning();
//			this.beaconsFoundInScanCycle.clear();
//		}
//	}
//
//	public void startMonitoring(MonitoringRegion monitoringRegion) {
//		checkNotOnUiThread();
//		Log.v(TAG, "Starting monitoring: " + monitoringRegion.region);
//		Preconditions.checkNotNull(this.adapter,
//				"Bluetooth adapter cannot be null");
//		this.monitoredRegions.add(monitoringRegion);
//		startScanning();
//	}
//
//	public void stopMonitoring(String regionId) {
//		Log.v(TAG, "Stopping monitoring: " + regionId);
//		checkNotOnUiThread();
//		Iterator<MonitoringRegion> iterator = this.monitoredRegions.iterator();
//		while (iterator.hasNext()) {
//			MonitoringRegion monitoringRegion = (MonitoringRegion) iterator
//					.next();
//			if (regionId.equals(monitoringRegion.region.getIdentifier())) {
//				iterator.remove();
//			}
//		}
//		if ((this.monitoredRegions.isEmpty()) && (this.rangedRegions.isEmpty())) {
//			removeAfterScanCycleCallback();
//			stopScanning();
//			this.beaconsFoundInScanCycle.clear();
//		}
//	}
//
//	private void startScanning() {
//		if (this.scanning) {
//			Log.d(TAG, "Scanning already in progress, not starting one more");
//			return;
//		}
//		if ((this.monitoredRegions.isEmpty()) && (this.rangedRegions.isEmpty())) {
//			Log.d(TAG, "Not starting scanning, no monitored on ranged regions");
//			return;
//		}
//		if (!this.adapter.isEnabled()) {
//			Log.d(TAG, "Bluetooth is disabled, not starting scanning");
//			return;
//		}
//		if (!this.adapter.startLeScan(this.leScanCallback)) {
//			Log.wtf(TAG, "Bluetooth adapter did not start le scan");
//			sendError(Integer.valueOf(-1));
//			return;
//		}
//		this.scanning = true;
//		removeAfterScanCycleCallback();
//		setAlarm(this.afterScanBroadcastPendingIntent, scanPeriodTimeMillis());
//	}
//
//	private void stopScanning() {
//		try {
//			this.scanning = false;
//			this.adapter.stopLeScan(this.leScanCallback);
//		} catch (Exception e) {
//			Log.wtf(TAG, "BluetoothAdapter throws unexpected exception", e);
//		}
//	}
//
//	private void sendError(Integer errorId) {
//		if (this.errorReplyTo != null) {
//			Message errorMsg = Message.obtain(null, 8);
//			errorMsg.obj = errorId;
//			try {
//				this.errorReplyTo.send(errorMsg);
//			} catch (RemoteException e) {
//				Log.e(TAG, "Error while reporting message, funny right?", e);
//			}
//		}
//	}
//
//	private long scanPeriodTimeMillis() {
//		if (!this.rangedRegions.isEmpty()) {
//			return this.foregroundScanPeriod.scanPeriodMillis;
//		}
//		return this.backgroundScanPeriod.scanPeriodMillis;
//	}
//
//	private long scanWaitTimeMillis() {
//		if (!this.rangedRegions.isEmpty()) {
//			return this.foregroundScanPeriod.waitTimeMillis;
//		}
//		return this.backgroundScanPeriod.waitTimeMillis;
//	}
//
//	private void setAlarm(PendingIntent pendingIntent, long delayMillis) {
//		this.alarmManager.set(2, SystemClock.elapsedRealtime() + delayMillis,
//				pendingIntent);
//	}
//
//	private void checkNotOnUiThread() {
//		Preconditions
//				.checkArgument(Looper.getMainLooper().getThread() != Thread
//						.currentThread(),
//						"This cannot be run on UI thread, starting BLE scan can be expensive");
//
//		Preconditions.checkNotNull(Boolean.valueOf(this.handlerThread
//				.getLooper() == Looper.myLooper()),
//				"It must be executed on service's handlerThread");
//	}
//
//	private BroadcastReceiver createBluetoothBroadcastReceiver() {
//		return new BroadcastReceiver() {
//			public void onReceive(Context context, Intent intent) {
//				if ("android.bluetooth.adapter.action.STATE_CHANGED"
//						.equals(intent.getAction())) {
//					int state = intent.getIntExtra(
//							"android.bluetooth.adapter.extra.STATE", -1);
//					if (state == 10)
//						SSBeaconService.this.handler.post(new Runnable() {
//							public void run() {
//								Log.i(TAG,
//										"Bluetooth is OFF: stopping scanning");
//								SSBeaconService.this
//										.removeAfterScanCycleCallback();
//								SSBeaconService.this.stopScanning();
//								SSBeaconService.this.beaconsFoundInScanCycle
//										.clear();
//							}
//
//						});
//					else if (state == 12)
//						SSBeaconService.this.handler.post(new Runnable() {
//							public void run() {
//								if ((!SSBeaconService.this.monitoredRegions
//										.isEmpty())
//										|| (!SSBeaconService.this.rangedRegions
//												.isEmpty())) {
//									Log.i(TAG,
//											String.format(
//													"Bluetooth is ON: resuming scanning (monitoring: %d ranging:%d)",
//													new Object[] {
//															Integer.valueOf(SSBeaconService.this.monitoredRegions
//																	.size()),
//															Integer.valueOf(SSBeaconService.this.rangedRegions
//																	.size()) }));
//
//									SSBeaconService.this.startScanning();
//								}
//							}
//
//						});
//				}
//			}
//
//		};
//	}
//
//	private void removeAfterScanCycleCallback() {
//		this.handler.removeCallbacks(this.afterScanCycleTask);
//		this.alarmManager.cancel(this.afterScanBroadcastPendingIntent);
//		this.alarmManager.cancel(this.scanStartBroadcastPendingIntent);
//	}
//
//	private BroadcastReceiver createAfterScanBroadcastReceiver() {
//		return new BroadcastReceiver() {
//			public void onReceive(Context context, Intent intent) {
//				SSBeaconService.this.handler
//						.post(SSBeaconService.this.afterScanCycleTask);
//			}
//
//		};
//	}
//
//	private BroadcastReceiver createScanStartBroadcastReceiver() {
//		return new BroadcastReceiver() {
//			public void onReceive(Context context, Intent intent) {
//				SSBeaconService.this.handler.post(new Runnable() {
//					public void run() {
//						SSBeaconService.this.startScanning();
//					}
//				});
//			}
//		};
//	}
//
//	private class InternalLeScanCallback implements
//			BluetoothAdapter.LeScanCallback {
//		private InternalLeScanCallback() {
//		}
//
//		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//			SSBeaconService.this.checkNotOnUiThread();
//			Beacon beacon = Utils.beaconFromLeScan(device, rssi, scanRecord);
//			Log.i(TAG, "Beacon " + beacon);
//			Log.i(TAG, "Beacon name: " + beacon.getName());
//			Log.i(TAG, beacon.getProximityUUID());
//
//			if ((beacon == null) || (!EstimoteBeacons.isEstimoteBeacon(beacon))) {
//				Log.i(TAG, "Device " + device + " is not an Estimote beacon");
//				return;
//			}
//			SSBeaconService.this.beaconsFoundInScanCycle.put(beacon,
//					Long.valueOf(System.currentTimeMillis()));
//		}
//
//	}
//
//	private class IncomingHandler extends Handler {
//		private IncomingHandler() {
//		}
//
//		public void handleMessage(Message msg) {
//			final int what = msg.what;
//			final Object obj = msg.obj;
//			final Messenger replyTo = msg.replyTo;
//			SSBeaconService.this.handler.post(new Runnable() {
//				public void run() {
//					switch (what) {
//					case 1:
//						RangingRegion rangingRegion = new RangingRegion(
//								(Region) obj, replyTo);
//						SSBeaconService.this.startRanging(rangingRegion);
//						break;
//					case 2:
//						String rangingRegionId = (String) obj;
//						SSBeaconService.this.stopRanging(rangingRegionId);
//						break;
//					case 4:
//						MonitoringRegion monitoringRegion = new MonitoringRegion(
//								(Region) obj, replyTo);
//						SSBeaconService.this.startMonitoring(monitoringRegion);
//						break;
//					case 5:
//						String monitoredRegionId = (String) obj;
//						SSBeaconService.this.stopMonitoring(monitoredRegionId);
//						break;
//					case 7:
//						SSBeaconService.this.errorReplyTo = replyTo;
//						break;
//					case 9:
//						Log.d(TAG, "Setting foreground scan period: "
//								+ SSBeaconService.this.foregroundScanPeriod);
//						SSBeaconService.this.foregroundScanPeriod = ((ScanPeriodData) obj);
//						break;
//					case 10:
//						Log.d(TAG, "Setting background scan period: "
//								+ SSBeaconService.this.backgroundScanPeriod);
//						SSBeaconService.this.backgroundScanPeriod = ((ScanPeriodData) obj);
//						break;
//					case 3:
//
//					case 6:
//
//					case 8:
//
//					default:
//						Log.d(TAG, "Unknown message: what=" + what + " obj="
//								+ obj);
//					}
//				}
//
//			});
//		}
//
//	}
//
//	private class AfterScanCycleTask implements Runnable {
//		private AfterScanCycleTask() {
//		}
//
//		private void processRanging() {
//			for (RangingRegion rangedRegion : SSBeaconService.this.rangedRegions)
//				rangedRegion
//						.processFoundBeacons(SSBeaconService.this.beaconsFoundInScanCycle);
//		}
//
//		private List<MonitoringRegion> findEnteredRegions(long currentTimeMillis) {
//			List<MonitoringRegion> didEnterRegions = new ArrayList<MonitoringRegion>();
//			for (Entry<Beacon, Long> entry : SSBeaconService.this.beaconsFoundInScanCycle
//					.entrySet()) {
//				for (MonitoringRegion monitoringRegion : matchingMonitoredRegions((Beacon) entry
//						.getKey())) {
//					monitoringRegion
//							.processFoundBeacons(SSBeaconService.this.beaconsFoundInScanCycle);
//					if (monitoringRegion.markAsSeen(currentTimeMillis)) {
//						didEnterRegions.add(monitoringRegion);
//					}
//				}
//			}
//			return didEnterRegions;
//		}
//
//		private List<MonitoringRegion> matchingMonitoredRegions(Beacon beacon) {
//			List<MonitoringRegion> results = new ArrayList<MonitoringRegion>();
//			for (MonitoringRegion monitoredRegion : SSBeaconService.this.monitoredRegions) {
//				if (Utils.isBeaconInRegion(beacon, monitoredRegion.region)) {
//					results.add(monitoredRegion);
//				}
//			}
//			return results;
//		}
//
//		private void removeNotSeenBeacons(long currentTimeMillis) {
//			for (RangingRegion rangedRegion : SSBeaconService.this.rangedRegions) {
//				rangedRegion.removeNotSeenBeacons(currentTimeMillis);
//			}
//			for (MonitoringRegion monitoredRegion : SSBeaconService.this.monitoredRegions)
//				monitoredRegion.removeNotSeenBeacons(currentTimeMillis);
//		}
//
//		private List<MonitoringRegion> findExitedRegions(long currentTimeMillis) {
//			List<MonitoringRegion> didExitMonitors = new ArrayList<MonitoringRegion>();
//			for (MonitoringRegion monitoredRegion : SSBeaconService.this.monitoredRegions) {
//				if (monitoredRegion.didJustExit(currentTimeMillis)) {
//					didExitMonitors.add(monitoredRegion);
//				}
//			}
//			return didExitMonitors;
//		}
//
//		private void invokeCallbacks(List<MonitoringRegion> enteredMonitors,
//				List<MonitoringRegion> exitedMonitors) {
//			for (RangingRegion rangingRegion : SSBeaconService.this.rangedRegions) {
//				try {
//					Message rangingResponseMsg = Message.obtain(null, 3);
//					rangingResponseMsg.obj = new RangingResult(
//							rangingRegion.region,
//							rangingRegion.getSortedBeacons());
//					rangingRegion.replyTo.send(rangingResponseMsg);
//				} catch (RemoteException e) {
//					Log.e(TAG, "Error while delivering responses", e);
//				}
//			}
//			for (MonitoringRegion didEnterMonitor : enteredMonitors) {
//				Message monitoringResponseMsg = Message.obtain(null, 6);
//				monitoringResponseMsg.obj = new MonitoringResult(
//						didEnterMonitor.region, Region.State.INSIDE,
//						didEnterMonitor.getSortedBeacons());
//				try {
//					didEnterMonitor.replyTo.send(monitoringResponseMsg);
//				} catch (RemoteException e) {
//					Log.e(TAG, "Error while delivering responses", e);
//				}
//			}
//			for (MonitoringRegion didEnterMonitor : exitedMonitors) {
//				Message monitoringResponseMsg = Message.obtain(null, 6);
//				monitoringResponseMsg.obj = new MonitoringResult(
//						didEnterMonitor.region, Region.State.OUTSIDE,
//						Collections.<Beacon> emptyList());
//
//				try {
//					didEnterMonitor.replyTo.send(monitoringResponseMsg);
//				} catch (RemoteException e) {
//					Log.e(TAG, "Error while delivering responses", e);
//				}
//			}
//		}
//
//		public void run() {
//			SSBeaconService.this.checkNotOnUiThread();
//			long now = System.currentTimeMillis();
//			SSBeaconService.this.stopScanning();
//			processRanging();
//			List<MonitoringRegion> enteredRegions = findEnteredRegions(now);
//			List<MonitoringRegion> exitedRegions = findExitedRegions(now);
//			removeNotSeenBeacons(now);
//			SSBeaconService.this.beaconsFoundInScanCycle.clear();
//			invokeCallbacks(enteredRegions, exitedRegions);
//			if (SSBeaconService.this.scanWaitTimeMillis() == 0L)
//				SSBeaconService.this.startScanning();
//			else
//				SSBeaconService.this.setAlarm(
//						SSBeaconService.this.scanStartBroadcastPendingIntent,
//						SSBeaconService.this.scanWaitTimeMillis());
//		}
//
//	}
//
// }
