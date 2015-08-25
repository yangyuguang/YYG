package com.pami.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * PMFragmentPagerAdapter 继承自 android 的FragmentPagerAdapter
 * 当fragment的个数不超过4个 可以使用此适配器 如果fragment的数量远远超过4个时建议使用 PMFragmentStatePagerAdapter
 * @author Administrator
 *
 */
public class PMFragmentPagerAdapter extends FragmentPagerAdapter{

	protected List<Fragment> mDatas = null;
	
	public PMFragmentPagerAdapter(FragmentManager fm,List<Fragment> mDatas) {
		super(fm);
		this.mDatas = mDatas;
	}
	
	public PMFragmentPagerAdapter(FragmentManager fm) {
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
