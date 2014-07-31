package com.hy.ss.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hy.ss.sdk.pub.entity.SSCity;
import com.hy.ss.shopsmartandroid.R;

public class SSCityListAdapter extends BaseAdapter {
	private Context mContext;
	private List<SSCity> mData;

	public SSCityListAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<SSCity> mData) {
		this.mData = mData;
	}

	public int getCount() {
		return mData.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = LayoutInflater.from(mContext);
		convertView = inflater.inflate(R.layout.list_item_city, null);

		TextView tv = (TextView) (convertView.findViewById(R.id.list_item_city));
		tv.setText(mData.get(position).getName());
		tv.setTextSize(20);
		return convertView;
	}
}
