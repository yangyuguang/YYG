package com.pami.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

public abstract class DragGridViewAdapter<T> extends PMBaseAdapter<T>{

	public int hidePosition = AdapterView.INVALID_POSITION;
	
	public DragGridViewAdapter(Context context, List<T> mData,int layoutId) {
		super(context, mData,layoutId);
	}

	
	public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    public void removeView(int pos) {
    	mData.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if(draggedPos < destPos) {
        	mData.add(destPos+1, getItem(draggedPos));
        	mData.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if(draggedPos > destPos) {
        	mData.add(destPos, getItem(draggedPos));
        	mData.remove(draggedPos+1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
    
}
