package com.pami.http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.pami.PMApplication;
import com.pami.utils.MLog;

public class HttpStringRequest {
	private static HttpStringRequest httpStringRequest;
	private static final int TIMEOUT_MS = 50 * 1000;

	private HttpStringRequest() {
	}

	public static HttpStringRequest getinstance(Context context) {
		if (httpStringRequest == null) {
			synchronized (HttpStringRequest.class) {
				if (httpStringRequest == null) {
					httpStringRequest = new HttpStringRequest();
				}
			}
		}
		return httpStringRequest;
	}

	/**
	 * @param context 上下文
	 * @param method post 或者 get
	 * @param url 接口url
	 * @param getparam map 拼接url传递的参数
	 * @param postparam map post传递的参数
	 * @param header map 头参数
	 * @param listener 请求相应回调
	 * @param errorListener 请求错误回调
	 */
	public StringRequest request(String tag, final int method, String url, final Map<String, String> getParam,
			final Map<String, String> postParam, final Map<String, String> header, Listener<String> listener,
			ErrorListener errorListener) {
		try {
			if (getParam != null) {
				StringBuilder params = new StringBuilder();
				params.append("?");
				Set<Map.Entry<String, String>> set = getParam.entrySet();
				for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
					params.append(entry.getKey() + "");
					params.append("=");
					params.append(URLEncoder.encode(entry.getValue() + "", "UTF-8"));
					params.append("&");
				}
				if (params.length() > 0) {
					url += params.substring(0, params.length() - 1);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		MLog.i("ssss", method + "" + url);
		StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return postParam;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return header == null ? super.getHeaders() : header;
			}

		};
		stringRequest.setTag(tag);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		PMApplication.getInstance().getRequestQueue().add(stringRequest);
		return stringRequest;

	}

	/**
	 * @param context 上下文
	 * @param method post 或者 get
	 * @param url 接口url
	 * @param param map 传递的参数
	 * @param header map 头参数
	 * @param listener 请求相应回调
	 * @param errorListener 请求错误回调
	 */
	public StringRequest request(String tag, final int method, String url, final Map<String, String> param,
			final Map<String, String> header, Listener<String> listener, ErrorListener errorListener) {
		try {
			if (method == Method.GET && param != null) {
				StringBuilder params = new StringBuilder();
				params.append("?");
				Set<Map.Entry<String, String>> set = param.entrySet();
				for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
					Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
					params.append(entry.getKey() + "");
					params.append("=");
					params.append(URLEncoder.encode(entry.getValue() + "", "UTF-8"));
					params.append("&");
				}
				if (params.length() > 0) {
					url += params.substring(0, params.length() - 1);
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		MLog.e("yyg", "----url--" + url);
		MLog.e("yyg", "----param--" + param.toString());

		StringRequest stringRequest = new StringRequest(method, url, listener, errorListener) {

			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				return param;
			}

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				return header == null ? super.getHeaders() : header;
			}

		};
		stringRequest.setTag(tag);
		stringRequest.setRetryPolicy(new DefaultRetryPolicy(TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		PMApplication.getInstance().getRequestQueue().add(stringRequest);
		return stringRequest;

	}

}
