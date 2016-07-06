package com.pami.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.pami.utils.ImageSwitchUtil;
import com.pami.utils.MLog;
import com.pami.utils.MResource;


/***
 *    选择开关
 * @author litienan
 *
 */
public class ChooseButton extends View implements OnTouchListener {
    private boolean nowChoose = false;// 记录当前按钮是否打开,true为打开,flase为关闭
    private boolean onSlip = false;// 记录用户是否在滑动的变量
    private float downX, nowX;// 按下时的x,当前的x
    private Rect btn_On, btn_Off;// 打开和关闭状态下,游标的Rect .
    private boolean isChgLsnOn = false;
    private OnChangedListener chgLsn;
    private Bitmap bg_on, bg_off, slip_btn;
    
    
    public ChooseButton(Context context){
        this(context, null);
    }

    public ChooseButton(Context context, AttributeSet attrs){
        this(context, attrs, 0);
        TypedArray array = null;
    }

    public ChooseButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        try {
        	bg_on = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnclose")));
        	bg_off = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnoff")));
        	slip_btn = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "choose_circle")));
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void init()throws Exception{// 初始化  
        btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
        btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0, bg_off.getWidth(),  slip_btn.getHeight());
        setOnTouchListener(this);// 设置监听器,也可以直接复写OnTouchEvent
    }
    
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int width = bg_on.getWidth();
        int height = bg_on.getHeight();
        setMeasuredDimension(width, height);
    }
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onDraw(Canvas canvas){// 绘图函数
        super.onDraw(canvas);
        Matrix matrix = new Matrix();
        Paint paint = new Paint();
        float x;
        
        if (onSlip){// 是否是在滑动状态,
        	
        	if (nowX -33< (bg_on.getWidth() / 2)){// 滑动到前半段与后半段的背景不同,在此做判断
                x = nowX - slip_btn.getWidth() / 2;
                canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
                MLog.e("yygDebug", "----关闭----->1"+"   ,   "+x);
            }else{
                x = bg_on.getWidth() - slip_btn.getWidth() / 2;
                canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
                MLog.e("yygDebug", "----打开----->2"+"   ,   "+x);
            }
        	
        }else {// 非滑动状态
            if (nowChoose)// 根据现在的开关状态设置画游标的位置
            {
                x = btn_Off.left;
                canvas.drawBitmap(bg_on, matrix, paint);// 初始状态为true时应该画出打开状态图片
                MLog.e("yygDebug", "----打开----->3"+"   ,   "+x);
            }else{
            	x = btn_On.left;
            	canvas.drawBitmap(bg_off, matrix, paint);
            	MLog.e("yygDebug", "----关闭----->4"+"   ,   "+x);
            }
        }
      
        if (x < 0)// 对游标位置进行异常判断...
            x = 0;
        else if (x > bg_on.getWidth() - slip_btn.getWidth()){
            x = bg_on.getWidth() - slip_btn.getWidth();
        }
        MLog.e("yygDebug", "--------->5"+"   ,   "+x);
        canvas.drawBitmap(slip_btn, x, 0, paint);// 画出游标.
    }

    public boolean onTouch(View v, MotionEvent event){
        switch (event.getAction()){
         // 根据动作来执行代码
            case MotionEvent.ACTION_MOVE:// 滑动
                nowX = event.getX();
                break;

            case MotionEvent.ACTION_DOWN:// 按下
                if (event.getX() > bg_on.getWidth() || event.getY() > bg_on.getHeight())
                    return false;
                onSlip = true;
                downX = event.getX();
                nowX = downX;
                break;

            case MotionEvent.ACTION_CANCEL: // 移到控件外部
                onSlip = false;
                boolean choose = nowChoose;
                if (nowX >= (bg_on.getWidth() / 2)){
                    nowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
                    nowChoose = true;
                }else{
                    nowX = nowX - slip_btn.getWidth() / 2;
                    nowChoose = false;
                }
                if (isChgLsnOn && (choose != nowChoose)) // 如果设置了监听器,就调用其方法..
                    chgLsn.OnChanged(nowChoose);
                break;
            case MotionEvent.ACTION_UP:// 松开
                onSlip = false;
                boolean LastChoose = nowChoose;
                if (event.getX() >= (bg_on.getWidth() / 2)){
                    nowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
                    nowChoose = true;
                }else {
                    nowX = nowX - slip_btn.getWidth() / 2;
                    nowChoose = false;
                }
                if (isChgLsnOn && (LastChoose != nowChoose)) // 如果设置了监听器,就调用其方法..                	
                	chgLsn.OnChanged(nowChoose);
                break;
            default:
        }
        invalidate();// 重画控件
        return true;
    }

    public void SetOnChangedListener(OnChangedListener l) {// 设置监听器,当状态修改的时候
        isChgLsnOn = true;
        chgLsn = l;
    }

    public interface OnChangedListener{
        abstract void OnChanged(boolean CheckState );
    }
    
    public void setCheckStaus(boolean status){
    	this.nowChoose = status;
    	invalidate();
    }


}
