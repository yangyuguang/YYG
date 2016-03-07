package com.pami.test;

import android.os.Bundle;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import com.pami.R;
import com.pami.widget.DragGridView;
import java.lang.reflect.Field;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements SlidingPaneLayout.PanelSlideListener{
    
    
	private SlidingPaneLayout mSlidingPaneLayout = null;
	private FrameLayout mContainerFl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	//TODO 通过反射来改变SlidingPanelayout的值
        try {
            mSlidingPaneLayout = new SlidingPaneLayout(this);
            //属性
            Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
            f_overHang.setAccessible(true);
            f_overHang.set(mSlidingPaneLayout, 0);

            mSlidingPaneLayout.setPanelSlideListener(this);
            mSlidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));
        } catch (Exception e) {
            e.printStackTrace();
        }

        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
      //添加两个view,这是左侧菜单，因为Activity是透明的，这里就不用设置了
        View leftView = new View(this);
      //设置全屏
        leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
      //添加到SlidingPaneLayout中
        mSlidingPaneLayout.addView(leftView, 0);


      //内容布局，用来存放Activity布局用的
        mContainerFl = new FrameLayout(this);
      //内容布局不应该是透明，这里加了白色背景   mContainerFl.setBackgroundColor(getResources().getColor(android.R.color.white));
        //全屏幕显示
        mContainerFl.setBackgroundColor(getResources().getColor(android.R.color.white));
        mContainerFl.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
      //添加到SlidingPaneLayout中
        mSlidingPaneLayout.addView(mContainerFl, 1);
        
    }

    
    
    /**********************[SlidingPaneLayout.PanelSlideListener 开始]***********************************/

    @Override
    public void setContentView(int id) {
        setContentView(getLayoutInflater().inflate(id, null));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View)
     */
    @Override
    public void setContentView(View v) {
        setContentView(v, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /* (non-Javadoc)
     * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    @Override
    public void setContentView(View v, ViewGroup.LayoutParams params) {
        super.setContentView(mSlidingPaneLayout, params);

        mContainerFl.removeAllViews();
        mContainerFl.addView(v, params);
    }

    @Override
    public void onPanelClosed(View view) {

    }

    @Override
    public void onPanelOpened(View view) {
    	//菜单打开后，我们结束掉这个Activity
        finish();
        this.overridePendingTransition(0, R.anim.sliding_out_right);
    }

    @Override
    public void onPanelSlide(View view, float v) {
    }
	
	/**********************[SlidingPaneLayout.PanelSlideListener 结束]***********************************/
}
