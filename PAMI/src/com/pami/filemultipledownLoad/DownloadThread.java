package com.pami.filemultipledownLoad;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.pami.listener.ThreadDownLoadListener;

import android.content.Context;
import android.util.Log;

public class DownloadThread extends Thread {

	/**
	 * 线程ID
	 */
	private int threadId = 0;
	/**
	 * 下载文件的开始位置
	 */
	private long mStartIndex = 0;
	/**
	 * 下载文件的结束位置
	 */
	private long mEndIndex = 0;
	/**
	 * 文件在服务器上的路径
	 */
	private String fileUrl = null;
	/**
	 * 文件下载到本地保存的路径
	 */
	private String filePath = null;
	/**
	 * 文件下载到本地 保存的文件名（带后缀）
	 */
	private String fileName = null;
	private Context mContext = null;
	private ThreadDownLoadListener threadDownLoadListener = null;
	
	private static final String RECORD_FILE_ALREADY_DOWN_LEN_FILES = "fileDownLoad/record";
	/**
	 * 已经下载的长度
	 */
	private long alreadDown = 0;
	
	private String recordFilePath = null;
	
	/**
	 * 
	 * @param fileUrl 文件在服务器上的路径
	 * @param threadId 线程ID
	 * @param startIndex 下载的开始位置
	 * @param endIndex 下载的结束位置
	 * @param filePath 文件保存的路径
	 * @param fileName 文件名称
	 */
	public DownloadThread(Context mContext,String fileUrl, int threadId, long startIndex, long endIndex, String filePath,String fileName,ThreadDownLoadListener threadDownLoadListener) {
		this.mContext = mContext;
		this.fileUrl = fileUrl;
		this.threadId = threadId;
		this.mStartIndex = startIndex;
		this.mEndIndex = endIndex;
		this.filePath = filePath;
		this.fileName = fileName;
		this.threadDownLoadListener = threadDownLoadListener;
		
		String path1 = mContext.getApplicationContext().getFilesDir().getAbsolutePath();
		recordFilePath = path1 + "/"+RECORD_FILE_ALREADY_DOWN_LEN_FILES + "/" + fileName.substring(0, fileName.lastIndexOf(".")) + threadId + ".txt";
		
		FileInputStream fis = null;
		InputStreamReader ips = null;
		BufferedReader br = null;
		try {
			
			File file = new File(path1+"/"+RECORD_FILE_ALREADY_DOWN_LEN_FILES);
			if(!file.exists()){
				file.mkdirs();
			}else{
				File raf = new File(recordFilePath);
				if(raf.exists()){
					 fis = new FileInputStream(raf);
					 ips = new InputStreamReader(fis);
					 br = new BufferedReader(ips);
					
					String alreadyIndex = br.readLine();
					alreadDown = Long.valueOf(alreadyIndex);
					log("线程:"+threadId+"   已经下载的长度:"+alreadyIndex+"  ,  "+alreadDown);
				}
			}
			if(threadDownLoadListener != null){
				threadDownLoadListener.onThreadDownLoad(alreadDown);
			}
			mStartIndex += alreadDown;
			log("线程:"+threadId+"   开始位置:"+mStartIndex+"    alreadDown："+alreadDown);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(br != null){
					br.close();
				}
				if(ips != null){
					ips.close();
				}
				if(fis != null){
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}

	@Override
	public void run() {
		try {
			
			
			if(mStartIndex >= mEndIndex){
				if(threadDownLoadListener != null){
					threadDownLoadListener.onThreadDownLoadComplete(threadId);
				}
				log("线程：" + threadId + "  之前就已经下载完成"+"   请求开始位置："+mStartIndex+"   请求开始结束："+mEndIndex);
				return;
			}
			
			URL url = new URL(fileUrl);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			
			// 重要： 请求服务器下载部分的文件 指定文件的大小
			conn.setRequestProperty("Range", "bytes=" + mStartIndex + "-" + mEndIndex);
			// RandomAccessFile 随机文件访问类
			/*
			 * 从服务器请求全部资源 返回的成功的状态码是200 如果从服务器请求部分资源 返回的成功的状态码是206
			 */
			int code = conn.getResponseCode();
			log("线程：" + threadId + "  CODE:" + code+"   请求开始位置："+mStartIndex+"   请求开始结束："+mEndIndex);
			if (code == 206) {
				// 返回设置了请求位置的对应的输入流
				InputStream is = conn.getInputStream();
				RandomAccessFile raf = new RandomAccessFile(filePath + "/" + fileName, "rwd");
				// 随机写文件的时候 从哪个位置开始写
				raf.seek(mStartIndex);

				byte[] buf = new byte[1024];
				int len = 0;
				
				log("线程：" + threadId + "  记录文件："+recordFilePath);
				
				while ((len = is.read(buf)) != -1) {
					RandomAccessFile file = new RandomAccessFile(recordFilePath, "rwd");
					raf.write(buf, 0, len);
					alreadDown += len;
					log("====线程【"+threadId+"】==下载中=====>"+alreadDown);
					if(threadDownLoadListener != null){
						threadDownLoadListener.onThreadDownLoad(len);
					}
					file.write(String.valueOf(alreadDown).getBytes());
					file.close();
				}
				is.close();
				raf.close();
				log("线程：" + threadId + "  下载完毕");
				if(threadDownLoadListener != null){
					threadDownLoadListener.onThreadDownLoadComplete(threadId);
				}
			}else{
				log("线程：" + threadId + "  下载失败:"+conn.getResponseMessage());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void log(String s){
		Log.e("yyggssD", "DownloadThread====>"+s);
	}
}
