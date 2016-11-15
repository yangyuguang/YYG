package com.yangyg.imageload;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.yangyg.imageload.download.ImageDownloadInfo;
import com.yangyg.imageload.download.ImageSourceType;
import com.yangyg.imageload.imageaware.ImageViewAware;
import com.yangyg.imageload.task.LoadAndDisplayImageNetworkTask;


/**
 * 加载通过 通过线程池 +  阻塞队列 的方式加载图片
 * 准备工作：
 * 1、ImageView的描述对象  控件的宽，高
 * 2、三级缓存数据（内存、硬盘）
 * 3、加载图片的设置（加载顺序：先进先出/后进先出）
 * 4、图片下载下来后对图片进行处理并显示
 * <p>
 * Created by 杨裕光 on 16/11/9.
 */
public class ImageLoader {

    private ImageLoaderConfiger mImageLoaderConfiger = null;

    private static ImageLoader mImageLoader = null;
    private Handler mHandler = new Handler();

    public static ImageLoader getInstance() {
        if (mImageLoader == null) {
            synchronized (ImageLoader.class) {
                if (mImageLoader == null) {
                    mImageLoader = new ImageLoader();
                }
            }
        }
        return mImageLoader;
    }

    private ImageLoader() {

    }

    public void init(Context context){
        mImageLoaderConfiger = new ImageLoaderConfiger(context);
    }

    public void displayImage(final String url, final ImageView imageView, DisplayImageOption displayImageOption) {

        if (imageView == null) {
            throw new IllegalArgumentException("dispaly view is null。");
        }
        imageView.setTag(url);
        this.displayImage(url, new ImageViewAware(imageView), displayImageOption);
    }

    private void displayImage(final String url, final ImageViewAware imageView, DisplayImageOption displayImageOption) {

        imageView.clearImageView();

        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(url.trim()) || "null".equals(url)) {
            // 地址为空 并且用户设置了地址为空时显示的图片 则显示用户设置的图片即可，否则不显示。
            if (displayImageOption.imageForEmptUri != 0) {
                imageView.setImageResource(displayImageOption.imageForEmptUri);
            }

            return;
        }

        if (displayImageOption == null) {
            displayImageOption = mImageLoaderConfiger.mDefauleDisplayImageOption;
        }

        //设置显示正在加载的图片
        if (displayImageOption.getImageLoadingId() != 0) {
            imageView.setImageResource(displayImageOption.imageLoadingId);
        }

        //开始加载
        if (displayImageOption.imageLoaderProgressListener != null) {
            displayImageOption.imageLoaderProgressListener.onStartLoader();
        }

        /*
        首先从内存中获取，注意 此处没有判断用户是否统一缓存到内存，都会从内存中取。
         */
        Bitmap memoryCache = mImageLoaderConfiger.lruMemoryCache.get((url + "_" + imageView.getWidth() + "_" + imageView.getHeight()).hashCode() + "");
        if (memoryCache != null && !memoryCache.isRecycled()) {
            Log.e("imageDownload", "从缓存加载图片");
            imageView.setImageBitmap(memoryCache);

            if (displayImageOption.imageLoaderProgressListener != null) {
                displayImageOption.imageLoaderProgressListener.onCompleteLoading();
            }
            return;
        }

        String lowerUrl = url.toLowerCase();
        if(lowerUrl.startsWith(ImageSourceType.HTTP.getPrefix()) || lowerUrl.startsWith(ImageSourceType.HTTPS.getPrefix())){
            ImageDownloadInfo mInfo = new ImageDownloadInfo(url, imageView, displayImageOption);
            LoadAndDisplayImageNetworkTask task = new LoadAndDisplayImageNetworkTask(mImageLoaderConfiger, mInfo, createHandler());
            //开始获取图片
            mImageLoaderConfiger.mNetworkExecutor.submit(task);
        }else if(lowerUrl.startsWith(ImageSourceType.FILE.getPrefix())){
            displayImageOption.setIsCacheDisk(false);
            ImageDownloadInfo mInfo = new ImageDownloadInfo(url, imageView, displayImageOption);
            LoadAndDisplayImageNetworkTask task = new LoadAndDisplayImageNetworkTask(mImageLoaderConfiger, mInfo, createHandler());
            mImageLoaderConfiger.mFileExecutor.submit(task);
        }

    }

    /**
     * 创建Handler
     *
     * @return
     */
    private Handler createHandler() {
        Handler mHandler = null;
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mHandler = new Handler();
        }
        return mHandler;
    }
}
