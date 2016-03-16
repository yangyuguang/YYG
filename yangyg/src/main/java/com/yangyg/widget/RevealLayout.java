package com.yangyg.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

/**
 * 按钮点击水波纹效果 用法：如果 TextView Button 需要使用点击水波纹效果 可以将TextView 或者 Button
 * 放入此Linearlayout中
 * 
 * @author 杨裕光
 * 
 */
public class RevealLayout extends LinearLayout {

	private static final long INVALIDATE_DURATION = 40;

	private static final String TAG = "RevealLayoutTest";

	/** 画半透明背景的画笔 */
	private Paint mPaint = null;

	/** 点击的View */
	private View taggerView = null;
	/** 点击的View的宽度和高度 */
	private int taggerWidth;
	private int taggerHeight;

	/** 宽度和高度最小值 */
	private int mMinWidthAndHeight;
	private int mMaxWidthAndHeight;

	/** 水波纹的中心点 */
	private int centerX;
	private int centerY;
	/** 水波纹的半径 */
	private int mRadiu;
	/** 圆半径变化梯度值 */
	private int mRadiuGap;
	/** 最大半径 */
	private int mMaxRadiu;

	/** 是否显示动画 */
	private boolean mShouldDoAnimation;
	/** 是否被按下 */
	private boolean mIsPressed;
	/** LinearLayout 在屏幕中的绝对位置 */
	private int[] mLocationInScreen = new int[2];

	private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

	public RevealLayout(Context context) {
		this(context, null);
	}

	public RevealLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RevealLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		try {
			init(context);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void init(Context context) {
		setWillNotDraw(false);
		mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#1b000000"));
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		this.getLocationOnScreen(mLocationInScreen);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		try {
			int x = (int) ev.getRawX();
			int y = (int) ev.getRawY();
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				/**
				 * 需要获取用户按下的操作 获取用户的点击的view 和 点击的坐标
				 */
				log("ACTION_DOWN");
				View taggerView = getTouchView(this, x, y);
				if (taggerView != null) {
					this.taggerView = taggerView;
					initParametersForChild(ev, taggerView);
					postInvalidateDelayed(INVALIDATE_DURATION);
				}
				break;
			}

			case MotionEvent.ACTION_UP: {
				mIsPressed = false;
				postInvalidateDelayed(INVALIDATE_DURATION);
				mDispatchUpTouchEventRunnable.event = ev;
				postDelayed(mDispatchUpTouchEventRunnable, 40);
				break;
			}

			case MotionEvent.ACTION_CANCEL: {
				mIsPressed = false;
				postInvalidateDelayed(INVALIDATE_DURATION);
				break;
			}

			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.dispatchTouchEvent(ev);
	}

	private void initParametersForChild(MotionEvent ev, View taggerView2) throws Exception {
		centerX = (int) ev.getX();
		centerY = (int) ev.getY();

		taggerWidth = taggerView2.getMeasuredWidth();
		taggerHeight = taggerView2.getMeasuredHeight();

		mRadiu = 0;
		mShouldDoAnimation = true;
		mIsPressed = true;

		mMinWidthAndHeight = Math.min(taggerHeight, taggerWidth);
		mMaxWidthAndHeight = Math.max(taggerHeight, taggerWidth);
		mRadiuGap = mMinWidthAndHeight / 8;

		mMaxRadiu = mMaxWidthAndHeight;

	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		try {
			if (!mShouldDoAnimation || taggerWidth <= 0 || taggerView == null) {
				return;
			}

			if (mRadiu > mMinWidthAndHeight / 2) {
				mRadiu += mRadiuGap * 4;
			} else {
				mRadiu += mRadiuGap;
			}

			int[] location = new int[2];
			taggerView.getLocationOnScreen(location);
			int left = location[0] - mLocationInScreen[0];
			int top = location[1] - mLocationInScreen[1];
			int right = left + taggerView.getMeasuredWidth();
			int bottom = top + taggerView.getMeasuredHeight();

			canvas.save();

			canvas.clipRect(left, top, right, bottom);
			canvas.drawCircle(centerX, centerY, mRadiu, mPaint);

			canvas.restore();

			if (mRadiu <= mMaxRadiu) {
				postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
			} else if (!mIsPressed) {
				mShouldDoAnimation = false;
				postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取点击的View
	 * 
	 * @return
	 */
	private View getTouchView(View view, int x, int y) throws Exception {
		View tagger = null;
		// 获取所有可触碰的View
		ArrayList<View> views = view.getTouchables();
		for (View child : views) {
			if (isTouchPointInView(child, x, y)) {
				tagger = child;
				break;
			}
		}
		return tagger;
	}

	/**
	 * 判断点（x，y） 是否在view上面
	 * 
	 * @param child
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isTouchPointInView(View child, int x, int y) throws Exception {
		int[] location = new int[2];
		/*
		 * 获取在整个屏幕内的绝对坐标，注意这个值是要从屏幕顶端算起， 也就是包括了通知栏的高度。
		 */
		child.getLocationOnScreen(location);

		int left = location[0];
		int top = location[1];
		int right = left + child.getMeasuredWidth();
		int bottom = top + child.getMeasuredHeight();
		if (child.isClickable() && y >= top && y <= bottom && x >= left && x <= right) {
			return true;
		}
		return false;

	}

	private class DispatchUpTouchEventRunnable implements Runnable {
		public MotionEvent event;

		@Override
		public void run() {
			try {
				if (taggerView == null || !taggerView.isEnabled()) {
					return;
				}

				if (isTouchPointInView(taggerView, (int) event.getRawX(), (int) event.getRawY())) {
					taggerView.performClick();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	private void log(String string) {
		Log.e(TAG, string);
	}

}
