package com.nzgreens.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author fei
 * @version V1.0
 * @date ${date} ${time}
 */
public class ImageUrlUtil {

	/**
	 * 路径
	 * @param id
	 * @param fileName
	 * @return
	 */
	public static String getImageSavePath(long id, String fileName) {
		if (ImageUtil.isInternalUrl(fileName))
		{
			if (StringUtils.isEmpty(fileName) || id <= 0 ) {
				return null;
			} else {
				return (ImageUtil.generateHierarchyImageWebPath(id) + fileName);
			}
		} else {
			return fileName;
		}
	}

	/**
	 * 路径
	 * @param id
	 * @param fileName
	 * @return
	 */
	public static String getImageSavePath(long id,int type ,String fileName) {
		if (ImageUtil.isInternalUrl(fileName))
		{
			if (StringUtils.isEmpty(fileName) || id <= 0 ) {
				return null;
			} else {
				return (type+"/"+ImageUtil.generateHierarchyImageWebPath(id) + fileName);
			}
		} else {
			return fileName;
		}
	}
}
