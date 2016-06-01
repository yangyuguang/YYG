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
	public void jsonPostRequest(String httpTag,String baseUrl, String method, ArrayMap<String, Object> params, ArrayMap<String, String> header, Listener jsonResult,
			ErrorListener errorListener) throws Exception{
		jsonRequest(httpTag, baseUrl, method,Request.Method.POST, params, header, jsonResult, errorListener);
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
	public void jsonGetRequest(String httpTag,String baseUrl, String method, ArrayMap<String, Object> params, ArrayMap<String, String> header, Listener jsonResult,
			ErrorListener errorListener) throws Exception{
		jsonRequest(httpTag, baseUrl, method,Request.Method.GET, params, header, jsonResult, errorListener);
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
	private void jsonRequest(String httpTag,String baseUrl, String methodName,int method, ArrayMap<String, Object> params, final ArrayMap<String, String> header, Listener jsonResult,
			ErrorListener errorListener) throws Exception{
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
					jsonParam, jsonResult, errorListener){
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return getHttpsHeaders(header);
				}
			};
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
	 * 发送https POST请求
	 * @param httpTag 请求标示符
	 * @param baseUrl 请求URL 如:https://www.xxx.com/api
	 * @param methodName 请求方法名
	 * @param params 请求参数 此参数最后会以JSONObject的方式发送
	 * @param jsonResult 返回的结果
	 * @param errorListener 返回的错误结果
	 * @throws Exception
	 */
	public void jsonPostRequestHttps(String httpTag, String baseUrl, String methodName, ArrayMap<String, Object> params, ArrayMap<String, String> headers, Listener jsonResult,
			ErrorListener errorListener)throws Exception{
		this.jsonRequestHttps(httpTag, baseUrl, methodName, Request.Method.POST, params, headers, jsonResult, errorListener);
	}
	
	/**
	 * 发送https GET请求
	 * @param httpTag 请求标示符
	 * @param baseUrl 请求URL 如:https://www.xxx.com/api
	 * @param methodName 请求方法名
	 * @param params 请求参数 
	 * @param jsonResult 返回的结果
	 * @param errorListener 返回的错误结果
	 * @throws Exception
	 */
	public void jsonGetRequestHttps(String httpTag, String baseUrl, String methodName, ArrayMap<String, Object> params, ArrayMap<String, String> headers, Listener jsonResult,
			ErrorListener errorListener)throws Exception{
		this.jsonRequestHttps(httpTag, baseUrl, methodName, Request.Method.GET, params, headers, jsonResult, errorListener);
	}
	
	/**
	 * 发送https 请求
	 * @param httpTag 请求标示符
	 * @param baseUrl 请求URL 如:https://www.xxx.com/api
	 * @param methodName 请求方法名
	 * @param method POST 或者  GET请求
	 * @param params 请求参数  此参数最后会以JSONObject的方式发送
	 * @param jsonResult 返回的结果
	 * @param errorListener 返回的错误结果
	 * @throws Exception
	 */
	private void jsonRequestHttps(String httpTag, String baseUrl, String methodName, int method, ArrayMap<String, Object> params, final ArrayMap<String, String> headers,Listener jsonResult,
			ErrorListener errorListener)throws Exception {
		
		PMApplication.getInstance().getRequestQueue().cancelAll(httpTag);
		JsonObjectRequest jsonRequest = null;
		if(method == Request.Method.POST){
			JSONObject jsonParam = null;
				jsonParam = getJSONObjectByMap(params);
			jsonRequest = new JsonObjectRequest(method, TextUtils.isEmpty(methodName) ? baseUrl : baseUrl + "/" + methodName,
					jsonParam, jsonResult, errorListener){
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return getHttpsHeaders(headers);
				}
			};
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
					null, jsonResult, errorListener){
				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {
					return getHttpsHeaders(headers);
				}
			};
		}
		
		jsonRequest.setTag(httpTag);
		jsonRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		PMApplication.getInstance().getRequestQueue().add(jsonRequest);
	}
	
	/**
	 * 获取header
	 * @param headers
	 * @return
	 */
	private ArrayMap<String,String> getHttpsHeaders(ArrayMap<String,String> headers){
		ArrayMap<String,String> header = new ArrayMap<String, String>();
		if(headers != null && !headers.isEmpty()){
			Set<String> set = headers.keySet();
			Iterator<String> ii = set.iterator();
			while(ii.hasNext()){
				String key = ii.next();
				String value = headers.get(key);
				header.put(key, value);
			}
		}
		header.put("Accept", "application/json");
		header.put("Content-Type", "application/json; charset=UTF-8");
		return header;
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
