package com.yangyg.popupwindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.List;

import com.yangyg.YYGApplication;
import com.yangyg.bean.FolderBean;
import com.yangyg.utils.ImageLoader;

/**
 * 选择图片文件的PopupWindow
 * Created by 杨裕光 on 15/9/21.
 */
public class SelectImageDirPopupWindow extends PopupWindow{

    private View mConvertView;
    private ListView mListView;
    private List<FolderBean> mData = null;
    private Context mContext = null;

    private OnListItemClickListener onListItemClickListener = null;


    public SelectImageDirPopupWindow(Context mContext,List<FolderBean> mData){
        this.mData = mData;
        this.mContext = mContext;

        mConvertView = LayoutInflater.from(mContext).inflate(mContext.getResources().getIdentifier("pami_select_img_popupwond_layout", "layout", mContext.getPackageName()),null);

        setContentView(mConvertView);
        setWidth(YYGApplication.getInstance().getDiaplayWidth());
        setHeight((int)(YYGApplication.getInstance().getDiaplayHeight() * 0.7));
        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setAnimationStyle(mContext.getResources().getIdentifier("select_img_popwindow_anim_style", "style", mContext.getPackageName()));

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE){
                    dismiss();
                    return true;
                }
                return false;
            }
        });


        initViews();
        initEvent();


    }

    private void initEvent() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(onListItemClickListener != null){
                    onListItemClickListener.onItemClickListener(mData.get(position));
                }
                dismiss();
            }
        });
    }

    private void initViews() {
        mListView = (ListView) mConvertView.findViewById(mContext.getResources().getIdentifier("listDir", "id", mContext.getPackageName()));
        mListView.setAdapter(new DirAdapter(mContext,mData));
    }


    private class DirAdapter extends ArrayAdapter<FolderBean>{

        private LayoutInflater mInflater;
        private List<FolderBean> mDatas;

        public DirAdapter(Context context, List<FolderBean> mDatas) {
            super(context, 0, mDatas);
            mInflater = LayoutInflater.from(context);
            this.mDatas = mDatas;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                ViewHolder vh;
                if(convertView == null){
                    convertView = mInflater.inflate(mContext.getResources().getIdentifier("pami_select_img_popupwind_item", "layout", mContext.getPackageName()),parent,false);
                    vh = new ViewHolder();
                    vh.dirItemImage = (ImageView) convertView.findViewById(mContext.getResources().getIdentifier("dirItemImage", "id", mContext.getPackageName()));
                    vh.dirItemName = (TextView) convertView.findViewById(mContext.getResources().getIdentifier("dirItemName", "id", mContext.getPackageName()));
                    vh.dirItemCount = (TextView) convertView.findViewById(mContext.getResources().getIdentifier("dirItemCount", "id", mContext.getPackageName()));

                    convertView.setTag(vh);
                }else{
                    vh = (ViewHolder) convertView.getTag();
                }
                Log.e("yyg",mDatas.get(position).getFirstImgPath());
                ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(vh.dirItemImage,mDatas.get(position).getFirstImgPath());
                vh.dirItemName.setText(mDatas.get(position).getName());
                vh.dirItemCount.setText(mDatas.get(position).getCount()+"张");
            }catch (Exception e){
                e.printStackTrace();
            }
            return convertView;
        }

        class ViewHolder{
            ImageView dirItemImage;
            TextView dirItemName;
            TextView dirItemCount;
        }
    }


    public interface OnListItemClickListener{
        void onItemClickListener(FolderBean bean);
    }

    public void setOnListItemClickListener(OnListItemClickListener onListItemClickListener){
        this.onListItemClickListener = onListItemClickListener;
    }


}
