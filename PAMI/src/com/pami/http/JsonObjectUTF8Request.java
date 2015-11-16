package com.pami.http;

import java.io.UnsupportedEncodingException;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;

public class JsonObjectUTF8Request extends JsonObjectRequest {

public JsonObjectUTF8Request(int method, String url, JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(method, url, jsonRequest, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	public JsonObjectUTF8Request(String url, JSONObject jsonRequest, Listener<JSONObject> listener,
			ErrorListener errorListener) {
		super(url, jsonRequest, listener, errorListener);
	}


	@Override
	protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
		try {
			// solution 1:
			String jsonString = new String(response.data, "gbk");

			return Response.success(new JSONObject(jsonString), HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JSONException je) {
			return Response.error(new ParseError(je));
		}
	}

}
