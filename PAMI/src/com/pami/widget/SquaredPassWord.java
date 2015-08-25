package com.pami.widget;

import com.pami.utils.MResource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 屏幕 手势密码锁
 * @author Administrator
 *
 */
public class SquaredPassWord extends View {
//	private ImageView i;
	private int length;
	private Point[] points = new Point[9];
	private Bitmap defualtPointMap = BitmapFactory.decodeResource(
			getResources(), MResource.getIdByName(getContext(), "drawable", "locus_round_original"));//R.drawable.locus_round_original
	private int poitleght = defualtPointMap.getWidth();
	private Bitmap selectPointMap = BitmapFactory.decodeResource(
			getResources(), MResource.getIdByName(getContext(), "drawable", "locus_round_click"));//R.drawable.locus_round_click
	private Point startPoint;
	private Point tempPoint;
	private StringBuffer passWBuffer = new StringBuffer();
//	private Bitmap lineBitmap = BitmapFactory.decodeResource(getResources(),
//			MResource.getIdByName(getContext(), "drawable", "locus_line"));//R.drawable.locus_line
//	private int lineBitmapheight = lineBitmap.getHeight();
//	private double lineBitmapWidth = lineBitmap.getWidth();
	
	private int startX;
	private int startY;
	private int moveX;
	private int moveY;
	private PasswordSettingOverListener listener = null;

	public PasswordSettingOverListener getListener() {
		return listener;
	}

	public void setListener(PasswordSettingOverListener listener) {
		this.listener = listener;
	}

	public SquaredPassWord(Context context) {
		super(context);

	}

	public SquaredPassWord(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public SquaredPassWord(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean flag = true;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			passWBuffer.delete(0, passWBuffer.length());
			int x = (int) event.getX();
			int y = (int) event.getY();
			for (Point point : points) {
				if (point.isInMyArea(x, y)) {
					point.setSelected(true);
					tempPoint = startPoint = point;
					startX = startPoint.getCenterX();
					startY = startPoint.getCenterY();
					passWBuffer.append(startPoint.getId());
				}
			}
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			moveX = (int) event.getX();
			moveY = (int) event.getY();
			for (Point point : points) {
				if (point.isInMyArea(moveX, moveY) && !point.isSelected()) {
					tempPoint.setNextID(point.getId());
					point.setSelected(true);
					tempPoint = point;
					startX = tempPoint.getCenterX();
					startY = tempPoint.getCenterY();
					passWBuffer.append(tempPoint.getId());
				}
			}
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			reSetData();
			startX = startY = moveX = moveY = 0;
			invalidate();
			flag = false;
			if(listener != null){
				listener.onOverSetting(passWBuffer.toString());
			}
			break;

		default:
			break;
		}
		return flag;
	}

	private void reSetData() {
		for (Point point : points) {
			point.setSelected(false);
			point.setNextID(point.getId());
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int width = getWidth() - getPaddingLeft() - getPaddingRight();
		int height = getHeight() - getPaddingTop() - getPaddingBottom();
		length = (width < height ? width : height);
		if (!(length > 0)) {

		}
		System.out.println(length);
		initPionts(points);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		if (moveX != 0 && moveY != 0 && startX != 0 && startY != 0) {
			
			drawLine(startX, startY, moveX, moveY, canvas);
		}
		drawLinePoint(canvas);
		super.onDraw(canvas);
	}

	
	@SuppressWarnings("null")
	private void initPionts(Point[] points) {
		int spacing = (length - poitleght * 3) / 2;

		if (points == null && points.length != 9) {
			return;
		} else {
			for (int i = 0; i < 9; i++) {
				int row = i / 3;
				int column = i % 3;

				int x = (poitleght + spacing) * column + getPaddingLeft();
				int y = (poitleght + spacing) * row + getPaddingTop();
				Point point = new Point((i + 1), x, y, poitleght);
				points[i] = point;
			}
		}
	}


	private void drawLinePoint(Canvas canvas) {
		if (startPoint != null) {
			drawP2POrbit(startPoint, canvas);
		}
		Paint paint = null;// new Paint();

		for (Point point : points) {
			if (point.isSelected()) {
				canvas.drawBitmap(selectPointMap, point.getX(), point.getY(),
						paint);
			} else {
				canvas.drawBitmap(defualtPointMap, point.getX(), point.getY(),
						paint);
			}
		}
	}

	private void drawP2POrbit(Point point, Canvas canvas) {
		if (point.getId() != point.nextID) {
			// canvas.concat(matrix);
			Point endPoint = null;
			
			for (Point point3 : points) {
				if (point3.getId() == point.getNextID()) {
					endPoint = point3;
					break;
				}
			}
			if (endPoint != null) {
			
				drawLine(point.getCenterX(), point.getCenterY(),
						endPoint.getCenterX(), endPoint.getCenterY(), canvas);
			
				drawP2POrbit(endPoint, canvas);
			}
		}
	}

	private void drawLine(int startX, int startY, int stopX, int stopY,
			Canvas canvas) {
		Paint paint = new Paint();
	
		// double hypotenuse = Math.hypot((stopX - startX), (stopY - startY));
		// float rotate = getDegrees(startX, startY, stopX, stopY);
		// Matrix matrix = new Matrix();
		// canvas.rotate(rotate, startX, startY);
		// matrix.preTranslate(0, 0);
		// matrix.setScale((float) (hypotenuse / lineBitmapWidth), 1.0f);
		// matrix.postTranslate(startX, startY - lineBitmapheight / 2.f);
		// canvas.drawBitmap(lineBitmap, matrix, paint);
		// canvas.rotate(-rotate, startX, startY);
		// canvas.save();

		paint.setColor(Color.parseColor("#228ce2"));
		paint.setStrokeWidth(8);
		canvas.drawLine(startX, startY, stopX, stopY, paint);

	}

	private float getDegrees(int startX, int startY, int stopX, int stopY) {
	
		double hypotenuse = Math.hypot((stopX - startX), (stopY - startY));
		double side = stopX - startX;
		double piAngle = Math.acos(side / hypotenuse);
		float rotate = (float) (180 / Math.PI * piAngle);
		if (stopY - startY < 0) {
			rotate = 360 - rotate;
		}
		return rotate;
	}


	public String getOrbitString() {
		return (passWBuffer == null ? null : passWBuffer.toString());
	}


	class Point {

		private int id;
		private int nextID;
		private int x;
		private int y;
		private boolean isSelected;
		private int width;

		public Point() {
			super();
			// TODO Auto-generated constructor stub
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getNextID() {
			return nextID;
		}

		public void setNextID(int nextID) {
			this.nextID = nextID;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean isSelected) {
			this.isSelected = isSelected;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public Point(int id, int x, int y, int width) {
			super();
			this.id = id;
			this.x = x;
			this.y = y;
			this.nextID = id;
			this.isSelected = false;
			this.width = width;
		}

		public int getCenterX() {
			return x + (width / 2);
		}

		private int getCenterY() {
			return y + (width / 2);
		}

		
		public boolean isInMyArea(int x, int y) {
			return (x >= this.x && x <= (this.x + this.width))
					&& (y >= this.y && y <= (this.y + this.width));
		}
	}

	public interface PasswordSettingOverListener {
		public void onOverSetting(String passwordStr);
	}
}