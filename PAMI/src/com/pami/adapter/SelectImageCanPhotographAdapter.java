package com.pami.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import java.util.ArrayList;
import java.util.List;

import com.pami.PMApplication;
import com.pami.http.ExceptionUtils;
import com.pami.utils.ImageLoader;
import com.pami.utils.MLog;

/**
 * Created by 杨裕光 on 15/9/20.
 */
public class SelectImageCanPhotographAdapter extends BaseAdapter {

	private List<String> mData = null;
	private String dirPath;
	private Context mContext;
	private int maxNum = 0;
	private ArrayList<String> selects = new ArrayList<String>();
	private OnSelectOrDeleteImgListener onSelectOrDeleteImgListener = null;

	public SelectImageCanPhotographAdapter(Context mContext, List<String> mData, String dirPath) {
		this.mData = mData;
		this.dirPath = dirPath;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (mData == null || mData.isEmpty()) {
			return 1;
		}
		return mData.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			final ViewHolder vh;
			if (convertView == null) {
				vh = new ViewHolder();
				convertView = LayoutInflater.from(mContext).inflate(
						mContext.getResources().getIdentifier("pami_select_img_gridview_item", "layout", mContext.getPackageName()), parent, false);
				vh.itemImage = (ImageView) convertView.findViewById(mContext.getResources().getIdentifier("itemImage", "id",
						mContext.getPackageName()));
				vh.itemSelect = (ImageButton) convertView.findViewById(mContext.getResources().getIdentifier("itemSelect", "id",
						mContext.getPackageName()));

				LayoutParams lp = (LayoutParams) vh.itemImage.getLayoutParams();
				lp.width = (PMApplication.getInstance().getDiaplayWidth() - 12) / 3;
				lp.height = (PMApplication.getInstance().getDiaplayWidth() - 12) / 3;
				vh.itemImage.setLayoutParams(lp);

				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			// 重置状态
			vh.itemImage.setImageResource(mContext.getResources().getIdentifier("pami_default_load_img", "drawable", mContext.getPackageName()));
			vh.itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_no_select", "drawable", mContext.getPackageName()));
			vh.itemImage.setColorFilter(null);
			vh.itemSelect.setVisibility(View.VISIBLE);
			final String filePath;
			if (position == 0) {
				filePath = "";
				vh.itemSelect.setVisibility(View.GONE);
				vh.itemImage.setImageResource(mContext.getResources().getIdentifier("taking_pictures", "drawable", mContext.getPackageName()));
			} else {
				filePath = dirPath + "/" + mData.get(position - 1);
				ImageLoader.getInstance(3, ImageLoader.Type.LIFO).loadImage(vh.itemImage, filePath);
				if (selects.contains(filePath)) {
					vh.itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_is_select", "drawable",
							mContext.getPackageName()));
					vh.itemImage.setColorFilter(Color.parseColor("#77000000"));
				}
			}

			vh.itemImage.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					try {
						if (position == 0) {
							if (onSelectOrDeleteImgListener != null) {
								onSelectOrDeleteImgListener.onPhone();
							}
						} else {
							if (selects.contains(filePath)) {
								selects.remove(filePath);
								vh.itemImage.setColorFilter(null);
								vh.itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_no_select", "drawable",
										mContext.getPackageName()));
							} else {
								if (maxNum <= selects.size()) {
									Toast.makeText(mContext, "最多只能选择" + maxNum + "图片", Toast.LENGTH_SHORT).show();
									return;
								}
								selects.add(filePath);
								vh.itemSelect.setImageResource(mContext.getResources().getIdentifier("pami_checkbox_is_select", "drawable",
										mContext.getPackageName()));
								vh.itemImage.setColorFilter(Color.parseColor("#77000000"));
							}

							if (onSelectOrDeleteImgListener != null) {
								onSelectOrDeleteImgListener.onSeletectOrDelete(selects.size());
							}
						}
					} catch (Exception e) {
						uploadException(e);
					}
				}
			});

		} catch (Exception e) {
			uploadException(e);
		}

		return convertView;
	}

	/**
	 * 上传log 日志
	 * 注意调用此方法 app 必须重写PMApplication 并在 onCreate方法中 调用 setExceptionUrl(url) 将上传log信息的URL注入系统。否则将调用无效 。
	 * 最后别忘记在清单文件中注册 重写的 PMApplication
	 * @param e
	 */
	public void uploadException(Exception e) {
		MLog.e("yyg", "有错误信息 ， 请认真查看log信息");
		e.printStackTrace();
		ExceptionUtils.uploadException(mContext, e, PMApplication.getInstance().getExceptionUrl());
	}

	class ViewHolder {
		ImageView itemImage;
		ImageButton itemSelect;
	}

	public void clearSelectImages(String dirPath) {
		this.dirPath = dirPath;
		selects.clear();
		notifyDataSetChanged();
	}

	public ArrayList<String> getSelectImage() {
		return this.selects;
	}

	public interface OnSelectOrDeleteImgListener {
		void onSeletectOrDelete(int selectNum);

		void onPhone();
	}

	public OnSelectOrDeleteImgListener getOnSelectOrDeleteImgListener() {
		return onSelectOrDeleteImgListener;
	}

	public void setOnSelectOrDeleteImgListener(OnSelectOrDeleteImgListener onSelectOrDeleteImgListener) {
		this.onSelectOrDeleteImgListener = onSelectOrDeleteImgListener;
	}

	public int getMaxNum() {
		return maxNum;
	}

	public void setMaxNum(int maxNum) {
		this.maxNum = maxNum;
	}

}
