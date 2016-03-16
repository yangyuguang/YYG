package com.pami.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import popupwindow.SelectImageDirPopupWindow;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pami.adapter.SelectImageCanPhotographAdapter;
import com.pami.adapter.SelectImageCanPhotographAdapter.OnSelectOrDeleteImgListener;
import com.pami.bean.FolderBean;
import com.pami.utils.FileHelper;
import com.pami.utils.ScreenManager;
import com.pami.utils.ScreenUtils;

/**
 * 选择图片
 * 
 * @author Administrator
 * 
 */
public class SelectImgCanPhotographActivity extends BaseActivity implements
		SelectImageDirPopupWindow.OnListItemClickListener, OnSelectOrDeleteImgListener{

	private GridView mGridView = null;

	private RelativeLayout mBottomLayout = null;
	private TextView dirName = null;
	private TextView dirCount = null;
	private TextView selectImageBtn = null;

	private File mCurrentDir;
	private int mMaxCount;

	private List<FolderBean> mDatas = new ArrayList<FolderBean>();
	private List<String> mImgs = new ArrayList<String>();

	private ProgressDialog dialog = null;
	private SelectImageCanPhotographAdapter adapter = null;

	private SelectImageDirPopupWindow mPopupWindow = null;
	
	private int maxNum = 9;
	private int selectNum = 0;
	private String appImgCacheDir = null;
	private String phoneImgPath = null;
	private boolean isHidTitlebar = false;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0: {
				dialog.dismiss();
				data2View();// 绑定数据到View中
				initPopupWindow();
				break;
			}
			}
		}
	};

	@Override
	public void initViewFromXML() throws Exception {
		hideTitleBar();
		setContent(getResources().getIdentifier("pami_select_img_activity_layout", "layout", getPackageName()));

		isHidTitlebar = getIntent().getBooleanExtra("isHidTitlebar", false);
		
		View titlebar_tv = (View) findViewById(getResources().getIdentifier("titleHeight", "id", getPackageName()));
		if(isHidTitlebar){
			
	        if(android.os.Build.VERSION.SDK_INT >= 19){
	        	RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titlebar_tv.getLayoutParams();
	            lp.height = ScreenUtils.getStatusHeight(SelectImgCanPhotographActivity.this);
	            titlebar_tv.setLayoutParams(lp);
	        }else{
	            titlebar_tv.setVisibility(View.GONE);
	        }
		}else{
			titlebar_tv.setVisibility(View.GONE);
		}
		
        
		maxNum = getIntent().getIntExtra("maxNum", 9);
		appImgCacheDir = getIntent().getStringExtra("appImgCacheDir");
		
		mGridView = (GridView) findViewById(getResources().getIdentifier("mGridView", "id", getPackageName()));
		mBottomLayout = (RelativeLayout) findViewById(getResources().getIdentifier("mBottomLayout", "id",
				getPackageName()));
		dirName = (TextView) findViewById(getResources().getIdentifier("dirName", "id", getPackageName()));
		dirCount = (TextView) findViewById(getResources().getIdentifier("dirCount", "id", getPackageName()));
		selectImageBtn = (TextView) findViewById(getResources().getIdentifier("selectImageBtn", "id", getPackageName()));

	}

	@Override
	public void initData() throws Exception {
		initDatas();
	}

	@Override
	public void fillView() throws Exception {
		
	}

	@Override
	public void initListener() throws Exception {
		mBottomLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.showAsDropDown(mBottomLayout, 0, 0);
				lightOff();
			}
		});
		
		selectImageBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(selectNum <= 0){
					return;
				}
				Intent intent = new Intent();
				intent.putStringArrayListExtra("resultDate", adapter.getSelectImage());
				setResult(RESULT_OK, intent);
				ScreenManager.getScreenManager().popActivity(SelectImgCanPhotographActivity.this);
			}
		});
		
		findViewById(getResources().getIdentifier("exitBtn", "id", getPackageName())).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ScreenManager.getScreenManager().popActivity(SelectImgCanPhotographActivity.this);
			}
		});
	}

	@Override
	public void onAppDownLine() {
		
	}

	private void initPopupWindow() {

		if (mPopupWindow == null) {
			mPopupWindow = new SelectImageDirPopupWindow(this, mDatas);

			mPopupWindow.setOnListItemClickListener(this);
			mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
				@Override
				public void onDismiss() {
					lightOn();
				}
			});
		}

	}

	private void lightOn() {
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 1.0f;
		getWindow().setAttributes(lp);

	}

	private void data2View() {
		if (mCurrentDir == null) {
			Toast.makeText(this, "未扫描到任何图片", Toast.LENGTH_SHORT).show();
			return;
		}

		dirCount.setText(mMaxCount + "张");
		dirName.setText(mCurrentDir.getName());
		
		mImgs.addAll(Arrays.asList(mCurrentDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
					return true;
				}
				return false;
			}
		})));

		adapter = new SelectImageCanPhotographAdapter(this, mImgs, mCurrentDir.getAbsolutePath());
		adapter.setOnSelectOrDeleteImgListener(this);
		adapter.setMaxNum(maxNum);
		mGridView.setAdapter(adapter);
	}

	private void lightOff() {
		
		WindowManager.LayoutParams lp = getWindow().getAttributes();
		lp.alpha = 0.7f;
		getWindow().setAttributes(lp);

	}

	/**
	 * 利用COntentProvider扫描手机中的所有图片 初始化数据
	 */
	private void initDatas() {
		if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			Toast.makeText(SelectImgCanPhotographActivity.this, "存储卡不可用", Toast.LENGTH_SHORT).show();
			return;
		}

		dialog = ProgressDialog.show(this, null, "正在加载...");

		// 开启线程扫描手机中的图片
		new Thread() {
			@Override
			public void run() {

				Uri mimgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver cr = SelectImgCanPhotographActivity.this.getContentResolver();
				Cursor cursor = cr.query(mimgUri, null, MediaStore.Images.Media.MIME_TYPE + " = ? or "
						+ MediaStore.Images.Media.MIME_TYPE + " = ?", new String[] { "image/png", "image/jpeg" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Set<String> mDirPath = new HashSet<String>();
				while (cursor.moveToNext()) {
					String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
					File parentFile = new File(path).getParentFile();
					if (parentFile == null) {
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();
					FolderBean bean = null;

					if (mDirPath.contains(dirPath)) {
						continue;
					} else {
						mDirPath.add(dirPath);
						bean = new FolderBean();
						bean.setDir(dirPath);
						bean.setFirstImgPath(path);
					}
					if (parentFile.list() == null) {
						continue;
					}

					int picSize = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
								return true;
							}
							return false;
						}
					}).length;

					bean.setCount(picSize);

					mDatas.add(bean);
					if (picSize > mMaxCount) {
						mMaxCount = picSize;
						mCurrentDir = parentFile;
					}
				}
				cursor.close();
				mDirPath = null;// 扫描完成 释放内存

				mHandler.sendEmptyMessage(0);
			}
		}.start();

	}

	@Override
	public void onItemClickListener(FolderBean bean) {
		if(mCurrentDir.getAbsolutePath().equals(bean.getDir())){
			return;
		}
		mMaxCount = bean.getCount();
		mCurrentDir = new File(bean.getDir());
		if(mImgs != null && !mImgs.isEmpty()){
			mImgs.clear();
		}
		mImgs.addAll(Arrays.asList(mCurrentDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg") || filename.endsWith(".jpeg") || filename.endsWith(".png")) {
					return true;
				}
				return false;
			}
		})));
		resetCompleteBtnStatus();
		dirCount.setText(mMaxCount + "张");
		dirName.setText(mCurrentDir.getName());
		adapter.clearSelectImages(mCurrentDir.getAbsolutePath());
	}

	@Override
	public void onSeletectOrDelete(int selectNum) {
		this.selectNum = selectNum;
		if(selectNum <= 0){
			resetCompleteBtnStatus();
		}else{
			selectImageBtn.setText("完成("+selectNum+"/"+maxNum+")");
		}
		
	}

	/**
	 * 初始化完成按钮的状态
	 */
	private void resetCompleteBtnStatus() {
		selectImageBtn.setText("完成");
	}

	@Override
	public void onPhone() {
		// 拍照
		try {
			camera();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static final int REQUEST_CAMERA = 0;
	
	/**
     * 去拍照
     */
    private void camera() throws Exception {
    	if(TextUtils.isEmpty(appImgCacheDir)){
    		showToast("请输入拍照图片的文件路径。");
    		return;
    	}
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = new File(appImgCacheDir);
            if(!file.exists()){
                file.mkdirs();
            }
            phoneImgPath = appImgCacheDir + System.currentTimeMillis() + ".jpg";
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(phoneImgPath)));
            startActivityForResult(cameraIntent, REQUEST_CAMERA);
        } else {
            showToast("请检查内存卡");
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        try {
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    case REQUEST_CAMERA://拍照返回

                        if (FileHelper.checkFileExists(phoneImgPath)) {
                            Intent intent = new Intent();
                            ArrayList<String> select = new ArrayList<String>();
                            select.add(phoneImgPath);
                            intent.putStringArrayListExtra("resultDate", select);
                            setResult(RESULT_OK, intent);
                            ScreenManager.getScreenManager().popActivity(this);
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	@Override
	protected void uploadException(Exception e) {
		// TODO Auto-generated method stub
		
	}

}
