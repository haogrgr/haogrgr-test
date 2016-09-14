package com.haogrgr.test.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import javax.xml.bind.DatatypeConverter;

/**
 * md5工具类
 * 
 * @author tudesheng
 * @since 2016年9月13日 下午1:44:09
 *
 */
public class MD5Utils {

	public static String md5Hex(String text) {
		Objects.requireNonNull(text);

		MessageDigest messageDigest = getMessageDigest("MD5");
		messageDigest.reset();
		messageDigest.update(text.getBytes(StandardCharsets.UTF_8));

		byte[] bytes = messageDigest.digest();
		String hexstr = DatatypeConverter.printHexBinary(bytes).toLowerCase();

		return hexstr;
	}

	private static MessageDigest getMessageDigest(String algorithmName) {
		try {
			return MessageDigest.getInstance(algorithmName);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError(e);
		}
	}

}
