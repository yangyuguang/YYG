package com.yangyg.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
/**
 * App相关辅助类
 * @author Administrator
 *
 */
public class AppUtils {

	/**
	 * 获取应用程序名称
	 */
	public static String getAppName(Context context) {
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
			int labelRes = packageInfo.applicationInfo.labelRes;
			return context.getResources().getString(labelRes);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * [获取应用程序版本名称信息]
	 * 
	 * @param context
	 * @return 当前应用的版本名称
	 */
	public static String getVersionName(Context context) {
		try {
			return getPachageInfo(context).versionName;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * [获取应用程序版本号]
	 * 
	 * @param context
	 * @return 当前应用的版本号
	 */
	public static int getVersionCode(Context context) {
		try {
			return getPachageInfo(context).versionCode;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return -100;
	}
	
	/**
	 * 获取PackageInfo 对象
	 * @param context
	 * @return
	 * @throws Exception
	 */
	private static PackageInfo getPachageInfo(Context context) throws Exception{
		PackageManager packageManager = context.getPackageManager();
		return packageManager.getPackageInfo(context.getPackageName(), 0);
	}
	
}
