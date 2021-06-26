package org.smartjq.mvc.admin.sys.upload;

import org.smartjq.config.MainConfig;
import org.springframework.stereotype.Service;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.region.Region;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

/**
 * 腾讯云 COS操作工具
 */
@Service
public class CosFileUtil {

    /**
     * 接口描述：文件上传
     */
    public String picCOS (String folderName, File cosFile, String alias) throws Exception {

        COSCredentials cred = new BasicCOSCredentials(MainConfig.cosSecretId, MainConfig.cosSecretKey);

        // 设置bucket的区域，COS区域简称，参考腾讯云 官网
        // https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(MainConfig.cosRegion));
        // 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);
        // 生成文件路径
        String key = folderName + "/" + alias + "." + cosFile.getName().substring(cosFile.getName().lastIndexOf(".") + 1);

        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        // 指定要上传到 COS 上的路径
        PutObjectRequest putObjectRequest = new PutObjectRequest(MainConfig.cosBucketName, key, cosFile);
        cosClient.putObject(putObjectRequest);
        cosClient.shutdown();
        Date expiration = new Date(System.currentTimeMillis() + 5 * 60 * 10000);
        URL url = cosClient.generatePresignedUrl(MainConfig.cosBucketName, key, expiration);
        if (null == url) {
            return "上传失败";
        }
        String returnUrl = MainConfig.cosDomainName + "/" + key;
        return returnUrl;
    }

    /**
     * 接口描述：文件删除
     */
    public void delFileCOS(String key) {
        COSCredentials cred = new BasicCOSCredentials(MainConfig.cosSecretId, MainConfig.cosSecretKey);

        // 设置bucket的区域，COS区域简称，参考腾讯云 官网
        // https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(MainConfig.cosRegion));
        // 生成cos客户端
        COSClient cosClient = new COSClient(cred, clientConfig);

        cosClient.deleteObject(MainConfig.cosBucketName, key);
    }

    /**
     * 接口描述：MultipartFile 转换为 File
     */
    public void inputStreamToFile(InputStream ins, File file) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os) {
                    os.close();
                }
                if (null != ins) {
                    ins.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
