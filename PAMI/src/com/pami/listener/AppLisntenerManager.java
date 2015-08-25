package com.pami.listener;

public class AppLisntenerManager {

	private static AppLisntenerManager instance = null;

	private AppLisntenerManager() {

	}

	public static AppLisntenerManager getInstance() {
		if (instance == null) {
			instance = new AppLisntenerManager();
		}
		return instance;
	}
	//-------------------
	
	private AppDownLineListener appDownLineListener = null;

	public AppDownLineListener getAppDownLineListener() {
		return appDownLineListener;
	}

	public void setAppDownLineListener(
			AppDownLineListener appDownLineListener) {
		this.appDownLineListener = appDownLineListener;
	}

}
