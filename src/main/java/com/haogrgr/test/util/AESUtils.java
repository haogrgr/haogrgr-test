package com.haogrgr.test.util;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 * AES加密解密工具类,该工具只依赖JDK
 * 
 * @author desheng.tu
 * @date 2015年4月13日 上午11:48:10
 */
public class AESUtils {

	public static final String ALGORITHM = "AES";
	public static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";

	public static void main(String[] args) {
		String secretkey = "XvQL99XfFuotkxjQ";
		String input = "haogrgrgrgrgrgrgrgrgrg";
		
		System.out.println(encryptToHex(input, secretkey));
		System.out.println(hexDecrypt((encryptToHex(input, secretkey)), secretkey));
		
	}

	/**
	 * AES加密
	 * @param whatToEncrypt 要加密的内容
	 * @param secretkey AES secret key
	 * @return 加密后的内容(Base64)
	 */
	public static String encryptToBase64(String whatToEncrypt, String secretkey) {
		byte[] result = encrypt(UTF8Bytes(whatToEncrypt), newKey(UTF8Bytes(secretkey)), newDefaultIV());
		return Base64.getEncoder().encodeToString(result);
	}
	
	/**
	 * AES加密
	 * @param whatToEncryptHex 要加密的内容
	 * @param secretkey AES secret key
	 * @return 加密后的内容Hex
	 */
	public static String encryptToHex(String whatToEncrypt, String secretkey) {
		byte[] result = encrypt(UTF8Bytes(whatToEncrypt), newKey(UTF8Bytes(secretkey)), newDefaultIV());
		return toHexString(result);
	}
	
	/**
	 * AES加密
	 * @param whatToEncrypt 要加密的内容
	 * @param secretkey AES secret key
	 * @param ivspec 向量
	 * @return 加密后的字节
	 */
	private static byte[] encrypt(byte[] whatToEncrypt, SecretKeySpec secretkey, AlgorithmParameterSpec ivspec) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.ENCRYPT_MODE, secretkey, ivspec);
			return cipher.doFinal(whatToEncrypt);
		} catch (Exception e) {
			throw new RuntimeException("加密失败", e);
		}
	}
	
	/**
	 * AES解密
	 * @param whatToDecrypt 要解密的内容
	 * @param secretkey AES secret key
	 * @return 解密后的utf8串
	 */
	public static String base64Decrypt(String whatToDecryptBase64, String secretkey) {
		byte[] result = decrypt(Base64.getDecoder().decode(whatToDecryptBase64), newKey(UTF8Bytes(secretkey)), newDefaultIV());
		return UTF8Str(result);
	}
	
	/**
	 * AES解密
	 * @param whatToDecryptHex 要解密的内容(hex)
	 * @param secretkey AES secret key
	 * @return 解密后的utf8串
	 */
	public static String hexDecrypt(String whatToDecryptHex, String secretkey) {
		byte[] result = decrypt(toByteArray(whatToDecryptHex), newKey(UTF8Bytes(secretkey)), newDefaultIV());
		return UTF8Str(result);
	}
	
	/**
	 * AES解密
	 * @param whatToDecrypt 要解密的内容
	 * @param secretkey AES secret key
	 * @param ivspec 向量
	 * @return 解密后的字节
	 */
	private static byte[] decrypt(byte[] whatToDecrypt, SecretKeySpec secretkey, AlgorithmParameterSpec ivspec) {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, secretkey, ivspec);
			return cipher.doFinal(whatToDecrypt);
		} catch (Exception e) {
			throw new RuntimeException("解密失败", e);
		}
	}

	/**
	 * 创建128位的加密key
	 */
	public static byte[] getAesKey() throws Exception {
		// Higher than 128-bit encryption requires a download of additional provider implementations for the JDK.
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		kgen.init(128);
		
		SecretKey skey = kgen.generateKey();
		return skey.getEncoded();
	}
	
	/**
	 * 字节数组转换为Hex字符串
	 */
	public static String toHexString(byte[] data) {
		return DatatypeConverter.printHexBinary(data);
	}

	/**
	 * Hex字符串转换为字节数组
	 */
	public static byte[] toByteArray(String hexStr) {
		return DatatypeConverter.parseHexBinary(hexStr);
	}
	
	/**
	 * str.getBytes("UTF-8")异常转换为运行时异常
	 */
	public static byte[] UTF8Bytes(String str) {
		try {
			return str.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("不支持的编码(" + str + ")", e);
		}
	}
	
	/**
	 * new String(buf, "UTF-8")异常转换为运行时异常
	 */
	public static String UTF8Str(byte[] buf) {
		try {
			return new String(buf, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 获取默认的向量
	 */
	private static AlgorithmParameterSpec newDefaultIV() {
		byte[] iv = { 10, 1, 11, 5, 12, 4, 15, 7, 13, 9, 23, 3, 2, 14, 8, 12 };
		return new IvParameterSpec(iv);
	}

	/**
	 * 获取SecretKeySpec对象, key.getByte();
	 */
	private static SecretKeySpec newKey(byte[] key) {
		return new SecretKeySpec(key, ALGORITHM);
	}

}