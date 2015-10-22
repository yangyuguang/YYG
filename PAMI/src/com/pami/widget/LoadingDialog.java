package com.pami.widget;

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

public class LoadingDialog extends DialogFragment {

	private String httpFlag;
	private OnDesmissListener onDesmissListener = null;
	private AnimationDrawable loading = null;
	
	public OnDesmissListener getOnDesmissListener() {
		return onDesmissListener;
	}

	public void setOnDesmissListener(OnDesmissListener onDesmissListener) {
		this.onDesmissListener = onDesmissListener;
	}

	public String getHttpFlag() {
		return httpFlag;
	}

	public void setHttpFlag(String httpFlag) {
		this.httpFlag = httpFlag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = LayoutInflater.from(getActivity()).inflate(getResources().getIdentifier("loading_dialog_view", "layout", getActivity().getPackageName()), container, false);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ImageView loading_mark_iv = (ImageView) view.findViewById(getResources().getIdentifier("loading_mark_iv", "id", getActivity().getPackageName()));
		loading = (AnimationDrawable) loading_mark_iv.getDrawable();
		loading.start();
        setCancelable(false);
        return view;
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		super.onDismiss(dialog);
		if(onDesmissListener != null){
			onDesmissListener.onDismiss(httpFlag);
		}
	}
	
	public interface OnDesmissListener{
		void onDismiss(String httpFlag);
	}
}
