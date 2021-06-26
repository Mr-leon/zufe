package org.smartjq.mvc.admin.smslog;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.admin.workflow.WorkFlowService;
import org.smartjq.mvc.common.utils.StringUtil;
import org.smartjq.mvc.common.model.DjSmsLog;
import org.smartjq.mvc.common.model.SysUser;
import org.smartjq.mvc.common.model.SysOrg;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.mvc.common.utils.Constants;
import org.smartjq.mvc.admin.common.DjConstants;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.plugin.shiro.ShiroKit;



public class DjSmsLogController extends BaseController {
	public static final DjSmsLogService service = DjSmsLogService.me;
	public static WorkFlowService wfservice = WorkFlowService.me;
	/***
	 * get list page
	 */
	public void getListPage(){
		renderIframe("list.html");
    }
	/***
     * list page data
     **/
    public void listData(){
    	String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
		String endTime = getPara("endTime","");
		String startTime = getPara("startTime","");
		String applyUser = getPara("applyUser","");
    	Page<Record> page = service.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize),startTime,endTime,applyUser);
    	renderPage(page.getList(),"",page.getTotalRow());
    }
    /***
     * save data
     */
    public void save(){
    	DjSmsLog o = getModel(DjSmsLog.class);
    	if(StrKit.notBlank(o.getId())){
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		o.setCreateTime(DateUtil.getCurrentTime());
    		o.save();
    	}
    	renderSuccess();
    }
    /***
     * edit page
     */
    public void getEditPage(){
    	String id = getPara("id");
    	String view = getPara("view");
		setAttr("view", view);
		DjSmsLog o = new DjSmsLog();
		if(StrKit.notBlank(id)){
    		o = service.getById(id);
    	}
		setAttr("o", o);
    	setAttr("formModelName",StringUtil.toLowerCaseFirstOne(DjSmsLog.class.getSimpleName()));
		renderIframe("edit.html");
    }
    /***
     * del
     * @throws Exception
     */
    public void delete() throws Exception{
		String ids = getPara("ids");
		service.deleteByIds(ids);
    	renderSuccess("删除成功!");
    }
}