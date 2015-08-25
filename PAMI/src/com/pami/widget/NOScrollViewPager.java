package com.pami.widget;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不能左右滑动的ViewPager
 * @author Administrator
 *
 */
public class NOScrollViewPager extends ViewPager {

    private boolean willIntercept = true;//是否拦截
    private boolean isCanScroll = true;//是否滚动
    
    public boolean isCanScroll() {
		return isCanScroll;
	}

    /**
     * 设置 true为 滚动
     * @param isCanScroll
     */
	public void setCanScroll(boolean isCanScroll) {
		this.isCanScroll = isCanScroll;
	}

	public NOScrollViewPager(Context context) {
            super(context);
    }
    
    public NOScrollViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
           return willIntercept ? super.onInterceptTouchEvent(arg0) : false;
    }
    	/**
    	 * 是否拦截
    	 * @param value
    	 */
    public void setTouchIntercept(boolean value){
            willIntercept = value;
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
    		return isCanScroll ? super.onTouchEvent(arg0) : false;
    
    }
}

