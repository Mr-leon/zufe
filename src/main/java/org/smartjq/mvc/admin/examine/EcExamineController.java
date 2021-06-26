package org.smartjq.mvc.admin.examine;

import java.io.File;
import java.util.*;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.apache.commons.lang3.StringUtils;
import org.smartjq.mvc.admin.indicatrix.EcIndicatrixService;
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
public class EcExamineController extends BaseController {
    public static final EcExamineService service = EcExamineService.me;
    public static final EcIndicatrixService iservice = EcIndicatrixService.me;
    public static WorkFlowService wfservice = WorkFlowService.me;

    /***
     * get list page
     */
    public void getListPage() {
        String iid = getPara("iid");
        if (StrKit.notBlank(iid)) {
            EcExamine o = new EcExamine();
            o.setIid(iid);
            setAttr("o", o);
        }
        renderIframe("list.html");
    }

    public void getIndicatrixBranchListPage() {
        String type = getPara("type");
        setAttr("type", type);
        renderIframe("indicatrixBrachlist.html");
    }

    public void getOrgBranchListPage() {
        String type = getPara("type");
        setAttr("type", type);
        renderIframe("indicatrixBrachlist.html");
    }

    /***
     * list page data
     **/
    public void indicatrixBranchList() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String uid = ShiroKit.getUserId();
        Page<Record> page = service.getPageIndicatrixBranch(Integer.valueOf(curr), Integer.valueOf(pageSize), uid);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * list page data
     **/
    public void orgBranchList() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        Page<Record> page = service.getPageOrgBranch(Integer.valueOf(curr), Integer.valueOf(pageSize));
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * get list page
     */
    public void getExamineBranchListPage() {
        String mid = getPara("mid");
        if (StrKit.notBlank(mid)) {
            setAttr("mid", mid);
        }
        renderIframe("examineBranchList.html");
    }

    /***
     * get list page
     */
    public void getOrgExamineBranchListPage() {
        String mid = getPara("mid");
        if (StrKit.notBlank(mid)) {
            setAttr("mid", mid);
        }
        setAttr("type", "org");
        renderIframe("examineBranchList.html");
    }

    public void examineBranchList() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String mid = getPara("mid");
        Page<Record> page = service.getPageExamineBranch(Integer.valueOf(curr), Integer.valueOf(pageSize), mid);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    public void orgExamineBranchList() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String mid = getPara("mid");
        Page<Record> page = service.getPageOrgExamineBranch(Integer.valueOf(curr), Integer.valueOf(pageSize), mid);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    public void getExamineBranch1Page() {
        String id = getPara("id");
        String eid = getPara("eid");
        String mid = getPara("mid");
        String ename = getPara("ename");
        String fraction = getPara("fraction");
        String file_name = getPara("file_name");
        String uname = getPara("uname");
        String date_write = getPara("date_write");
        setAttr("id", id);
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("ename", ename);
        setAttr("fraction", fraction);
        setAttr("file_name", file_name);
        setAttr("uname", uname);
        setAttr("date_write", date_write);
        renderIframe("examineBranch1.html");
    }

    public void getExamineBranch2Page() {
        String id = getPara("id");
        String eid = getPara("eid");
        String mid = getPara("mid");
        String ename = getPara("ename");
        String fraction = getPara("fraction");
        String uname = getPara("uname");
        String date_write = getPara("date_write");
        EcExamine e = EcExamine.dao.getById(eid);
        EcExamineSon s = EcExamineSon.dao.getById(id);
        setAttr("id", id);
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("ename", ename);
        setAttr("fraction", fraction);
        setAttr("e_text", e.getEText());
        setAttr("memo", s.getMemo());
        setAttr("uname", uname);
        setAttr("date_write", date_write);
        renderIframe("examineBranch2.html");
    }

    public void getOrgExamineBranch1Page() {
        String id = getPara("id");
        String eid = getPara("eid");
        String mid = getPara("mid");
        String ename = getPara("ename");
        String fraction = getPara("fraction");
        String file_name = getPara("file_name");
        String uname = getPara("uname");
        String date_write = getPara("date_write");
        String euname = getPara("euname");
        String date_examine = getPara("date_examine");
        setAttr("id", id);
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("ename", ename);
        setAttr("fraction", fraction);
        setAttr("file_name", file_name);
        setAttr("uname", uname);
        setAttr("date_write", date_write);
        setAttr("euname", uname);
        setAttr("date_examine", date_write);
        renderIframe("orgExamineBranch1.html");
    }

    public void getOrgExamineBranch2Page() {
        String id = getPara("id");
        String eid = getPara("eid");
        String mid = getPara("mid");
        String ename = getPara("ename");
        String fraction = getPara("fraction");
        String uname = getPara("uname");
        String date_write = getPara("date_write");
        String euname = getPara("euname");
        String date_examine = getPara("date_examine");
        EcExamine e = EcExamine.dao.getById(eid);
        EcExamineSon s = EcExamineSon.dao.getById(id);
        setAttr("id", id);
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("ename", ename);
        setAttr("fraction", fraction);
        setAttr("e_text", e.getEText());
        setAttr("memo", s.getMemo());
        setAttr("uname", uname);
        setAttr("date_write", date_write);
        setAttr("euname", uname);
        setAttr("date_examine", date_write);
        renderIframe("orgExamineBranch2.html");
    }

    /***
     * list data
     **/
    public void getSubjectList() {
        List<Record> sublist = iservice.listSubject();
        renderJson(sublist);
    }

    /***
     * list page data
     **/
    public void listData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String iid = getPara("iid", "");
        Page<Record> page = service.getPage(Integer.valueOf(curr), Integer.valueOf(pageSize), iid);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * save data
     */
    public void save() {
        EcExamine o = getModel(EcExamine.class);
        if (StrKit.notBlank(o.getId())) {
            o.update();
        } else {
            o.setId(UuidUtil.getUUID());
            o.save();
        }
        renderSuccess();
    }

    /***
     * edit page
     */
    public void getEditPage() {
        String id = getPara("id");
        String view = getPara("view");
        setAttr("view", view);
        EcExamine e = new EcExamine();
        if (StrKit.notBlank(id)) {
            e = service.getById(id);
            List<EcOrganization> list = EcOrganization.dao.getAllOrgByEid(e.getOids());
            List<String> idList = new ArrayList<String>();
            List<String> nameList = new ArrayList<String>();
            for (EcOrganization o : list) {
                idList.add(o.getId());
                nameList.add(o.getName());
            }
            setAttr("oids", StringUtils.join(idList, ","));
            setAttr("oname", StringUtils.join(nameList, ","));
        } else {
            String iid = getPara("iid");
            e.setIid(iid);
        }
        setAttr("o", e);
        setAttr("formModelName", StringUtil.toLowerCaseFirstOne(EcExamine.class.getSimpleName()));
        renderIframe("edit.html");
    }

    /***
     * del
     * @throws Exception
     */
    public void delete() throws Exception {
        String ids = getPara("ids");
        service.deleteByIds(ids);
        renderSuccess("删除成功");
    }

    /***
     * 给用户赋值角色
     */
    public void chooseUnEableOrg() {
        String eid = getPara("eid");
        setAttr("eid", eid);
        renderIframe("chooseUnEableOrg.html");
    }

    public void downloadTemp() {
        String fileNamePath = getPara("file");
        try {
            System.out.println("filePath: " + fileNamePath);
            File file = new File(fileNamePath);
            System.out.println("file" + file);
            renderFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            renderError("系统异常，请联系管理员");
        }
    }

    /**
     * 补录主界面
     */
    public void goAdditionalRecording() {
        renderIframe("additionalRecording.html");
    }

    /**
     * 加载补录主界面数据
     */
    public void additionalRecordingListData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        Page<Record> page = service.getARPage(Integer.valueOf(curr), Integer.valueOf(pageSize), year);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /**
     * 进入分党委补录
     */
    public void getARListPage() {
        String iid = getPara("iid");
        String mid = getPara("mid");
        String oid = ShiroKit.getUserOrgId();
        EcOrganization o = EcOrganization.dao.getById(oid);
        Integer nodeCount = EcOrganization.dao.getChildrenCountByPid(oid);
        EcIndicatrix i = EcIndicatrix.dao.getById(iid);
        setAttr("o", o);
        setAttr("i", i);
        setAttr("mid", mid);
        setAttr("nodeCount", nodeCount);
        renderIframe("arList0.html");
    }

    /**
     * 进入党支部补录
     */
    public void getARListPage1() {
        String iid = getPara("iid");
        String mid = getPara("mid");
        String oid = ShiroKit.getUserOrgId();
        EcOrganization o = EcOrganization.dao.getById(oid);
        Integer nodeCount = EcOrganization.dao.getChildrenCountByPid(oid);
        EcIndicatrix i = EcIndicatrix.dao.getById(iid);
        setAttr("o", o);
        setAttr("i", i);
        setAttr("mid", mid);
        setAttr("nodeCount", nodeCount);
        renderIframe("arList1.html");
    }

    /**
     * 加载分党委补录
     */
    public void listARData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        Page<Record> page = service.getARFPage(Integer.valueOf(curr), Integer.valueOf(pageSize), mid, iid, oid);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /**
     * 加载党支部补录
     */
    public void listARData1() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        Page<Record> page = service.getARDPage1(Integer.valueOf(curr), Integer.valueOf(pageSize), mid, iid, oid);
        renderPage(page.getList(), "", page.getTotalRow());
    }


    public void getARMold1Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold1.html");
    }

    public void getARMold2Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold2.html");
    }

    public void getARMold3Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold3.html");
    }

    public void getARMold4Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        EcExamine ecExamine = EcExamine.dao.getById(eid);
        setAttr("e", ecExamine);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold4.html");
    }

    public void getARMold5Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        EcExamine ecExamine = EcExamine.dao.getById(eid);
        setAttr("e", ecExamine);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold5.html");
    }

    public void getARMold6Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        EcExamine ecExamine = EcExamine.dao.getById(eid);
        setAttr("e", ecExamine);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold6.html");
    }

    public void getARMold7Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold7.html");
    }

    public void getARMold8Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold8.html");
    }

    public void getARMold9Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        setAttr("eid", eid);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold9.html");
    }

    public void getARMold10Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        EcExamine ecExamine = EcExamine.dao.getById(eid);
        setAttr("e", ecExamine);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold10.html");
    }

    public void getARMold11Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        EcExamine ecExamine = EcExamine.dao.getById(eid);
        setAttr("e", ecExamine);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold11.html");
    }

    public void getARMold12Page() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        String iid = getPara("iid");
        String oid = ShiroKit.getUserOrgId();
        String name = getPara("name");
        String frequency = getPara("frequency");
        EcExamine ecExamine = EcExamine.dao.getById(eid);
        setAttr("e", ecExamine);
        setAttr("mid", mid);
        setAttr("oid", oid);
        setAttr("iid", iid);
        setAttr("name", name);
        setAttr("frequency", frequency);
        renderIframe("arMold12.html");
    }

    public void arSaveToBN() {
        EcExamineSon o = getModel(EcExamineSon.class);
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        o.setId(UuidUtil.getUUID());
        o.setUidWrite(ShiroKit.getUserId());
        o.setDateWrite(year + "-01-01 00:00:00");
        o.save();
        renderSuccess();
    }

    public void arSaveToJD() {
        EcExamineSon o = getModel(EcExamineSon.class);
        String num = getPara("num");
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        if (num.equals("1")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-01-01 00:00:00");
            o.save();
        } else if (num.equals("2")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-04-01 00:00:00");
            o.save();
        } else if (num.equals("3")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-08-01 00:00:00");
            o.save();
        }
        renderSuccess();
    }

    public void arSaveToMouth() {
        EcExamineSon o = getModel(EcExamineSon.class);
        String num = getPara("num");
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        if (num.equals("1")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-01-01 00:00:00");
            o.save();
        } else if (num.equals("2")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-02-01 00:00:00");
            o.save();
        } else if (num.equals("3")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-03-01 00:00:00");
            o.save();
        } else if (num.equals("4")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-04-01 00:00:00");
            o.save();
        } else if (num.equals("5")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-05-01 00:00:00");
            o.save();
        } else if (num.equals("6")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-06-01 00:00:00");
            o.save();
        } else if (num.equals("7")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-07-01 00:00:00");
            o.save();
        } else if (num.equals("8")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-08-01 00:00:00");
            o.save();
        } else if (num.equals("9")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-09-01 00:00:00");
            o.save();
        } else if (num.equals("10")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-10-01 00:00:00");
            o.save();
        } else if (num.equals("11")) {
            o.setId(UuidUtil.getUUID());
            o.setUidWrite(ShiroKit.getUserId());
            o.setDateWrite(year + "-11-01 00:00:00");
            o.save();
        }
        renderSuccess();
    }

}