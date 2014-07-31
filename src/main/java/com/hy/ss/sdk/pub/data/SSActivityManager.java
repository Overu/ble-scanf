package com.hy.ss.sdk.pub.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.hy.ss.constants.AppKeys;
import com.hy.ss.sdk.pub.entity.SSActivity;

public class SSActivityManager {
	// private static final String TAG = (SSActivityManager.class.getName());

	public static List<SSActivity> parseAllActivity(Context context,
			String marketID) {
		List<SSActivity> activities = new ArrayList<SSActivity>();

		try {
			// InputStream inStream = context.getAssets().open(
			// SSAssetsManager.getActivityJsonPath(marketID));
			FileInputStream inStream = new FileInputStream(new File(
					SSFileManager.getActivityJsonPath(marketID)));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(AppKeys.JSON_KEY_ACTIVITY)) {
				JSONArray array = jsonObject
						.getJSONArray(AppKeys.JSON_KEY_ACTIVITY);
				for (int i = 0; i < array.length(); i++) {
					SSActivity activity = new SSActivity();
					activity.parseJson(array.optJSONObject(i));
					activities.add(activity);
				}
			}
			inputReader.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return activities;
	}

	public static SSActivity parseActivityWithShopID(Context context,
			String marketID, String shopID) {
		List<SSActivity> activities = parseAllActivity(context, marketID);

		for (int i = 0; i < activities.size(); i++) {
			SSActivity activity = activities.get(i);
			if (activity.getShopID().equals(shopID)) {
				return activity;
			}
		}
		return null;
	}

	public static SSActivity parseActivityWithActivityID(Context context,
			String marketID, String activityID) {
		List<SSActivity> activities = parseAllActivity(context, marketID);

		for (int i = 0; i < activities.size(); i++) {
			SSActivity activity = activities.get(i);
			if (activity.getActivityID().equals(activityID)) {
				return activity;
			}
		}
		return null;
	}
}
