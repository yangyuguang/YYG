package com.pami;

import java.lang.reflect.Method;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.util.ArrayMap;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.pami.activity.BaseActivity;

public class PMApplication extends Application {

	public boolean isDown;
	public boolean isRun;
	
	/**
	 * 是否存在 NavigationBar  true 表示存在  false 表示不存在
	 */
	public boolean isHasNavigationBar;
	
	/**
	 * 全局的volley请求队列
	 */
	private RequestQueue requestQueue;
	/**
	 * 全局上下文对象
	 */
	private Context mContext;

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
	 * 上传log的地址
	 */
	private String exceptionUrl = null;
	

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		this.requestQueue = Volley.newRequestQueue(getApplicationContext());
		isHasNavigationBar = checkDeviceHasNavigationBar(this);
	}
	
	/**
	 * 获取请求队列
	 * @return
	 */
	public RequestQueue getRequestQueue(){
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

	/**
	 * 初始化屏幕的宽和高
	 */
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
	 * 获取屏幕是否存在 NavigationBar
	 * @param context
	 * @return
	 */
	private boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }
	
}
