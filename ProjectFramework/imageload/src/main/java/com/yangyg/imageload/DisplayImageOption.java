package com.yangyg.imageload;


import com.yangyg.imageload.listener.ImageLoaderProgressListener;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 此类用于设置显示 缓存图片需要用到的参数
 * 如：imageLoadingId（正在加载时显示的图片）、imageFail（加载失败显示的图片）、imageForEmptUri（图片地址为空时显示的图片）
 * isCacheMemory（是否可以缓存到内存 默认可以）、isCacheDisk（是否缓存到硬盘 默认可以）、cacheDiskPath（缓存到硬盘的路径）
 * ImageLoaderProgressListener 监听整个图片的下载过程
 * Created by 杨裕光 on 16/11/14.
 */
public class DisplayImageOption {


    int imageLoadingId = 0;
    int imageFail = 0;
    int imageForEmptUri = 0;

    //是否内存缓存
    AtomicBoolean isCacheMemory = new AtomicBoolean(true);
    //是否本地缓存
    AtomicBoolean isCacheDisk = new AtomicBoolean(true);
    //硬盘缓存的路径
    String cacheDiskPath = null;

    public ImageLoaderProgressListener getImageLoaderProgressListener() {
        return imageLoaderProgressListener;
    }

    public DisplayImageOption setImageLoaderProgressListener(ImageLoaderProgressListener imageLoaderProgressListener) {
        this.imageLoaderProgressListener = imageLoaderProgressListener;
        return this;
    }

    ImageLoaderProgressListener imageLoaderProgressListener;

    public int getImageLoadingId() {
        return imageLoadingId;
    }

    public DisplayImageOption setImageLoadingId(int imageLoadingId) {
        this.imageLoadingId = imageLoadingId;
        return this;
    }

    public int getImageFail() {
        return imageFail;
    }

    public DisplayImageOption setImageFail(int imageFail) {
        this.imageFail = imageFail;
        return this;
    }

    public int getImageForEmptUri() {
        return imageForEmptUri;
    }

    public DisplayImageOption setImageForEmptUri(int imageForEmptUri) {
        this.imageForEmptUri = imageForEmptUri;
        return this;
    }

    public boolean getIsCacheMemory() {
        return isCacheMemory.get();
    }

    public DisplayImageOption setIsCacheMemory(boolean isCacheMemory) {
        this.isCacheMemory.set(isCacheMemory);
        return this;
    }

    public boolean getIsCacheDisk() {
        return isCacheDisk.get();
    }

    public DisplayImageOption setIsCacheDisk(boolean isCacheDisk) {
        this.isCacheDisk.set(isCacheDisk);
        return this;
    }

    public String getCacheDiskPath() {
        return cacheDiskPath;
    }

    public DisplayImageOption setCacheDiskPath(String cacheDiskPath) {
        this.cacheDiskPath = cacheDiskPath;
        return this;
    }

    public static DisplayImageOption createSimple(){
        return new DisplayImageOption();
    }

}