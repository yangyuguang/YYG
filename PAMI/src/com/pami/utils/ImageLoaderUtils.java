package com.pami.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.pami.utils.BitmapUtils;

/**
 * 图片加载类
 * @author 杨裕光
 *
 */
public class ImageLoaderUtils {

    private static ImageLoaderUtils mImageLoaderUtil;
    private static int mDefultImageResId = 0;
    private static int mErrorImageResId = 0;
    private static int mEmptyImageResId = 0;
    private static ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private static Context mContext = null;
    private LruCache<String, Bitmap> mImageLoaderCache = null;

    private ImageLoaderUtils(Context context) {
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration iff = ImageLoaderConfiguration.createDefault(context);
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(mDefultImageResId)
                .showImageForEmptyUri(mEmptyImageResId)
                .showImageOnFail(mErrorImageResId).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory / 8;
        mImageLoaderCache = new LruCache<String, Bitmap>(cacheMemory){
        	@Override
        	protected int sizeOf(String key, Bitmap value) {
        		return value.getRowBytes() * value.getHeight();
        	}
        };
        
    }
    
    public static ImageLoaderUtils getinstance(Context context,int defultImageResId,int errorImageResId,int emptyImageResId) {
        mContext = context;
        mDefultImageResId = defultImageResId;
        mErrorImageResId = errorImageResId;
        mEmptyImageResId = emptyImageResId;
        if (mImageLoaderUtil == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mImageLoaderUtil == null) {
                    mImageLoaderUtil = new ImageLoaderUtils(context);
                }
            }
        }
        return mImageLoaderUtil;
    }

    public static ImageLoaderUtils getinstance(Context context) {
        mContext = context;
        if (mImageLoaderUtil == null) {
            synchronized (ImageLoaderUtils.class) {
                if (mImageLoaderUtil == null) {
                    mImageLoaderUtil = new ImageLoaderUtils(context);
                }
            }
        }
        return mImageLoaderUtil;
    }

    /**
     * 获取图片
     * 并设置图片缓存到内存和本地
     * @param imageView
     * @param url
     */
    public void getImage(final ImageView imageView, String url){
        imageLoader.displayImage(url, imageView, options);
    }

    /**
     * 获取图片
     * 并设置图片缓存到内存和本地
     * @param imageView
     * @param url
     * @param defImageResId
     */
    public void getImage(final ImageView imageView, String url,
                         final int defImageResId) {
        final DisplayImageOptions optionsT = new DisplayImageOptions.Builder()
                .showImageOnLoading(defImageResId)
                .showImageForEmptyUri(defImageResId)
                .showImageOnFail(defImageResId).cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(url, imageView, optionsT);
    }


    /**
     * 获取图片
     * 并设置图片缓存到内存和本地
     * @param imageView
     * @param url
     * @param defImageResId
     */
    public void getImageNoDisc(final ImageView imageView, String url,
                               final int defImageResId) {
        final DisplayImageOptions optionsT = new DisplayImageOptions.Builder()
                .showImageOnLoading(defImageResId)
                .showImageForEmptyUri(defImageResId)
                .showImageOnFail(defImageResId).cacheInMemory(true)
                .cacheOnDisc(false).considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        imageLoader.displayImage(url, imageView, optionsT);
    }

    /**
     * 获取圆形图片
     * @param imageView
     * @param url
     */
    public void getRoundedCornerBitmap(ImageView imageView, String url){
    	getRoundedCornerBitmap(imageView, url, mDefultImageResId,mEmptyImageResId,mErrorImageResId, true);
    }
    
    /**
     * 获取圆形图片 并设置默认图片
     * @param imageView
     * @param url
     * @param defImageResId
     */
    public void getRoundedCornerBitmap(ImageView imageView, String url, int defImageResId) {
    	getRoundedCornerBitmap(imageView, url, defImageResId,defImageResId,defImageResId, true);
    }
    
    /**
     * 获取圆形图片
     * @param imageView
     * @param url
     */
    public void getRoundedCornerBitmap(ImageView imageView, String url,boolean isCacheInMemory){
    	getRoundedCornerBitmap(imageView, url, mDefultImageResId,mEmptyImageResId,mErrorImageResId,isCacheInMemory);
    }
    
    /**
     * 获取圆形图片 并设置默认图片
     * @param imageView
     * @param url
     * @param defImageResId
     */
    public void getRoundedCornerBitmap(final ImageView imageView, String url, int defImageResId,boolean isCacheInMemory) {
    	getRoundedCornerBitmap(imageView, url, defImageResId,defImageResId,defImageResId,isCacheInMemory);
    }
    
    /**
     * 获取圆形图片
     * @param imageView
     * @param url
     * @param defImageResId
     */
    private void getRoundedCornerBitmap(final ImageView imageView, final String url, int defultImageResId,int emptyImageResId,int errorImageResId,boolean isCacheInMemory) {
    	imageView.setTag(url);
    	final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defultImageResId)
                .showImageForEmptyUri(emptyImageResId)
                .showImageOnFail(errorImageResId).cacheInMemory(isCacheInMemory)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisc(true).considerExifParams(true)
                .displayer(new BitmapDisplayer() {

                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware,
                                        LoadedFrom arg2) {
                        if (!(imageAware instanceof ImageViewAware)) {
                            throw new IllegalArgumentException(
                                    "ImageAware should wrap ImageView. ImageViewAware is expected.");
                        }
                        try {
                        	
                        	if(imageView.getTag().toString().equals(url)){
                        	
                        		Bitmap resultBitmap = mImageLoaderCache.get(url);
                        		if(resultBitmap == null){
                        			Bitmap bb = BitmapUtils.scaleBitmap(imageView, bitmap);
                                    Bitmap tailorBitmap = BitmapUtils.tailorBitmap(imageView, bb);
                                    resultBitmap = BitmapUtils.toRoundBitmap(tailorBitmap);
                                    mImageLoaderCache.put(url, resultBitmap);
                                    bb.recycle();
                                    tailorBitmap.recycle();
                                    bb = null;
                                    tailorBitmap = null;
                        		}
                        		
                        		if(imageView.getTag().toString().equals(url)){
                                	imageAware.setImageBitmap(resultBitmap);
                                }
                        		
                        	}
                        	
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).build();
        imageLoader.displayImage(url, imageView, options);
    }
    
    /**
     * 获取圆角图片
     *
     * @param imageView
     * @param url
     * @param cornerRadiusPixels
     */
    public void getImageRoundBitmap(ImageView imageView, String url, int cornerRadiusPixels) {
    	getImageRoundBitmap(imageView, url, cornerRadiusPixels, mDefultImageResId, mEmptyImageResId, mErrorImageResId, true);
    }
    
    /**
     * 获取圆角图片 并设置默认图片
     * @param imageView
     * @param url
     * @param defImageResId
     * @param cornerRadiusPixels
     */
    public void getImageRoundBitmap( ImageView imageView,  String url, int defImageResId, int cornerRadiusPixels) {
    	getImageRoundBitmap(imageView, url, cornerRadiusPixels, defImageResId, defImageResId, defImageResId,true);
    }
    
    /**
     * 获取圆角图片
     *
     * @param imageView
     * @param url
     * @param cornerRadiusPixels
     */
    public void getImageRoundBitmap(ImageView imageView, String url, int cornerRadiusPixels, boolean isCacheInMemory) {
    	getImageRoundBitmap(imageView, url, cornerRadiusPixels, mDefultImageResId, mEmptyImageResId, mErrorImageResId,isCacheInMemory);
    }
    
    /**
     * 获取圆角图片 并设置默认图片
     * @param imageView
     * @param url
     * @param defImageResId
     * @param cornerRadiusPixels
     */
    public void getImageRoundBitmap( ImageView imageView,  String url, int defImageResId, int cornerRadiusPixels, boolean isCacheInMemory) {
    	getImageRoundBitmap(imageView, url, cornerRadiusPixels, defImageResId, defImageResId, defImageResId,isCacheInMemory);
    }
    
    

    /**
     * 获取圆角图片
     *
     * @param imageView
     * @param url
     * @param cornerRadiusPixels
     */
    private void getImageRoundBitmap(final ImageView imageView, final String url, final int cornerRadiusPixels,int defultImageResId,int emptyImageResId,int errorImageResId,boolean isCacheInMemory) {
    	imageView.setTag(url);
        final DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(defultImageResId)
                .showImageForEmptyUri(emptyImageResId)
                .showImageOnFail(errorImageResId).cacheInMemory(isCacheInMemory)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .cacheOnDisc(true).considerExifParams(true)
                .displayer(new BitmapDisplayer() {

                    @Override
                    public void display(Bitmap bitmap, ImageAware imageAware,
                                        LoadedFrom arg2) {
                        if (!(imageAware instanceof ImageViewAware)) {
                            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
                        }
                        try {
                        	if(imageView.getTag().toString().equals(url)){
                        		Bitmap resultBitmap = mImageLoaderCache.get(url);
                        		if(resultBitmap == null){
                        			Bitmap bb = BitmapUtils.scaleBitmap(imageView, bitmap);
                                    Bitmap tailorBitmap = BitmapUtils.tailorBitmap(imageView, bb);
                                    resultBitmap = BitmapUtils.circularBeadBitmap(tailorBitmap,cornerRadiusPixels*1.0f);
                                    mImageLoaderCache.put(url, resultBitmap);
                                    bb.recycle();
                                    tailorBitmap.recycle();
                                    bb = null;
                                    tailorBitmap = null;
                        		}
                        		
                                if(imageView.getTag().toString().equals(url)){
                                	imageAware.setImageBitmap(resultBitmap);
                                }
                                
                        	}
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).build();
        /**
         * 
         */
        imageLoader.displayImage(url, imageView, options);
    }
}
