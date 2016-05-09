package com.pami.widget;

import com.pami.utils.BitmapUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
/**
 * 圆形ImageView
 * @author Administrator
 *
 */
public class CircularImageView extends ImageView {

	public CircularImageView(Context context) {
		super(context);
	}

	public CircularImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircularImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		try {
			Drawable drawable = getDrawable();

			if (drawable == null) {
				return;
			}

			if (getWidth() == 0 || getHeight() == 0) {
				return;
			}

			Bitmap b = ((BitmapDrawable) drawable).getBitmap();

			if (null == b) {
				return;
			}

			Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

			int w = getWidth();

			//根据控件的大小 先压缩 然后再裁剪
			Bitmap scaleBitmap = BitmapUtils.scaleBitmap(this, bitmap);
			Bitmap tailorBitmap = BitmapUtils.tailorBitmap(this, scaleBitmap);
			
			Bitmap roundBitmap = getCroppedAngleBitmap(tailorBitmap, w);
			canvas.drawBitmap(roundBitmap, 0, 0, null);
		} catch (Exception e) {
			super.onDraw(canvas);
		}
		

	}

	public static Bitmap getCroppedAngleBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f,
				(float) (sbmp.getWidth() / 2 * 0.98), paint);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}
	

}