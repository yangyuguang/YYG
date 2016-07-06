package com.yangyg.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yangyg.R;
import com.yangyg.SystemBarTintManager;
import com.yangyg.YYGApplication;
import com.yangyg.listener.AppDownLineListener;
import com.yangyg.listener.AppLisntenerManager;
import com.yangyg.listener.HttpActionListener;
import com.yangyg.listener.ViewInit;
import com.yangyg.utils.HidenSoftKeyBoard;
import com.yangyg.utils.NetUtils;
import com.yangyg.utils.ScreenManager;
import com.yangyg.utils.ScreenUtils;
import com.yangyg.utils.Util;
import com.yangyg.widget.LoadingDialog;
import com.yangyg.widget.LoadingDialog.OnDesmissListener;
import com.yangyg.widget.switchback.SlidingPaneLayout;

import java.lang.reflect.Field;
import java.util.List;


public abstract class BaseActivity extends FragmentActivity implements ViewInit, HttpActionListener, OnClickListener, AppDownLineListener, SlidingPaneLayout.PanelSlideListener{

	private FrameLayout activity_base_titlebar = null;
	private FrameLayout activity_base_content = null;

	private LoadingDialog loadingDialog;
	/** dialog是否显示 */
	private boolean loadingDialogIsShow = false;
	private boolean is_hidKeyDown = true;
	private TextView base_activity_line = null;
	private View leftView;

	private Toast mToast;

	private HorizontalScrollView vp = null;
//	public LinearLayout app_context_root_layout = null;

	@Override
	protected void onCreate(Bundle arg0) {
		initSwipeBackFinish();
		super.onCreate(arg0);

		try {
			loadViewbefore();
			setContentView(getResources().getIdentifier("base_activity_layout", "layout", getPackageName()));

			activity_base_titlebar = (FrameLayout) findViewById(getResources().getIdentifier("activity_base_titlebar", "id", getPackageName()));
			activity_base_content = (FrameLayout) findViewById(getResources().getIdentifier("activity_base_content", "id", getPackageName()));
//			app_context_root_layout = (LinearLayout) findViewById(getResources().getIdentifier("app_context_root_layout", "id", getPackageName()));

			base_activity_line = (TextView) findViewById(getResources().getIdentifier("base_activity_line", "id", getPackageName()));


			View navigationBarHeight = findViewById(getResources().getIdentifier("navigationBarHeight", "id", getPackageName()));
			if (YYGApplication.getInstance().isHasNavigationBar) {
				navigationBarHeight.setVisibility(View.VISIBLE);
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) navigationBarHeight.getLayoutParams();
				lp.height = ScreenUtils.getNavigationBarHeight(this);
				lp.width = YYGApplication.getInstance().getDiaplayWidth();
				navigationBarHeight.setLayoutParams(lp);
			} else {
				navigationBarHeight.setVisibility(View.GONE);
			}

			setTitleBar(getResources().getIdentifier("titlebar_base", "layout", getPackageName()));
			setBarColor();
			initViewFromXML();
			initData();
			fillCacheData();
			fillView();
			initListener();
			ScreenManager.getScreenManager().pushActivity(this);

		} catch (Exception e) {
			uploadException(e);
		}
	}

	/**
	 * 初始化滑动返回
	 */
	private void initSwipeBackFinish() {

		if (isSupportSwipeBack()) {
			SlidingPaneLayout slidingPaneLayout = new SlidingPaneLayout(this);
			slidingPaneLayout.setmTouchSwitchBackSize(setMTouchSwitchBack());
			//通过反射改变mOverhangSize的值为0，这个mOverhangSize值为菜单到右边屏幕的最短距离，默认
			//是32dp，现在给它改成0
			try {
				//属性
				Field f_overHang = SlidingPaneLayout.class.getDeclaredField("mOverhangSize");
				f_overHang.setAccessible(true);
				f_overHang.set(slidingPaneLayout, 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			slidingPaneLayout.setPanelSlideListener(this);
			slidingPaneLayout.setSliderFadeColor(getResources().getColor(android.R.color.transparent));

			leftView = new View(this);
			leftView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
			slidingPaneLayout.addView(leftView, 0);

			ViewGroup decor = (ViewGroup) getWindow().getDecorView();
			ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
			decorChild.setBackgroundColor(getResources().getColor(android.R.color.white));
			decor.removeView(decorChild);
			decor.addView(slidingPaneLayout);
			slidingPaneLayout.addView(decorChild, 1);

		}
	}

	/**
	 * 是否支持滑动返回
	 *
	 * @return
	 */
	protected boolean isSupportSwipeBack() {
		return true;
	}

	/**
	 * 设置滑动返回 点击的x区域
	 * @return
     */
	protected float setMTouchSwitchBack(){
		return 100f;
	}
	/**
	 * 上传log 日志
	 * 注意调用此方法 app 必须重写PMApplication 并在 onCreate方法中 调用 setExceptionUrl(url) 将上传log信息的URL注入系统。否则将调用无效 。
	 * 最后别忘记在清单文件中注册 重写的 PMApplication
	 * 
	 * MLog.e("yyg", "有错误信息 ， 请认真查看log信息");
		e.printStackTrace();
		ExceptionUtils.uploadException(this, e, PMApplication.getInstance().getExceptionUrl());
	 * @param e
	 */
	protected void uploadException(Exception e){};

	public void loadViewbefore() throws Exception {

	}

	/**
	 * 设置titlebar的颜色 颜色值在color.xml文件的 colorPrimaryDark 颜色值
	 */
	private void setBarColor() throws Exception {
		setBarColor(getResources().getIdentifier("colorPrimaryDark", "color", getPackageName()));
	}

	/**
	 * 指定titlebar的颜色
	 * 
	 * @param colorId
	 *            颜色ID
	 */
	private void setBarColor(int colorId) throws Exception {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(colorId);
		}
	}

	/**
	 * 显示提示
	 * 
	 * @param content
	 */
	public void showToast(String content) {
		try {
			mToast = Toast.makeText(BaseActivity.this, content, Toast.LENGTH_SHORT);
			Util.setToast(mToast);
			mToast.show();
		} catch (Exception e) {
			uploadException(e);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppLisntenerManager.getInstance().setAppDownLineListener(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppLisntenerManager.getInstance().setAppDownLineListener(this);
	}

	/**
	 * 设置标题栏布局
	 * 
	 * @param layoutResID
	 */
	public void setTitleBar(int layoutResID) throws Exception {
		activity_base_titlebar.setVisibility(View.VISIBLE);
		activity_base_titlebar.removeAllViews();
		LayoutInflater.from(this).inflate(layoutResID, activity_base_titlebar);
	}

	/**
	 * 显示标题栏
	 */
	protected void showTitleBar() throws Exception {
		activity_base_titlebar.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏标题栏
	 */
	protected void hideTitleBar() throws Exception {
		activity_base_titlebar.setVisibility(View.GONE);
		base_activity_line.setVisibility(View.GONE);
	}

	/**
	 * 设置标题栏
	 * 
	 * @param child
	 */
	public void setTitleBar(View child) throws Exception {
		activity_base_titlebar.removeAllViews();
		activity_base_titlebar.addView(child);
	}

	/**
	 * 获取activity标题栏
	 * 
	 * @return
	 */
	public ViewGroup getTitleBar() throws Exception {
		return activity_base_titlebar;
	}

	/**
	 * 填充缓存数据
	 */
	protected void fillCacheData() throws Exception {

	}

	/**
	 * 设置内容布局
	 * 
	 * @param layoutResID
	 */
	protected void setContent(int layoutResID) throws Exception {
		activity_base_content.removeAllViews();
		LayoutInflater.from(this).inflate(layoutResID, activity_base_content);
	}

	@Override
	public void handleActionStart(String actionName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleActionFinish(String actionName, Object object) {
		// TODO Auto-generated method stub

	}

	/**
	 * 显示加载中的踢提示框 使用默认的提示文字
	 * 
	 * @param httpTags
	 * @throws Exception
	 */
	public void showLoadingDialog(String... httpTags) throws Exception {
		if (NetUtils.isNetworkConnected(BaseActivity.this)) {
			setLoadingDialogHintMessage(this.getString(getResources().getIdentifier("text_loading", "string", getPackageName())));
			showLoadingDialogByhttpTags(httpTags);
		}
	}

	/**
	 * 显示加载中的踢提示框 使用默认的提示文字
	 * 
	 * @param httpTags
	 * @throws Exception
	 */
	public void showLoadingDialogAndHint(String hintMessage, String... httpTags) throws Exception {
		if (NetUtils.isNetworkConnected(BaseActivity.this)) {
			setLoadingDialogHintMessage(hintMessage);
			showLoadingDialogByhttpTags(httpTags);
		}
	}

	/**
	 * 设置dialog的提示信息
	 * 
	 * @param str
	 */
	private void setLoadingDialogHintMessage(String str) throws Exception {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(str);
			loadingDialog.setOnDesmissListener(new OnDesmissListener() {

				@Override
				public void onDismiss(List<String> httpFlag) {
					try {
						loadingDialogIsShow = false;
						if (httpFlag != null && !httpFlag.isEmpty()) {
//							clearHttpRequest(httpFlag);
						}
					} catch (Exception e) {
						uploadException(e);
					}
				}
			});
		} else {
			this.loadingDialog.setHintMessage(str);
		}
	}

	/**
	 * 显示加载的dialog
	 * 
	 * @param httpTags
	 * @throws Exception
	 */
	private void showLoadingDialogByhttpTags(String... httpTags) throws Exception {
		dismissDialog();
		loadingDialog.setHttpFlags(httpTags);
		loadingDialog.show(getSupportFragmentManager(), "loadingDialog" + System.currentTimeMillis());
		loadingDialogIsShow = true;
	}

//	/**
//	 * 根据tag撤销请求
//	 * 
//	 * @param httpTag
//	 */
//	private void clearHttpRequest(String httpTag) throws Exception {
//		if (TextUtils.isEmpty(httpTag)) {
//			return;
//		}
//		PMApplication.getInstance().clearRequest(httpTag);
//	}

//	/**
//	 * 根据tag撤销请求
//	 * 
//	 * @param httpTag
//	 */
//	private void clearHttpRequest(List<String> httpTags) throws Exception {
//		for (String httpTag : httpTags) {
//			clearHttpRequest(httpTag);
//		}
//	}

	public void dismissDialog() throws Exception {
		if (loadingDialog != null && loadingDialogIsShow) {
				loadingDialog.dismiss();
				loadingDialog = null;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		try {

			if (is_hidKeyDown) {
				if (ev.getAction() == MotionEvent.ACTION_DOWN) {
					dismissDialog();
					View v = getCurrentFocus();
					if (HidenSoftKeyBoard.isShouldHideInput(v, ev)) {
						HidenSoftKeyBoard.hideSoftInput(v.getWindowToken(), getApplication());
					}
				}
			}
		} catch (Exception e) {
			uploadException(e);
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		try {
			dismissDialog();
			activity_base_titlebar = null;
			activity_base_content = null;
			base_activity_line = null;
			if(mToast != null){
				mToast.cancel();
				mToast = null;
			}
		} catch (Exception e) {
			uploadException(e);
		}
	}

	@Override
	public void onClick(View v) {
		try {

			// 如果所有按钮都需要校验的操作可以放在此处
			if (Util.isFastClick()) {
				// 默认是连续点击
				return;
			}
			onButtonClick(v);
		} catch (Exception e) {
			uploadException(e);
		}
	}

	/**
	 * 手指触碰其它区域 键盘是否可以隐藏
	 * 
	 * @param is_hidKeyDown
	 *            true 表示可以隐藏 false表示不能隐藏
	 */
	public void setHidKeyDown(boolean is_hidKeyDown) {
		this.is_hidKeyDown = is_hidKeyDown;
	}

	/**
	 * 按钮点击方法的回调接口 防止连续点击
	 * 
	 * @param v
	 */
	protected void onButtonClick(View v) throws Exception {
	}

	@Override
	public void onDowLine() {
		try {
			onAppDownLine();
		} catch (Exception e) {
			uploadException(e);
		}
	}

	/**
	 * app下线
	 * 
	 * @param
	 */
	public abstract void onAppDownLine() throws Exception;

	/**
	 * 销毁activity
	 */
	public void finishActivity() {
		try {
			ScreenManager.getScreenManager().popActivity(BaseActivity.this);
		} catch (Exception e) {
			uploadException(e);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(getSupportFragmentManager().getBackStackEntryCount() <= 1){
				finishActivity();
			}else{
				getSupportFragmentManager().popBackStack();
				return true;
			}

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onPanelClosed(View view) {

	}

	@Override
	public void onPanelOpened(View view) {
		finishActivity();
		this.overridePendingTransition(0, R.anim.slide_out_right);
	}

//	private BaseActivity reciprocalSecondActivity = null;
	@Override
	public void onPanelSlide(View view, float v) {
//		if(reciprocalSecondActivity == null){
//			int activitySize = ScreenManager.getScreenManager().getActivitySize();
//			reciprocalSecondActivity = (BaseActivity) ScreenManager.getScreenManager().getActivityByIndex(activitySize - 2);
//		}
//
//		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) reciprocalSecondActivity.app_context_root_layout.getLayoutParams();
//		int leftMargin = (int) ((1-v) * 300f);
//		lp.leftMargin = -leftMargin;
//		reciprocalSecondActivity.app_context_root_layout.setLayoutParams(lp);
	}

	/**
	 * 添加Fragment
	 * @param fragment
	 */
	protected void addFragment(Fragment fragment)throws Exception{
		if(fragment != null){
			if(getFragmentLayoutId() == -100){
				showToast("");
				return;
			}
			FragmentManager manager = getSupportFragmentManager();
			Fragment oldFragment = manager.findFragmentByTag(fragment.getClass().getSimpleName());
			FragmentTransaction ft = manager.beginTransaction();
			if(manager.getFragments() != null && !manager.getFragments().isEmpty()){
				ft.setCustomAnimations(R.anim.home_push_leftin, R.anim.home_push_leftout,R.anim.home_push_rightin,R.anim.home_push_rightout);
			}
			if(oldFragment == null){
				ft.add(getFragmentLayoutId(), fragment, fragment.getClass().getSimpleName()).addToBackStack(fragment.getClass().getSimpleName()).commitAllowingStateLoss();
			}else{
				ft.replace(getFragmentLayoutId(), oldFragment, oldFragment.getClass().getSimpleName()).addToBackStack(oldFragment.getClass().getSimpleName()).commitAllowingStateLoss();
			}
		}
	}

	/**
	 * 移除当前Fragment
	 */
	protected void removeFragment()throws Exception{
		if(getSupportFragmentManager().getBackStackEntryCount() > 1){
			getSupportFragmentManager().popBackStack();
		}else{
			finishActivity();
		}
	}

	/**
	 * 获取布局中Fragment的ID
	 * @return
	 */
	protected int getFragmentLayoutId()throws Exception{
		return -100;
	};

}
