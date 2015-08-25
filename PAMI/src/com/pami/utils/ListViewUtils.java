package com.pami.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
/**
 * 用于操作ListView的工具类
 * @author Administrator
 *
 */
public class ListViewUtils {

	/**
	 * 根据ListView中 item的数量 设置ListView 的高度
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {    
        ListAdapter listAdapter = listView.getAdapter();     
        if (listAdapter == null) {    
            return;    
        }    
    
        int totalHeight = 0;    
        for (int i = 0; i < listAdapter.getCount(); i++) {    
            View listItem = listAdapter.getView(i, null, listView);    
            listItem.measure(0, 0);    
            totalHeight += listItem.getMeasuredHeight();    
        }    
    
        ViewGroup.LayoutParams params = listView.getLayoutParams();    
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));    
        listView.setLayoutParams(params);    
    }
}
