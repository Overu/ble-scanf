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
import com.hy.ss.sdk.pub.entity.SSMarket;

public class SSMarketManager {
	// private static final String TAG = (SSMarketManager.class.getName());

	public static List<SSMarket> loadMarketFromFiles(Context context,
			String cityID) {

		List<SSMarket> markets = new ArrayList<SSMarket>();

		try {
			// InputStream inStream = context.getAssets().open(
			// SSFileManager.getMarketJsonPath(cityID));
			FileInputStream inStream = new FileInputStream(new File(
					SSFileManager.getMarketJsonPath(cityID)));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(AppKeys.JSON_KEY_MARKETS)) {
				JSONArray array = jsonObject
						.getJSONArray(AppKeys.JSON_KEY_MARKETS);
				for (int i = 0; i < array.length(); i++) {
					SSMarket market = new SSMarket();
					market.parseJson(array.optJSONObject(i));
					markets.add(market);
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

		return markets;
	}

	public static SSMarket loadMarketFromFilesById(Context context,
			String cityID, String marketID) {
		List<SSMarket> markets = loadMarketFromFiles(context, cityID);

		for (int i = 0; i < markets.size(); i++) {
			SSMarket market = markets.get(i);
			if (market.getMarketID().equals(marketID)) {
				return market;
			}
		}
		return null;
	}

	public static SSMarket loadMarketFromFilesByName(Context context,
			String cityID, String marketName) {
		List<SSMarket> markets = loadMarketFromFiles(context, cityID);

		for (int i = 0; i < markets.size(); i++) {
			SSMarket market = markets.get(i);
			if (market.getName().equals(marketName)) {
				return market;
			}
		}
		return null;
	}
}
