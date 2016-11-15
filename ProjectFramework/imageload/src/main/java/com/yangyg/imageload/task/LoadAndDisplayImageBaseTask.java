package com.yangyg.imageload.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.text.TextUtils;

import com.yangyg.imageload.ImageLoaderConfiger;
import com.yangyg.imageload.download.ImageDownloadInfo;
import com.yangyg.imageload.download.InputStreamBean;
import com.yangyg.imageload.listener.ImageLoaderProgressListener;
import com.yangyg.imageload.util.BitmapUtils;
import com.yangyg.imageload.util.FileHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by 杨裕光 on 16/11/15.
 */
public abstract class LoadAndDisplayImageBaseTask implements Runnable {

    protected Handler mHandler = null;
    protected ImageLoaderConfiger mImageLoaderConfiger = null;
    protected ImageDownloadInfo mInfo = null;

    public LoadAndDisplayImageBaseTask(ImageLoaderConfiger mImageLoaderConfiger, ImageDownloadInfo mInfo, Handler mHandler) {
        this.mImageLoaderConfiger = mImageLoaderConfiger;
        this.mHandler = mHandler;
        this.mInfo = mInfo;
    }

    /**
     * 设置加载图片失败时，需要显示的图片
     */
    protected void setFailImageToView() {
        if (mInfo.getmDisplayImageOption().getImageFail() != 0) {
            setImageByResId(mInfo.getmDisplayImageOption().getImageFail());
        }
    }

    /**
     * 设置图片地址为空时，需要显示的图片
     */
    protected void setEmptUriImageToView() {
        if (mInfo.getmDisplayImageOption().getImageFail() != 0) {
            setImageByResId(mInfo.getmDisplayImageOption().getImageForEmptUri());
        }
    }

    /**
     * 从图片资源文件中加载图片到控件上
     * @param resId
     */
    protected void setImageByResId(final int resId) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mInfo.getmImageViewAware().setImageResource(resId);
            }
        });
    }

    /**
     * 从InputStreamBean加载图片
     *
     * @param isBean InputStreamBean
     * @throws Exception
     */
    protected void loadImageFromStrean(InputStreamBean isBean) throws Exception {
        if (isBean == null) {
            // 显示默认的图片
            setFailImageToView();
        } else {
            byte[] buf = new byte[32 * 1024];
            int len = 0;
            int currentLen = 0;

            InputStream is = isBean.getInputStream();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while ((len = is.read(buf)) > 0) {
                //将数据先写到
                baos.write(buf, 0, len);
                //更新加载进度
                currentLen += len;
                onProgressUpdate(mInfo.getmDisplayImageOption().getImageLoaderProgressListener(), currentLen, isBean.getTotal());
            }

            ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
            final Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

            setImageBitmap(bitmap);

            //将图片缓存到硬盘
            if (mInfo.getmDisplayImageOption().getIsCacheDisk()) {
                //通过图片的URL地址的hashCode值 创建图片的名称
                String imageDiskCacheName = mInfo.getUrl().hashCode() + "";

                //创建图片缓存的路径，如果路径不存在则创建路径
                String imageDiskCachePath = TextUtils.isEmpty(mInfo.getmDisplayImageOption().getCacheDiskPath()) ? mImageLoaderConfiger.getDefaultDiskCachePath() : mInfo.getmDisplayImageOption().getCacheDiskPath();
                File imageDiskCachePathFile = new File(imageDiskCachePath);
                if (!imageDiskCachePathFile.exists()) {
                    imageDiskCachePathFile.mkdirs();
                }

                File imageDiskCacheFile = new File(imageDiskCachePath + File.separator + imageDiskCacheName);
                if (!imageDiskCacheFile.exists()) {
                    // 保存图片
                    FileHelper.saveBitmap(imageDiskCachePath, imageDiskCacheName, bitmap);
                }
            }

            //加载完成
            if (mInfo.getmDisplayImageOption().getImageLoaderProgressListener() != null) {
                mInfo.getmDisplayImageOption().getImageLoaderProgressListener().onCompleteLoading();
            }

        }
    }

    protected void onProgressUpdate(ImageLoaderProgressListener imageLoaderProgressListener, int current, int total) {
        if (imageLoaderProgressListener != null) {
            imageLoaderProgressListener.onProgressUpdate(current, total);
        }
    }

    protected void setImageBitmap(Bitmap bitmap) throws Exception {

        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        Bitmap scaleBitmap = BitmapUtils.scaleBitmap(mInfo.getmImageViewAware().getImageView(), bitmap);
        final Bitmap tailorBitmap = BitmapUtils.tailorBitmap(mInfo.getmImageViewAware().getImageView(), scaleBitmap);
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                setBitmapToView(tailorBitmap);
            }
        });
    }

    protected void setBitmapToView(Bitmap tailorBitmap) {
        Object tag = mInfo.getmImageViewAware().getImageView().getTag();
        if (tag != null && !TextUtils.isEmpty(tag.toString()) && tag.toString().equals(mInfo.getUrl())) {
            mInfo.getmImageViewAware().setImageBitmap(tailorBitmap);
        }

        //将图片缓存到内存
        if (mInfo.getmDisplayImageOption().getIsCacheMemory()) {
            mImageLoaderConfiger.getLruMemoryCache().put((mInfo.getUrl() + "_" + mInfo.getmImageViewAware().getWidth() + "_" + mInfo.getmImageViewAware().getHeight()).hashCode() + "", tailorBitmap);
        }
    }
}
