package com.yangyg.utils;

import java.util.Stack;

import android.app.Activity;
/**
 * activity管理类
 * @author Administrator
 *
 */
public class ScreenManager {
	
	private static ScreenManager instance;
	private static Stack<Activity> activityStack;

	private ScreenManager() {
		
	}

	public static ScreenManager getScreenManager() {
		if (instance == null) {
			instance = new ScreenManager();
			activityStack = new Stack<Activity>();
		}
		return instance;
	}
	
	/**
	 * 判断Activity是否存在
	 * @param cls 
	 * @return true表示存在  false表示不存在
	 */
	public boolean isExist(Class cls){
		if(cls == null){
			return false;
		}
		for(int i=0;i<activityStack.size();i++){
			Activity aa = activityStack.get(i);
			if(aa.getClass() == cls){
				return true;
			}
		}
		return false;
	}

	/**
	 * 关闭最后一个Activity
	 */
	public void popLastActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 关闭指定的Activity
	 * @param activity 需要关闭的Activity
	 */
	public void popActivity(Activity activity) {
		if (activity != null) {
			activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}
	
	/**
	 * 获取当前Activity
	 * @return
	 */
	public Activity currentActivity() {
		Activity activity = null;
		if (activityStack != null && !activityStack.isEmpty()) {
			activity = activityStack.lastElement();
		}
		return activity;
	}
	
	/**
	 * 添加Activity到管理队列
	 * @param activity 需要添加到队列中的Activity
	 */
	public void pushActivity(Activity activity) {
		activityStack.add(activity);
	}

	/**
	 * 将某个Activity 移动到队列顶部
	 * @param cls
	 */
	public void startActivity(Class cls) {
		for (int i = 0; i < activityStack.size(); i++) {
			if (activityStack.get(i).getClass().equals(cls)) {
				activityStack.set(0, activityStack.get(i));
			}
		}
	}

	/**
	 * 关闭所有Activity cls除外
	 * @param cls 不能关闭的activity
	 */
	public void popAllActivityExceptOne(Class cls) {
		while (true) {
			Activity activity = currentActivity();
			if (activity == null) {
				break;
			}
			if (cls != null && activity.getClass().equals(cls)) {
				break;
			}
			popActivity(activity);
		}
	}

	/**
	 * 关闭指定的activity
	 * @param cls 需要关闭的activity
	 */
	public void popActivity(Class cls){
		for(int i=0;i<activityStack.size();i++){
			Activity activity = activityStack.get(i);
			if(cls == activity.getClass()){
				popActivity(activity);
				break;
			}
		}
	}

	// 退出
	public void exit(Class cls) {
		try {
			ScreenManager.getScreenManager().popAllActivityExceptOne(cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}

}
