package com.pami.widget;

import java.util.ArrayList;
import java.util.List;

import com.pami.utils.MLog;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * 实现流式布局和热门标签
 */
public class YYGFlowLayout extends ViewGroup {

	private static final String TAG = "YYGFlowLayout";

	public YYGFlowLayout(Context context) {
		this(context, null);
	}

	public YYGFlowLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public YYGFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	private List<List<View>> mAllViews = new ArrayList<List<View>>();// 每一行的View
	private List<Integer> mLinesHeight = new ArrayList<Integer>();// 每一行的高度

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		mAllViews.clear();
		mLinesHeight.clear();
		int width = getWidth();

		int childNumber = getChildCount();
		int childWidth = 0;
		int childHeight = 0;

		int lineHeight = 0;
		int lineWidth = 0;

		MarginLayoutParams lp = null;

		List<View> lineViews = new ArrayList<View>();
		// 第一步计算 总共需要多少行？ 每一行放多少个控件
		for (int i = 0; i < childNumber; i++) {
			View childView = getChildAt(i);
			childWidth = childView.getMeasuredWidth();
			childHeight = childView.getMeasuredHeight();
			lp = (MarginLayoutParams) childView.getLayoutParams();

			if ((lineWidth + childWidth + lp.leftMargin + lp.rightMargin) > width) {
				// 一行放不下 另起一行
				mLinesHeight.add(lineHeight);
				mAllViews.add(lineViews);
				lineViews = new ArrayList<View>();
				lineViews.add(childView);
				lineWidth = childWidth + lp.leftMargin + lp.rightMargin;
				lineHeight = childHeight + lp.topMargin + lp.bottomMargin;

			} else {
				//
				lineHeight = Math.max(lineHeight,
						(childHeight + lp.topMargin + lp.bottomMargin));
				lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
				lineViews.add(childView);
			}

			if (i == childNumber - 1) {
				mLinesHeight.add(lineHeight);
				mAllViews.add(lineViews);
			}
		}

		// 第二步 将每个控件摆放到合适的位置
		int left = 0;
		int top = 0;

		int lineNum = mAllViews.size();
		int lineViewNum = 0;
		int lineUpHeight = 0;
		View childView = null;
		View childUpView = null;
		MarginLayoutParams childViewLp = null;
		MarginLayoutParams childUpViewLp = null;
		for (int i = 0; i < lineNum; i++) {
			lineHeight = mLinesHeight.get(i);// 这一行的高度
			lineViews = mAllViews.get(i);
			lineViewNum = lineViews.size();
			if (i != 0) {
				lineUpHeight = mLinesHeight.get(i - 1);
				top += lineUpHeight;
			}
			left = 0;
			for (int j = 0; j < lineViewNum; j++) {
				childView = lineViews.get(j);
				childViewLp = (MarginLayoutParams) childView.getLayoutParams();
				if (childView.getVisibility() == View.GONE) {
					continue;
				}
				if (j != 0) {
					childUpView = lineViews.get(j - 1);
					childUpViewLp = (MarginLayoutParams) childUpView
							.getLayoutParams();
					left += childUpView.getMeasuredWidth()
							+ childUpViewLp.rightMargin
							+ childViewLp.leftMargin;
				} else {
					left += childViewLp.leftMargin;
				}

				int topTmp = (lineHeight - childView.getMeasuredHeight()) / 2
						+ top;

				// log("----【子view的摆放位置】【"+i+"-"+j+"】【"+childView.getMeasuredWidth()+"】---->"+left+"  ,  "+top+"  ,  "+(left
				// + childView.getMeasuredWidth())+"  ,  "+(top +
				// childView.getMeasuredHeight()));
				childView.layout(left, topTmp,
						left + childView.getMeasuredWidth(),
						topTmp + childView.getMeasuredHeight());
			}
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = 0;
		int height = 0;

		int childCount = getChildCount();

		int lineWidth = 0;
		int lineHeight = 0;
		MarginLayoutParams lp = null;
		int childWidth = 0;
		int childHeight = 0;
		View childView = null;
		for (int i = 0; i < childCount; i++) {
			childView = getChildAt(i);
			measureChild(childView, widthMeasureSpec, heightMeasureSpec);
			lp = (MarginLayoutParams) childView.getLayoutParams();
			childWidth = childView.getMeasuredWidth() + lp.leftMargin
					+ lp.rightMargin;
			childHeight = childView.getMeasuredHeight() + lp.topMargin
					+ lp.bottomMargin;

			if ((lineWidth + childWidth) > widthSize) {
				// 如果宽度超出了范围
				width = Math.max(lineWidth, width);
				height += lineHeight;

				lineWidth = childWidth;
				lineHeight = childHeight;
			} else {
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight, childHeight);
			}

			if (i == (childCount - 1)) {
				width = Math.max(width, lineWidth);
				height += lineHeight;
			}
		}

//		log("----》宽："
//				+ ((widthMode == MeasureSpec.EXACTLY) ? widthSize : width)
//				+ "  高："
//				+ ((heightMode == MeasureSpec.EXACTLY) ? heightSize : height));
		setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? widthSize
				: width, (heightMode == MeasureSpec.EXACTLY) ? heightSize
				: height);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	private void log(String s) {
		MLog.e(TAG, s);
	}

}
