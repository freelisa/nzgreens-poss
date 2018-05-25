/*
 *The code is written by 51juzhai, All rights is reserved.
 */
package com.nzgreens.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUtil {

	public static final String webSeparator = "/";

	private static final Logger log = LoggerFactory.getLogger(ImageUtil.class);

	private static final int EXTERNAL_URL_CHAR = ':';

	private static final Map<String, String> IMAGE_TYPE_MAP = new HashMap<String, String>();

	static {
		IMAGE_TYPE_MAP.put("jpeg", "FFD8FF");
		IMAGE_TYPE_MAP.put("jpg", IMAGE_TYPE_MAP.get("jpeg"));
		IMAGE_TYPE_MAP.put("gif", "47494638");
		IMAGE_TYPE_MAP.put("png", "89504E47");
		IMAGE_TYPE_MAP.put("bmp", "424D");
	}

	/**
	 * 图片分层存储目录
	 *
	 * @param id   标示符ID
	 * @param size 大小尺寸(0表示原图)
	 * @return 路劲
	 */
	public static String generateHierarchyImagePath(long id, int size) {
		String path = FileUtil.generateHierarchyPath(id);
		return path + File.separator + size + File.separator;
	}

	/**
	 * 生成图片文件名，所有图片都转成JPG
	 *
	 * @return 文件名
	 */
	public static String generateUUIDJpgFileName() {
		return FileUtil.generateUUIDFileName("jpg");
	}

	/**
	 * 图片分层web浏览路径
	 *
	 * @param id
	 * @return 图片web路径
	 */
	public static String generateHierarchyImageWebPath(long id) {
		StringBuilder path = new StringBuilder();
		path.append(FileUtil.generateHierarchyWebPath(id)).append(webSeparator);
		return path.toString();
	}


	/**
	 * 是否内部图片地址
	 *
	 * @param picUrl
	 * @return
	 */
	public static boolean isInternalUrl(String picUrl) {
		if (StringUtils.isEmpty(picUrl)) {
			return true;
		} else {
			return picUrl.indexOf(EXTERNAL_URL_CHAR) < 0;
		}
	}

	// 得到图片的宽度
	public static int getImgFileWidth(File file) {
		Image src = null;
		try {
			src = ImageIO.read(file);
			return src.getWidth(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// 得到图片的高度
	public static int getImgFileHeight(File file) {
		Image src = null;
		try {
			src = ImageIO.read(file);
			return src.getHeight(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static void main(String[] args) throws IOException {
		//clipImageCenter("d:/WIN_20140314_212454.jpg", "d:/WIN_20140314_2124541.jpg", 180, 120);
		//narrowImageByScale("d:/1422200949303btno.jpg", 0.14f);
	}

}
