package com.nzgreens.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * 文件处理工具类
 * 
 * @author wujiajun Created on 2010-5-24
 */
public class FileUtil {


	/**
	 * 文件读取成二进制
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static byte[] readFileToByteArray(File file) throws IOException {
		return FileUtils.readFileToByteArray(file);
	}

	/**
	 * 创建file，自动创建目录
	 * 
	 * @param directoryPath
	 *            文件目录
	 * @param fileName
	 *            文件名
	 * @return File
	 */
	public static File newFile(String directoryPath, String fileName) {
		File directory = new File(directoryPath);
		if (!directory.exists()) {
			directory.mkdirs();
		}
		File file = new File(directory.getAbsoluteFile() + File.separator
				+ fileName);
		return file;
	}

	/**
	 * 把String写成文件
	 * 
	 * @param directoryPath
	 *            目录路径
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 */
	public static boolean writeStringToFile(String directoryPath,
			String fileName, String fileContent) {
		File file = newFile(directoryPath, fileName);
		try {
			FileUtils.writeStringToFile(file, fileContent);
		} catch (IOException e) {
			/*log.debug("write file error.[directoryPath=" + directoryPath
					+ ",fileName=" + fileName + "]");
			log.error(e);*/
			return false;
		}
		return true;
	}

	/**
	 * 把流写成文件
	 * 
	 * @param directoryPath
	 *            目录路径
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 */
	public static boolean writeStreamToFile(String directoryPath,
			String fileName, InputStream fileContent) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte[] content = new byte[1024];
		int b = 0;
		try {
			while ((b = fileContent.read(content)) != -1) {
				baos.write(content, 0, b);
				// content = new byte[1024];
			}
		} catch (IOException e) {
			/*log.debug("read stream error.[directoryPath=" + directoryPath
					+ ",fileName=" + fileName + "]");
			log.error(e);*/
			return false;
		}
		return writeByteArrayToFile(directoryPath, fileName, baos.toByteArray());
	}

	/**
	 * 把byte数组写成文件
	 * 
	 * @param directoryPath
	 *            目录路径
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 */
	public static boolean writeByteArrayToFile(String directoryPath,
			String fileName, byte[] fileContent) {
		File file = newFile(directoryPath, fileName);
		try {
			FileUtils.writeByteArrayToFile(file, fileContent);
		} catch (IOException e) {
			/*log.debug("write file error.[directoryPath=" + directoryPath
					+ ",fileName=" + fileName + "]");
			log.error(e);*/
			return false;
		}
		return true;
	}

	/**
	 * 拷贝文件到另一个文件
	 * 
	 * @param directoryPath
	 *            拷贝目的地目录路径
	 * @param fileName
	 *            文件名
	 * @param srcFile
	 *            被拷贝文件
	 */
	public static boolean writeFileToFile(String directoryPath,
			String fileName, File srcFile) {
		File file = newFile(directoryPath, fileName);
		try {
			FileUtils.copyFile(srcFile, file);
		} catch (IOException e) {
			/*log.debug("write file error.[directoryPath=" + directoryPath
					+ ",fileName=" + fileName + "]");
			log.error(e);*/
			return false;
		}
		return true;
	}

	/**
	 * 剪切文件
	 * 
	 * @param orgDirectoryPath
	 *            原始目录
	 * @param destDirectoryPath
	 *            目标目录
	 * @param fileName
	 *            文件名
	 * @return true 成功;false 失败
	 */
	public static boolean cutOrRenameFile(String orgDirectoryPath,
			String destDirectoryPath, String orgFileName, String destFileName) {
		File orgFile = newFile(orgDirectoryPath, orgFileName);
		if (null == orgFile || !orgFile.exists()) {
			/*log.debug("can not find org file[directory=" + orgDirectoryPath
					+ ", fileName=" + orgFileName + "].");*/
			return false;
		} else if (!orgFile.canRead()) {
			/*log.debug("can not read org file[directory=" + orgDirectoryPath
					+ ", fileName=" + orgFileName + "].");*/
			return false;
		}
		File destFile = newFile(destDirectoryPath, destFileName);
		if (null == destFile) {
			/*log.debug("can not create dest file[directory=" + destDirectoryPath
					+ ", fileName=" + destFileName + "].");*/
			return false;
		} else if (destFile.exists() && !destFile.canWrite()) {
			/*log.debug("can not write org file[directory=" + orgDirectoryPath
					+ ", fileName=" + destFileName + "].");*/
			return false;
		}
		try {
			FileUtils.copyFile(orgFile, destFile);
			return true;
		} catch (IOException e) {
			/*log.error(e);*/
			return false;
		}
	}

	/**
	 * 如果存在，强行删除
	 * 
	 * @param directoryPath
	 * @param fileName
	 * @return
	 */
	public static boolean forceDelete(String directoryPath, String fileName) {
		File file = newFile(directoryPath, fileName);
		try {
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			/*log.debug("delete file error.[directoryPath=" + directoryPath
					+ ",fileName=" + fileName + "]");
			log.error(e);*/
			return false;
		}
		return true;
	}

	/**
	 * 生成分层次路径 例如（id:859684278 return:8/5968/4278）
	 * 
	 * @param id
	 *            分层ID
	 * @return 层次路径
	 */
	public static String generateHierarchyPath(long id) {
		StringBuilder path = new StringBuilder();
		for (int i = 2; i >= 1; i--) {
			long basicNum = Double.valueOf(Math.pow(2000, i)).longValue();
			path.append(id / basicNum).append(File.separator);
			id = id % basicNum;
		}
		path.append(id);
		return path.toString();
	}

	/**
	 * 生成分层次路径，用于显示web
	 * 
	 * @param id
	 * @return
	 */
	public static String generateHierarchyWebPath(long id) {
		String webSeparator = "/";
		StringBuilder path = new StringBuilder();
		for (int i = 2; i >= 1; i--) {
			long basicNum = Double.valueOf(Math.pow(2000, i)).longValue();
			path.append(id / basicNum).append(webSeparator);
			id = id % basicNum;
		}
		path.append(id);
		return path.toString();
	}

	/**
	 * 通过UUID生成文件名
	 * 
	 * @param contentType
	 *            文件后缀名（如：jpg txt gif ...）
	 * @return UUID.toString()+"."+contentType
	 */
	public static String generateUUIDFileName(String contentType) {
		StringBuilder fileName = new StringBuilder();
		fileName.append(UUID.randomUUID().toString()).append(".")
				.append(contentType);
		return fileName.toString();
	}

	/**
	 * 获取文件后缀名
	 * 
	 * @param fileName
	 * @return 如果没有则返回null
	 */
	public static String getFileSuffix(String fileName) {
		int index = StringUtils.lastIndexOf(fileName, ".");
		if (index >= 0) {
			return fileName.substring(index + 1, fileName.length())
					.toLowerCase();
		} else {
			return fileName;
		}
	}

	/**
	 * 将流转化成byte[]
	 * 
	 * @param iStrm
	 * @return
	 * @throws IOException
	 */
	public static byte[] inputStreamToByte(InputStream inputStream)
			throws IOException {
		ByteArrayOutputStream bytestream = null;
		try {
			bytestream = new ByteArrayOutputStream();
			int ch;
			while ((ch = inputStream.read()) != -1) {
				bytestream.write(ch);
			}
			byte imgdata[] = bytestream.toByteArray();
			return imgdata;
		} finally {
			if (bytestream != null) {
				bytestream.close();
			}
		}

	}

	public static void main(String[] args) {
		System.out.println(FileUtil.generateHierarchyPath(7999999L));
		System.out.println(FileUtil.generateHierarchyWebPath(7999999999L));
	}
}
