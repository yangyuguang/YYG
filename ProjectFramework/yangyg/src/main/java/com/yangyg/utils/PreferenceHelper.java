package com.yangyg.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;

public class PreferenceHelper {

	private Context context = null;
	
	private static PreferenceHelper instance = null;
	
	private PreferenceHelper(){}
	private PreferenceHelper(Context context){
		this.context = context;
	}
	
	public static PreferenceHelper getInstance(Context context){
		if(instance == null){
			instance = new PreferenceHelper(context);
		}
		return instance;
	}
	
	public boolean getBoolean(String key, boolean defValue) {
		
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getBoolean(key, defValue);
	}

	public void putBoolean(String key, boolean value) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public int getInt(String key, int defValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getInt(key, defValue);
	}

	public void putInt(String key, int value) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public long getLong(String key, long defValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getLong(key, defValue);
	}

	public void putLong(String key, long value) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public String getString(String key, String defValue) {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
		return settings.getString(key, defValue);
	}

	public void putString(String key, String value) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public void remove(String key) {
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		editor.remove(key);
		editor.commit();
	}

	public void registerOnPrefChangeListener(OnSharedPreferenceChangeListener listener) {
		try {
			PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void unregisterOnPrefChangeListener(OnSharedPreferenceChangeListener listener) {
		try {
			PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(listener);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
