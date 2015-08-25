package com.pami.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * PMFragmentStatePagerAdapter 继承自 android 的FragmentStatePagerAdapter
 * 当有大量的fragment时使用此适配器。如果fragment的个数不超过4个 可以使用 FragmentPagerAdapter
 * @author Administrator
 *
 */
public class PMFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

	protected List<Fragment> mDatas = null;
	
	public PMFragmentStatePagerAdapter(FragmentManager fm,List<Fragment> mDatas) {
		super(fm);
		this.mDatas = mDatas;
	}
	
	public PMFragmentStatePagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		return mDatas.get(arg0);
	}

	@Override
	public int getCount() {
		if(mDatas == null || mDatas.isEmpty()){
			return 0;
		}
		return mDatas.size();
	}

}
