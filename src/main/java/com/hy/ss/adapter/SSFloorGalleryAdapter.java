package com.hy.ss.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.TextView;

import com.hy.ss.sdk.pub.entity.SSMapInfo;

@SuppressWarnings("deprecation")
public class SSFloorGalleryAdapter extends BaseAdapter {
	private Context mContext;
	private List<SSMapInfo> mData;

	public SSFloorGalleryAdapter(Context context) {
		this.mContext = context;
	}

	public void setData(List<SSMapInfo> mData) {
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

		TextView tv = new TextView(mContext);
		tv.setText(mData.get(position).getFloorString());
		tv.setTextSize(20);
		tv.setLayoutParams(new Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		return tv;
	}
}
