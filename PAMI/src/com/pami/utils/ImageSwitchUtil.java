package com.pami.utils;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * 图片相关转换
 * 
 * @author Administrator
 * 
 */
public class ImageSwitchUtil {

	/**
	 * 将 drawable 转换成 Bitmap
	 * @param drawable
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable drawable) {

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
				drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);

		Canvas canvas = new Canvas(bitmap);

		drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * 将Bitmap 转换成byte[]
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);

		return baos.toByteArray();
	}

	/**
	 * 将 byte[] 转换成 Bitmap
	 * @param b
	 * @return
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {

		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		}else {
			return null;
		}
	}
}
