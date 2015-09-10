package com.pami.widget;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pami.utils.MResource;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class LoadRefreshListView extends ListView implements OnScrollListener {

	private OnLoadMoreAndRefreshListener onLoadMoreAndRefreshListener = null;
	/** 是否允许加载更多 */
	private boolean isLoadingMore = false;
	/** 是否允许刷新 */
	private boolean isRefresh = false;

	/**
	 * 手指按下时的Y坐标
	 */
	private int startY = -100;

	private View headView = null;
	/** 正在加载的ProgressBar */
	private ProgressBar head_load_progressbar = null;
	/** 显示下拉刷新状态图片的ImageView */
	private ImageView head_load_status_icon = null;
	/** 显示下拉刷新 上次刷新的时间 */
	private TextView head_load_time = null;
	/** 显示下拉刷新状态文本信息的TextView */
	private TextView head_load_msg = null;
	/** listView头正常状态 */
	private static final int HEAD_NORMAL = 3;
	/** 下拉刷新 */
	private static final int HEAD_PULL = 4;
	/** 松开刷新 */
	private static final int HEAD_RELEASE = 5;
	/** 正在刷新 */
	private static final int HEAD_REFRESHING = 6;
	private int headCurrentState = HEAD_NORMAL;
	/** HeadView的高度 */
	private int headTotalHeight = 0;
	private RotateAnimation headMarkFromDownToUpAnimation = null;
	private RotateAnimation headMarkFromUpToDownAnimation = null;

	private View footView = null;
	/** 底部显示加载状态文本的TextView */
	private TextView list_foot_view_show_msg = null;
	private ProgressBar load_more_progressbar = null;
	/** FootView 隐藏的动画 */
	private TranslateAnimation mHiddenFootViewAnimation = null;
	private TranslateAnimation mShowFootViewAnimation = null;

	/** FootView 正常 */
	private static final int FOOT_NORMAL = 0;
	/** FootView 正在加载更多 */
	private static final int FOOT_LOADING = 1;
	/** FootView 没有更多可以加载，此时需要隐藏Foot 或者显示不能加载更多 */
	private static final int FOOT_NO_MORE = 2;
	/**FootView 当前状态*/
	private int footCurrentState = FOOT_NORMAL;
	/** 最后一条记录是否可见 */
	private boolean isLastItemVisible = false;
	private int firstVisibleItem = 0;
	
	private final static int RATIO = 3;

	public LoadRefreshListView(Context context) {
		this(context, null);
	}

	public LoadRefreshListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoadRefreshListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		try {
			initView(context);
		} catch (Exception e) {
			Log.e("yyg", "有错");
			e.printStackTrace();
		}
	}

	/**
	 * 初始化 Head Foot 以及动画
	 */
	private void initView(Context context) throws Exception {
		initHeadView(context);
		initFootView(context);
		initAnimation();
		initListener();
	}

	/**
	 * 设置监听
	 */
	private void initListener() throws Exception {
		setOnScrollListener(this);
	}

	/**
	 * 初始化动画
	 */
	private void initAnimation() throws Exception {
		mHiddenFootViewAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
		mHiddenFootViewAnimation.setDuration(800);
		
		
		mShowFootViewAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowFootViewAnimation.setDuration(800);
		
		headMarkFromDownToUpAnimation = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		headMarkFromDownToUpAnimation.setDuration(500);
		headMarkFromDownToUpAnimation.setFillAfter(true);
		headMarkFromDownToUpAnimation.setInterpolator(new DecelerateInterpolator());
		
		headMarkFromUpToDownAnimation = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		headMarkFromUpToDownAnimation.setDuration(500);
		headMarkFromUpToDownAnimation.setFillAfter(true);
	}

	/**
	 * 初始化Foot
	 */
	private void initFootView(Context context) throws Exception {
//		footView = LayoutInflater.from(context).inflate(R.layout.foot_view, null);
		footView = LayoutInflater.from(context).inflate(MResource.getIdByName(context, "layout", "foot_view"), null);
		list_foot_view_show_msg = (TextView) footView.findViewById(MResource.getIdByName(context, "id", "list_foot_view_show_msg"));
		load_more_progressbar = (ProgressBar) footView.findViewById(MResource.getIdByName(context, "id", "load_more_progressbar"));
		addFooterView(footView);
	}

	/**
	 * 初始化Head
	 */
	private void initHeadView(Context context) throws Exception {
		headView = LayoutInflater.from(context).inflate(MResource.getIdByName(context, "layout", "head_view"), null);
		head_load_progressbar = (ProgressBar) headView.findViewById(MResource.getIdByName(context, "id", "head_load_progressbar"));
		head_load_status_icon = (ImageView) headView.findViewById(MResource.getIdByName(context, "id", "head_load_status_icon"));
		head_load_time = (TextView) headView.findViewById(MResource.getIdByName(context, "id", "head_load_time"));
		head_load_msg = (TextView) headView.findViewById(MResource.getIdByName(context, "id", "head_load_msg"));
		
		measureView(headView);

		headTotalHeight = headView.getMeasuredHeight();
		setHeadTopPadding(-headTotalHeight);
		addHeaderView(headView);
	}

	/**
	 * 设置head的顶部边距
	 * 
	 * @param topPadding
	 */
	private void setHeadTopPadding(int topPadding) {
		if(topPadding > (headTotalHeight + 200)){
			topPadding = headTotalHeight + 200;
		}
		headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),
				headView.getPaddingBottom());
		headView.invalidate();
	}
	/**
	 * 设置foot的底部边距
	 * 
	 * @param topPadding
	 */
	private void setFootBottomPadding(int bottomPadding) {
		footView.setPadding(footView.getPaddingLeft(), footView.getPaddingTop(), footView.getPaddingRight(),
				bottomPadding);
		footView.invalidate();
	}

	/**
	 * 告诉父布局 View占多大的高度
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidth = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int lpHeight = p.height;
		int childHeight = 0;
		if (lpHeight > 0) {
			childHeight = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeight = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}

		view.measure(childWidth, childHeight);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		/**
		 * 如果还有更多数据可以加载  则需要判断是否显示最后一项
		 */
			if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
				isLastItemVisible = true;
			} else {
				isLastItemVisible = false;
			}
			if(firstVisibleItem == 0){
				this.firstVisibleItem  = firstVisibleItem;
			}
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
	}
	
	/**
	 * 根据状态更新ListView的状态
	 * 
	 * @param footLoading
	 */
	private void updateListViewByState(int state,boolean isHidFoot) {
		switch (state) {
		case FOOT_NORMAL: {
			footView.setVisibility(View.VISIBLE);
			this.footCurrentState = state;
			load_more_progressbar.setVisibility(View.INVISIBLE);
			list_foot_view_show_msg.setText("上拉加载更多···");
			break;
		}
		case FOOT_LOADING: {
			footView.setVisibility(View.VISIBLE);
			this.footCurrentState = state;
			load_more_progressbar.setVisibility(View.VISIBLE);
			list_foot_view_show_msg.setText("正在努力加载数据···");
			// 通过接口回调给页面
			if(onLoadMoreAndRefreshListener != null){
				onLoadMoreAndRefreshListener.onLoadMore();
			}
			break;
		}
		case FOOT_NO_MORE: {
			this.footCurrentState = state;
			if(isHidFoot){
				footView.setVisibility(View.GONE);
			}
			load_more_progressbar.setVisibility(View.INVISIBLE);
			list_foot_view_show_msg.setText("不能加载更多···");
			break;
		}
		case HEAD_NORMAL:{
			this.headCurrentState = state;
			head_load_progressbar.setVisibility(View.GONE);
			head_load_status_icon.setVisibility(View.VISIBLE);
			head_load_msg.setText("下拉刷新");
			setHeadTopPadding(-headTotalHeight);
			break;
		}
		case HEAD_PULL:{
			this.headCurrentState = state;
			head_load_progressbar.setVisibility(View.GONE);
			head_load_status_icon.setVisibility(View.VISIBLE);
			head_load_msg.setText("下拉刷新");
			break;
		}
		
		case HEAD_RELEASE:{
			this.headCurrentState = state;
			head_load_progressbar.setVisibility(View.GONE);
			head_load_status_icon.setVisibility(View.VISIBLE);
			head_load_msg.setText("松开刷新");
			break;
		}
		
		case HEAD_REFRESHING:{
			this.headCurrentState = state;
			head_load_progressbar.setVisibility(View.VISIBLE);
			head_load_status_icon.clearAnimation();
			head_load_status_icon.setVisibility(View.GONE);
			setHeadTopPadding(0);
			head_load_msg.setText("正在刷新");
			// 通过接口回调给页面
			if(onLoadMoreAndRefreshListener != null){
				onLoadMoreAndRefreshListener.onRefresh();
			}
			break;
			
		}

		default:
			break;
		}
	}
	
	/**
	 * 没有更多数据可以加载
	 */
	public void noLoadMore(){
		updateListViewByState(FOOT_NO_MORE,false);
	}
	
	/**
	 * 没有更多数据可以加载 并且设置FootView是否隐藏
	 * @param isHidFoot true表示隐藏FootView
	 */
	public void noLoadMore(boolean isHidFoot){
		updateListViewByState(FOOT_NO_MORE,isHidFoot);
	}
	
	/**
	 * 完成刷新 或者 加载更多
	 */
	public void completeAction(){
		updateListViewByState(FOOT_NORMAL,false);
		updateListViewByState(HEAD_NORMAL,false);
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		head_load_time.setText("上次更新时间 "+sm.format(new Date(System.currentTimeMillis())));
		startY = -100;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		try {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN: {
				if((isLoadingMore && isLastItemVisible) || (isRefresh && firstVisibleItem == 0)){
					startY = (int) ev.getY();
				}
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				if(startY < 0 && ((isLoadingMore && isLastItemVisible) || (isRefresh && firstVisibleItem == 0))){
					startY = (int) ev.getY();
				}
				actionMove(ev);
				
				break;
			}
			case MotionEvent.ACTION_UP: {
				actionUp(ev);
				startY = -1;
				break;
			}
			
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 手指抬起
	 * @param ev
	 */
	private void actionUp(MotionEvent ev) {
		int upY = (int)ev.getY();
		if(upY > startY){
			//TODO 向下滑动 刷新数据
			if(isRefresh){
				setFootBottomPadding(0);
				if(headCurrentState == HEAD_RELEASE){
					updateListViewByState(HEAD_REFRESHING,false);
				}else{
					updateListViewByState(HEAD_NORMAL,false);
				}
			}
			
		}else{
			if(isLoadingMore){
				setFootBottomPadding(0);
			}
			if(isRefresh && headCurrentState != HEAD_REFRESHING){
				setHeadTopPadding(-headTotalHeight);
			}
		}
	}

	/**
	 * 手指移动
	 * @param ev
	 * @throws Exception 
	 */
	private void actionMove(MotionEvent ev) throws Exception {
		int moveY = (int)ev.getY();
		if(moveY > startY){
			//TODO 向下滑动 刷新数据
			setFootBottomPadding(0);
			if(isRefresh && firstVisibleItem == 0 && headCurrentState != HEAD_REFRESHING){
				switch (headCurrentState) {
				case HEAD_NORMAL:{
					int head_view_move_distance = (int)(moveY - startY)/RATIO;
					setHeadTopPadding(head_view_move_distance - headTotalHeight);
					updateListViewByState(HEAD_PULL,false);
					break;
				}
				case HEAD_PULL:{
					
					int head_view_move_distance = (int)(moveY - startY)/RATIO;
					setHeadTopPadding(head_view_move_distance - headTotalHeight);
					if(head_view_move_distance >= (headTotalHeight + (headTotalHeight/3))){
						updateListViewByState(HEAD_RELEASE,false);
						Log.e("yygtt", "head_view_move_distance = 切换到松开刷新");
						head_load_status_icon.clearAnimation();
						head_load_status_icon.startAnimation(headMarkFromDownToUpAnimation);
					}else{
						updateListViewByState(HEAD_PULL,false);
					}
					break;
				}
				case HEAD_RELEASE:{
					int head_view_move_distance = (int)(moveY - startY)/RATIO;
					Log.e("yygtt", "head_view_move_distance = "+head_view_move_distance+" moveY="+moveY+
							" startY="+startY+" headTotalHeight="+headTotalHeight+" 对比高度："+(headTotalHeight + (headTotalHeight/3)));
					setHeadTopPadding(head_view_move_distance - headTotalHeight);
					if(head_view_move_distance < (headTotalHeight + (headTotalHeight/3))){
						updateListViewByState(HEAD_PULL,false);
						Log.e("yygtt", "head_view_move_distance = 切换到下拉刷新");
						head_load_status_icon.clearAnimation();
						head_load_status_icon.startAnimation(headMarkFromUpToDownAnimation);
					}else{
						updateListViewByState(HEAD_RELEASE,false);
					}
					
					break;
				}
				
				

				default:
					break;
				}
			}
		}else{
			if(isLoadingMore && isLastItemVisible && footCurrentState != FOOT_NO_MORE){
				
				int foot_view_move_distance = startY - moveY;
				if(foot_view_move_distance > 200){
					foot_view_move_distance = 200;
				}
				setFootBottomPadding(foot_view_move_distance);
				
				if(footCurrentState == FOOT_NORMAL){
					updateListViewByState(FOOT_LOADING,false);
				}
			}
		}
	}

	/**
	 * 设置是否允许加载更多 和 刷新
	 * 
	 * @param isLoadingMore
	 *            true表示可以加载更多
	 * @param isRefresh
	 *            true表示可以下拉刷新
	 */
	public void setDragPermissions(boolean isLoadingMore, boolean isRefresh) {
		this.isLoadingMore = isLoadingMore;
		if(!isLoadingMore){
			footView.setVisibility(View.GONE);
		}
		this.isRefresh = isRefresh;
		
	}
	
	public interface OnLoadMoreAndRefreshListener{
		public void onRefresh();
		public void onLoadMore();
	}
	
	public OnLoadMoreAndRefreshListener getOnLoadMoreAndRefreshListener() {
		return onLoadMoreAndRefreshListener;
	}

	public void setOnLoadMoreAndRefreshListener(OnLoadMoreAndRefreshListener onLoadMoreAndRefreshListener) {
		this.onLoadMoreAndRefreshListener = onLoadMoreAndRefreshListener;
	}

}
