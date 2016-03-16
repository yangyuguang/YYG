package com.yangyg.utils;

import android.app.Activity;
import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 键盘显示操作工具
 * @author Administrator
 *
 */
public class HidenSoftKeyBoard {


	public static boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] l = { 0, 0 };
			v.getLocationInWindow(l);
			int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
					+ v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		
		return false;
	}
	
	public static void hideSoftInput(IBinder token,Context context) {
		if (token != null) {
			InputMethodManager im = (InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
			im.hideSoftInputFromWindow(
				token, InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * 隐藏软键盘
	 */
	public static void hideKeyboard(Activity activity) {
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null) {
				InputMethodManager manager = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				manager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}
}
