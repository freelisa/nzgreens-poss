package com.nzgreens.common.utils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NumberUtil {

	public static final String ROUND_CEILING = "ROUND_CEILING";

	public static final BigDecimal HUNDRED = new BigDecimal("100");

	public static final BigDecimal TEN = new BigDecimal("10");

	public static int setNumberScale(int a, int b, String strategy) {
		try {
			BigDecimal ab = new BigDecimal(a);
			BigDecimal bb = new BigDecimal(b);
			if (ROUND_CEILING.equals(strategy)) {
				return ab.divide(bb, BigDecimal.ROUND_CEILING).intValue();
			}
		} catch (Exception e) {
		}
		return 0;

	}

	public static Float getAverageStart(int startNum, int commentNum) {
		try {
			BigDecimal bdStartNum = new BigDecimal(startNum);
			BigDecimal bdCommentNum = new BigDecimal(commentNum);

			return bdStartNum.divide(bdCommentNum, 1, BigDecimal.ROUND_HALF_UP)
					.floatValue();
		} catch (Exception e) {
		}
		return 0F;
	}

	public static Float getAverageStart(Float startNum) {

		BigDecimal bg = new BigDecimal(startNum);

		return bg.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();

	}

	public boolean isNumber(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	public static int intValue(Integer value) {
		if (value == null) {
			return 0;
		} else {
			return value.intValue();
		}
	}
}