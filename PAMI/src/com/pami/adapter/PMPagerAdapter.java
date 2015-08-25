package com.pami.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class PMPagerAdapter extends PagerAdapter {

	protected List<View> mData = null;
	protected Context context = null;
	
	public PMPagerAdapter(Context context,List<View> mData){
		this.context = context;
		this.mData = mData;
	}
	
	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		((ViewPager) container).addView(mData.get(position));
		return mData.get(position);
	}

}
