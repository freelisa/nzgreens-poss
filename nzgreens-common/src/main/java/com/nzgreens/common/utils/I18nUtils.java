package com.nzgreens.common.utils;

import java.util.Locale;

/**
 * Created by fei on 2017/5/27.
 */
public class I18nUtils {

	/**
	 * 获取国际化配置
	 *
	 * @param language
	 * @return
	 */
	public static Locale getLocale(String language) {
		if (org.apache.commons.lang3.StringUtils.isEmpty(language)) {
			return Locale.ENGLISH;
		}
		String[] languages = org.apache.commons.lang3.StringUtils.split(language, "-");
		if (languages.length == 1) {
			return new Locale(languages[0], "");
		} else if (languages.length == 2) {
			return new Locale(languages[0], languages[1]);
		} else {
			return Locale.ENGLISH;
		}
	}
}
