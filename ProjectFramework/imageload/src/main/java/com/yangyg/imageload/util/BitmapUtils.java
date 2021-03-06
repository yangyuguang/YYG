package com.yangyg.imageload.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Date;

public class BitmapUtils {


    /**
     * 获得缩略图路径 此方法首先会将图片压缩到指定大小 然后再保存到本地文件 并返回此文件的路径
     *
     * @param imagePath       原图路径
     * @param maxBorderLenght 图片长宽的最大值
     * @param maxSize         图片大小的最大值
     * @return
     */
    public static String getThumbUploadPath(String imagePath, int maxBorderLenght, long maxSize, String saveFilePath)
            throws Exception {
        Bitmap bitmap = null;
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(imagePath, options);
            int height = options.outHeight;
            int width = options.outWidth;

            int trueHeight = options.outHeight;
            int trueWidth = options.outWidth;

            options.inSampleSize = 1;

            if (height > maxBorderLenght || width > maxBorderLenght) {
                if (width > height) {
                    trueHeight = maxBorderLenght * height / width;
                    trueWidth = maxBorderLenght;
                    options.inSampleSize = width / maxBorderLenght;
                } else {
                    trueHeight = maxBorderLenght;
                    trueWidth = maxBorderLenght * width / height;
                    options.inSampleSize = height / maxBorderLenght;
                }
                if (options.inSampleSize < 0) {
                    options.inSampleSize = 1;
                }
            }
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Config.RGB_565;
            try {
                bitmap = BitmapFactory.decodeFile(imagePath, options);
            } catch (OutOfMemoryError e) {
                return getThumbUploadPath(imagePath, (int) (maxBorderLenght * 0.8), maxSize, saveFilePath);
            }
            try {
                bitmap = compressImageSize(Bitmap.createScaledBitmap(bitmap, trueWidth, trueHeight, false), maxSize);
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
            }
            bitmap = rotaingImageView(readPictureDegree(imagePath), bitmap);
            bitmap = compressImageSize(bitmap, maxSize);
        } catch (Exception e) {
        }
        String imageFileName = (new Date()).getTime() + ".jpg";
        return saveBitmap(bitmap, saveFilePath, imageFileName);
    }

    // ---------------------------------------------------------------------------------

    /**
     * 等比例压缩图片
     *
     * @param path   图片的路径 /etc/ff/yyg/1.png
     * @param width  图片需要显示的宽度值
     * @param height 图片需要显示的高度值
     * @return
     */
    public static Bitmap equalRatioCompressImage(String path, int width, int height) throws Exception {

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        options.inSampleSize = getSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, options);

    }

    /**
     * 等比例压缩图片
     *
     * @param resources Resources对象
     * @param resId     资源图片ID
     * @param width     图片需要显示的宽度值
     * @param height    图片需要显示的高度值
     * @return
     * @throws Exception
     */
    public static Bitmap equalRatioCompressImage(Resources resources, int resId, int width, int height) throws Exception {

        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = getSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * 等比例压缩图片
     *
     * @param data   图片字节数组
     * @param width  图片需要显示的宽度值
     * @param height 图片需要显示的高度值
     * @return
     * @throws Exception
     */
    public static Bitmap equalRatioCompressImage(byte[] data, int width, int height) throws Exception {
        if (data == null || data.length <= 0) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        options.inSampleSize = getSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /**
     * 等比例压缩图片
     *
     * @param is     图片数据的输入流
     * @param width  图片需要显示的宽度值
     * @param height 图片需要显示的高度值
     * @return
     * @throws Exception
     */
    public static Bitmap equalRatioCompressImage(InputStream is, int width, int height) throws Exception {
        if (is == null) {
            return null;
        }
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inSampleSize = getSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(is, null, options);
    }

    /**
     * 计算sampleSize
     *
     * @param options2
     * @param width
     * @param height
     * @return
     */
    public static int getSampleSize(Options options2, int width, int height) throws Exception {
        int sampleSize = 1;
        int outWidth = options2.outWidth;
        int outHeight = options2.outHeight;
        if (outWidth > width || outHeight > height) {
            int widthRatio = Math.round(outWidth * 1.0f / width);
            int heightRatio = Math.round(outHeight * 1.0f / height);
            sampleSize = Math.max(widthRatio, heightRatio);
        }
        return sampleSize;
    }

    /**
     * 图片大小压缩 如果图片大小超过 maxSize 将压缩到maxSize之内
     *
     * @param image   图片
     * @param maxSize 最大（200kb）
     * @return
     */
    public static Bitmap compressImageSize(Bitmap image, long maxSize) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中

        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
            options -= 5;// 每次都减少10
            baos.reset();// 重置baos即清空baos
            image.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片

        return bitmap;
    }

    /**
     * 图片大小压缩 如果图片大小超过 maxSize 将压缩到maxSize之内
     *
     * @param image    图片
     * @param maxSize  最大（200kb）
     * @param filePath 存放图片文件的路径
     * @return
     * @throws Exception
     */
    public static File compressImageSizeToFile(Bitmap image, long maxSize, String filePath) throws Exception {

        if (TextUtils.isEmpty(filePath)) {
            throw new Exception("filePath is null");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(CompressFormat.JPEG, 100, baos);

        int options = 100;
        while (baos.toByteArray().length / 1024 > maxSize) {
            options -= 5;
            baos.reset();
            image.compress(CompressFormat.JPEG, options, baos);
        }

        File f = new File(filePath);
        if (!f.exists()) {
            f.mkdirs();
        }

        File file = new File(filePath + "/" + System.currentTimeMillis() + ".jpg");
        FileOutputStream fos = new FileOutputStream(file);
        baos.writeTo(fos);
        fos.flush();
        fos.close();
        return file;

    }

    /**
     * 保存图片到本地文件夹
     *
     * @param bm           图片
     * @param savePath     图片保存的路径
     * @param saveFileName 图片保存的名称
     * @return
     */
    public static String saveBitmap(Bitmap bm, String savePath, String saveFileName) throws Exception {
        String imageP = "";

        File oF = new File(savePath);//
        if (!oF.exists()) {
            oF.mkdirs();
        }
        File f = new File(savePath, saveFileName);
        imageP = f.getAbsolutePath();
        if (f.exists()) {
            f.delete();
        }
        FileOutputStream out = new FileOutputStream(f);
        bm.compress(CompressFormat.JPEG, 90, out);
        out.flush();
        out.close();
        return imageP;
    }

    /**
     * 旋转图片
     *
     * @param angle  旋转的角度
     * @param bitmap 原图
     * @return Bitmap 旋转后的图
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) throws Exception {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        if (matrix != null) {
            // 创建新的图片
            try {
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, (int) (bitmap.getWidth()), (int) (bitmap.getHeight()),
                        matrix, true);
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree 旋转的角度
     */
    public static int readPictureDegree(String path) throws Exception {
        int degree = 0;
        ExifInterface exifInterface = new ExifInterface(path);
        int orientation = exifInterface
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                degree = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                degree = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                degree = 270;
                break;
        }
        return degree;
    }

    /**
     * 图片转为灰色
     *
     * @param bmSrc
     * @return
     */
    public static Bitmap bitmap2Gray(Bitmap bmSrc) throws Exception {
        int width, height;
        height = bmSrc.getHeight();
        width = bmSrc.getWidth();
        Bitmap bmpGray = null;
        bmpGray = Bitmap.createBitmap(width, height, Config.RGB_565);
        Canvas c = new Canvas(bmpGray);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmSrc, 0, 0, paint);

        return bmpGray;
    }

    /**
     * Bitmap转byte
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBytes(Bitmap bitmap) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (bitmap == null) {// quality
            return null;
        }
        bitmap.compress(CompressFormat.PNG, 0, baos);// 压缩位图
        return baos.toByteArray();// 创建分配字节数组
    }

    /**
     * 转换图片成圆形
     *
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) throws Exception {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas
        paint.setColor(color);

        // 以下有两种方法画圆,drawRounRect和drawCircle
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);//
        // 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle

        return output;
    }

    /**
     * 获取圆角图片
     *
     * @param bitmap             原始图片
     * @param cornerRadiusPixels 角度
     * @return
     */
    public static Bitmap circularBeadBitmap(Bitmap bitmap, float cornerRadiusPixels) throws Exception {

        Bitmap tagger = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(tagger);
        canvas.drawARGB(0, 0, 0, 0);

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);

        Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(src);
        canvas.drawRoundRect(rectF, cornerRadiusPixels * 1.0f, cornerRadiusPixels * 1.0f, mPaint);
        mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

        canvas.drawBitmap(bitmap, 0, 0, mPaint);

        return tagger;
    }

    /**
     * 等比例裁剪图片 将图片大小根据ImageView的大小裁剪至合适的大小 （裁剪是以图片的中心点做为中心点）
     *
     * @param imageView
     * @param bitmap
     * @return
     */
    public static Bitmap tailorBitmap(ImageView imageView, Bitmap bitmap) throws Exception {
        int ivWidth = imageView.getWidth();
        int ivHeight = imageView.getHeight();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        if (ivWidth <= 0) {
            ivWidth = lp.width;
        }

        if (ivWidth <= 0) {
            ivWidth = getImagViewFieldValue(imageView, "mMaxWidth");
        }

        if (ivWidth <= 0) {
            throw new Exception("ImageView 的宽度小于等于0。");
        }

        if (ivHeight <= 0) {
            ivHeight = lp.height;
        }

        if (ivHeight <= 0) {
            ivHeight = getImagViewFieldValue(imageView, "mMaxHeight");
        }

        if (ivHeight <= 0) {
            throw new Exception("ImageView 的高度小于等于0。");
        }

        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        int retX = 0;
        int retY = 0;
        int width = ivWidth;
        int height = ivHeight;

        float widthRatio = bWidth * 1.0f / ivWidth;// 2
        float heightRatio = bHeight * 1.0f / ivHeight;// 3

        if (bWidth >= ivWidth && bHeight >= ivHeight) {

            float minRatio = Math.min(widthRatio, heightRatio);
            if (minRatio > 1) {
                retX = Math.round((bWidth - (ivWidth * minRatio)) / 2);
                retY = Math.round((bHeight - (ivHeight * minRatio)) / 2);
            } else if (widthRatio <= 1 && heightRatio <= 1) {
                retX = 0;
                retY = 0;
                width = bWidth;
                height = bHeight;
            } else if (widthRatio <= 1) {
                retX = 0;
                retY = (bHeight - ivHeight) / 2;
                width = bWidth;
            } else if (heightRatio <= 1) {
                retX = (bWidth - ivWidth) / 2;
                retY = 0;
                height = bHeight;
            }

            width = bWidth - retX * 2;
            height = bHeight - retY * 2;
            return Bitmap.createBitmap(bitmap, retX, retY, width, height, null, false);

        } else if (bWidth >= ivWidth) {

            retX = Math.round((bWidth - (ivWidth * widthRatio)) / 2);
            width = bWidth - retX * 2;
            height = bHeight;
            return Bitmap.createBitmap(bitmap, retX, retY, width, height, null, false);
        } else if (bHeight >= ivHeight) {
            width = bWidth;
            retY = Math.round((bHeight - (ivHeight * heightRatio)) / 2);
            height = bHeight - retY * 2;
            return Bitmap.createBitmap(bitmap, retX, retY, width, height, null, false);
        }

        // 不需要裁剪
        return bitmap;
    }

    /**
     * 通过反射机制获取属性
     *
     * @param object    属性对象
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

    /**
     * 根据ImageView的大小 缩放Bitmap 注意只有当图片的宽高 同时大于 或者 同时小于ImageView的宽高时才会缩放
     *
     * @param imageView
     * @param bitmap
     * @return
     */
    public static Bitmap scaleBitmap(ImageView imageView, Bitmap bitmap) throws Exception {
        int ivWidth = imageView.getWidth();
        int ivHeight = imageView.getHeight();
        int bWidth = bitmap.getWidth();
        int bHeight = bitmap.getHeight();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        if (ivWidth <= 0) {
            ivWidth = lp.width;
        }

        if (ivWidth <= 0) {
            ivWidth = getImagViewFieldValue(imageView, "mMaxWidth");
        }

        if (ivWidth <= 0) {
            throw new Exception("ImageView 的宽度小于等于0。");
        }

        if (ivHeight <= 0) {
            ivHeight = lp.height;
        }

        if (ivHeight <= 0) {
            ivHeight = getImagViewFieldValue(imageView, "mMaxHeight");
        }

        if (ivHeight <= 0) {
            throw new Exception("ImageView 的高度小于等于0。");
        }

        if (bWidth >= ivWidth && bHeight >= ivHeight) {
            // 将 Bitmap缩小
            float widthRatio = ivWidth * 1.0f / bWidth;// 0.3
            float heightRatio = ivHeight * 1.0f / bHeight;// 0.6
            // 取最大值 即缩小的比例最小
            float maxRatio = Math.max(widthRatio, heightRatio);
            Matrix matrix = new Matrix();
            matrix.postScale(maxRatio, maxRatio);
            return Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);
        }
        if (bWidth <= ivWidth && bHeight <= ivHeight) {
            // 将 Bitmap放大
            float widthRatio = ivWidth * 1.0f / bWidth;// 2
            float heightRatio = ivHeight * 1.0f / bHeight;// 3

            float minRatio = Math.min(widthRatio, heightRatio);
            Matrix matrix = new Matrix();
            matrix.postScale(minRatio, minRatio);
            return Bitmap.createBitmap(bitmap, 0, 0, bWidth, bHeight, matrix, true);

        }
        return bitmap;
    }

    /**
     * 将View 转换成Bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    public static byte[] bitmapSwitchToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, output);
        bitmap.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
