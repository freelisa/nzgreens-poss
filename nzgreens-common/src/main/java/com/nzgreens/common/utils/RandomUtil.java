package com.nzgreens.common.utils;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RandomUtil {
	public static final String ALLCHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String LETTERCHAR = "abcdefghijkllmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String NUMBERCHAR = "0123456789";

	/**
	 * 返回一个定长的随机字符串(只包含大小写字母、数字)
	 *
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(ALLCHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机字符串(只包含数字)
	 *
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateInt(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(NUMBERCHAR.charAt(random.nextInt(NUMBERCHAR.length())));
		}
		return sb.toString();
	}


	/**
	 * 返回一个定长的随机纯字母字符串(只包含大小写字母)
	 *
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateMixString(int length) {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			sb.append(ALLCHAR.charAt(random.nextInt(LETTERCHAR.length())));
		}
		return sb.toString();
	}

	/**
	 * 返回一个定长的随机纯大写字母字符串(只包含大小写字母)
	 *
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateLowerString(int length) {
		return generateMixString(length).toLowerCase();
	}

	/**
	 * 返回一个定长的随机纯小写字母字符串(只包含大小写字母)
	 *
	 * @param length 随机字符串长度
	 * @return 随机字符串
	 */
	public static String generateUpperString(int length) {
		return generateMixString(length).toUpperCase();
	}

	/**
	 * 生成一个定长的纯0字符串
	 *
	 * @param length 字符串长度
	 * @return 纯0字符串
	 */
	public static String generateZeroString(int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append('0');
		}
		return sb.toString();
	}

	/**
	 * 根据数字生成一个定长的字符串，长度不够前面补0
	 *
	 * @param num       数字
	 * @param fixdlenth 字符串长度
	 * @return 定长的字符串
	 */
	public static String toFixdLengthString(long num, int fixdlenth) {
		StringBuilder sb = new StringBuilder();
		String strNum = String.valueOf(num);
		if (fixdlenth - strNum.length() >= 0) {
			sb.append(generateZeroString(fixdlenth - strNum.length()));
		} else {
			throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth
					+ "的字符串发生异常！");
		}
		sb.append(strNum);
		return sb.toString();
	}

	/**
	 * 每次生成的len位数都不相同
	 *
	 * @param param
	 * @return 定长的数字
	 */
	public static int getNotSimple(int[] param, int len) {
		Random rand = new Random();
		for (int i = param.length; i > 1; i--) {
			int index = rand.nextInt(i);
			int tmp = param[index];
			param[index] = param[i - 1];
			param[i - 1] = tmp;
		}
		int result = 0;
		for (int i = 0; i < len; i++) {
			result = result * 10 + param[i];
		}
		return result;
	}

	/**
	 * 从map中随机取得一个key
	 *
	 * @param map
	 * @return
	 */
	public static <K, V> K getRandomKeyFromMap(Map<K, V> map) {
		int rn = getRandomInt(map.size());
		int i = 0;
		for (K key : map.keySet()) {
			if (i == rn) {
				return key;
			}
			i++;
		}
		return null;
	}

	/**
	 * 获得一个[0,max)之间的随机整数。
	 *
	 * @param max
	 * @return
	 */
	public static int getRandomInt(int max) {
		return new Random().nextInt(max);
	}

	//双重校验锁获取一个Random单例
	public static ThreadLocalRandom getRandom() {
		return ThreadLocalRandom.current();
	}

	public static void main(String[] args) {
		System.out.println("返回一个定长的随机字符串(只包含大小写字母、数字):" + generateString(10));
		System.out
				.println("返回一个定长的随机纯字母字符串(只包含大小写字母):" + generateMixString(10));
		System.out.println("返回一个定长的随机纯大写字母字符串(只包含大小写字母):"
				+ generateLowerString(10));
		System.out.println("返回一个定长的随机纯小写字母字符串(只包含大小写字母):"
				+ generateUpperString(10));
		System.out.println("生成一个定长的纯0字符串:" + generateZeroString(10));
		System.out.println("根据数字生成一个定长的字符串，长度不够前面补0:"
				+ toFixdLengthString(123, 10));
		int[] in = {1, 2, 3, 4, 5, 6, 7};
		System.out.println("每次生成的len位数都不相同:" + getNotSimple(in, 3));

		System.out.println("生成len长度数字字符串:" + generateInt(5));
	}
}