package org.smartjq.mvc.admin.noticeuser;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.smartjq.mvc.admin.workflow.WorkFlowService;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.model.DjNoticeUser;
import org.smartjq.mvc.common.model.SysMenu;
import org.smartjq.mvc.common.model.SysUserCommonFunction;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.mvc.common.utils.StringUtil;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.plugin.shiro.ShiroKit;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;



public class DjNoticeUserController extends BaseController {
	public static final DjNoticeUserService service = DjNoticeUserService.me;
	public static WorkFlowService wfservice = WorkFlowService.me;
	/***
	 * get list page
	 */
	public void getListPage(){
		HttpServletRequest request = getRequest();
		String url = request.getRequestURL().toString();
		String userId = ShiroKit.getUserId();
		String context = request.getContextPath();
		String menu = url.substring(url.indexOf(context) + context.length());
		SysMenu sm = smService.findByUrl(menu);
		if(null != sm) {
			SysUserCommonFunction o = cfService.getByMenuId(sm.getId(), userId);
			if(null != o) {
				o.setUseTime(new Date());
		    	o.update();
			}else {
				o = new SysUserCommonFunction();
				o.setCreateTime(DateUtil.dateToString(new Date(), 0));
				o.setIfDel("0");
				o.setIfFixed("0");
				o.setMenuId(sm.getId());
				o.setUserid(userId);
		    	o.setId(UuidUtil.getUUID());
				o.setUseTime(new Date());
		    	o.save();
			}
		}
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
		String applyUser = getPara("userName","");
		String id = getPara("id","");
		String ifSign = getPara("ifSign","");
    	Page<Record> page = service.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize),startTime,endTime,applyUser, ifSign, id);
    	renderPage(page.getList(),"",page.getTotalRow());
    }
    
	
	/***
	 * 根据公告ID查看用户通知公告
	 */
	public void getUserNoticeById(){
		List<Record> list = service.findUserNotice(ShiroKit.getUserId(), getPara("id"));
		if(list != null && list.size() > 0) {
			setAttr("if_sign", list.get(0).getObject("if_sign"));
		}else {
			setAttr("if_sign", "0");
		}
		renderJson();
	}
    
    
    /***
     * save data
     */
    public void save(){
    	DjNoticeUser o = getModel(DjNoticeUser.class);
    	if(StrKit.notBlank(o.getId())){
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		o.setSignTime(DateUtil.getCurrentTime());
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
		DjNoticeUser o = new DjNoticeUser();
    	o = service.getById(id);
		setAttr("o", o);
    	setAttr("formModelName",StringUtil.toLowerCaseFirstOne(DjNoticeUser.class.getSimpleName()));
		renderIframe("edit.html");
    } 
}