package com.pami.test;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.pami.adapter.PMBaseAdapter;
import com.pami.adapter.ViewHolder;

public class TestAdapter extends PMBaseAdapter<String> {

	public TestAdapter(Context context, List<String> mData,int layoutId) {
		super(context, mData,layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = LayoutInflater.from(context).inflate(R.layout.item, null);
//		TextView tv = (TextView) convertView.findViewById(R.id.text);
//		tv.setText(mData.get(position));
		return convertView;
	}

	@Override
	public void getViews(ViewHolder holder, String t, int position) {
		// TODO Auto-generated method stub
		
	}


}
