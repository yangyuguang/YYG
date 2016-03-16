package com.pami.adapter;

import java.util.ArrayList;
import java.util.List;

import com.pami.PMApplication;
import com.pami.http.ExceptionUtils;
import com.pami.utils.ImageLoader;
import com.pami.utils.MLog;

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
			final ImageView itemImage = holder.getView(mContext.getResources().getIdentifier("itemImage", "id", mContext.getPackageName()));
			final ImageView itemSelect = holder.getView(mContext.getResources().getIdentifier("itemSelect", "id", mContext.getPackageName()));

			LayoutParams lp = (LayoutParams) itemImage.getLayoutParams();
			lp.width = (PMApplication.getInstance().getDiaplayWidth() - 12) / 3;
			lp.height = (PMApplication.getInstance().getDiaplayWidth() - 12) / 3;
			itemImage.setLayoutParams(lp);

			itemImage.setImageResource(mContext.getResources().getIdentifier("pami_default_load_img", "drawable", mContext.getPackageName()));
			itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_no_select", "drawable", mContext.getPackageName()));

			itemImage.setColorFilter(null);
			final String filePath = dirPath + "/" + mData.get(position);
			itemImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {

						if (selects.contains(filePath)) {
							selects.remove(filePath);
							itemImage.setColorFilter(null);
							itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_no_select", "drawable",
									mContext.getPackageName()));
						} else {
							if (maxNum <= selects.size()) {
								Toast.makeText(mContext, "最多只能选择" + maxNum + "图片", Toast.LENGTH_SHORT).show();
								return;
							}
							selects.add(filePath);
							itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_is_select", "drawable",
									mContext.getPackageName()));
							itemImage.setColorFilter(Color.parseColor("#77000000"));
						}

						if (onSelectOrDeleteImgListener != null) {
							onSelectOrDeleteImgListener.onSeletectOrDelete(selects.size());
						}
					} catch (Exception e) {
						uploadException(e);
					}
				}
			});

			ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(itemImage, filePath);
			if (selects.contains(filePath)) {
				itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_is_select", "drawable", mContext.getPackageName()));
				itemImage.setColorFilter(Color.parseColor("#77000000"));
			}
		} catch (Exception e) {
			uploadException(e);
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

	public interface OnSelectOrDeleteImgListener {
		void onSeletectOrDelete(int selectNum);
	}

	public OnSelectOrDeleteImgListener getOnSelectOrDeleteImgListener() {
		return onSelectOrDeleteImgListener;
	}

	public void setOnSelectOrDeleteImgListener(OnSelectOrDeleteImgListener onSelectOrDeleteImgListener) {
		this.onSelectOrDeleteImgListener = onSelectOrDeleteImgListener;
	}

	/**
	 * 上传log 日志
	 * 注意调用此方法 app 必须重写PMApplication 并在 onCreate方法中 调用 setExceptionUrl(url) 将上传log信息的URL注入系统。否则将调用无效 。
	 * 最后别忘记在清单文件中注册 重写的 PMApplication
	 * @param e
	 */
	protected void uploadException(Exception e) {
		MLog.e("yyg", "有错误信息 ， 请认真查看log信息");
		e.printStackTrace();
		ExceptionUtils.uploadException(mContext, e, PMApplication.getInstance().getExceptionUrl());
	}
}
