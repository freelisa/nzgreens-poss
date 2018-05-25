package com.nzgreens.common.utils;

import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author fei
 * @version V1.0
 */
public class StringUtil {
	public static final String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？ 　]";
	public static final String IP_REGEX = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			+ "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	public static final String EMAIL_REGEX = "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 验证手机号码
	 *
	 * @param mobile
	 * @return
	 */
	public static boolean isMobileNum(String mobile) {
		if (StringUtils.isBlank(mobile)) {
			return false;
		}
		String pattern = "^((13[0-9])|(14[0-9])|(15[0-9])|(18[0-9])|(17[0-9]))\\d{8}$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(mobile);
		return m.matches();

	}

	/**
	 * 验证邮箱
	 *
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		boolean flag = false;
		try {
			Pattern regex = Pattern.compile(EMAIL_REGEX);
			Matcher matcher = regex.matcher(email);
			flag = matcher.matches();
		} catch (Exception e) {
			flag = false;
		}
		return flag;
	}

	public static String hashPassword(String mobile, String password) {
		try {
			return md5(mobile + "+" + password);
		} catch (Exception e) {
			return null;
		}
	}

	public static String md5(String text) {
		try {
			return md5(text.getBytes("UTF-8"));
		} catch (Exception e) {
			return null;
		}
	}

	public static String md5(byte[] source) throws Exception {
		int bufferSize = 4096;
		byte[] buffer = new byte[4096];

		MessageDigest md5 = MessageDigest.getInstance("MD5");

		int remain = source.length;

		while (remain > 0) {
			int len = (remain > bufferSize) ? bufferSize : remain;
			System.arraycopy(source, source.length - remain, buffer, 0, len);
			remain = remain - len;

			md5.update(buffer, 0, len);
		}

		return byte2Hex(md5.digest());
	}

	public static String byte2Hex(byte[] bytes) throws Exception {
		final String HEX = "0123456789abcdef";

		String result = "";
		for (int i = 0; i < bytes.length; i++) {
			result += HEX.charAt(bytes[i] >> 4 & 0x0F);
			result += HEX.charAt(bytes[i] & 0x0F);
		}

		return new String(result);
	}

	public static String getMobileMask(String mobile) {
		return mobile.substring(0, 3) + "****" + mobile.substring(7, 11);
	}

	public static boolean checkHasSpecialSymbol(String str) {
		if (str == null) {
			return false;
		}
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find();
	}

	public static String getUUID32() {
		return StringUtils.remove(getUUID(), "-");
	}

	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	public static String aesDecrypt(String content, String key, String iv) {
		try {
			byte[] encrypted1 = new BASE64Decoder().decodeBuffer(content);

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

			byte[] original = cipher.doFinal(encrypted1);
			String originalString = new String(original);
			return originalString;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 *
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s == null) {
			return "";
		}
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 截取指定分隔符前的字符串内容。
	 *
	 * @param str       待截取的字符串
	 * @param separator 分隔符
	 * @return 返回指定分隔符前的字符串内容。
	 */
	public static String substringBefore(String str, String separator) {
		Assert.notEmpty(str);
		Assert.notEmpty(separator);

		int pos = str.indexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取最后一个分隔符前的字符串内容。
	 *
	 * @param str       待截取的字符串
	 * @param separator 分隔符
	 * @return 返回最后一个分隔符前的字符串内容。
	 */
	public static String substringBeforeLast(String str, String separator) {
		Assert.notNull(str);
		Assert.notEmpty(separator);

		int pos = str.lastIndexOf(separator);
		if (pos == -1) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * 截取指定分隔符后的字符串内容。
	 *
	 * @param str       待截取的字符串
	 * @param separator 分隔符
	 * @return 返回指定分隔符后的字符串内容。
	 */
	public static String substringAfter(String str, String separator) {
		Assert.notEmpty(str);
		Assert.notEmpty(separator);

		int pos = str.indexOf(separator);
		if (pos == -1) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取最后一个分隔符后的字符串内容。
	 *
	 * @param str       待截取的字符串
	 * @param separator 分隔符
	 * @return 返回最后一个分隔符后的字符串内容。
	 */
	public static String substringAfterLast(String str, String separator) {
		Assert.notEmpty(str);
		Assert.notEmpty(separator);

		int pos = str.lastIndexOf(separator);
		if (pos == -1 || pos == (str.length() - separator.length())) {
			return "";
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * 截取两个分隔符之间的字符串。
	 *
	 * @param str            待截取的字符串
	 * @param startSeparator 开始分隔符
	 * @param endSeparator   结束分隔符
	 * @return 返回两个分隔符之间的字符串。
	 */
	public static String substringBetween(String str, String startSeparator,
										  String endSeparator) {
		if (str == null || startSeparator == null || endSeparator == null) {
			return null;
		}
		int start = str.indexOf(startSeparator);
		if (start != -1) {
			int end = str
					.indexOf(endSeparator, start + startSeparator.length());
			if (end != -1) {
				return str.substring(start + startSeparator.length(), end);
			}
		}
		return null;
	}

	public static void main(String[] args) {
		System.out.println(subZeroAndDot("1.0"));
		System.out.println(subZeroAndDot("1.00"));
		System.out.println(subZeroAndDot("1.01"));
		System.out.println(subZeroAndDot("1.010"));
		System.out.println(subZeroAndDot("1.0100"));
		System.out.println(hashPassword("password","123456"));
		System.out.println(aesEncrypt("a123456","6Oh^.qj8x*u)loR9","SlrC^*eVMwCVyF]U"));
	}

	public static String aesEncrypt(String content, String key, String iv) {
		try {
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

			SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
			IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
			byte[] encrypted = cipher.doFinal(content.getBytes());

			return new sun.misc.BASE64Encoder().encode(encrypted);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean isIp(String ip) {
		if (StringUtils.isBlank(ip)) {
			return false;
		}
		Pattern p = Pattern.compile(IP_REGEX);
		Matcher m = p.matcher(ip);
		return m.matches();

	}

	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}


	/**
	 * 截取字符串
	 *
	 * @param str
	 * @param length
	 * @return
	 */
	public static String subText(String str, int length) {
		if (str.length() > length) {
			return str.substring(0, length) + "...";
		} else {
			return str;
		}

	}

	public static String objToString(Object obj) {

		if (obj == null) {
			return "";
		} else {
			return obj.toString();
		}
	}
}
