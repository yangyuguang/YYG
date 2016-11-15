package com.yangyg.imageload.cache;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * 缓存到内存
 * Created by 杨裕光 on 16/11/10.
 */
public class LruMemoryCache {

    private final LruCache<String, Bitmap> mCache;

    public LruMemoryCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 6;
        mCache = new LruCache<String, Bitmap>(cacheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public void put(String key, Bitmap value) {
        synchronized (this) {
            mCache.put(key, value);
        }
    }

    public Bitmap get(String key) {
        synchronized (this) {
            return mCache.get(key);
        }
    }
}
