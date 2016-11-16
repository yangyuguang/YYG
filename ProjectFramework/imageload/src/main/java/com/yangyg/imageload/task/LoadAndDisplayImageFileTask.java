package com.yangyg.imageload.task;

import android.graphics.Movie;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.yangyg.imageload.ImageLoaderConfiger;
import com.yangyg.imageload.download.ImageDownload;
import com.yangyg.imageload.download.ImageDownloadInfo;
import com.yangyg.imageload.download.ImageSourceType;
import com.yangyg.imageload.download.InputStreamBean;

import java.io.File;
import java.io.FileInputStream;

/**
 * 加载并显示图片的Task
 * Created by 杨裕光 on 16/11/10.
 */
public class LoadAndDisplayImageFileTask extends LoadAndDisplayImageBaseTask {


    public LoadAndDisplayImageFileTask(ImageLoaderConfiger mImageLoaderConfiger, ImageDownloadInfo mInfo, Handler mHandler) {
        super(mImageLoaderConfiger, mInfo, mHandler);
    }

    @Override
    public void run() {
        try {

            //图片地址为空  或者  只包含file://
            String fileUrl = ImageSourceType.FILE.crop(mInfo.getUrl());
            if(TextUtils.isEmpty(fileUrl) || TextUtils.isEmpty(fileUrl.trim())){
                setEmptUriImageToView();
                return;
            }

            File imageFile = new File(fileUrl);
            if(!imageFile.exists()){
                setEmptUriImageToView();
                return;
            }

            //加载本地图片就不需要考虑缓存的问题了
            ImageDownload imageDownload = new ImageDownload(5 * 1000, 20 * 1000);
            InputStreamBean isBean = imageDownload.getInputStringFromFilePath(fileUrl);
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