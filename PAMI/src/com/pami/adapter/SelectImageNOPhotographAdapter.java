package com.pami.adapter;

import java.util.ArrayList;
import java.util.List;

import com.pami.PMApplication;
import com.pami.utils.ImageLoader;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

public class SelectImageNOPhotographAdapter extends PMBaseAdapter<String> {

	private String dirPath;
	private int maxNum = 0;
	private ArrayList<String> selects = new ArrayList<String>();
	
	private OnSelectOrDeleteImgListener onSelectOrDeleteImgListener = null;

	private SelectImageNOPhotographAdapter(Context context, List<String> mData, int layoutId) {
		super(context, mData, layoutId);
	}

	public SelectImageNOPhotographAdapter(Context context, List<String> mData, int layoutId, String dirPath) {
		this(context, mData, layoutId);
		this.dirPath = dirPath;
	}

	@Override
	public void getViews(ViewHolder holder, String t, int position) {

		try {
			// 重置状态
			final ImageView itemImage = holder.getView(context.getResources().getIdentifier("itemImage", "id",
					context.getPackageName()));
			final ImageView itemSelect = holder.getView(context.getResources().getIdentifier("itemSelect", "id",
					context.getPackageName()));
			
			LayoutParams lp = (LayoutParams) itemImage.getLayoutParams();
			lp.width = (PMApplication.getInstance().getDiaplayWidth() - 12)/3;
			lp.height = (PMApplication.getInstance().getDiaplayWidth() - 12)/3;
			itemImage.setLayoutParams(lp);
			
			itemImage.setImageResource(context.getResources().getIdentifier("pami_default_load_img", "drawable",
					context.getPackageName()));
			itemSelect.setImageResource(context.getResources().getIdentifier("pami_checkbox_no_select", "drawable",
					context.getPackageName()));

			itemImage.setColorFilter(null);
			final String filePath = dirPath + "/" + mData.get(position);
			itemImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (selects.contains(filePath)) {
						selects.remove(filePath);
						itemImage.setColorFilter(null);
						itemSelect.setImageResource(context.getResources().getIdentifier("pami_checkbox_no_select",
								"drawable", context.getPackageName()));
					} else {
						if (maxNum <= selects.size()) {
							Toast.makeText(context, "最多只能选择" + maxNum + "图片", Toast.LENGTH_SHORT).show();
							return;
						}
						selects.add(filePath);
						itemSelect.setImageResource(context.getResources().getIdentifier("pami_checkbox_is_select",
								"drawable", context.getPackageName()));
						itemImage.setColorFilter(Color.parseColor("#77000000"));
					}
					
					if(onSelectOrDeleteImgListener != null){
						onSelectOrDeleteImgListener.onSeletectOrDelete(selects.size());
					}
				}
			});

			ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(itemImage, filePath);
			if (selects.contains(filePath)) {
				itemSelect.setImageResource(context.getResources().getIdentifier("pami_checkbox_is_select", "drawable",
						context.getPackageName()));
				itemImage.setColorFilter(Color.parseColor("#77000000"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void clearSelectImages(String dirPath) {
		this.dirPath = dirPath;
		selects.clear();
		notifyDataSetChanged();
	}

	public ArrayList<String> getSelectImage() {
		return this.selects;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}
	
	public interface OnSelectOrDeleteImgListener{
		void onSeletectOrDelete(int selectNum);
	}
	
	public OnSelectOrDeleteImgListener getOnSelectOrDeleteImgListener() {
		return onSelectOrDeleteImgListener;
	}

	public void setOnSelectOrDeleteImgListener(OnSelectOrDeleteImgListener onSelectOrDeleteImgListener) {
		this.onSelectOrDeleteImgListener = onSelectOrDeleteImgListener;
	}
}
