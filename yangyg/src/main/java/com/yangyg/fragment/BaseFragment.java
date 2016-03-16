package com.yangyg.fragment;

import java.util.List;

import com.yangyg.YYGApplication;
import com.yangyg.activity.BaseActivity;
import com.yangyg.listener.HttpActionListener;
import com.yangyg.listener.ViewInit;
import com.yangyg.utils.MLog;
import com.yangyg.utils.NetUtils;
import com.yangyg.utils.ScreenManager;
import com.yangyg.utils.Util;
import com.yangyg.widget.LoadingDialog;
import com.yangyg.widget.LoadingDialog.OnDesmissListener;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.Toast;


public abstract class BaseFragment extends Fragment implements ViewInit, HttpActionListener, OnClickListener {

	protected FrameLayout titleBar;
	private boolean isChangeTitleBar = true;
	private LoadingDialog loadingDialog;
	private boolean loadingDialogIsShow = false;
	private Toast mToast;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try {
			initViewFromXML();
			initData();
			fillView();
			initListener();
		} catch (Exception e) {
			uploadException(e);
		}
	}

	/**
	 * 上传log 日志 注意调用此方法 app 必须重写PMApplication 并在 onCreate方法中
	 * 调用setExceptionUrl(url) 将上传log信息的URL注入系统。否则将调用无效 。 最后别忘记在清单文件中注册 重写的
	 * PMApplication
	 * 
	 * @param e
	 */
	protected void uploadException(Exception e){};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			// initData();
			titleBar = new FrameLayout(getActivity());
		} catch (Exception e) {
			uploadException(e);
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
	public void setTitleBar() throws Exception {
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
			uploadException(e);
		}
	}

	public void setChangeTitleBar(boolean isChange) {
		this.isChangeTitleBar = isChange;
	}

	@Override
	public void handleActionError(String actionName, Object object) {
		
	}

	@Override
	public void handleActionStart(String actionName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleActionFinish(String actionName, Object object) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleActionSuccess(String actionName, Object object) {
		// TODO Auto-generated method stub

	}

	/**
	 * 显示加载中的踢提示框 使用默认的提示文字
	 * 
	 * @param httpTags
	 * @throws Exception
	 */
	public void showLoadingDialog(String... httpTags) throws Exception {
		if (NetUtils.isNetworkConnected(getActivity())) {
			setLoadingDialogHintMessage(this.getString(getResources().getIdentifier("text_loading", "string", getActivity().getPackageName())));
			showLoadingDialogByhttpTags(httpTags);
		}
	}

	/**
	 * 显示加载中的踢提示框 使用默认的提示文字
	 * 
	 * @param httpTags
	 * @throws Exception
	 */
	public void showLoadingDialogAndHint(String hintMessage, String... httpTags) throws Exception {
		if (NetUtils.isNetworkConnected(getActivity())) {
			setLoadingDialogHintMessage(hintMessage);
			showLoadingDialogByhttpTags(httpTags);
		}
	}

	/**
	 * 设置dialog的提示信息
	 * 
	 * @param str
	 */
	private void setLoadingDialogHintMessage(String str) throws Exception {
		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(str);
			loadingDialog.setOnDesmissListener(new OnDesmissListener() {

				@Override
				public void onDismiss(List<String> httpFlag) {
					try {
						loadingDialogIsShow = false;
//						if (httpFlag != null && !httpFlag.isEmpty()) {
//							clearHttpRequest(httpFlag);
//						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			this.loadingDialog.setHintMessage(str);
		}
	}

	/**
	 * 显示加载的dialog
	 * 
	 * @param httpTags
	 * @throws Exception
	 */
	private void showLoadingDialogByhttpTags(String... httpTags) throws Exception {
		if (loadingDialogIsShow) {
			return;
		}
		loadingDialog.setHttpFlags(httpTags);
		loadingDialog.show(getActivity().getSupportFragmentManager(), "loadingDialog" + System.currentTimeMillis());
		loadingDialogIsShow = true;
	}

//	/**
//	 * 根据tag撤销请求
//	 * 
//	 * @param httpTag
//	 */
//	private void clearHttpRequest(String httpTag) throws Exception {
//		if (TextUtils.isEmpty(httpTag)) {
//			return;
//		}
//		PMApplication.getInstance().clearRequest(httpTag);
//	}

//	/**
//	 * 根据tag撤销请求
//	 * 
//	 * @param httpTag
//	 */
//	private void clearHttpRequest(List<String> httpTags) throws Exception {
//		for (String httpTag : httpTags) {
//			clearHttpRequest(httpTag);
//		}
//	}

	public void dismissDialog() throws Exception {
		if (loadingDialog != null && loadingDialogIsShow) {
			loadingDialog.dismiss();
			loadingDialog = null;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			if (Util.isFastClick()) {
				return;
			}
			this.onButtonClick(v);
		} catch (Exception e) {
			uploadException(e);
		}
	}

	/**
	 * 按钮点击方法的回调接口 防止连续点击
	 * 
	 * @param v
	 */
	protected void onButtonClick(View v) throws Exception {
	}

	/**
	 * 销毁activity
	 */
	public void finishActivity() {
		ScreenManager.getScreenManager().popActivity(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			dismissDialog();
			titleBar = null;
			if (mToast != null) {
				mToast.cancel();
				mToast = null;
			}
		} catch (Exception e) {
			uploadException(e);
		}
	}

	/**
	 * 显示提示
	 * 
	 * @param content
	 */
	public void showToast(String content) {
		try {
			mToast = Toast.makeText(getActivity(), content, Toast.LENGTH_SHORT);
			Util.setToast(mToast);
			mToast.show();
		} catch (Exception e) {
			uploadException(e);
		}
	}

}
