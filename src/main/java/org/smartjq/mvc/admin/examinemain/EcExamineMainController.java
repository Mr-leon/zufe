package org.smartjq.mvc.admin.examinemain;

import java.util.*;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
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
public class EcExamineMainController extends BaseController {
    public static final EcExamineMainService service = EcExamineMainService.me;
    public static final EcIndicatrixService iservice = EcIndicatrixService.me;
    public static WorkFlowService wfservice = WorkFlowService.me;

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
     * list data
     **/
    public void getSubjectList() {
        List<Record> sublist = iservice.listSubject();
        renderJson(sublist);
    }

    /***
     * save data
     */
    public void save() {
        String ids = getPara("ids");
        String iid = getPara("iid");
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String[] idarr = ids.split(",");
        for (String oid : idarr) {
            EcExamineMain o = new EcExamineMain();
            o.setYear(String.valueOf(yearInt));
            String id = UuidUtil.getUUID();
            o.setId(id);
            o.setIid(iid);
            o.setOid(oid);
            o.save();
        }
        renderSuccess();
    }

    /***
     * get show list
     */
    public void getShowFirst() {
        renderIframe("showFirst.html");
    }

    /***
     * get show list
     */
    public void getShowList() {
        renderIframe("showList.html");
    }

    /***
     * list page data
     **/
    public void firstListData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        Page<Record> page = service.getFirstPage(Integer.valueOf(curr), Integer.valueOf(pageSize), year);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * list page data
     **/
    public void firstExamineListData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String year = String.valueOf(yearInt);
        Page<Record> page = service.getFirstPage(Integer.valueOf(curr), Integer.valueOf(pageSize), year);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * get list page
     */
    public void getListPage() {
        String iid = getPara("iid");
        if (StrKit.notBlank(iid)) {
            EcExamineMain o = new EcExamineMain();
            o.setIid(iid);
            setAttr("o", o);
        }
        renderIframe("list.html");
    }

}