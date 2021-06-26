package org.smartjq.mvc.admin.sys.upload;

import java.io.File;

import org.smartjq.config.MainConfig;
import org.smartjq.mvc.common.utils.UuidUtil;

import com.jfinal.upload.UploadFile;

/**
 * FileService 实现类
 */
public class CosFileService {

    private CosFileUtil cosFileUtil = new CosFileUtil();


    /**	
     * 接口描述：图片上传
     */
    public String fileUpload(String folderName, UploadFile multipartFile) throws Exception{
        File file = multipartFile.getFile();
        String returnUrl = cosFileUtil.picCOS(folderName, file, UuidUtil.getUUID());

        // 删除临时文件
        File del = new File(file.toURI());
        del.delete();

        return returnUrl;
    }

    /**
     * 接口描述：删除图片
     * 参数delFileUrl为节点域名（即腾讯云静态资源访问域名）+存储桶名称+文件路径，如：
     * https://test-mini-image-**********.cos-website.ap-beijing.myqcloud.com/test-mini-image-**********（存储桶名称）/001.png
     */
    public void fileDel(String delFileUrl) {

        String[] index = delFileUrl.split("com/");
        if (index.length < 2) {
            System.out.println("参数异常");
        }
        String domainName = index[0] + "com";
        // 校验节点域名是否有效
        if (domainName.equals(MainConfig.cosDomainName)) {
            cosFileUtil.delFileCOS(index[1]);
        }
    }
}
