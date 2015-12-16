package com.pami.fragment;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.pami.PMApplication;
import com.pami.activity.BaseActivity;
import com.pami.listener.HttpActionListener;
import com.pami.listener.ViewInit;
import com.pami.utils.JsonUtils;
import com.pami.utils.MLog;
import com.pami.utils.ScreenManager;
import com.pami.utils.Util;
import com.pami.widget.LoadingDialog;
import com.pami.widget.LoadingDialog.OnDesmissListener;

public abstract class BaseFragment extends Fragment implements ViewInit, HttpActionListener,OnClickListener{

	protected FrameLayout titleBar;
	private boolean isChangeTitleBar = true;
	private LoadingDialog loadingDialog;
	
	
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
					Toast.makeText(getActivity(), JsonUtils.getSuccessData(result, "error_text"), Toast.LENGTH_SHORT).show();
					break;
				}
				case -9:{
					MLog.e("yyg", "返回-9");
					Toast.makeText(getActivity(), JsonUtils.getSuccessData(result, "error_text"), Toast.LENGTH_SHORT).show();
					break;
				}

				default:
					Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
					break;
				}
			}else{
				Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
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
		
		if(loadingDialog == null){
			loadingDialog = new LoadingDialog();
		}
		loadingDialog.setHttpFlag(httpTag.toString());
		loadingDialog.setOnDesmissListener(new OnDesmissListener() {
			
			@Override
			public void onDismiss(String httpFlag) {
				if(!TextUtils.isEmpty(httpFlag)){
					clearHttpRequest(httpFlag);
				}
			}
		});
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loadingDialog"+System.currentTimeMillis());
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
	
	/**
	 * 销毁activity
	 */
	public void finishActivity(){
		ScreenManager.getScreenManager().popActivity(getActivity());
	}
	
}
