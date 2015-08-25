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
import com.pami.utils.MResource;


/***
 *    选择开关
 * @author litienan
 *
 */
public class ChooseButton extends View implements OnTouchListener {
    private boolean NowChoose = false;// 记录当前按钮是否打开,true为打开,flase为关闭
    private boolean isChecked;
    private boolean OnSlip = false;// 记录用户是否在滑动的变量
    private float DownX, NowX;// 按下时的x,当前的x
    private Rect Btn_On, Btn_Off;// 打开和关闭状态下,游标的Rect .
    private boolean isChgLsnOn = false;
    private OnChangedListener ChgLsn;
    private Bitmap bg_on, bg_off, slip_btn;
    
    
    public void setNowX(float x){
    	this.NowX = x;
    	invalidate();
    }
    public ChooseButton(Context context){
        super(context);
        try {
        	bg_on = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnclose")));
        	bg_off = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnoff")));
        	slip_btn = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "choose_circle")));
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    public ChooseButton(Context context, AttributeSet attrs){
        super(context, attrs);
        TypedArray array = null;
        try {
        	bg_on = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnclose")));
        	bg_off = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnoff")));
        	slip_btn = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "choose_circle")));
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(array != null){
				array.recycle();
			}
		}
    }

    public ChooseButton(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        TypedArray array = null;
        try {
        	bg_on = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnclose")));
        	bg_off = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "turnoff")));
        	slip_btn = ImageSwitchUtil.drawableToBitmap(context.getResources().getDrawable(MResource.getIdByName(context, "drawable", "choose_circle")));
			init();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(array != null){
				array.recycle();
			}
		}
    }

    private void init()throws Exception{// 初始化  
        Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
        Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0, bg_off.getWidth(),  slip_btn.getHeight());
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
//        LogUtils.e("------【"+NowX+"】【"+bg_on.getWidth()+"】【】【】-------------");
        if (NowX -33< (bg_on.getWidth() / 2))// 滑动到前半段与后半段的背景不同,在此做判断
        {
            x = NowX - slip_btn.getWidth() / 2;
            canvas.drawBitmap(bg_off, matrix, paint);// 画出关闭时的背景
        }else{
            x = bg_on.getWidth() - slip_btn.getWidth() / 2;
            canvas.drawBitmap(bg_on, matrix, paint);// 画出打开时的背景
        }
        if (OnSlip)// 是否是在滑动状态,
        {
            if (NowX >= bg_on.getWidth())// 是否划出指定范围,不能让游标跑到外头,必须做这个判断
                x = bg_on.getWidth() - slip_btn.getWidth() / 2;// 减去游标1/2的长度...
            else if (NowX < 0){
                x = 0;
            } else{
                x = NowX - slip_btn.getWidth() / 2;
            }
        }else {// 非滑动状态
            if (NowChoose)// 根据现在的开关状态设置画游标的位置
            {
                x = Btn_Off.left;
                canvas.drawBitmap(bg_on, matrix, paint);// 初始状态为true时应该画出打开状态图片
            }else{
            	x = Btn_On.left;
            }
        }
      
        if (isChecked){
            canvas.drawBitmap(bg_on, matrix, paint);
            x = Btn_Off.left;
            isChecked = !isChecked;
        }

        if (x < 0)// 对游标位置进行异常判断...
            x = 0;
        else if (x > bg_on.getWidth() - slip_btn.getWidth()){
            x = bg_on.getWidth() - slip_btn.getWidth();
        }
        canvas.drawBitmap(slip_btn, x, 0, paint);// 画出游标.
    }

    public boolean onTouch(View v, MotionEvent event){
        switch (event.getAction()){
         // 根据动作来执行代码
            case MotionEvent.ACTION_MOVE:// 滑动
                NowX = event.getX();
                break;

            case MotionEvent.ACTION_DOWN:// 按下
                if (event.getX() > bg_on.getWidth() || event.getY() > bg_on.getHeight())
                    return false;
                OnSlip = true;
                DownX = event.getX();
                NowX = DownX;
                break;

            case MotionEvent.ACTION_CANCEL: // 移到控件外部
                OnSlip = false;
                boolean choose = NowChoose;
                if (NowX >= (bg_on.getWidth() / 2)){
                    NowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
                    NowChoose = true;
                }else{
                    NowX = NowX - slip_btn.getWidth() / 2;
                    NowChoose = false;
                }
                if (isChgLsnOn && (choose != NowChoose)) // 如果设置了监听器,就调用其方法..
                    ChgLsn.OnChanged(NowChoose);
                break;
            case MotionEvent.ACTION_UP:// 松开
                OnSlip = false;
                boolean LastChoose = NowChoose;
                if (event.getX() >= (bg_on.getWidth() / 2)){
                    NowX = bg_on.getWidth() - slip_btn.getWidth() / 2;
                    NowChoose = true;
                }else {
                    NowX = NowX - slip_btn.getWidth() / 2;
                    NowChoose = false;
                }
                if (isChgLsnOn && (LastChoose != NowChoose)) // 如果设置了监听器,就调用其方法..                	
                	ChgLsn.OnChanged(NowChoose);
                break;
            default:
        }
        invalidate();// 重画控件
        return true;
    }

    public void SetOnChangedListener(OnChangedListener l) {// 设置监听器,当状态修改的时候
        isChgLsnOn = true;
        ChgLsn = l;
    }

    public interface OnChangedListener{
        abstract void OnChanged(boolean CheckState );
    }

    public void setCheck(boolean isChecked){
        this.isChecked = isChecked;
        NowChoose = isChecked;
    }

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

}