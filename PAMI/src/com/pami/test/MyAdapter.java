package com.pami.test;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pami.adapter.DragGridViewAdapter;
import com.pami.adapter.ViewHolder;

public class MyAdapter extends DragGridViewAdapter<String> {

	public MyAdapter(Context context, List<String> mData, int layoutId) {
		super(context, mData, layoutId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void getViews(ViewHolder holder, String t, int position) {
		// TODO Auto-generated method stub
		
	}




//	@Override
//	public View getMyView(int position, View view, ViewGroup viewGroup) {
//		TextView tv;
//        if(view == null) {
//            tv = new TextView(context);
//        }
//        else {
//            tv = (TextView)view;
//        }
//
//        //hide时隐藏Text
//        if(position != hidePosition) {
//            tv.setText(mData.get(position).toString());
//        }
//        else {
//            tv.setText("");
//        }
//        tv.setId(position);
//        return tv;
//	}

}
