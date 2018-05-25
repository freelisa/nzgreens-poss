package com.nzgreens.common.utils;

import java.security.MessageDigest;

/**
 * Created by longgang on 2017/5/17.
 */
public class SecureUtil {

	public static String md5(String text) {
		try {
			return md5(text.getBytes("UTF-8"));
		} catch (Exception e) {
		}
		return "";
	}

	public static String md5(byte[] source) throws Exception {
		short bufferSize = 4096;
		byte[] buffer = new byte[4096];
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		int remain = source.length;

		while (remain > 0) {
			int len = remain > bufferSize ? bufferSize : remain;
			System.arraycopy(source, source.length - remain, buffer, 0, len);
			remain -= len;
			md5.update(buffer, 0, len);
		}

		return byte2Hex(md5.digest());
	}

	public static String byte2Hex(byte[] bytes) throws Exception {
		String HEX = "0123456789abcdef";
		String result = "";

		for (int i = 0; i < bytes.length; ++i) {
			result = result + "0123456789abcdef".charAt(bytes[i] >> 4 & 15);
			result = result + "0123456789abcdef".charAt(bytes[i] & 15);
		}

		return new String(result);
	}


	public static String md5Str(String inputStr) {
		String rt = null;
		char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
		try {
			byte[] strTemp = inputStr.getBytes("UTF-8");
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			rt = new String(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rt;
	}

}
