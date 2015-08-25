package com.pami.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pami.utils.MResource;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 下拉刷新的ListView
 * @author Administrator
 *
 */
public class DragListView extends ListView implements OnClickListener,OnScrollListener {

	private View headview;
	private TextView headview_refreshtext, headview_updatetime_text;
	private ImageView headview_imageview;
	private ProgressBar headview_progressbar;
	private int headview_height, headview_width;
	private RelativeLayout headviewRefresh;
	private LinearLayout headViewchildContent;
	// headview状态
	private final static int HeadView_Nomal = 0;// 正常
	private final static int HeadView_Pull = 1;// 下拉
	private final static int HeadView_Release = 2;// 下拉后松开
	private final static int HeadView_Loading = 3;// 加载中
	private final static int HeadView_Over = 4;// 加载结束
	private int startY;
	private int moveY;
	private int fisrtvisibleitem = -1;
	private int lastitem = 0;
	private View footView;
	private TextView footview_text;
	private ImageView footview_img;
	private AnimationDrawable animationDrawable;

	private boolean refreshable = false, loadMoreable = false;
	private boolean isNoMore = false;
	// footview状态
	private final static int FootView_Nomal = 6;// 正常
	private final static int FootView_Loading = 7;// 加载中
	private final static int FootView_Over = 8;// 加载结束
	private final static int FootView_Gone = 9;// 隐藏
	private final static int FootView_NoMore = 10;

	private Animation animation, animation2;
	private onRefreshAndLoadMoreListener refreshAndLoadMoreListener;//
	private int Ratio = 3;// listview下拉距离与headview移动距离比
	private int headviewState = HeadView_Nomal;
	private int footviewState = FootView_Gone;
	private int count = 0;
	private int mType;//1是全部活动列表，2是全部据点列表
	// 控制listview的滚动事件
	private boolean listViewIsScroll = true;
	// 记录一次完整的滑动
	private boolean Isintact = false;
	// 控制listview是否回弹
	private boolean isBack = false;

	public DragListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initDragListView(context);
	}

	public DragListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initDragListView(context);
	}

	public DragListView(Context context) {
		super(context);
		initDragListView(context);
	}

	// 初始化DragListView
	public void initDragListView(Context context) {

		initHeadView(context);
		initFootView(context);
		initAnimation();
	}

	// 初始化HeadView
	public void initHeadView(Context context) {
		
		headview = LayoutInflater.from(context).inflate(MResource.getIdByName(context, "layout", "customview_draglistview_refresh_headview"), null);
		headview_imageview = (ImageView) headview.findViewById(MResource.getIdByName(context, "id", "headview_imageview"));
		headview_progressbar = (ProgressBar) headview.findViewById(MResource.getIdByName(context, "id", "headview_progressbar"));
		headview_refreshtext = (TextView) headview.findViewById(MResource.getIdByName(context, "id", "headview_refreshtext"));
		headview_updatetime_text = (TextView) headview.findViewById(MResource.getIdByName(context, "id", "headview_updatetime_text"));
		headviewRefresh = (RelativeLayout) headview.findViewById(MResource.getIdByName(context, "id", "head_contentLayout"));
		headViewchildContent = (LinearLayout) headview.findViewById(MResource.getIdByName(context, "id", "childHeadview_content"));

		measureView(headview);
		headview_height = headviewRefresh.getMeasuredHeight();
		headview_width = headviewRefresh.getMeasuredWidth();
		headview.setPadding(0, -headview_height, 0, 0);
		addHeaderView(headview, null, false);
		setOnScrollListener(this);
		updateListView(HeadView_Nomal);
	}

	// 初始化Footview
	public void initFootView(Context context) {
		footView = LayoutInflater.from(context).inflate(MResource.getIdByName(context, "layout", "customview_draglistview_refresh_footview"), null);
		footview_img = (ImageView) footView.findViewById(MResource.getIdByName(context, "id", "footview_img"));
		footview_text = (TextView) footView.findViewById(MResource.getIdByName(context, "id", "footview_text"));
		animationDrawable = (AnimationDrawable) footview_img.getDrawable();
		footView.setOnClickListener(this);
		updateListView(FootView_Gone);
		// addFooterView(footView, null, false);
	}

	// 初始化动画
	public void initAnimation() {
		animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setFillAfter(true);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setDuration(300);
		animation2 = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation2.setFillAfter(true);
		animation2.setInterpolator(new DecelerateInterpolator());
		animation2.setDuration(300);

	}

	public void setOnRefreshAndLoadMoreListener(onRefreshAndLoadMoreListener refreshAndLoadMoreListener) {
		this.refreshAndLoadMoreListener = refreshAndLoadMoreListener;
	}

	@Override
	public void onClick(View v) {
		if (v == footView && footviewState == FootView_Nomal) {
			updateListView(FootView_Loading);
			onLadMore();
		}
	}

	// 点击刷新
	public void clickRefresh() {
		if (headviewState != HeadView_Loading) {
			setSelection(0);
			headviewState = HeadView_Loading;
			updateListView(headviewState);
			onRefresh();
		}
	}

	/**
	 * 设置子headview
	 * 
	 * @param view
	 */
	public void setChildHeadView(View view) {
		headViewchildContent.addView(view);
	}

	// 动作接口
	public interface onRefreshAndLoadMoreListener {
		// 下拉刷新
		public void onRefresh();

		// 加载更多
		public void onLoadMore();
	}

	private void onRefresh() {
		if (this.refreshAndLoadMoreListener != null) {
			refreshAndLoadMoreListener.onRefresh();
		}
		isNoMore = false;
	}

	private void onLadMore() {
		if (this.refreshAndLoadMoreListener != null) {
			refreshAndLoadMoreListener.onLoadMore();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		this.fisrtvisibleitem = firstVisibleItem;
		lastitem = firstVisibleItem + visibleItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (loadMoreable && fisrtvisibleitem != 0 && !isNoMore) {
			if (this.getAdapter().getCount() == lastitem && footviewState != FootView_Loading && scrollState == this.SCROLL_STATE_IDLE) {
				if (count % 2 == 0) {
					updateListView(FootView_Nomal);
				} else {
					updateListView(FootView_Loading);
				}
				count++;
			}
		}
	}

	// 触摸事件
	public boolean onTouchEvent(MotionEvent event) {
		if (refreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				actionDown(event);
				break;
			case MotionEvent.ACTION_MOVE:
				actionMove(event);
				break;

			case MotionEvent.ACTION_UP:
				actionUp(event);
				break;
			}
		}
		// 判断是否是拦截滑动
		if (listViewIsScroll) {
			return super.onTouchEvent(event);
		} else {
			return true;

		}
	}

	// 手指按下
	public void actionDown(MotionEvent event) {
		if (Isintact == false && fisrtvisibleitem == 0) {
			// 记录手指按下的位置
			startY = (int) event.getY();
			Isintact = true;
		}
	}

	// 手指移动
	public void actionMove(MotionEvent event) {
		moveY = (int) event.getY();
		if (Isintact == false && fisrtvisibleitem == 0) {
			startY = (int) event.getY();
			Isintact = true;
		}
		if(Isintact==false){
			return;
		}
		
		// 如果滑动时headview的状态是正在加载中直接返回
		if (Isintact == false && headviewState == HeadView_Loading) {
			return;
		}
		

		// 下拉listview时headview的偏移量
		int offset = (int) ((moveY - startY) / Ratio);

		switch (headviewState) {
		case HeadView_Nomal:
			if (offset > 0) {
				headview.setPadding(0, offset - headview_height, 0, 0);
				// 改为下拉状态 并更新listview
				updateListView(HeadView_Pull);
			}
			break;
		case HeadView_Pull:
			headview.setPadding(0, offset - headview_height, 0, 0);
			if (offset < 0) {
				listViewIsScroll = false;
				headview.setPadding(0, -headview_height, 0, 0);
				updateListView(HeadView_Nomal);
			} else if (offset >= headview_height) {
				updateListView(HeadView_Release);
			}
			break;
		case HeadView_Release:
			headview.setPadding(0, offset - headview_height, 0, 0);
			if (offset > 0 && offset < headview_height) {
				// headview全部缩回
				isBack = true;
				updateListView(HeadView_Pull);
			} else if (offset < 0) {
				updateListView(HeadView_Nomal);
			}
			break;
		case HeadView_Loading:
			headview.setPadding(0, 0, 0, 0);
			listViewIsScroll = true;
			break;
		}

	}

	// 手指抬起
	public void actionUp(MotionEvent event) {
		listViewIsScroll = true;
		Isintact = false;
		isBack = false;
		switch (headviewState) {
		case HeadView_Loading:
			return;
		case HeadView_Nomal:
			return;
		case HeadView_Pull:
			headview.setPadding(0, -headview_height, 0, 0);
			updateListView(HeadView_Nomal);
			break;
		case HeadView_Release:
			headview.setPadding(0, 0, 0, 0);
			updateListView(HeadView_Loading);
			onRefresh();
			break;
		case HeadView_Over:
			headview.setPadding(0, -headview_height, 0, 0);
			updateListView(HeadView_Nomal);
			break;

		default:
			break;
		}
	}
	
	/**
	 * 
	 * @param type 1是活动，2是据点
	 */
	public void setType(int type) {
		mType = type;
	}

	// 更新ListView界面
	public void updateListView(int state) {
		switch (state) {

		case HeadView_Nomal:
			headview.setPadding(0, -headview_height, 0, 0);
			headviewState = HeadView_Nomal;
			headview_imageview.setVisibility(View.VISIBLE);
			headview_imageview.clearAnimation();
			headview_imageview.clearAnimation();
			break;
		case HeadView_Pull:
			headviewState = HeadView_Pull;
			headview_imageview.clearAnimation();
			headview_progressbar.setVisibility(View.GONE);
			headview_refreshtext.setText("下拉刷新");
			if (isBack) {
				isBack = false;
				headview_imageview.clearAnimation();
				headview_imageview.startAnimation(animation2);
			}
			break;
		case HeadView_Release:
			headviewState = HeadView_Release;
			headview_imageview.clearAnimation();
			headview_progressbar.setVisibility(View.GONE);
			headview_refreshtext.setText("松开刷新");
			headview_imageview.startAnimation(animation);
			break;
		case HeadView_Loading:
			headview.setPadding(0, 0, 0, 0);
			headviewState = HeadView_Loading;
			headview_imageview.clearAnimation();
			headview_imageview.setVisibility(View.GONE);
			headview_progressbar.setVisibility(View.VISIBLE);
			headview_refreshtext.setText("刷新中...");
			break;
		case HeadView_Over:
			headviewState = HeadView_Over;
			break;
		case 5:

			break;
		case FootView_Nomal:
			footviewState = FootView_Nomal;
			addMFootView();
			footView.setVisibility(View.VISIBLE);
			footview_img.setVisibility(View.GONE);
			footview_text.setVisibility(View.VISIBLE);
			footview_text.setText("上拉或点击加载更多");
			setSelection(lastitem);
			break;
		case FootView_Loading:
			footviewState = FootView_Loading;
			addMFootView();
			footView.setVisibility(View.VISIBLE);
			footview_img.setVisibility(View.VISIBLE);
			animationDrawable.start();
			onLadMore();
//			footview_text.setVisibility(View.GONE);
			footview_text.setVisibility(View.VISIBLE);
			footview_text.setText("加载中");
			break;
		case FootView_Over:
			footviewState = FootView_Over;
			addMFootView();
			footView.setVisibility(View.VISIBLE);
			footview_img.setVisibility(View.GONE);
			footview_text.setVisibility(View.VISIBLE);
			footview_text.setText("加载完毕");
			break;
		case FootView_Gone:
			removeMFooterView();
			footView.setVisibility(View.GONE);
			footviewState = FootView_Gone;
			break;
		case FootView_NoMore:
			footviewState = FootView_NoMore;
//			addMFootView();
//			footView.setVisibility(View.VISIBLE);
//			footview_img.setVisibility(View.GONE);
//			footview_text.setVisibility(View.VISIBLE);
			
			footView.setVisibility(View.GONE);
			footview_img.setVisibility(View.GONE);
			footview_text.setVisibility(View.GONE);
			this.removeFooterView(footView);
			
//			switch (mType) {
//			case 1:
//				footview_text.setText(R.string.listview_downstop_activitiestext);
//				break;
//			case 2:
//				footview_text.setText(R.string.listview_downstop_placsetext);
//				break;
//			default:
//				footview_text.setText(R.string.listview_downstop_defaulttext);
//				break;
//			}
			
			isNoMore = true;
			break;
		default:
			break;
		}
	}

	private void addMFootView() {
		if (this.getFooterViewsCount() < 1) {
			this.addFooterView(footView, null, false);
		}
	}

	private void removeMFooterView() {
		if (this.getFooterViewsCount() > 0) {
			this.removeFooterView(footView);
		}
	}

	/**
	 * 加载更多完成
	 */
	public void completeLoadMore() {
		updateListView(FootView_Gone);
	}

	/**
	 * 没有更多数据
	 */
	public void noMore() {
		updateListView(FootView_NoMore);
		setSelection(lastitem);
	}

	public void completeRefresh() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		headview_updatetime_text.setText("上次更新时间" + simpleDateFormat.format(new Date(System.currentTimeMillis())));
		updateListView(HeadView_Nomal);
	}

	// 获取listview下拉距离与headview移动距离比
	public int getRatio() {
		return Ratio;
	}

	// 设置listview下拉距离与headview移动距离比
	public void setRatio(int ratio) {
		Ratio = ratio;
	}

	// 设置是否可下拉刷新及加载更多
	public void setRefreshableAndLoadMoreable(boolean refreshable, boolean loadMoreable) {
		this.refreshable = refreshable;
		this.loadMoreable = loadMoreable;
	}

	// 测量headview的大小
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}
}
