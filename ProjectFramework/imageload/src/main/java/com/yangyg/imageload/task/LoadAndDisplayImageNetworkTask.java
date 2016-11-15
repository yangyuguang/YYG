package com.yangyg.imageload.task;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.yangyg.imageload.ImageLoaderConfiger;
import com.yangyg.imageload.download.ImageDownload;
import com.yangyg.imageload.download.ImageDownloadInfo;
import com.yangyg.imageload.download.InputStreamBean;

import java.io.File;

/**
 * 加载并显示图片的Task
 * Created by 杨裕光 on 16/11/10.
 */
public class LoadAndDisplayImageNetworkTask extends LoadAndDisplayImageBaseTask {


    public LoadAndDisplayImageNetworkTask(ImageLoaderConfiger mImageLoaderConfiger, ImageDownloadInfo mInfo, Handler mHandler) {
        super(mImageLoaderConfiger, mInfo, mHandler);
    }

    @Override
    public void run() {
        try {

            String imageDiskCacheName = mInfo.getUrl().hashCode() + "";
            ImageDownload imageDownload = new ImageDownload(5 * 1000, 20 * 1000);
            InputStreamBean isBean = null;

            String imageDiskCachePath = TextUtils.isEmpty(mInfo.getmDisplayImageOption().getCacheDiskPath()) ? mImageLoaderConfiger.getDefaultDiskCachePath() : mInfo.getmDisplayImageOption().getCacheDiskPath();
            File imageDiskCachePathFile = new File(imageDiskCachePath);
            if (!imageDiskCachePathFile.exists()) {
                imageDiskCachePathFile.mkdirs();
            } else {
                isBean = imageDownload.getInputStringFromFilePath(imageDiskCachePath + File.separator + imageDiskCacheName);
            }

            if (isBean == null) {
                isBean = imageDownload.getInputStringFromNetwork(mInfo.getUrl());
            }
            loadImageFromStrean(isBean);
        } catch (Exception e) {
            //图片加载失败 显示加载失败的图片
            setFailImageToView();
            // 出错显示图片
            Log.i("imageDownload", "加载图片出错。");
            e.printStackTrace();
        }
    }
}