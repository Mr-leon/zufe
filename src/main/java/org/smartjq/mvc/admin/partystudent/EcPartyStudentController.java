package org.smartjq.mvc.admin.partystudent;

import com.jfinal.upload.UploadFile;
import org.smartjq.mvc.common.base.BaseController;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * @author CYZ
 */
public class EcPartyStudentController extends BaseController {
	public static final EcPartyStudentService service = EcPartyStudentService.me;
	/***
	 * get list page
	 */
	public void getListPage(){
		renderIframe("list.html");
    }

	public void downloadTemp() {
		try {
			String filePath = this.getClass().getResource("/").getPath();
			System.out.println("filePath: " + filePath);
			File file = new File(filePath + "template/房间押金导入模板.xlsx");
			renderFile(file);
		} catch (Exception e) {
			e.printStackTrace();
			renderError("系统异常，请联系管理员");
		}
	}

	/***
	 * 导入押金
	 * importPriceExcel
	 * @throws FileNotFoundException
	 */
	public void importExcel() throws FileNotFoundException {
		UploadFile uploadFile = this.getFile();
		String fileName = uploadFile.getOriginalFileName();
		String hz = fileName.substring(fileName.lastIndexOf(".") + 1);//文件后缀
		File file = uploadFile.getFile();
		String str = service.importExcel(file);
		if ("no".equals(str)) {
			renderError("导入失败");
		} else {
			renderSuccess("导入成功");
		}
	}
	
}