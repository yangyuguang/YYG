package com.yangyg.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * AES加密解密
 * 
 * @author liubin
 * 
 */
public class AESUtil {
	/**
	 * AES加密算法的模式和填充方式，使用的是AES/ECB/PKCS5Padding。
	 */
	public static final String AES_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";

	/**
	 * AES加密，使用"AES/ECB/PKCS5Padding"模式和填充方式。
	 * @param password 加密密钥
	 * @param content 要加密的内容
	 * @return
	 * @throws Exception
	 */
	public static byte[] aesEncrypt(String password, String content) throws Exception {

		SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);// 创建密码器
		byte[] byteContent = content.getBytes("utf-8");
		cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(byteContent);
		return result; // 加密

	}

	/**
	 * AES解密，使用"AES/ECB/PKCS5Padding"模式和填充方式。
	 * @param content 要解密的内容
	 * @param password 解密密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] aesDecrypt(byte[] content, String password) throws Exception {
		SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance(AES_CIPHER_ALGORITHM);// 创建密码器
		cipher.init(Cipher.DECRYPT_MODE, key);// 初始化
		byte[] result = cipher.doFinal(content);
		return result; // 解密
	}

	/**
	 * 将2进制数组转换为16进制字符串（AES）
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 把16进制字符串转换成字节数组
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}

	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}
}