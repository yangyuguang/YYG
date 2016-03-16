package com.yangyg.http;


import android.support.v4.util.ArrayMap;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.yangyg.YYGApplication;

import java.io.File;
import java.util.List;


public class HttpFileRequest {

	public static final String TYPE = "image/png";
	/**
	 * 上传单个文件
	 * @param tag 请求标示
	 * @param url 请求地址
	 * @param filePath 文件绝对路径
	 * @param fileName 文件名 类似于h5中 <input type="file" name="mFile" >  中的 name
	 * @param params 请求参数
	 * @param headMap 请求头
	 * @param callback 回调
	 */
	public void uploadFile(String tag,String url,String filePath,String fileName,ArrayMap<String,String> params,ArrayMap<String,String> headMap,Callback callback)throws Exception{
		uploadFile( tag, url,new File(filePath) , fileName, params, headMap, callback);
	}
	/**
	 * 上传单个文件
	 * @param tag 请求标示
	 * @param url 请求地址
	 * @param file 文件
	 * @param fileName 文件名 类似于h5中 <input type="file" name="mFile" >  中的 name
	 * @param params 请求参数
	 * @param headMap 请求头
	 * @param callback 回调
	 */
	public void uploadFile(String tag,String url,File file,String fileName,ArrayMap<String,String> params,ArrayMap<String,String> headMap,Callback callback)throws Exception{
		//取消此tag的所有请求
		OkHttpClient client = YYGApplication.getInstance().getDefaultOkHttpClient();
    	client.cancel(tag);
    	
    	//添加请求参数到表单
		MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
		if(params != null && !params.isEmpty()){
			for(String key:params.keySet()){
				multipartBuilder.addFormDataPart(key, params.get(key));
			}
		}
		
		//添加文件到表单
		multipartBuilder.addFormDataPart(fileName, file.getName(), RequestBody.create(MediaType.parse(TYPE), file));
		
		//创建Request对象
		Request request = null;
		if(headMap != null && !headMap.isEmpty()){
			Headers headers = Headers.of(headMap);
			request = new Request.Builder().url(url).post(multipartBuilder.build()).tag(tag).headers(headers).build();
		}else{
			request = new Request.Builder().url(url).post(multipartBuilder.build()).tag(tag).build();
		}
		Call call = client.newCall(request);
        call.enqueue(callback);
	}
	
	/**
	 * 上传多个文件
	 * @param tag
	 * @param url
	 * @param files
	 * @param fileName
	 * @param params
	 * @param headMap
	 * @param callback
	 * @throws Exception
	 */
	public void uploadFiles(String tag,String url,List<File> files,String fileName,ArrayMap<String,String> params,ArrayMap<String,String> headMap,Callback callback)throws Exception{
		//取消此tag的所有请求
				OkHttpClient client = YYGApplication.getInstance().getDefaultOkHttpClient();
		    	client.cancel(tag);
		    	
		    	//添加请求参数到表单
				MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
				if(params != null && !params.isEmpty()){
					for(String key:params.keySet()){
						multipartBuilder.addFormDataPart(key, params.get(key));
					}
				}
				
				//添加文件到表单
				
				if(files != null && !files.isEmpty()){
					for(File file:files){
						multipartBuilder.addFormDataPart(fileName, file.getName(), RequestBody.create(MediaType.parse(TYPE), file));
					}
				}
				
				//创建Request对象
				Request request = null;
				if(headMap != null && !headMap.isEmpty()){
					Headers headers = Headers.of(headMap);
					request = new Request.Builder().url(url).post(multipartBuilder.build()).tag(tag).headers(headers).build();
				}else{
					request = new Request.Builder().url(url).post(multipartBuilder.build()).tag(tag).build();
				}
				Call call = client.newCall(request);
		        call.enqueue(callback);
	}
	
	public void uploadFiles(String tag,String url,File[] files,String fileName,ArrayMap<String,String> params,ArrayMap<String,String> headMap,Callback callback)throws Exception{
		//取消此tag的所有请求
		OkHttpClient client = YYGApplication.getInstance().getDefaultOkHttpClient();
    	client.cancel(tag);
    	
    	//添加请求参数到表单
		MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
		if(params != null && !params.isEmpty()){
			for(String key:params.keySet()){
				multipartBuilder.addFormDataPart(key, params.get(key));
			}
		}
		
		//添加文件到表单
		if(files != null && files.length > 0){
			for(File file:files){
				multipartBuilder.addFormDataPart(fileName, file.getName(), RequestBody.create(MediaType.parse(TYPE), file));
			}
		}
		
		//创建Request对象
		Request request = null;
		if(headMap != null && !headMap.isEmpty()){
			Headers headers = Headers.of(headMap);
			request = new Request.Builder().url(url).post(multipartBuilder.build()).tag(tag).headers(headers).build();
		}else{
			request = new Request.Builder().url(url).post(multipartBuilder.build()).tag(tag).build();
		}
		Call call = client.newCall(request);
        call.enqueue(callback);
	}
}
