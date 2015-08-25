package com.pami;

import java.util.LinkedList;
import java.util.List;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pami.activity.BaseActivity;
import com.pami.utils.MLog;

public class PMApplication extends Application {

	public boolean isDown;
	public boolean isRun;
	private RequestQueue requestQueue;
	/**
	 * 全局上下文对象
	 */
	private Context mContext;

	/**
	 * 将所有FragmentActivity存放在链表中，便于管理
	 */
	private List<FragmentActivity> activityList;

	/**
	 * 全局FragmentManager实例
	 */
	private FragmentManager mFragmentManager;

	/**
	 * 全局LApplication唯一实例
	 */
	private static PMApplication instance;

	/**
	 * 全局的是否打开DEBUG模式<br />
	 * 默认为true
	 */
	private boolean dev_mode = true;

	/**
	 * 全局屏幕宽度
	 */
	private int mDiaplayWidth;

	/**
	 * 全局屏幕高度
	 */
	private int mDiaplayHeight;

	/**
	 * 全局APP服务器主路径
	 */
	private String appBaseUrl;
	
	/**
	 * 是否允许销毁所有Activity
	 * 默认为true
	 */
	private boolean destroyActivitys = true;
	
	private String exceptionUrl = null;

	@Override
	public void onCreate() {
		super.onCreate();
		MLog.e("yyg", "执行onCreate方法");
		instance = this;
		this.requestQueue = Volley.newRequestQueue(getApplicationContext());
	}
	
	/**
	 * 获取请求队列
	 * @return
	 */
	public RequestQueue getRequestQueue(){
		MLog.e("yyg", "获取请求队列");
		return requestQueue;
	}
	
	/**
	 * 撤销请求
	 * @param requestTag
	 */
	public void clearRequest(String requestTag){
		if(requestQueue == null){
			return;
		}
		requestQueue.cancelAll(requestTag);
	}
	
	/**
	 * 获取一个LApplication的实例
	 * 
	 * @return
	 */
	public static synchronized PMApplication getInstance() {
		if (instance == null) {
			instance = new PMApplication();
		}
		return instance;
	}

	/**
	 * 设置一个LApplication的实例
	 * 
	 * @param app
	 */
	public static void setLApplication(PMApplication app) {
		instance = app;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @return
	 */
	public int getDiaplayWidth() {
		if (mDiaplayWidth <= 0) {
			computeDiaplayWidthAndHeight();
		}
		return mDiaplayWidth;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @return
	 */
	public int getDiaplayHeight() {
		if (mDiaplayHeight <= 0) {
			computeDiaplayWidthAndHeight();
		}
		return mDiaplayHeight;
	}

	private void computeDiaplayWidthAndHeight() {
		WindowManager mWindowManager = ((BaseActivity) getContext()).getWindowManager();
		Display display = mWindowManager.getDefaultDisplay();
		mDiaplayWidth = display.getWidth();
		mDiaplayHeight = display.getHeight();
	}

	/**
	 * 
	 * @return 获取上下文对象
	 */
	public Context getContext() {
		if (mContext == null) {
			mContext = this;
		}
		return mContext;
	}

	/**
	 * 设置上下文对象
	 * 
	 * @param mContext
	 */
	public void setContext(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * 添加一个Activity到数组里
	 * 
	 * @param activity
	 */
	public void addActivity(FragmentActivity activity) {
		if (this.activityList == null) {
			activityList = new LinkedList<FragmentActivity>();
		}
		this.activityList.add(activity);
	}

	/**
	 * 删除一个Activity在数组里
	 * 
	 * @param activity
	 */
	public void delActivity(FragmentActivity activity) {
		if (activityList != null) {
			activityList.remove(activity);
		}
	}

	/**
	 * 设置一个 FragmentManager 对象
	 * 
	 * @param fm
	 */
	public void setFragmentManager(FragmentManager fm) {
		this.mFragmentManager = fm;
	}

	/**
	 * 
	 * @return 获取一个 FragmentManager 对象
	 */
	public FragmentManager getFragmentManager() {
		return this.mFragmentManager;
	}

	/**
	 * 完全退出应用<br />
	 * 需要设置destroyActivitys为true
	 */
	public void exitApp() {
		if (destroyActivitys) {
			for (FragmentActivity f : activityList) {
				f.finish();
			}
			System.exit(0);
		}
	}

	/**
	 * 获取是否启用Debug模式
	 * 
	 * @return
	 */
	public boolean getDevMode() {
		return dev_mode;
	}

	/**
	 * 设置是否启用Debug模式
	 * 
	 * @param openDebugMode
	 */
	public void setDevMode(boolean openDebugMode) {
		this.dev_mode = openDebugMode;
	}

	/**
	 * 获取应用主服务器路径
	 * 
	 * @return
	 */
	public String getAppBaseUrl() {
		return appBaseUrl;
	}

	/**
	 * 设置应用主服务器路径
	 * 
	 * @param appServiceUrl
	 */
	public void setAppBaseUrl(String appBaseUrl) {
		this.appBaseUrl = appBaseUrl;
	}

	/**
	 * 获取应用是否允许销毁所有Activity
	 * 
	 * @return
	 */
	public boolean getIsDestroyActivitys() {
		return destroyActivitys;
	}
	
	/**
	 * 获取存放异常的路径
	 * @return
	 */
	public String getExceptionUrl() {
		return exceptionUrl;
	}

	/**
	 * 设置存放异常的路径
	 * @param exceptionUrl
	 */
	public void setExceptionUrl(String exceptionUrl) {
		this.exceptionUrl = exceptionUrl;
	}

	/**
	 * 设置应用是否允许销毁所有Activity
	 * 
	 * @param destroyActivitys
	 */
	public void setDestroyActivitys(boolean destroyActivitys) {
		this.destroyActivitys = destroyActivitys;
	}
	
}
