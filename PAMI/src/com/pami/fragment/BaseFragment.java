package com.pami.fragment;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.pami.PMApplication;
import com.pami.activity.BaseActivity;
import com.pami.listener.HttpActionListener;
import com.pami.listener.ViewInit;
import com.pami.utils.JsonUtils;
import com.pami.utils.MLog;
import com.pami.utils.Util;

public abstract class BaseFragment extends Fragment implements ViewInit, HttpActionListener,OnClickListener{

	protected FrameLayout titleBar;
	private boolean isChangeTitleBar = true;
	private Dialog loadingDialog;
	private AnimationDrawable loading = null;
	
	private List<String> httpFlags = new ArrayList<String>();

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			initViewFromXML();
			initData();
			fillView();
			initListener();
		} catch (Exception e) {
			MLog.e("yyg", "fragment onActivityCreated方法有错");
			e.printStackTrace();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
//			initData();
			titleBar = new FrameLayout(getActivity());
		} catch (Exception e) {
			MLog.e("yyg", "fragment onCreate方法有错");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 填充缓存数据
	 */
	protected void fillCacheData() {

	}

	/**
	 * 设置activity的titlebar
	 */
	public void setTitleBar()throws Exception{
		((BaseActivity) getActivity()).setTitleBar(titleBar);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		try {
			
			if (isChangeTitleBar && isVisibleToUser) {
				setTitleBar();
			}
		} catch (Exception e) {
			MLog.e("yyg", "有错");
			e.printStackTrace();
		}
	}
	
	public void setChangeTitleBar(boolean isChange) {
		this.isChangeTitleBar = isChange;
	}
	
	@Override
	public void handleActionStart(String actionName, Object object) {
		MLog.e("yyg", "BaseFragment:handleActionStart【"+actionName+"】");
		
	}
	
	
	@Override
	public void handleActionError(String actionName, Object object) {
		MLog.e("yyg", "BaseFragment:handleActionError【"+actionName+"】");
		try {
			String result = object.toString();
			if(result.indexOf("code") >= 0){
				int code = JsonUtils.getCode(result);
				switch (code) {
				case -1:{
					MLog.e("yyg", "返回-1");
					Toast.makeText(getActivity(), JsonUtils.getSuccessData(result, "error_text"), 1).show();
					break;
				}
				case -9:{
					MLog.e("yyg", "返回-9");
					Toast.makeText(getActivity(), JsonUtils.getSuccessData(result, "error_text"), 1).show();
					break;
				}

				default:
					break;
				}
			}else{
				Toast.makeText(getActivity(), result, 1).show();
			}
			
		} catch (Exception e) {
			MLog.e("yyg", "BaseActivity 【handleActionError】 错误回调有错");
			e.printStackTrace();
		}
	}
	
	public void showLoadingDialog(Object httpTag) {
		showLoadingDialog(httpTag,this.getString(getResources().getIdentifier("text_loading", "string", getActivity().getPackageName())));
	}

	public void showLoadingDialog(Object httpTag,String loadingStr) {
		dismissDialog();
		AlertDialog.Builder builder = new Builder(getActivity());
		loadingDialog = builder.create();
		loadingDialog.show();
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(getResources().getIdentifier("loading_dialog_view", "layout", getActivity().getPackageName()), null);
		ImageView loading_mark_iv = (ImageView) view.findViewById(getResources().getIdentifier("loading_mark_iv", "id", getActivity().getPackageName()));
		loading = (AnimationDrawable) loading_mark_iv.getDrawable();

		if(httpTag != null){
			view.setTag(httpTag);
		}
		
		loadingDialog.setContentView(view);
		Window dialogWindow = loadingDialog.getWindow();
		loadingDialog.setCanceledOnTouchOutside(true);
		dialogWindow.setGravity(Gravity.CENTER);
		android.view.WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
		layoutParams.height = getActivity().getWindowManager().getDefaultDisplay().getWidth() * 2 / 3;
		layoutParams.width = getActivity().getWindowManager().getDefaultDisplay().getWidth() * 2 / 3;
		dialogWindow.setAttributes(layoutParams);
		loading.start();
		
		loadingDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface arg0) {
				Object oo = view.getTag();
				if(oo instanceof List){
					List<String> tmpTag = (List<String>) oo;
					for(String tag:tmpTag){
						clearHttpRequest(tag);
					}
				}else if(oo instanceof String){
					clearHttpRequest(oo.toString());
				}
				
			}
		});
		
	}
	
	/**
	 * 根据tag撤销请求
	 * @param httpTag
	 */
	private void clearHttpRequest(String httpTag){
		if(TextUtils.isEmpty(httpTag)){
			return;
		}
		if(httpFlags.contains(httpTag)){
			httpFlags.remove(httpTag);
			PMApplication.getInstance().clearRequest(httpTag);
		}
		
	}

	public void dismissDialog() {
		if (loadingDialog != null) {
			try {
				loading.stop();
				loadingDialog.dismiss();
				loadingDialog = null;
			} catch (Exception e) {
				MLog.e("yyg", "去掉加载框有错。");
			}
		}
	}
	
	@Override
	public void handleActionSuccess(String actionName, Object object) {
		MLog.e("yyg", "BaseFragment:handleActionSuccess【"+actionName+"】");
	}
	
	@Override
	public void handleActionFinish(String actionName, Object object) {
		MLog.e("yyg", "BaseFragment:handleActionFinish【"+actionName+"】");
	}
	
	@Override
	public void onClick(View v) {
		if(Util.isFastClick()){
			return;
		}
		
		this.onButtonClick(v);
	}
	
	/**
	 * 显示提示
	 * 
	 * @param content
	 */
	public void showToast(String content) {
		try {
			Toast mToast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
			Util.setToast(mToast);
			mToast.show();
		} catch (Exception e) {
			MLog.e("yyg", "Toast有错");
			e.printStackTrace();
		}
	}
	
	/**
	 * 按钮点击方法的回调接口 防止连续点击
	 * @param v
	 */
	protected void onButtonClick(View v){}
}
