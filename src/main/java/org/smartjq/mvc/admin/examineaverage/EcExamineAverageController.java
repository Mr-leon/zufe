package org.smartjq.mvc.admin.examineaverage;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.admin.workflow.WorkFlowService;
import org.smartjq.mvc.common.model.*;
import org.smartjq.mvc.common.utils.StringUtil;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.mvc.common.utils.Constants;
import org.smartjq.mvc.admin.common.DjConstants;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.plugin.shiro.ShiroKit;



/**
 * @author CYZ
 */
public class EcExamineAverageController extends BaseController {
	public static final EcExamineAverageService service = EcExamineAverageService.me;
	public static WorkFlowService wfservice = WorkFlowService.me;

	/***
	 * get list page
	 */
	public void getListPage(){
		String iid = getPara("iid");
		String mid = getPara("mid");
		String oid = ShiroKit.getUserOrgId();
		EcOrganization o = EcOrganization.dao.getById(oid);
		Integer nodeCount = EcOrganization.dao.getChildrenCountByPid(oid);
		EcIndicatrix i = EcIndicatrix.dao.getById(iid);
		setAttr("o",o);
		setAttr("i",i);
		setAttr("mid",mid);
		setAttr("nodeCount",nodeCount);
		renderIframe("list0.html");
    }
	/***
	 * get list page
	 */
	public void getListPage1(){
		String iid = getPara("iid");
		String mid = getPara("mid");
		String oid = ShiroKit.getUserOrgId();
		EcOrganization o = EcOrganization.dao.getById(oid);
		Integer nodeCount = EcOrganization.dao.getChildrenCountByPid(oid);
		EcIndicatrix i = EcIndicatrix.dao.getById(iid);
		setAttr("o",o);
		setAttr("i",i);
		setAttr("mid",mid);
		setAttr("nodeCount",nodeCount);
		renderIframe("list1.html");
	}

	/***
	 * get list page
	 */
	public void getExamineList(){
		String iid = getPara("iid");
		String mid = getPara("mid");
		String oid = ShiroKit.getUserOrgId();
		EcOrganization o = EcOrganization.dao.getById(oid);
		Integer nodeCount = EcOrganization.dao.getChildrenCountByPid(oid);
		EcIndicatrix i = EcIndicatrix.dao.getById(iid);
		setAttr("o",o);
		setAttr("i",i);
		setAttr("mid",mid);
		setAttr("nodeCount",nodeCount);
		renderIframe("examineList.html");
	}

	public void getMole1Page(){
		String eid = getPara("eid");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		String name = getPara("name");
		String frequency = getPara("frequency");
		setAttr("eid",eid);
		setAttr("mid",mid);
		setAttr("oid",oid);
		setAttr("iid",iid);
		setAttr("name",name);
		setAttr("frequency",frequency);
		renderIframe("mold1.html");
	}

	public void getMole2Page(){
		String eid = getPara("eid");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		String name = getPara("name");
		String frequency = getPara("frequency");
		EcExamine ecExamine = EcExamine.dao.getById(eid);
		setAttr("e",ecExamine);
		setAttr("mid",mid);
		setAttr("oid",oid);
		setAttr("iid",iid);
		setAttr("name",name);
		setAttr("frequency",frequency);
		renderIframe("mold2.html");
	}

	public void getMole3Page(){
		String eid = getPara("eid");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		String name = getPara("name");
		String frequency = getPara("frequency");
		setAttr("eid",eid);
		setAttr("mid",mid);
		setAttr("iid",iid);
		setAttr("oid",oid);
		setAttr("name",name);
		setAttr("frequency",frequency);
		renderIframe("mold3.html");
	}

	public void getMole4Page(){
		String eid = getPara("eid");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		String name = getPara("name");
		String frequency = getPara("frequency");
		EcExamine ecExamine = EcExamine.dao.getById(eid);
		setAttr("e",ecExamine);
		setAttr("mid",mid);
		setAttr("oid",oid);
		setAttr("iid",iid);
		setAttr("name",name);
		setAttr("frequency",frequency);
		renderIframe("mold4.html");
	}
	public void getMole5Page(){
		String eid = getPara("eid");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		String name = getPara("name");
		String frequency = getPara("frequency");
		setAttr("eid",eid);
		setAttr("mid",mid);
		setAttr("oid",oid);
		setAttr("iid",iid);
		setAttr("name",name);
		setAttr("frequency",frequency);
		renderIframe("mold5.html");
	}

	public void getMole6Page(){
		String eid = getPara("eid");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		String name = getPara("name");
		String frequency = getPara("frequency");
		EcExamine ecExamine = EcExamine.dao.getById(eid);
		setAttr("e",ecExamine);
		setAttr("mid",mid);
		setAttr("oid",oid);
		setAttr("iid",iid);
		setAttr("name",name);
		setAttr("frequency",frequency);
		renderIframe("mold6.html");
	}

	/***
     * list page data
     **/
    public void listData(){
    	String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
    	Page<Record> page = service.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize),mid,iid,oid);
    	renderPage(page.getList(),"",page.getTotalRow());
    }
	public void listData1(){
		String curr = getPara("pageNumber");
		String pageSize = getPara("pageSize");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		Page<Record> page = service.getPage1(Integer.valueOf(curr),Integer.valueOf(pageSize),mid,iid,oid);
		renderPage(page.getList(),"",page.getTotalRow());
	}
	/***
	 * list page data
	 **/
	public void examineListData(){
		String curr = getPara("pageNumber");
		String pageSize = getPara("pageSize");
		String mid = getPara("mid");
		String iid = getPara("iid");
		String oid = ShiroKit.getUserOrgId();
		Page<Record> page = service.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize),mid,iid,oid);
		renderPage(page.getList(),"",page.getTotalRow());
	}
    /***
     * save data
     */
    public void save(){
    	EcExamineAverage o = getModel(EcExamineAverage.class);
    	if(StrKit.notBlank(o.getId())){
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		o.save();
    	}
    	renderSuccess();
    }
}