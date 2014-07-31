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
import com.hy.ss.sdk.pub.entity.SSCity;

public class SSCityManager {
	// private static final String TAG = (SSCityManager.class.getName());

	public static List<SSCity> loadCityFromFiles(Context context) {
		List<SSCity> cities = new ArrayList<SSCity>();

		try {

			// InputStream inStream = context.getAssets().open(
			// SSAssetsManager.getCityJsonPath());
			// InputStream inStream =
			FileInputStream inStream = new FileInputStream(new File(
					SSFileManager.getCityJsonPath()));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(AppKeys.JSON_KEY_CITIES)) {
				JSONArray array = jsonObject
						.getJSONArray(AppKeys.JSON_KEY_CITIES);
				for (int i = 0; i < array.length(); i++) {
					SSCity city = new SSCity();
					city.parseJson(array.optJSONObject(i));
					cities.add(city);
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

		return cities;
	}

	public static SSCity loadCityFromFilesById(Context context, String cityId) {
		List<SSCity> cities = loadCityFromFiles(context);

		for (int i = 0; i < cities.size(); i++) {
			SSCity city = cities.get(i);
			if (city.getCityID().equals(cityId)) {
				return city;
			}
		}
		return null;
	}

	public static SSCity loadCityFromFilesByName(Context context,
			String cityName) {
		List<SSCity> cities = loadCityFromFiles(context);

		for (int i = 0; i < cities.size(); i++) {
			SSCity city = cities.get(i);
			if (city.getName().equals(cityName)) {
				return city;
			}
		}
		return null;
	}
}
