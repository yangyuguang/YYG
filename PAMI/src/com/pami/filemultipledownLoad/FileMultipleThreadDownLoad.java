package com.pami.filemultipledownLoad;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.pami.listener.ThreadDownLoadListener;

import android.content.Context;
import android.util.Log;

public class FileMultipleThreadDownLoad implements ThreadDownLoadListener{

	private final static int THREAD_SIZE = 3;
	private long fileLength = 0;
	/**
	 * 文件存储路径
	 */
	private String filePath = null;
	/**
	 * 文件临时存储路径
	 */
	private String fileTmpPath = null;
	private static final String FILE_TMP_PATH_ONE = "fileDownLoad/tmpFile";
	private String fileName = null;
	
	private Context mContext = null;
	
	private FileMultipleThreadDownLoadListener onFileMultipleThreadDownLoadListener = null;
	
	private AtomicLong currLen = new AtomicLong();
	private Map<String,Integer> completeThread = new HashMap<String,Integer>();
	
	public FileMultipleThreadDownLoad(Context context){
		this.mContext = context;
		fileTmpPath = mContext.getApplicationContext().getFilesDir().getAbsolutePath() + "/" + FILE_TMP_PATH_ONE;
		File tmpFile = new File(fileTmpPath);
		if(!tmpFile.exists()){
			tmpFile.mkdirs();
		}
	}
	
	/**
	 * 
	 * @param fileUrl
	 * @param filePath
	 * @param onFileMultipleThreadDownLoadListener
	 * @throws Exception
	 */
	public void startDownLoad(final String fileUrl, String filePaths, final FileMultipleThreadDownLoadListener onFileMultipleThreadDownLoadListener) throws Exception{
		
		this.onFileMultipleThreadDownLoadListener = onFileMultipleThreadDownLoadListener;
		this.filePath = filePaths;
		if(onFileMultipleThreadDownLoadListener != null){
			onFileMultipleThreadDownLoadListener.onStartDownLoad();
		}
		
		//判断文件路径是否正确 必须带后缀
		if(fileUrl.contains(".")){
			fileName = fileUrl.hashCode()+fileUrl.substring(fileUrl.lastIndexOf("."));
		}else{
			if(onFileMultipleThreadDownLoadListener != null){
				onFileMultipleThreadDownLoadListener.onDownLoadError(500, "无法识别的文件。");
			}
			return;
		}
		
		//如果之前已经下载过了 则直接告诉view 已经下载完成了
		File oldFile = new File(filePath + "/" + fileName);
		if(oldFile.exists()){
			if(onFileMultipleThreadDownLoadListener != null){
				onFileMultipleThreadDownLoadListener.onCompleteDownLoading(filePath + "/" + fileName);
			}
			return;
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					URL url = new URL(fileUrl);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");

					int code = conn.getResponseCode();
					if (code == 200) {
						// 服务器返回的数据长度，实际上就是文件的长度
						fileLength = conn.getContentLength();
						log("文件的长度:" + fileLength);
						File file = new File(filePath);
						if(!file.exists()){
							file.mkdirs();
						}
						RandomAccessFile raf = new RandomAccessFile(filePath + "/" + fileName, "rwd");
						raf.setLength(fileLength);
						raf.close();
					} else {
						if(onFileMultipleThreadDownLoadListener != null){
							onFileMultipleThreadDownLoadListener.onDownLoadError(500, "服务器错误");
						}
						return;
					}

					// 平均每个线程下载的文件大小
					long blockSize = fileLength / THREAD_SIZE;
					
					//开启线程执行下载
					for (int threadId = 1; threadId <= THREAD_SIZE; threadId++) {
						long startIndex = (threadId - 1) * blockSize;
						long endIndex = threadId * blockSize - 1;
						if (threadId == THREAD_SIZE) {
							endIndex = fileLength;
						}
						
						log("线程：" + threadId + "  下载----" + startIndex + "  -  " + endIndex);
						new DownloadThread(mContext,fileUrl, threadId, startIndex, endIndex, fileTmpPath, fileName, FileMultipleThreadDownLoad.this).start();
						

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
	}

	@Override
	public void onThreadDownLoad(long downLen) {
		synchronized (this) {
			long curr = currLen.addAndGet(downLen);
			if(onFileMultipleThreadDownLoadListener != null){
				onFileMultipleThreadDownLoadListener.onDownLoading(fileLength, curr);
			}
		}
		
	}

	@Override
	public void onThreadDownLoadComplete(int threadId) {
		synchronized (completeThread) {
			try {
				completeThread.put(threadId+"", threadId);
				if(completeThread.size() >= THREAD_SIZE){
					//将文件从临时文件区 转 到正式区域 然后再把临时文件给删除
					File tmpFile = new File(fileTmpPath + "/" + fileName);
					FileInputStream is = new FileInputStream(tmpFile);
					byte[] buf = new byte[1024];
					int len = 0;
					RandomAccessFile raf = new RandomAccessFile(filePath + "/" + fileName, "rwd");
					while((len = is.read(buf)) != -1){
						raf.write(buf, 0, len);
					}
					is.close();
					raf.close();
					tmpFile.delete();
					
					// 如果下载完成 则要删除记录下载位置的文件
					String path1 = mContext.getApplicationContext().getFilesDir().getAbsolutePath();
					for(int mThreadId = 1; mThreadId <= THREAD_SIZE; mThreadId++){
						File file = new File(path1+"/fileDownLoad/record/" + fileName.substring(0, fileName.lastIndexOf(".")) + mThreadId+".txt");
						if(file.exists()){
							file.delete();
						}
					}
					
					//通知View 下载完成 并把下载完成的路径给View
					if(onFileMultipleThreadDownLoadListener != null){
						onFileMultipleThreadDownLoadListener.onCompleteDownLoading(filePath+"/"+fileName);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void log(String s){
		Log.e("yyggssF", "FileMultipleThreadDownLoad====>"+s);
	}
	
	
}
