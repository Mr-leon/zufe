package org.smartjq.mvc.admin.organization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.admin.workflow.WorkFlowService;
import org.smartjq.mvc.common.dto.ZtreeNode;
import org.smartjq.mvc.common.model.*;
import org.smartjq.mvc.common.utils.*;
import org.smartjq.mvc.admin.common.DjConstants;
import org.smartjq.plugin.shiro.ShiroKit;


/**
 * @author CYZ
 */
public class EcOrganizationController extends BaseController {
    public static final EcOrganizationService service = EcOrganizationService.me;
    public static WorkFlowService wfservice = WorkFlowService.me;

    /***
     * get list page
     */
    public void getListPage() {
        setAttr("org", EcOrganization.dao.getAllMenu());
        renderIframe("list.html");
    }

    /***
     * list page data
     **/
    public void listData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String fid = getPara("fid", "");
        String name = getPara("name", "");
        Page<Record> page = service.getPage(Integer.valueOf(curr), Integer.valueOf(pageSize), fid, name);
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * save data
     */
    public void save() {
        EcOrganization o = getModel(EcOrganization.class);
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
        String fid = getPara("fid");
        String view = getPara("view");
        setAttr("view", view);
        //添加和修改
        if (StrKit.notBlank(id)) {
            EcOrganization organization = EcOrganization.dao.getById(id);
            EcOrganization parent = EcOrganization.dao.getById(organization.getFid());
            setAttr("o", organization);
            setAttr("p", parent);
        }
        //添加子模块
        if (StrKit.notBlank(fid)) {
            EcOrganization parent = EcOrganization.dao.getById(fid);
            setAttr("p", parent);
        }
        renderIframe("edit.html");
    }

    /***
     * 获取选择父级页面
     */
    public void getSelectOneOrgPage() {
        renderIframe("selectOneOrg.html");
    }

    /**
     * 返回所有分党委
     */
    public void getAllOrgTreeNode() {
        String fid = getPara("fid","1");
        renderJson(EcOrganization.dao.getParentAll(fid));
    }

    /**
     *  根据eid查询对应的未涉及分党委
     */
    public void getAllOrgByEid(){
        String eid = getPara("eid");
        EcExamine e = EcExamine.dao.getById(eid);
        String oids = e.getOids();
        renderJson(EcOrganization.dao.getAllOrgByEid(oids));
    }

    /**
     *  查询当前登录用户组织机构下未录入的分党委
     */
    public void getSonOrgByUid(){
        String mid = getPara("mid");
        String eid = getPara("eid");
        renderJson(EcOrganization.dao.getSonOrgByUid(mid,eid));
    }

    /***
     * 返回所有组织结构
     */
    public void getAllOrgTree() {
        List<EcOrganization> ecOrganizationList = EcOrganization.dao.getAllMenu();
        //数据库中的组织结构
        List<ZtreeNode> nodelist = EcOrganization.dao.toZTreeNode(ecOrganizationList, true);
        //页面展示的,带根节点
        List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();
        //声明根节点
        ZtreeNode root = new ZtreeNode();
        root.setId("#root");
        root.setName("组织结构");
        root.setChildren(nodelist);
        root.setOpen(true);
        rootList.add(root);
        renderJson(rootList);
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

    public void getOrgTree(){
        String orgid = getPara("orgid");
        String ifAllChild = "1";//默认查询孩子
        Boolean open = false;//是否展开所有
        Boolean ifOnlyLeaf = false;//是否只选叶子
        if(StrKit.notBlank(getPara("ifAllChild"))){//是否查询所有孩子
            ifAllChild = getPara("ifAllChild");
        }
        if(StrKit.notBlank(getPara("ifOpen"))){//是否查询所有孩子
            if("1".equals(getPara("ifOpen"))){
                open = true;
            }
        }
        if(StrKit.notBlank(getPara("ifOnlyLeaf"))){//是否查询所有孩子
            if("1".equals(getPara("ifOnlyLeaf"))){
                ifOnlyLeaf = true;
            }
        }
        if(SysRole.dao.ifSuperAdmin(ShiroKit.getUserId())){//如果是超级管理员
            List<EcOrganization> menuList = EcOrganization.dao.getChildrenAllTree("#root");
            List<ZtreeNode> nodelist = EcOrganization.dao.toZTreeNode(menuList,open,ifOnlyLeaf);//数据库中的菜单
            List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
            //声明根节点
            EcOrganization org = EcOrganization.dao.getById("#root");
            ZtreeNode root = new ZtreeNode();
            root.setId("#root");
            if(org==null){
                root.setName("组织结构");
            }else{
                root.setName(org.getName());
            }
            root.setChildren(nodelist);
            root.setOpen(true);
            root.setIcon(ContextUtil.getCtx()+"/common/plugins/zTree_v3/css/zTreeStyle/img/diy/9.png");
            rootList.add(root);
            renderJson(rootList);
        }else{
            if(StrKit.isBlank(orgid)){//如果没传递过来机构id，使用子公司id
                orgid = ShiroKit.getUserOrgId();
            }
            List<EcOrganization> menuList = new ArrayList<EcOrganization>();
            if("1".equals(ifAllChild)){
                menuList = EcOrganization.dao.getChildrenAllTree(orgid);
            }else{
                menuList = EcOrganization.dao.getChildrenByPid(orgid);
            }
            List<ZtreeNode> nodelist = EcOrganization.dao.toZTreeNode(menuList,open,ifOnlyLeaf);//数据库中的菜单
            List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
            EcOrganization org = EcOrganization.dao.getById(orgid);//传进来的id
            ZtreeNode root = new ZtreeNode();
            if(org!=null){
                root.setId(org.getId());
                root.setName(org.getName());
            }
            root.setChildren(nodelist);
            root.setOpen(true);
            root.setIcon(ContextUtil.getCtx()+"/common/plugins/zTree_v3/css/zTreeStyle/img/diy/1_open.png");
            rootList.add(root);
            renderJson(rootList);
        }
    }

    /***
     * 获取选择树页面
     * */
    public void getUserSelectOneOrgPage(){
        String orgid = getPara("orgid");
        String ifAllChild = getPara("ifAllChild");
        String ifOpen = getPara("ifOpen");
        String ifOnlyLeaf = getPara("ifOnlyLeaf");
        setAttr("orgid", orgid);
        setAttr("ifAllChild", ifAllChild);
        setAttr("ifOpen", ifOpen);
        setAttr("ifOnlyLeaf", ifOnlyLeaf);
        renderIframe("/WEB-INF/admin/organization/userSelectOneOrg.html");
    }

}