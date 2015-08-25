package com.pami.utils;

public class ChatFaceUtils {

	/**
	 * 聊天表情处理（发送emoji表情时  发送的表情字符串需要经过此处理 否则IOS不能正确接收）
	 * @param faceName
	 * @return
	 */
	public static String getFaceString(String faceName){
		return String.valueOf(Character.toChars(Integer.parseInt(faceName, 16)));
	}
}
