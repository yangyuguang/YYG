package com.pami.http;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.pami.utils.MLog;

public class VolleyImageCache implements ImageCache {

	private LruCache<String, Bitmap> cache = null;
	private final int maxSize = 10 * 1024 * 1024;//10M
	private static VolleyImageCache install = null;
	
	public static VolleyImageCache getInstence(){
		if(install == null){
			install = new VolleyImageCache();
		}
		return install;
	}
	
	private VolleyImageCache(){
		cache = new LruCache<String, Bitmap>(maxSize){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		MLog.e("yyg", "Volley图片地址："+url);
		return cache.get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		cache.put(url, bitmap);
	}

}
