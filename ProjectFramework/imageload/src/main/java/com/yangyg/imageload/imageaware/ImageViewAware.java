package com.yangyg.imageload.imageaware;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;

/**
 *
 * Created by 杨裕光 on 16/11/9.
 */
public class ImageViewAware implements ViewAware{

    private ImageView mImageView = null;

    public ImageViewAware(){

    }

    public ImageViewAware(ImageView mImageView){
        this.mImageView = mImageView;
    }

    @Override
    public int getWidth() {
        int width = 0;
        try {
            if(mImageView != null){
                ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
                DisplayMetrics dm = mImageView.getContext().getResources().getDisplayMetrics();

                width = mImageView.getWidth();

                if (width <= 0) {
                    width = lp.width;
                }

                if (width <= 0) {
                    width = getImagViewFieldValue(mImageView, "mMaxWidth");
                }

                if (width <= 0) {
                    width = dm.widthPixels;
                }
            }
        }catch (Exception e){
            Log.e("imageDownload","获取控件宽出现异常");
            e.printStackTrace();
        }finally {
            return width;
        }
    }

    @Override
    public int getHeight() {
        int height = 0;
        try {
            if(mImageView != null){
                ViewGroup.LayoutParams lp = mImageView.getLayoutParams();
                DisplayMetrics dm = mImageView.getContext().getResources().getDisplayMetrics();

                height = mImageView.getHeight();

                if (height <= 0) {
                    height = lp.height;
                }

                if (height <= 0) {
                    height = getImagViewFieldValue(mImageView, "mMaxHeight");//检查最大值
                }

                if (height <= 0) {
                    height = dm.heightPixels;
                }
            }
        }catch (Exception e){
            Log.e("imageDownload","获取控件宽出现异常");
            e.printStackTrace();
        }finally {
            return height;
        }
    }

    public ImageView getImageView(){
        return mImageView;
    }

    /**
     * 设置图片
     * @param bitmap
     */
    public void setImageBitmap(Bitmap bitmap){
        mImageView.setImageBitmap(bitmap);
    }
    /**
     * 设置图片
     * @param resource
     */
    public void setImageResource(int resource){
        mImageView.setImageResource(resource);
    }

    /**
     * 清除imageView 之前显示的View
     */
    public void clearImageView(){
        mImageView.setImageDrawable(null);
    }

    /**
     * 通过反射机制获取属性
     *
     * @param object 属性对象
     * @param fieldName 属性名称
     * @return
     */
    private static int getImagViewFieldValue(Object object, String fieldName) throws Exception {

        int value = 0;

        Field field = ImageView.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        int fieldValue = field.getInt(object);

        if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
            value = fieldValue;
        }

        return value;
    }
}
