package com.yangyg.http;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

public class HttpStringRequest {
	
	public void sendRequestString(String url,String method , Map<String,Object> params,Map<String,String> headMap,Callback callback){
		if("post".equals(method.toLowerCase())){
			sendRequestStringPost(url, params, headMap, callback);
		}else{
			sendRequestStringGet(url, params, headMap, callback);
		}
	}
	
	/**
     * 参数是String 的GET请求
     * @param url
     * @param params
     */
	public void sendRequestStringGet(String url, Map<String,Object> params,Map<String,String> headMap,Callback callback){
    	OkHttpClient client = new OkHttpClient();
    	StringBuffer sb = new StringBuffer();
    	sb.append(url);
    	if(params != null && !params.isEmpty()){
    		Set<String> set = params.keySet();
    		Iterator<String> ii = set.iterator();
    		int index = 0;
    		while(ii.hasNext()){
    			if(index > 0){
    				sb.append("&");
    			}else{
    				if(!url.endsWith("?")){
    					sb.append("?");
    				}
    			}
    			String key = ii.next();
    			String value = params.get(key).toString();
    			sb.append(key);
    			sb.append("=");
    			sb.append(value);
    			index++;
    		}
    	}
    	Request request;
    	if(headMap != null && !headMap.isEmpty()){
    		Headers headers = Headers.of(headMap);
    		request = new Request.Builder().url(sb.toString()).headers(headers).build();
    	}else{
    		request = new Request.Builder().url(sb.toString()).build();
    	}
    	Call call = client.newCall(request);
    	call.enqueue(callback);
    }
    
    
    /**
     * 参数是String 的POST请求
     * @param url
     * @param params
     */
    public void sendRequestStringPost(String url, Map<String,Object> params,Map<String,String> headMap,Callback callback){
    	OkHttpClient client = new OkHttpClient();
    	FormEncodingBuilder builder = new FormEncodingBuilder();
    	if(params != null && !params.isEmpty()){
    		Set<String> set = params.keySet();
    		Iterator<String> ii = set.iterator();
    		while(ii.hasNext()){
    			String key = ii.next();
    			String value = params.get(key).toString();
    			builder.add(key, value);
    		}
    	}
    	
    	Request request;
    	if(headMap != null && !headMap.isEmpty()){
    		Headers headers = Headers.of(headMap);
    		request = new Request.Builder().url(url).post(builder.build()).headers(headers).build();
    	}else{
    		request = new Request.Builder().url(url).post(builder.build()).build();
    	}
    	Call call = client.newCall(request);
    	call.enqueue(callback);
    }
	

}
