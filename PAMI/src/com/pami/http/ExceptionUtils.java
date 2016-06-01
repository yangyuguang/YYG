package com.pami.http;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.support.v4.util.ArrayMap;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.pami.utils.MLog;

/**
 * 异常信息上传工具类
 * @author 杨裕光
 *
 */
public class ExceptionUtils {
	
	/**
	 * 上传log 信息
	 * @param context
	 * @param e
	 * @param baseUrl
	 * @param method
	 */
	public static void uploadException(Context context,Exception e,String baseUrl){
		if(context == null || TextUtils.isEmpty(baseUrl)){
			MLog.e("yyg", "无法上传异常信息  请求地址是空");
			return;
		}
		
		
		HttpJSONRequest request = HttpJSONRequest.getinstance(context);
		String httpTag = System.currentTimeMillis()+"";
		
		ArrayMap<String,Object> params = new ArrayMap<String,Object>();
		params.put("device_msg", getDeviceMsg(context).toString());
		params.put("exception_msg", getExceptionMsg(e).toString());
		
//		try {
//			request.jsonPostRequest(httpTag, baseUrl, null, params, new Response.Listener<Object>() {
//
//				@Override
//				public void onResponse(Object response) {
//					// TODO Auto-generated method stub
//					
//				}
//			}, new Response.ErrorListener() {
//
//				@Override
//				public void onErrorResponse(VolleyError error) {
//					// TODO Auto-generated method stub
//					
//				}
//			});
//		} catch (Exception e1) {
//		}
	}
	
	
	/**
	 * 获取异常信息
	 * @param e
	 * @return
	 */
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
	
	/**
	 * 获取设备信息
	 * @param context
	 * @return
	 */
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
