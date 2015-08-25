package com.pami.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.pami.utils.MLog;
import com.pami.utils.MResource;

public class DownloadAppService extends IntentService {

	private String app_url = null;
	private static String savePath = "/sdcard/updateApkDemo/";
	private static final String saveFileName = savePath + System.currentTimeMillis() +".apk";
	
	private NotificationManager mNotificationManager = null;
	private Notification mNotification = null;
	private static final int NOTIFY_ID = 0;
	
	private static long totalLenght = 0;
	
	public DownloadAppService() {
		super("DownloadAppService");
	}
	public DownloadAppService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		app_url = intent.getStringExtra("app_url");
		MLog.i("ph", "-------- url =" +app_url);
		try {
			mNotificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
			mNotification = new Notification(MResource.getIdByName(getApplicationContext(), "drawable", "pm_nitifacation_icon"), "开始下载", System.currentTimeMillis());
			// 放置在"正在运行"栏目中
			mNotification.flags = Notification.FLAG_ONGOING_EVENT;
			RemoteViews contentView = new RemoteViews(getPackageName(), MResource.getIdByName(getApplicationContext(), "layout", "pm_download_app_notification"));
			contentView.setTextViewText(MResource.getIdByName(getApplicationContext(), "id", "name"), "APK 正在下载中...");
			// 指定个性化视图
			mNotification.contentView = contentView;
			mNotificationManager.notify(NOTIFY_ID, mNotification);
			
			MLog.e("yyg", "----------开始下载-------->");
			downloadApp();
			MLog.e("yyg", "----------结束下载-------->");
		} catch (Exception e) {
			MLog.e("yyg", "----------有错-------->");
			e.printStackTrace();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	private int lastRate = 0;
	private void downloadApp()throws Exception {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
				if(TextUtils.isEmpty(app_url)){
					MLog.e("yyg", "--------【app 下载地址为空】------->");
					return;
				}
				URL url = new URL(app_url);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				totalLenght = conn.getContentLength();
				MLog.e("yygs", "总长度:"+totalLenght);
				InputStream is = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				File ApkFile = new File(saveFileName);
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = -1;
				double read_total_lenght = 0;
				byte buf[] = new byte[1024];
				while((count = is.read(buf)) > 0){
					fos.write(buf, 0, count);
					read_total_lenght += count;
					Message msg = mHandler.obtainMessage();
					msg.what = 0;
					msg.obj = read_total_lenght;
					
					int num =  (int) ((read_total_lenght/totalLenght)*100);
					if(num >= lastRate + 1){
						mHandler.sendMessage(msg);	
						lastRate = num;
					}
					count = -1;
				}
				fos.flush();
				is.close();
				fos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void updateNotifivcation(double read_total_lenght){
		RemoteViews contentview = mNotification.contentView;
		DecimalFormat df = new DecimalFormat("########0.00");   
		long tt = Math.round((Double.valueOf(df.format((read_total_lenght/totalLenght))) * 100));
		long lenght = totalLenght / 100;
		double ff = read_total_lenght / lenght;
		contentview.setTextViewText(MResource.getIdByName(getApplicationContext(), "id", "tv_progress"), tt + "%");
		MLog.e("yyg", "---【read_total_lenght:"+read_total_lenght+"】【百分比:"+tt+"】【进度条刻度:"+(new Double(ff)).intValue()+"】【ff:"+ff+"】---------------");
		contentview.setProgressBar(MResource.getIdByName(getApplicationContext(), "id", "progressbar"), 100, (new Double(ff)).intValue(), false);
//		contentview.setProgressBar(MResource.getIdByName(getApplicationContext(), "id", "progressbar"), totalLenght, ((read_total_lenght/totalLenght) * 100), false);
		mNotificationManager.notify(NOTIFY_ID, mNotification);
		if(read_total_lenght == totalLenght){
			//TODO 表示下载完成
			installApk();
		}
	}
	
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				double read_total_lenght = (Double) msg.obj;
				updateNotifivcation(read_total_lenght);
				break;

			default:
				break;
			}
		}
	};
	/**
	 * 安装apk
	 * 
	 * @param url
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		getApplication().startActivity(i);

	}
}
