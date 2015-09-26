package com.pami.http;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pami.utils.MLog;

public class ExceptionUtils {
	public static void uploadException(Context context,Exception e,String baseUrl,String method){
		if(context == null || TextUtils.isEmpty(baseUrl)){
			MLog.e("yyg", "无法上传异常信息");
			return;
		}
		
		
		HttpJSONRequest request = HttpJSONRequest.getinstance(context);
		String httpTag = System.currentTimeMillis()+"";
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("device_msg", getDeviceMsg(context).toString());
		params.put("exception_msg", getExceptionMsg(e).toString());
		
		request.jsonPostRequest(httpTag, baseUrl, method, params, new Response.Listener<Object>() {

			@Override
			public void onResponse(Object response) {
				// TODO Auto-generated method stub
				
			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	public static StringBuffer getExceptionMsg(Exception e){
		StringBuffer exceptionMsg = new StringBuffer();
		exceptionMsg.append(e.getMessage());
		exceptionMsg.append("\n");
		StackTraceElement[] ees = e.getStackTrace();
		int index = 0;
		for(StackTraceElement ee : ees){
			if(index > 0){
				exceptionMsg.append("\n");
			}
			exceptionMsg.append(ee.toString());
			index++;
		}
		return exceptionMsg;
	}
	
	public static StringBuffer getDeviceMsg(Context context){
		StringBuffer deviceMsg = new StringBuffer();
		TelephonyManager mTm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		deviceMsg.append("设备ID:");
		deviceMsg.append(imei);
		String imsi = mTm.getSubscriberId();
		deviceMsg.append("   用户ID:");
		deviceMsg.append(imsi);
		String mtype = android.os.Build.MODEL; // 手机型号
		deviceMsg.append("   手机型号:");
		deviceMsg.append(mtype);
		String numer = mTm.getLine1Number(); // 手机号码，有的可得，有的不可得
		deviceMsg.append("   手机号码:");
		deviceMsg.append(numer);
		deviceMsg.append("   SDK版本:");
		deviceMsg.append(android.os.Build.VERSION.SDK);
		deviceMsg.append("   系统版本:");
		deviceMsg.append(android.os.Build.VERSION.RELEASE);
		deviceMsg.append("   异常发生时间：");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		deviceMsg.append(sf.format(new Date(System.currentTimeMillis())));
		
		return deviceMsg;
	}
}
