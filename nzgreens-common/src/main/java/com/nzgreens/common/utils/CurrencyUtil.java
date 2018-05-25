package com.nzgreens.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class CurrencyUtil {
	private static final BigDecimal HUNDRED = new BigDecimal("100");


	/**
	 * 元转分
	 *
	 * @param yuanAmount
	 * @return
	 * @throws Exception
	 */
	public static Long convertYuanToFen(String yuanAmount) throws Exception {
		if (StringUtils.isBlank(yuanAmount)) {
			throw new Exception("输入金额为空!");
		}

		yuanAmount = yuanAmount.trim();
		yuanAmount = yuanAmount.substring(1).replace(",","");
//		if (!NumberUtils.isNumber(yuanAmount)) {
//			throw new Exception("输入金额格式错误!");
//		}

		BigDecimal result = new BigDecimal(yuanAmount);
		result = result.multiply(HUNDRED);

		return result.setScale(0, BigDecimal.ROUND_UP).longValue();
	}

	/**
	 * 元转分
	 *
	 * @param yuanAmount
	 * @return
	 * @throws Exception
	 */
	public static Long convertYuanToFens(String yuanAmount) throws Exception {
		if (StringUtils.isBlank(yuanAmount)) {
			throw new Exception("输入金额为空!");
		}

		yuanAmount = yuanAmount.trim();
		if (!NumberUtils.isNumber(yuanAmount)) {
			throw new Exception("输入金额格式错误!");
		}

		BigDecimal result = new BigDecimal(yuanAmount);
		result = result.multiply(HUNDRED);

		return result.setScale(0, BigDecimal.ROUND_UP).longValue();
	}

	/**
	 * 元转分
	 *
	 * @param yuanAmount
	 * @return
	 * @throws Exception
	 */
	public static Integer convertYuanToFenResInt(String yuanAmount) throws Exception {
		if (StringUtils.isBlank(yuanAmount)) {
			throw new Exception("输入金额为空!");
		}

		yuanAmount = yuanAmount.trim();
		if (!NumberUtils.isNumber(yuanAmount)) {
			throw new Exception("输入金额格式错误!");
		}

		BigDecimal result = new BigDecimal(yuanAmount);
		result = result.multiply(HUNDRED);

		return result.setScale(0, BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * 元转分
	 *
	 * @param yuanAmount
	 * @return
	 * @throws Exception
	 */
	public static Long convertYuanToFen(Float yuanAmount) throws Exception {
		return convertYuanToFen(String.valueOf(yuanAmount));
	}


	public static Long convertYuanToFen(Integer yuanAmount) throws Exception {
		return convertYuanToFen(String.valueOf(yuanAmount));
	}

	/**
	 * 分转元                                    <br/>
	 * 保留2位小数                       <br/>
	 * <p/>
	 * 101.0 ===》 1.01  <br/>
	 * 101.1 ===》 1.02  <br/>
	 * 101.5 ===》 1.02  <br/>
	 * 101.9 ===》 1.02  <br/>
	 *
	 * @param fenAmount
	 * @return
	 * @throws Exception
	 */
	public static String convertFenToYuan(String fenAmount) throws Exception {
		if (StringUtils.isBlank(fenAmount)) {
			throw new Exception("输入金额为空!");
		}

		fenAmount = fenAmount.trim();
		if (!NumberUtils.isNumber(fenAmount)) {
			throw new Exception("输入金额格式错误!");
		}

		BigDecimal result = new BigDecimal(fenAmount);
		result = result.divide(HUNDRED);

		return result.setScale(2, BigDecimal.ROUND_UP).toString();
	}

	public static String convertFenToYuan(String fenAmount, int scale) throws Exception {
		if (StringUtils.isBlank(fenAmount)) {
			throw new Exception("输入金额为空!");
		}

		fenAmount = fenAmount.trim();
		if (!NumberUtils.isNumber(fenAmount)) {
			throw new Exception("输入金额格式错误!");
		}

		BigDecimal result = new BigDecimal(fenAmount);
		result = result.divide(HUNDRED);

		return result.setScale(scale, BigDecimal.ROUND_UP).toString();
	}

	/**
	 * 分转元                                    <br/>
	 * 保留2位小数                       <br/>
	 * <p/>
	 * 101.0 ===》 1.01  <br/>
	 * 101.1 ===》 1.02  <br/>
	 * 101.5 ===》 1.02  <br/>
	 * 101.9 ===》 1.02  <br/>
	 *
	 * @param fenAmount
	 * @return
	 * @throws Exception
	 */
	public static String convertFenToYuan(Integer fenAmount, int scale) throws Exception {
		return convertFenToYuan(String.valueOf(fenAmount), scale);
	}

	public static String convertFenToYuan(Integer fenAmount) throws Exception {
		if (fenAmount == null || fenAmount == 0) {
			return "0.00";
		}
		BigDecimal result = new BigDecimal(fenAmount);
		result = result.divide(HUNDRED);

		return result.setScale(2, BigDecimal.ROUND_UP).toString();
	}

	public static String convertFenToYuan(Long fenAmount) throws Exception {
		if (fenAmount == null || fenAmount == 0) {
			return "0.00";
		}
		BigDecimal result = new BigDecimal(fenAmount);
		result = result.divide(HUNDRED);

		return result.setScale(2, BigDecimal.ROUND_UP).toString();
	}

	/**
	 * 修正单位为元的金额   <br/>
	 * 保留2位小数                    <br/>
	 * <p/>
	 * 1.005 ===》 1.01 <br/>
	 * 1.000 ===》 1.01 <br/>
	 * 1.001 ===> 1.01 <br/>
	 *
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String fixYuanAmount(String amount) throws Exception {
		if (StringUtils.isBlank(amount)) {
			throw new Exception("输入金额为空!");
		}

		amount = amount.trim();
		if (!NumberUtils.isNumber(amount)) {
			throw new Exception("输入金额格式错误!");
		}

		BigDecimal result = new BigDecimal(amount);

		return result.setScale(2, BigDecimal.ROUND_UP).toString();
	}

	/**
	 * 修正单位为元的金额   <br/>
	 * 保留2位小数                    <br/>
	 * <p/>
	 * 1.005 ===》 1.01 <br/>
	 * 1.000 ===》 1.01 <br/>
	 * 1.001 ===> 1.01 <br/>
	 *
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static BigDecimal fixYuanAmount(BigDecimal amount) {
		if (amount == null) {
			return new BigDecimal("0.0");
		}

		return amount.setScale(2, BigDecimal.ROUND_UP);
	}

	/**
	 * numberFormatAmount：将数字格式化成金额格式  ###,##0.00
	 *
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static String numberFormatAmount(String amount) throws Exception {
		String result = fixYuanAmount(amount);
		DecimalFormat df = new DecimalFormat("###,##0.00");
		return df.format(new BigDecimal(result));
	}

	/**
	 * 修正单位为分的金额   <br/>
	 * 结果为整数                      <br/>
	 * <p/>
	 * 1.00 ===》 1 <br/>
	 * 1.01 ===》 2 <br/>
	 * 1.05 ===> 2 <br/>
	 * 1.09 ===> 2 <br/>
	 *
	 * @param amount
	 * @return
	 * @throws Exception
	 */
	public static Integer fixFenAmount(String amount) throws Exception {
		if (StringUtils.isBlank(amount)) {
			throw new Exception("输入金额为空!");
		}

		amount = amount.trim();
		if (!NumberUtils.isNumber(amount)) {
			throw new Exception("输入金额格式错误!");
		}

		BigDecimal result = new BigDecimal(amount);

		return Integer.valueOf(result.setScale(0, BigDecimal.ROUND_UP).toString());
	}

	/**
	 * 两个Integer类型数相加   <br/>
	 * 结果为Integer        <br/>
	 */
	public static Integer add(Integer one, Integer second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.add(bTwo).intValue();
	}

	/**
	 * 两个Integer类型数相减   <br/>
	 * 结果为Integer        <br/>
	 */
	public static Integer subtract(Integer one, Integer second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.subtract(bTwo).intValue();
	}

	/**
	 * 两个Integer类型数相乘   <br/>
	 * 结果为双精度             <br/>
	 */
	public static Integer multiply(Integer one, Integer second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.multiply(bTwo).intValue();
	}

	/**
	 * 两个int类型数相乘   <br/>
	 * 结果为Integer 类型            <br/>
	 */
	public static Integer multiply(int one, int second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.multiply(bTwo).intValue();
	}

	/**
	 * 两个int类型数相乘   <br/>
	 * 结果为Integer 类型            <br/>
	 */
	public static Integer multiply(Integer one, int second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.multiply(bTwo).intValue();
	}

	/**
	 * 两个String类型数相乘   <br/>
	 * 结果为String类型            <br/>
	 */
	public static String multiply(String one, String second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.multiply(bTwo).toString();
	}

	/**
	 * 两个String类型数相乘   <br/>
	 * 结果为String类型            <br/>
	 * 保留小数位为point位       <br/>
	 */
	public static String multiply(String one, String second, int point) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.multiply(bTwo).setScale(point, BigDecimal.ROUND_HALF_UP).toString();
	}

	/**
	 * 一个Integer类型数,一个String相乘   <br/>
	 * 结果为Integer类型 <br/>
	 */
	public static Integer multiply(Integer one, String second) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.multiply(bTwo).intValue();
	}

	/**
	 * 两个Integer类型数相除 取point位数的精度   <br/>
	 * 结果为双精度                                              <br/>
	 */
	public static Double divide(Integer one, Integer second, int point) {
		BigDecimal bOne = new BigDecimal(one);
		BigDecimal bTwo = new BigDecimal(second);

		return bOne.divide(bTwo, point, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static int compare(String amount1, String amount2) throws Exception {
		if (StringUtils.isBlank(amount1) || StringUtils.isBlank(amount2)) {
			throw new Exception("输入金额为空!");
		}

		if (!NumberUtils.isNumber(amount1) || !NumberUtils.isNumber(amount2)) {
			throw new Exception("输入金额格式错误!");
		}
		BigDecimal d1 = new BigDecimal(amount1);
		BigDecimal d2 = new BigDecimal(amount2);

		return d1.compareTo(d2);
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 *
	 * @param s
	 * @return
	 */
	public static String subZeroAndDot(String s) {
		if (s.indexOf(".") > 0) {
			s = s.replaceAll("0+?$", "");//去掉多余的0
			s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
		}
		return s;
	}
}
