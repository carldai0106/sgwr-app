package com.sgwr.app.utils;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.http.util.EncodingUtils;

import android.util.Base64;

/**
 * 加密解密工具包
 * 
 * @author Winter Lau
 * @date 2011-12-26
 */
public class CyptoUtils {

	public static final String ALGORITHM_DES = "DES/CBC/PKCS5Padding";
	public static final byte[] ivKeys = EncodingUtils.getBytes("#BCSINT~",
			"utf-8");

	/**
	 * DES算法，加密
	 * 
	 * @param data
	 *            待加密字符串
	 * @param key
	 *            加密私钥，长度不能够小于8位
	 * @return 加密后的字节数组，一般结合Base64编码使用
	 * @throws InvalidAlgorithmParameterException
	 * @throws Exception
	 */
	public static String encode(String key, String data)
	{
		if (data == null)
			return null;
		try
		{
			DESKeySpec dks = new DESKeySpec(
					EncodingUtils.getBytes(key, "utf-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec(ivKeys);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = cipher.doFinal(data.getBytes());

			return Base64.encodeToString(bytes, Base64.DEFAULT);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return data;
		}
	}

	/**
	 * DES算法，解密
	 * 
	 * @param data
	 *            待解密字符串
	 * @param key
	 *            解密私钥，长度不能够小于8位
	 * @return 解密后的字节数组
	 * @throws Exception
	 *             异常
	 */
	public static String decode(String key, String data)
	{
		if (data == null)
			return null;
		try
		{

			DESKeySpec dks = new DESKeySpec(
					EncodingUtils.getBytes(key, "utf-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			// key的长度不能够小于8位字节
			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
			IvParameterSpec iv = new IvParameterSpec(ivKeys);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(Cipher.DECRYPT_MODE, secretKey, paramSpec);
			byte[] bytes = Base64.decode(data.getBytes(), Base64.DEFAULT);
			return new String(cipher.doFinal(bytes));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return data;
		}
	}
}
