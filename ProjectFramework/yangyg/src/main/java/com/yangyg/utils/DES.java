package com.yangyg.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密 或者 解密
 * @author Administrator
 *
 */
public class DES {

	private static byte[] iv = {1,2,3,4,5,6,7,8};  
	
	/**
	 * 使用DES加密
	 * @param encryptString 加密的字符串
	 * @param encryptKey  加密的Key
	 * @return
	 * @throws Exception
	 */
    public static String encryptDES(String encryptString, String encryptKey) throws Exception {  
        IvParameterSpec zeroIv = new IvParameterSpec(iv);  
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");  
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);  
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());  
        return Base64.encode(encryptedData);  
    }  
    
    /**
     * 使用DES解密
     * @param decryptString 解密的字符串
     * @param decryptKey  解密的key
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString, String decryptKey) throws Exception {  
        byte[] byteMi = Base64.decode(decryptString);  
        IvParameterSpec zeroIv = new IvParameterSpec(iv);  
        SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");  
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");  
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);  
        return new String(decryptedData);  
    }
}
