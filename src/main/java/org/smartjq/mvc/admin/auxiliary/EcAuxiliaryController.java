package org.smartjq.mvc.admin.auxiliary;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.admin.workflow.WorkFlowService;
import org.smartjq.mvc.common.utils.StringUtil;
import org.smartjq.mvc.common.model.EcAuxiliary;
import org.smartjq.mvc.common.model.SysUser;
import org.smartjq.mvc.common.model.SysOrg;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.mvc.common.utils.Constants;
import org.smartjq.mvc.admin.common.DjConstants;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.plugin.shiro.ShiroKit;


public class EcAuxiliaryController extends BaseController {
    public static final EcAuxiliaryService service = EcAuxiliaryService.me;
    public static WorkFlowService wfservice = WorkFlowService.me;

    /***
     * get list page
     */
    public void getListPage() {
        renderIframe("list.html");
    }

    /***
     * list page data
     **/
    public void listData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String endTime = getPara("endTime", "");
        String startTime = getPara("startTime", "");
        String applyUser = getPara("applyUser", "");
        Page<Record> page = service.getPage(Integer.valueOf(curr), Integer.valueOf(pageSize), startTime, endTime, applyUser);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * save data
     */
    public void save() {
        EcAuxiliary o = getModel(EcAuxiliary.class);
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
        EcAuxiliary o = new EcAuxiliary();
        if (StrKit.notBlank(id)) {
            o = service.getById(id);

        }
        setAttr("o", o);
        setAttr("formModelName", StringUtil.toLowerCaseFirstOne(EcAuxiliary.class.getSimpleName()));
        renderIframe("edit.html");
    }

    /***
     * del
     * @throws Exception
     */
    public void delete() throws Exception {
        String ids = getPara("ids");
        service.deleteByIds(ids);
        renderSuccess("删除成功!");
    }

    public void getMouthALl() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        renderJson(EcAuxiliary.dao.findMouthAllByOid(eid, mid));
    }
    public void getJDALl() {
        String eid = getPara("eid");
        String mid = getPara("mid");
        renderJson(EcAuxiliary.dao.findJDAllByOid(eid, mid));
    }
}