package com.pami.activity;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pami.PMApplication;
import com.pami.SystemBarTintManager;
import com.pami.http.ExceptionUtils;
import com.pami.listener.AppDownLineListener;
import com.pami.listener.AppLisntenerManager;
import com.pami.listener.HttpActionListener;
import com.pami.listener.ViewInit;
import com.pami.utils.HidenSoftKeyBoard;
import com.pami.utils.JsonUtils;
import com.pami.utils.MLog;
import com.pami.utils.NetUtils;
import com.pami.utils.ScreenManager;
import com.pami.utils.ScreenUtils;
import com.pami.utils.Util;

public abstract class BaseActivity extends FragmentActivity implements ViewInit, HttpActionListener, OnClickListener,
		AppDownLineListener {

	private FrameLayout activity_base_titlebar = null;
	private FrameLayout activity_base_content = null;
	private AnimationDrawable loading = null;

	private Dialog loadingDialog;
	private boolean is_hidKeyDown = true;
	private TextView base_activity_line = null;
	private View loding_layout;

	public int barHeight;
	public PMApplication myApplication;

	public Context mContext;
	private Toast mToast;

	private SlidingPaneLayout mSlidingPaneLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		try {

			loadViewbefore();
			setVolumeControlStream(AudioManager.STREAM_MUSIC);// 使得音量键控制媒体声音
			mContext = this;
			setContentView(getResources().getIdentifier("base_activity_layout", "layout", getPackageName()));

			activity_base_titlebar = (FrameLayout) findViewById(getResources().getIdentifier("activity_base_titlebar",
					"id", getPackageName()));
			activity_base_content = (FrameLayout) findViewById(getResources().getIdentifier("activity_base_content",
					"id", getPackageName()));

			base_activity_line = (TextView) findViewById(getResources().getIdentifier("base_activity_line", "id",
					getPackageName()));

			loding_layout = findViewById(getResources().getIdentifier("loding_layout", "id", getPackageName()));

			myApplication = PMApplication.getInstance();
			myApplication.setContext(mContext);
			myApplication.addActivity(this);
			myApplication.setContext(this);
			myApplication.setFragmentManager(this.getSupportFragmentManager());


			setTitleBar(getResources().getIdentifier("titlebar_base", "layout", getPackageName()));
			setBarColor();
//			showTitleBar();
			initViewFromXML();
			initData();
			fillCacheData();
			fillView();
			initListener();
			ScreenManager.getScreenManager().pushActivity(this);

		} catch (Exception e) {
			MLog.e("yyg", "构建view有错【BaseActivity】:" + e.toString());
			e.printStackTrace();
			ExceptionUtils.uploadException(this, e, PMApplication.getInstance().getExceptionUrl(), "exception.action");
		}
	}

	public void loadViewbefore() {

	}

	private void setBarColor() {
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(getResources().getIdentifier("colorPrimaryDark", "color", getPackageName()));
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
			MLog.e("yyg", "Toast有错");
			e.printStackTrace();
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
	public void handleActionError(String actionName, Object object) {
		try {
			String result = object.toString();
			if (result.indexOf("code") >= 0) {
				int code = JsonUtils.getCode(result);
				switch (code) {
				case -1: {
					MLog.e("yyg", "返回-1");
					Toast.makeText(this, JsonUtils.getSuccessData(result, "error_text"), 1).show();
					break;
				}
				case -9: {
					MLog.e("yyg", "返回-9");
					Toast.makeText(this, JsonUtils.getSuccessData(result, "error_text"), 1).show();
					break;
				}

				default:
					break;
				}
			} else {
				Toast.makeText(this, result, 1).show();
			}

		} catch (Exception e) {
			MLog.e("yyg", "BaseActivity 【handleActionError】 错误回调有错");
			e.printStackTrace();
		}
	}

	@Override
	public void handleActionSuccess(String actionName, Object object) {
		MLog.i("ssss", actionName);
	}

	@Override
	public void handleActionStart(String actionName, Object object) {

	}

	@Override
	public void handleActionFinish(String actionName, Object object) {

	}

	public void showLoadingDialog() {
		if (NetUtils.isNetworkConnected(BaseActivity.this)) {
			showLoadingDialog(this.getString(getResources().getIdentifier("text_loading", "string", getPackageName())));
		}

	}

	public boolean loadingIdVisible() {
		return loding_layout.getVisibility() == View.VISIBLE;
	}

	public void showLoadingDialog(String loadingStrs) {
		dismissDialog();

		AlertDialog.Builder builder = new Builder(BaseActivity.this);
		loadingDialog = builder.create();
		loadingDialog.show();
		LayoutInflater inflater = LayoutInflater.from(BaseActivity.this);
		View view = inflater.inflate(getResources().getIdentifier("loading_dialog_view", "layout", getPackageName()),
				null);
		ImageView loading_mark_iv = (ImageView) view.findViewById(getResources().getIdentifier("loading_mark_iv", "id",
				getPackageName()));
		TextView loding_mark_tv = (TextView) view.findViewById(getResources().getIdentifier("loding_mark_tv", "id",
				getPackageName()));
		loding_mark_tv.setText(loadingStrs);
		loading = (AnimationDrawable) loading_mark_iv.getDrawable();

		loadingDialog.setContentView(view);
		Window dialogWindow = loadingDialog.getWindow();
		loadingDialog.setCanceledOnTouchOutside(false);
		dialogWindow.setGravity(Gravity.CENTER);
		android.view.WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
		layoutParams.height = getWindowManager().getDefaultDisplay().getWidth() * 2 / 3;
		layoutParams.width = getWindowManager().getDefaultDisplay().getWidth() * 2 / 3;
		dialogWindow.setAttributes(layoutParams);
		loading.start();
		loadingDialog.show();
	}

	public void dismissDialog() {
		if (loadingDialog != null) {
			try {
				loading.stop();
				loadingDialog.dismiss();
				loadingDialog = null;
			} catch (Exception e) {
				e.printStackTrace();
				MLog.e("yyg", "去掉加载框有错。");
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (is_hidKeyDown) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				dismissDialog();
				View v = getCurrentFocus();
				if (HidenSoftKeyBoard.isShouldHideInput(v, ev)) {
					HidenSoftKeyBoard.hideSoftInput(v.getWindowToken(), getApplication());
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ScreenManager.getScreenManager().popActivity(BaseActivity.this);
	}

	@Override
	public void onClick(View v) {
		// 如果所有按钮都需要校验的操作可以放在此处
		if (Util.isFastClick()) {
			// 默认是连续点击
			return;
		}
		onButtonClick(v);
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
	protected void onButtonClick(View v) {
	}

	@Override
	public void onDowLine() {
		onAppDownLine();
	}

	/**
	 * app下线
	 * 
	 * @param v
	 */
	public abstract void onAppDownLine();

	
}
