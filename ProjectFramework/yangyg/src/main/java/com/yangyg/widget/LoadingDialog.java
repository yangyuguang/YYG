package com.yangyg.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class LoadingDialog extends DialogFragment {

	private List<String> httpFlags;
	private OnDesmissListener onDesmissListener = null;
	private AnimationDrawable loading = null;
	
	private TextView loding_mark_tv = null;
	private String hintMessage = null;
	
	public LoadingDialog(String hintMessage){
		this.hintMessage = hintMessage;
	}
	
	public OnDesmissListener getOnDesmissListener() {
		return onDesmissListener;
	}

	public void setOnDesmissListener(OnDesmissListener onDesmissListener) {
		this.onDesmissListener = onDesmissListener;
	}

	public List<String> getHttpFlags() {
		return httpFlags;
	}

	public void setHttpFlags(String... httpFlags) {
		if(this.httpFlags == null || this.httpFlags.isEmpty()){
			this.httpFlags = new ArrayList<String>();
		}
		this.httpFlags.clear();
		if(httpFlags != null && httpFlags.length > 0){
			for(String httpFlag:httpFlags){
				this.httpFlags.add(httpFlag);
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(getResources().getIdentifier("loading_dialog_view", "layout", getActivity().getPackageName()), container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView loading_mark_iv = (ImageView) view.findViewById(getResources().getIdentifier("loading_mark_iv", "id", getActivity().getPackageName()));
        loding_mark_tv = (TextView) view.findViewById(getResources().getIdentifier("loding_mark_tv", "id", getActivity().getPackageName()));
        loding_mark_tv.setText(hintMessage);
		loading = (AnimationDrawable) loading_mark_iv.getDrawable();
		loading.start();
//        setCancelable(false);
        return view;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(onDesmissListener != null){
			onDesmissListener.onDismiss(httpFlags);
		}
	}
	
	public interface OnDesmissListener{
		void onDismiss(List<String> httpFlag);
	}
	
	/**
	 * 设置提示信息
	 * @param str
	 */
	public void setHintMessage(String str){
		this.loding_mark_tv.setText(str);
	}
}
