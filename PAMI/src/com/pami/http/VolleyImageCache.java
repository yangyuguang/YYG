package com.pami.http;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.pami.PMApplication;
import com.pami.utils.MLog;

public class VolleyImageCache implements ImageCache {

	private static VolleyImageCache install = null;
	
	public static VolleyImageCache getInstence(){
		if(install == null){
			install = new VolleyImageCache();
		}
		return install;
	}
	
	private VolleyImageCache(){
		
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		MLog.e("yyg", "Volley图片地址："+url);
		return PMApplication.getInstance().getmImageLoaderCache().get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		PMApplication.getInstance().getmImageLoaderCache().put(url, bitmap);
	}

}
