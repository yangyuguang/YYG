package com.yangyg.imageload.download;

import com.yangyg.imageload.DisplayImageOption;
import com.yangyg.imageload.imageaware.ImageViewAware;

/**
 * 下载图片Task的Info
 * Created by 杨裕光 on 16/11/10.
 */
public class ImageDownloadInfo {

    private String url = null;
    private ImageViewAware mImageViewAware = null;
    private DisplayImageOption mDisplayImageOption = null;

    public ImageDownloadInfo(String url, ImageViewAware mImageViewAware, DisplayImageOption mDisplayImageOption) {
        this.url = url;
        this.mImageViewAware = mImageViewAware;
        this.mDisplayImageOption = mDisplayImageOption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ImageViewAware getmImageViewAware() {
        return mImageViewAware;
    }

    public void setmImageViewAware(ImageViewAware mImageViewAware) {
        this.mImageViewAware = mImageViewAware;
    }

    public DisplayImageOption getmDisplayImageOption() {
        return mDisplayImageOption;
    }

    public void setmDisplayImageOption(DisplayImageOption mDisplayImageOption) {
        this.mDisplayImageOption = mDisplayImageOption;
    }
}
