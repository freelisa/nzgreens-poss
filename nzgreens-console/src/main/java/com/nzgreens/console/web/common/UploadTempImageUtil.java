package com.nzgreens.console.web.common;

import com.nzgreens.common.utils.RandomUtil;
import com.nzgreens.common.utils.TimeUtil;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class UploadTempImageUtil {
    /**
     * 保存上传图片
     *
     * @param multiFile
     * @param tempPath
     * @return
     * @throws IOException
     */
    public static File processImage(MultipartFile multiFile, String tempPath) throws IOException {
        //获得图片后缀
        String fileSuffix = multiFile.getOriginalFilename().substring(
                multiFile.getOriginalFilename().lastIndexOf("."));

        //创建图片名称:当前时间+后缀
        String fileName = (TimeUtil.format(TimeUtil.FORMAT_YYYYMMDDHHMMSSSSS) + RandomUtil.generateInt(5) + fileSuffix).toLowerCase();

        //创建图片路径
        String filePath = tempPath;

        //获得站点根目录在文件系统上的位置+ imagesTemp创建文件目录
        File file = new File(HttpRequestUtil.getApplicationContextPath()
                + filePath);

        //目录存在返回true，否则返回false
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }

        //文件路径加文件名
        filePath += "/" + fileName;

        //创建文件
        file = new File(HttpRequestUtil.getApplicationContextPath()
                + "/" + filePath);
        //转移文件
        multiFile.transferTo(file);

        return file;
    }


    /**
     * base64转图片
     * @param base64Str
     * @param tempPath
     * @return
     * @throws IOException
     */
    public static String processImage(String base64Str, String tempPath) throws IOException {

        String[] strArray = base64Str.split(",");

        //获得图片后缀
        String fileSuffix = strArray[0].substring(strArray[0].indexOf("/") + 1, strArray[0].indexOf(";"));

        //创建图片名称:当前时间+后缀
        String fileName = (TimeUtil.format(TimeUtil.FORMAT_YYYYMMDDHHMMSSSSS) + "." + fileSuffix).toLowerCase();

        //创建图片路径
        String filePath = tempPath;

        //获得站点根目录在文件系统上的位置+ imagesTemp创建文件目录
        File file = new File(HttpRequestUtil.getApplicationContextPath()
                + filePath);

        //目录存在返回true，否则返回false
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }

        //文件路径加文件名
        filePath += "/" + fileName;

        BASE64Decoder decoder = new BASE64Decoder();
        //Base64解码
        byte[] b = decoder.decodeBuffer(strArray[1]);
        for (int i = 0; i < b.length; ++i) {
            if (b[i] < 0) {//调整异常数据
                b[i] += 256;
            }
        }
        //生成图片
        OutputStream out = new FileOutputStream(HttpRequestUtil.getApplicationContextPath()
                + "/" + filePath);
        out.write(b);
        out.flush();
        out.close();

        return fileName;
    }
}
