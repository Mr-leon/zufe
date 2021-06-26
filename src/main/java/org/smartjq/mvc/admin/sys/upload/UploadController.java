/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.mvc.admin.sys.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.upload.UploadFile;
import org.smartjq.config.MainConfig;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.mvc.common.utils.office.excel.ExcelUtil;

/***
 * 通知公告控制器（web）
 * @author Administrator
 *
 */
public class UploadController extends BaseController {
	
	/***
	 * 文件上传
	 */
	public void upload(){
		CosFileService fileService = new CosFileService();
		UploadFile file = getFile("file","/content" + File.separator + DateUtil.getCurrentDate());
		String url = null;
		try {
	        String folderName = "images";
	        url = fileService.fileUpload(folderName, file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		Map<String,String> data = new HashMap<String , String>();
		if(null != url) {
			data.put("filename", file.getFileName());
			data.put("path", url);
			renderSuccess(data,"上传成功");
		}else {
			renderError("上传失败");
		}
	}
	
	/***
	 * 导入组织结构，角色，用户
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    public void importOrgRoleUser() throws FileNotFoundException, IOException{
    	List<List<String>> list = ExcelUtil.excelToList("D:/1.xlsx");
    	SysOrgImportService.me.importOrg(list);
    	SysRoleImportService.me.importRole(list);
    	SysUserImportService.me.importUser(list);
    	renderSuccess();
    }
}
