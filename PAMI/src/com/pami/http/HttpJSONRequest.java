package com.pami.http;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pami.PMApplication;
import com.pami.utils.JsonUtils;
import com.pami.utils.MLog;

public class HttpJSONRequest {

	private static HttpJSONRequest httpStringRequest;
	private static final int TIMEOUT_MS = 50 * 1000;
	
	private HttpJSONRequest(){}

	public static HttpJSONRequest getinstance(Context context) {
		if (httpStringRequest == null) {
			synchronized (HttpJSONRequest.class) {
				if (httpStringRequest == null) {
					httpStringRequest = new HttpJSONRequest();
				}
			}
		}
		return httpStringRequest;
	}


	/**
	 * 以post的方式 发送http 请求
	 * @param httpTag 请求标示符
	 * @param baseUrl  请求URL 如:http://www.xxx.com/api/
	 * @param method 请求方法名
	 * @param params 请求参数 此参数最后会以JSONObject的方式发送
	 * @param jsonResult 请求结果
	 * @param errorListener 请求错误结果
	 */
	public void jsonPostRequest(String httpTag,String baseUrl, String method, ArrayMap<String, Object> params, Listener jsonResult,
			ErrorListener errorListener) {
		jsonRequest(httpTag, baseUrl, method,Request.Method.POST, params, jsonResult, errorListener);
	}
	
	/**
	 * 以get的方式 发送http 请求
	 * @param httpTag 请求标示符
	 * @param baseUrl  请求URL 如:http://www.xxx.com/api/
	 * @param method 请求方法名
	 * @param params 请求参数 此参数最后会以JSONObject的方式发送
	 * @param jsonResult 请求结果
	 * @param errorListener 请求错误结果
	 */
	public void jsonGetRequest(String httpTag,String baseUrl, String method, ArrayMap<String, Object> params, Listener jsonResult,
			ErrorListener errorListener) {
		jsonRequest(httpTag, baseUrl, method,Request.Method.GET, params, jsonResult, errorListener);
	}
	/**
	 * 发送http请求  参数封装成JSON
	 * @param httpTag 请求标示符
	 * @param baseUrl  请求URL 如:http://www.xxx.com/api
	 * @param method 请求方法名
	 * @param params 请求参数 此参数最后会以JSONObject的方式发送
	 * @param jsonResult 请求结果
	 * @param errorListener 请求错误结果
	 */
	private void jsonRequest(String httpTag,String baseUrl, String methodName,int method, ArrayMap<String, Object> params, Listener jsonResult,
			ErrorListener errorListener) {
		PMApplication.getInstance().getRequestQueue().cancelAll(httpTag);
		JsonObjectRequest jsonRequest = null;
		if(method == Request.Method.POST){
			JSONObject jsonParam = null;
			try {
				jsonParam = getJSONObjectByMap(params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jsonRequest = new JsonObjectRequest(method, TextUtils.isEmpty(methodName) ? baseUrl : baseUrl + "/" + methodName,
					jsonParam, jsonResult, errorListener);
		}else{
			StringBuffer sb = new StringBuffer();
			int index = 0;
			Set<String> set = params.keySet();
			Iterator<String> ii = set.iterator();
			while(ii.hasNext()){
				String key = ii.next();
				String value = params.get(key).toString();
				if(index > 0){
					sb.append("&");
				}
				sb.append(key);
				sb.append("=");
				sb.append(value);
				index++;
			}
			jsonRequest = new JsonObjectRequest(method, TextUtils.isEmpty(methodName) ? baseUrl : baseUrl + "/" + methodName+"?"+sb.toString(),
					null, jsonResult, errorListener);
		}
		
		
		jsonRequest.setTag(httpTag);
		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		PMApplication.getInstance().getRequestQueue().add(jsonRequest);
	}
	
	/**
	 * 以post的方式 发送https 请求
	 * @param httpTag 请求标示符
	 * @param method 请求方法名
	 * @param baseUrl 请求URL 如:https://www.xxx.com/api/
	 * @param params  请求参数  此参数最后会以JSONObject的方式发送
	 * @param jsonResult 返回的结果
	 * @param errorListener 返回的错误结果
	 */
	public void requestJSONPostByHttps(String httpTag, String methodName, String baseUrl,ArrayMap<String, Object> params, Listener jsonResult,
			ErrorListener errorListener) {
		
		JSONObject jsonParam = null;
		try {
			jsonParam = getJSONObjectByMap(params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		MLog.w("yyg", "请求URL【" + (TextUtils.isEmpty(methodName) ? baseUrl : baseUrl + "/" + methodName) + "】");
		JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, baseUrl+methodName,
				jsonParam, jsonResult, errorListener){
			@Override
			public Map<String, String> getHeaders()
					throws AuthFailureError {
				ArrayMap<String,String> headers = new ArrayMap<String, String>();
				headers.put("Accept", "application/json");
				headers.put("Content-Type", "application/json; charset=UTF-8");
//				return super.getHeaders();
				return headers;
			}
		};
		jsonRequest.setTag(httpTag);
		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		PMApplication.getInstance().getRequestQueue().add(jsonRequest);
		
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
