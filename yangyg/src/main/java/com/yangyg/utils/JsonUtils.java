package com.yangyg.utils;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.util.ArrayMap;

public class JsonUtils {


	/**
	 * @param result
	 * @return
	 * 服务器返回是否成功
	 */
	public static boolean isSuccess (String result){
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status=jsonObject.optInt("code",1);
			if(status == 1){
				return true;
			}else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	public static int getCode(String result){
		try {
			JSONObject jsonObject = new JSONObject(result);
			int status=jsonObject.optInt("code",1);
			return status;
		} catch (JSONException e) {
			e.printStackTrace();
			return -100;
		}
	}
	/**
	 * @param result
	 * @return
	 * 获取成功结果之后的data节点数据
	 */
	public static String getSuccessData(String result,String feild){
		try {
			JSONObject jsonObject=new JSONObject(result);
			return jsonObject.optString(feild,"");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @param result
	 * @return
	 * 获取调用失败的信息
	 */
	public static String getResponseMessage(String result){
		try {
			JSONObject jsonObject=new JSONObject(result);
			return jsonObject.optString("error_text","");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * @param result
	 * @return
	 * 获取成功结果之后的data节点数据
	 */
	public static String getFiledData(String result,String feild){
		try {
			JSONObject jsonObject=new JSONObject(result);
			return jsonObject.optString(feild,"");
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public static ArrayMap<String, Object> getAboutUs(String json){
		ArrayMap<String, Object> map = new ArrayMap<String, Object>();
		try {
			JSONObject jsonObject = new JSONObject(json);
			map.put("url", jsonObject.getString("url"));
			return map;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * 将一个List对象装换成一个JSONArray对象
	 * 注意List里面的bean对象需要实现toString 方法 并且返回的字符串必须符合json数据的格式
	 * @param list
	 * @return
	 */
	public static <T> JSONArray getJsonArrayByList(List<T> list){
		JSONArray ja = new org.json.JSONArray();
		for(T t:list){
			ja.put(getJsonByObject(t));
		}
		return ja;
	}
	
	/**
	 * 将一个Object 对象 转换成 json对象
	 * 注意:t对象需要实现toString 方法 并且返回的字符串必须符合json数据的格式
	 * @param t
	 * @return
	 */
	public static <T> JSONObject getJsonByObject(T t){
		JSONObject jo = null;
		try {
			jo = new JSONObject(t.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo;
	}
}
