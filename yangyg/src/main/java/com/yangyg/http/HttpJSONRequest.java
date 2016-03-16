package com.yangyg.http;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

import android.support.v4.util.ArrayMap;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.yangyg.YYGApplication;
import com.yangyg.utils.JsonUtils;

public class HttpJSONRequest extends BaseHttpRequest{
	
	
	
	/**
     * 参数是JSON的方式 发送请求
     * @param url
     * @param method
     * @param params
     */
    public void sendRequestJsonParams(String tag,String url,String method,ArrayMap<String,Object> params,ArrayMap<String,String> headMap,Callback callback)throws Exception{
    	if("post".equals(method.toLowerCase())){
    		sendRequestJsonPostParams(tag, url, params, headMap, callback);
    	}else{
    		sendRequestJsonPostParams(tag, url, params, headMap, callback);
    	}
    }
    
    /**
     * 参数是JSON的方式 发送POST 请求
     * @param url
     * @param params
     */
    private void sendRequestJsonPostParams(String tag,String url,ArrayMap<String,Object> params,ArrayMap<String,String> headMap,Callback callback)throws Exception{
    	JSONObject json =  getJSONObjectByMap(params);
    	OkHttpClient client = YYGApplication.getInstance().getDefaultOkHttpClient();
    	client.cancel(tag);
    	RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
        
    	Request request;
    	if(headMap != null && !headMap.isEmpty()){
    		Headers headers = Headers.of(headMap);
    		request = new Request.Builder().url(url).post(body).tag(tag).headers(headers).build();
    	}else{
    		request = new Request.Builder().url(url).post(body).tag(tag).build();
    	}
         Call call = client.newCall(request);
         call.enqueue(callback);
    }
    
    /**
	 * 将map对象 装换成JSONObject 对象
	 * @param params 请求参数
	 * @return
	 * @throws Exception
	 */
	private <T> JSONObject getJSONObjectByMap(ArrayMap<String,Object> params)throws Exception{
		if(params != null && !params.isEmpty()){
			JSONObject jsonParam = new JSONObject();
			if (params != null && !params.isEmpty()) {
				Set<String> set = params.keySet();
				Iterator<String> ii = set.iterator();
				while (ii.hasNext()) {
					String key = ii.next();
					Object obj = params.get(key);
					if(obj instanceof List){
						List<T> list = (List<T>) obj;
						jsonParam.put(key, JsonUtils.getJsonArrayByList(list));
					}else if(obj instanceof String){
						jsonParam.put(key, obj.toString());
					}else{
						jsonParam.put(key, obj);
					}
					
				}
			}
			return jsonParam;
		}else{
			return null;
		}
	}
    
}
