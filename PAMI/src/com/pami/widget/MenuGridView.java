package com.pami.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View.MeasureSpec;
import android.widget.GridView;

/**
 * 当ScrollView 嵌套GridView时 此GridView 会撑开全部
 * 
 * @author Administrator
 * 
 */
public class MenuGridView extends GridView {
	public MenuGridView(Context context) {
		super(context);
	}

	public MenuGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MenuGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
	
}
