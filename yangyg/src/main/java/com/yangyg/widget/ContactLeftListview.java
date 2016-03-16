package com.yangyg.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * 选择城市右边滑动字母选择的区域
 * @author yangyuguang
 *
 */
public class ContactLeftListview extends Button {

	public interface OnTouchAssortListener {													//定义接口
		public void onTouchAssortListener(String s);
		public void onTouchAssortUP();
	}

	public ContactLeftListview(Context context) {
		super(context);
	}

	public ContactLeftListview(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContactLeftListview(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}


	private String[] assort = { "↑" , "A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z","#"};
	private Paint paint = new Paint();

	private int selectIndex = -1;

	private OnTouchAssortListener onTouch;


	public void setOnTouchAssortListener(OnTouchAssortListener onTouch) {
		this.onTouch = onTouch;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setBackgroundColor(Color.argb(0, 0, 0, 0));														//设置背景颜色透明
		int height = getHeight();
		int width = getWidth();
		int interval = height / assort.length;

		for (int i = 0, length = assort.length; i < length; i++) {

			paint.setAntiAlias(true);

			paint.setColor(Color.parseColor("#009bd4"));
			paint.setTextSize(20);
			if (i == selectIndex) {																							//点击时展现其他效果
				paint.setColor(Color.parseColor("#b3b3b3"));
				paint.setFakeBoldText(true);
			}

			float xPos = width / 2 - paint.measureText(assort[i]) / 2;								//位置设定

			float yPos = interval * i + interval;
			canvas.drawText(assort[i], xPos, yPos, paint);												//绘制界面
			paint.reset();
		}

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		float y = event.getY();
		int index = (int) (y / getHeight() * assort.length);
		if (index >= 0 && index < assort.length && ContactLeftListview.this.getVisibility() == View.VISIBLE) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:

				if (selectIndex != index) {
					selectIndex = index;
					if (onTouch != null) {
						onTouch.onTouchAssortListener(assort[selectIndex]);
					}

				}
				break;
			case MotionEvent.ACTION_DOWN:
				selectIndex = index;
				if (onTouch != null) {
					onTouch.onTouchAssortListener(assort[selectIndex]);
				}

				break;
			case MotionEvent.ACTION_UP:
				if (onTouch != null) {
					onTouch.onTouchAssortUP();
				}
				selectIndex = -1;
				break;
			}
		} else {
			selectIndex = -1;
			if (onTouch != null) {
				onTouch.onTouchAssortUP();
			}
		}
		invalidate();

		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}
}
