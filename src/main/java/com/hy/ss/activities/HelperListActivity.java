package com.hy.ss.activities;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.HttpClient;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.SimpleAdapter;

import com.esri.android.runtime.ArcGISRuntime;
import com.hy.ss.constants.AppKeys;

public abstract class HelperListActivity extends ListActivity {

	HttpURLConnection conn;
	HttpClient client;

	public class IntentPair {
		String name;
		Intent intent;

		public IntentPair(String name, Intent intent) {
			this.name = name;
			this.intent = intent;
		}
	}

	protected IntentPair[] intents;

	protected abstract void constructList();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ArcGISRuntime.setClientId("aBYcI5ED55pNyHQ8");

		constructList();

		setListAdapter(new SimpleAdapter(this, getData(intents),
				android.R.layout.simple_list_item_1, new String[] { "title" },
				new int[] { android.R.id.text1 }));
		getListView().setTextFilterEnabled(true);
	}

	protected List<Map<String, Object>> getData(IntentPair[] intents) {

		List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

		for (IntentPair pair : intents) {
			addItem(myData, pair.name, pair.intent);
		}
		// Collections.sort(myData, sDisplayNameComparator);
		return myData;
	}

	protected Intent activityIntent(String pkg, String componentName) {
		Intent result = new Intent();
		result.setClassName(pkg, componentName);
		return result;
	}

	protected void addItem(List<Map<String, Object>> data, String name,
			Intent intent) {
		Map<String, Object> temp = new HashMap<String, Object>();
		temp.put("title", name);
		temp.put(AppKeys.KEY_INTENT, intent);
		data.add(temp);
	}

}
