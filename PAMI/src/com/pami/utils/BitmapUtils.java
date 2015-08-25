package com.pami.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;

public class BitmapUtils {

	public static Bitmap getScaleBitmap(String filename, int viewWidth, int viewHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		options.inSampleSize = getSimpleSize(options, viewWidth, viewHeight);
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}

	/**
	 * 软引用创建，必要时释放
	 * @param id
	 * @param b
	 * @return
	 */
	private static Options options;

	public static Bitmap creatBitmap(Context context,int id) {
		if (options == null) {
			options = new BitmapFactory.Options();
			// options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			options.inPurgeable = true;
			options.inInputShareable = true;
		}

		InputStream is = context.getResources().openRawResource(id);
		return BitmapFactory.decodeStream(is, null, options);
	}

	public static int getSimpleSize(BitmapFactory.Options options, int viewWidth, int viewHeight) {
		int simpleSize = 1;
		int imgHeight = options.outHeight;
		int imgWidth = options.outWidth;

		int widthRatio = (int) Math.ceil(imgWidth / viewWidth);
		int heightRatio = (int) Math.ceil(imgHeight / viewHeight);
		if (widthRatio > 1 && heightRatio > 1) {
			if (widthRatio > heightRatio) {
				simpleSize = widthRatio;
			} else {
				simpleSize = heightRatio;
			}
		}
		return simpleSize;
	}

	/**
	 * 保存发送的图片
	 * @param imagePath 
	 * @param width
	 * @param height
	 * @param maxSize
	 * @return
	 */
	public static String getThumbUploadPath( String imagePath, int width, int height, long maxSize,String saveFilePath) {
		try {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;
			
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath, newOpts);// 此时返回bm为空

			newOpts.inSampleSize = getSimpleSize(newOpts, width, height);// 设置缩放比例
			newOpts.inJustDecodeBounds = false;
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bitmap = BitmapFactory.decodeFile(imagePath, newOpts);
			return saveBitmap(compressImage(bitmap, maxSize), saveFilePath,
					new File(imagePath).getName());// 压缩好比例大小后再进行质量压缩
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获得缩略图路径 此方法首先会将图片压缩到指定大小 然后再保存到本地文件 并返回此文件的路径
	 * @param imagePath 原图路径
	 * @param maxBorderLenght 图片长宽的最大值
	 * @param maxSize 图片大小的最大值
	 * @return
	 */
	public static String getThumbUploadPath(String imagePath, int maxBorderLenght, long maxSize,String saveFilePath) {
		Bitmap bitmap = null;
		try {
			BitmapFactory.Options options = new BitmapFactory.Options();
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
			options.inPreferredConfig = Bitmap.Config.RGB_565;
			try {
				bitmap = BitmapFactory.decodeFile(imagePath, options);
			} catch (OutOfMemoryError e) {
				return getThumbUploadPath(imagePath, (int) (maxBorderLenght * 0.8), maxSize,saveFilePath);
			}
			try {
				bitmap = compressImage(Bitmap.createScaledBitmap(bitmap, trueWidth, trueHeight, false), maxSize);
			} catch (OutOfMemoryError e) {
			} catch (Exception e) {
			}
			bitmap = rotaingImageView(readPictureDegree(imagePath), bitmap);
			bitmap = compressImage(bitmap, maxSize);
		} catch (Exception e) {
		}
		String imageFileName = (new Date()).getTime() + ".jpg";
		return saveBitmap(bitmap, saveFilePath, imageFileName);
	}

	/**
	 * 图片大小压缩 如果图片大小超过 maxSize 将压缩到maxSize之内
	 * 
	 * @param image 图片
	 * @param maxSize 最大（200kb）
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image, long maxSize) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > maxSize) { // 循环判断如果压缩后图片是否大于300kb,大于继续压缩
			options -= 5;// 每次都减少10
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	/**
	 * 保存图片到本地文件夹
	 * 
	 * @param bm 图片
	 * @param savePath 图片保存的路径
	 * @param saveFileName 图片保存的名称
	 * @return
	 */
	public static String saveBitmap(Bitmap bm, String savePath, String saveFileName) {
		String imageP = "";

		try {
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
			bm.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.flush();
			out.close();
		} catch (OutOfMemoryError e) {
			imageP = savePath;
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			imageP = savePath;
			e.printStackTrace();
		} catch (Exception e) {
			imageP = savePath;
			e.printStackTrace();
		}
		return imageP;
	}

	/**
	 * 读取图片属性：旋转的角度
	 * 
	 * @param path 图片绝对路径
	 * @return degree 旋转的角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * 旋转图片
	 * 
	 * @param angle
	 * @param bitmap
	 * @return Bitmap
	 */
	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
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
	 * 转换图片成圆形
	 * 
	 * @param bitmap 传入Bitmap对象
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap) {
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
	 * 图片转为灰色
	 * 
	 * @param bmSrc
	 * @return
	 */
	public static Bitmap bitmap2Gray(Bitmap bmSrc) {
		int width, height;
		height = bmSrc.getHeight();
		width = bmSrc.getWidth();
		Bitmap bmpGray = null;
		bmpGray = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
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
	 * @param bitmap
	 * @return
	 */
	public static byte[] getBytes(Bitmap bitmap){  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    if(bitmap == null){//quality
	    	return null;
	    }
	    bitmap.compress(Bitmap.CompressFormat.PNG, 0, baos);//压缩位图  
	    return baos.toByteArray();//创建分配字节数组  
	}

}
