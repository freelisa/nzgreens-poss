package com.nzgreens.console.util;

import com.alibaba.fastjson.JSON;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author sylar
 * @Description:
 * @date 2019/8/25 20:45
 */
@Component
public class CosUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CosUtils.class);

    private COSClient cosClient;

    @PostConstruct
    private void init(){
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = "AKIDL0QaUJIj4965omw63ua4PabTGhz7MtX3";
        String secretKey = "ZsobEhz30oTAlDXjCW7aiLqDQTL3b19R";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        cosClient = new COSClient(cred, clientConfig);
    }


    public void upload(String filePath){
        upload(filePath, filePath.replaceAll("/data/upload", "/statics"));
    }

    public void upload(String filePath, String key){
        try {
            // 指定要上传的文件
            File localFile = new File(filePath);
            // 指定要上传到的存储桶
            String bucketName = "nz-1258464860";
            // 指定要上传到 COS 上对象键
            LOGGER.info("原始路径->"+filePath);
            LOGGER.info("上传KEY->"+key.replaceAll("/data/upload", "/statics"));
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key.replaceAll("/data/upload", "/statics"), localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            LOGGER.info(JSON.toJSONString(putObjectResult));
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    public void upload(String filePath, File file){
        try {
            // 指定要上传的文件
            File localFile = file;
            // 指定要上传到的存储桶
            String bucketName = "nz-1258464860";
            // 指定要上传到 COS 上对象键
            System.out.println("原始路径->"+filePath);
            LOGGER.info("原始路径->"+filePath);
            System.out.println("上传KEY->"+filePath.replaceAll("/data/upload", "/statics"));
            LOGGER.info("上传KEY->"+filePath.replaceAll("/data/upload", "/statics"));
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, filePath.replaceAll("/data/upload", "/statics"), localFile);
            PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
            LOGGER.info(JSON.toJSONString(putObjectResult));
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // 1 初始化用户身份信息（secretId, secretKey）。
        String secretId = "AKIDL0QaUJIj4965omw63ua4PabTGhz7MtX3";
        String secretKey = "ZsobEhz30oTAlDXjCW7aiLqDQTL3b19R";
        COSCredentials cred = new BasicCOSCredentials(secretId, secretKey);
        // 2 设置 bucket 的区域, COS 地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        // clientConfig 中包含了设置 region, https(默认 http), 超时, 代理等 set 方法, 使用可参见源码或者常见问题 Java SDK 部分。
        Region region = new Region("ap-chengdu");
        ClientConfig clientConfig = new ClientConfig(region);
        // 3 生成 cos 客户端。
        COSClient cosClient = new COSClient(cred, clientConfig);
        try {
            String path = "/data/upload";		//要遍历的路径
            File file = new File(path);		//获取其file对象
            func(file, cosClient);
        } catch (CosServiceException serverException) {
            serverException.printStackTrace();
        } catch (CosClientException clientException) {
            clientException.printStackTrace();
        }
    }

    private  static void func(File file, COSClient cosClient){
        File[] fs = file.listFiles();
        for(File f:fs){
            if(f.isDirectory())	//若是目录，则递归打印该目录下的文件
                func(f, cosClient);
            if(f.isFile())		{
                System.out.println(f.getAbsolutePath());
                // 指定要上传到的存储桶
                String bucketName = "nz-1258464860";
                System.out.println(f.getAbsolutePath().replaceAll("/data/upload", "/statics"));
                // 指定要上传到 COS 上对象键
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, f.getAbsolutePath().replaceAll("/data/upload", "/statics"), f);
                PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
                LOGGER.info(JSON.toJSONString(putObjectResult));
            }
        }
    }

}
