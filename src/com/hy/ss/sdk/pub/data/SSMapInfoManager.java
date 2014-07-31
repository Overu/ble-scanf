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
import com.hy.ss.sdk.pub.entity.SSMapInfo;

public class SSMapInfoManager {

	public static List<SSMapInfo> loadMapInfoFromFiles(Context context,
			String marketID) {

		List<SSMapInfo> mapInfos = new ArrayList<SSMapInfo>();

		try {

			// InputStream inStream = context.getAssets().open(
			// SSAssetsManager.getMapInfoJsonPath(marketID));
			FileInputStream inStream = new FileInputStream(new File(
					SSFileManager.getMapInfoJsonPath(marketID)));
			InputStreamReader inputReader = new InputStreamReader(inStream);
			BufferedReader bufReader = new BufferedReader(inputReader);

			String line = "";
			StringBuffer jsonStr = new StringBuffer();
			while ((line = bufReader.readLine()) != null)
				jsonStr.append(line);

			JSONObject jsonObject = new JSONObject(jsonStr.toString());
			if (jsonObject != null
					&& !jsonObject.isNull(AppKeys.JSON_KEY_MAPINFOS)) {
				JSONArray array = jsonObject
						.getJSONArray(AppKeys.JSON_KEY_MAPINFOS);
				for (int i = 0; i < array.length(); i++) {
					SSMapInfo mapInfo = new SSMapInfo();
					mapInfo.parseJson(array.optJSONObject(i));
					mapInfos.add(mapInfo);
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
		return mapInfos;
	}

	public static SSMapInfo loadMapInfoFromFilesById(Context context,
			String marketID, String mapID) {
		List<SSMapInfo> mapInfos = loadMapInfoFromFiles(context, marketID);

		for (int i = 0; i < mapInfos.size(); i++) {
			SSMapInfo mapInfo = mapInfos.get(i);
			if (mapInfo.getMapID().equals(mapID)) {
				return mapInfo;
			}
		}
		return null;
	}

}
