package com.yangyg;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.Display;
import android.view.WindowManager;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.yangyg.activity.BaseActivity;
import com.yangyg.http.BaseHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;


public class YYGApplication extends Application {

//	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	public OkHttpClient okHttpClient = null;

	protected long connectionTimeOut = 5;
	protected long writeTimeout = 10;
	protected long readTimeOut = 30;

//	public boolean isDown;
//	public boolean isRun;

	/**
	 * 是否存在 NavigationBar true 表示存在 false 表示不存在
	 */
	public boolean isHasNavigationBar;

	/**
	 * 全局上下文对象
	 */
	private Context mContext;

	/**
	 * 全局LApplication唯一实例
	 */
	private static YYGApplication instance;

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
		isHasNavigationBar = checkDeviceHasNavigationBar(this);

	}

	/**
	 * 初始化默认的 okHttpClient
	 * 
	 * @return
	 */
	public synchronized OkHttpClient getDefaultOkHttpClient() {
		if (okHttpClient == null) {
			synchronized (BaseHttpRequest.class) {
				if (okHttpClient != null) {
					return okHttpClient;
				}
				okHttpClient = new OkHttpClient();
				// 设置cookie
				okHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
				okHttpClient.setConnectTimeout(connectionTimeOut, TimeUnit.SECONDS);
				okHttpClient.setWriteTimeout(writeTimeout, TimeUnit.SECONDS);
				okHttpClient.setReadTimeout(readTimeOut, TimeUnit.SECONDS);

				try {
					setCertificates(getAssets().open("Kangs100.cer"));
					// setCertificates(getAssets().open("kyfw.12306.cn.cer"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return okHttpClient;
	}

	public void setCertificates(InputStream... certificates) {
		try {
			CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null);
			int index = 0;
			for (InputStream certificate : certificates) {
				String certificateAlias = Integer.toString(index++);
				keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

				try {
					if (certificate != null)
						certificate.close();
				} catch (IOException e) {
				}
			}

			SSLContext sslContext = SSLContext.getInstance("TLS");

			TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

			trustManagerFactory.init(keyStore);
			sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
			okHttpClient.setSslSocketFactory(sslContext.getSocketFactory());

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取一个LApplication的实例
	 * 
	 * @return
	 */
	public static synchronized YYGApplication getInstance() {
		if (instance == null) {
			instance = new YYGApplication();
		}
		return instance;
	}

	/**
	 * 设置一个LApplication的实例
	 * 
	 * @param app
	 */
	public static void setLApplication(YYGApplication app) {
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
	 * 
	 * @return
	 */
	public String getExceptionUrl() {
		return exceptionUrl;
	}

	/**
	 * 设置存放异常的路径
	 * 
	 * @param exceptionUrl
	 */
	public void setExceptionUrl(String exceptionUrl) {
		this.exceptionUrl = exceptionUrl;
	}

	/**
	 * 获取屏幕是否存在 NavigationBar
	 * 
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
