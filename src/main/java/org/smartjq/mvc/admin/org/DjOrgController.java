/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.mvc.admin.org;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.dto.ZtreeNode;
import org.smartjq.mvc.common.model.DjOrg;
import org.smartjq.mvc.common.model.DjPartyMember;
import org.smartjq.mvc.common.model.SysRole;
import org.smartjq.mvc.common.utils.ContextUtil;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.plugin.shiro.ShiroKit;

import java.util.ArrayList;
import java.util.List;

/***
 * 菜单管理控制器
 */

public class DjOrgController extends BaseController {
	public static final DjOrgService service = DjOrgService.me;
	/***
	 * 获取列表页面
	 */
	public void getListPage(){
//		String userParentChildCompanyId = ShiroKit.getUserParentChileCompanyId();//上级子公司id
		setAttr("pid",ShiroKit.getUserOrgId());
    	renderIframe("list.html");
    }
    /***
     * 获取树
     */
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
    		List<DjOrg> menuList = DjOrg.dao.getChildrenAllTree("#root");
        	List<ZtreeNode> nodelist = DjOrg.dao.toZTreeNode(menuList,open,ifOnlyLeaf);//数据库中的菜单
        	List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
        	//声明根节点
        	DjOrg rootOrg = DjOrg.dao.getById("#root");
        	ZtreeNode root = new ZtreeNode();
        	root.setId("#root");
        	if(rootOrg==null){
        		root.setName("公司组织结构");
        	}else{
        		root.setName(rootOrg.getName());
        	}
        	root.setChildren(nodelist);
        	root.setOpen(true);
        	root.setIcon(ContextUtil.getCtx()+"/common/plugins/zTree_v3/css/zTreeStyle/img/diy/9.png");
        	rootList.add(root);
        	renderJson(rootList);
    	}else{
    		if(StrKit.isBlank(orgid)){//如果没传递过来机构id，使用子公司id
    			orgid = ShiroKit.getUserParentChileCompanyId();
    		}
    		if(StrKit.isBlank(orgid)){//如果没传递过来机构id，使用子公司id
    			orgid = ShiroKit.getUserOrgId();
    		}
    		List<DjOrg> menuList = new ArrayList<DjOrg>();
    		if("1".equals(ifAllChild)){
    			menuList = DjOrg.dao.getChildrenAllTree(orgid);
    		}else{
    			menuList = DjOrg.dao.getChildrenByPid(orgid, "0");
    		}
        	List<ZtreeNode> nodelist = DjOrg.dao.toZTreeNode(menuList,open,ifOnlyLeaf);//数据库中的菜单
        	List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
        	DjOrg org = DjOrg.dao.getById(orgid);//传进来的id
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
    public void getOrgTreeOnlyLeaf(){
    	List<DjOrg> menuList = DjOrg.dao.getChildrenAllTree("#root");
    	List<ZtreeNode> nodelist = DjOrg.dao.toZTreeNode(menuList,true,true);//数据库中的菜单
    	List<ZtreeNode> rootList = new ArrayList<ZtreeNode>();//页面展示的,带根节点
    	//声明根节点
    	ZtreeNode root = new ZtreeNode();
    	root.setId("#root");
    	root.setName("公司部门组织结构");
    	root.setChildren(nodelist);
    	root.setOpen(true);
    	root.setNocheck(true);
    	rootList.add(root);
    	renderJson(rootList);
    }
    /***
     * 获取分页数据
     **/
    public void listData(){
    	String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
    	String pid = getPara("pid");
    	Page<Record> page = DjOrg.dao.getChildrenPageByPid(Integer.valueOf(curr),Integer.valueOf(pageSize),pid);
    	renderPage(page.getList(),"" ,page.getTotalRow());
    }
    
    /***
     * 获取编辑页面
     */
    public void getEditPage(){
    	setAttr("type", getPara("type"));
    	//添加和修改
    	String id = getPara("id");
    	if(StrKit.notBlank(id)){//修改
    		DjOrg o = DjOrg.dao.getById(id);
    		DjOrg parent = DjOrg.dao.getById(o.getParentId());
    		setAttr("o", o);
    		setAttr("p", parent);
    		setAttr("type", o.getType());
    		if("1".equals(parent.getType())){//父级就是公司
    			setAttr("parentCompany", parent);//父级子公司
    		}else if("0".equals(parent.getType())){//父级是部门
    			String parentCompanyId = parent.getParentChildCompanyId();//该部门所属子公司
        		DjOrg parentCompany = DjOrg.dao.getById(parentCompanyId);
        		setAttr("parentCompany", parentCompany);//父级子公司
    		}
    	}
    	String parentid = getPara("parentid");
    	if(StrKit.notBlank(parentid)){
    		DjOrg parent = DjOrg.dao.getById(parentid);
    		setAttr("p", parent);//父级机构
    		if("1".equals(parent.getType())){//父级就是公司
    			setAttr("parentCompany", parent);//父级子公司
    		}else if("0".equals(parent.getType())){//父级是部门
    			String parentCompanyId = parent.getParentChildCompanyId();//该部门所属子公司
        		DjOrg parentCompany = DjOrg.dao.getById(parentCompanyId);
        		setAttr("parentCompany", parentCompany);//父级子公司
    		}
    	}
    	renderIframe("edit.html");
    }
    /***
     * 保存
     */
    public void save(){
    	DjOrg o = getModel(DjOrg.class);
    	if(StrKit.notBlank(o.getId())){
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		o.save();
    	}
    	renderSuccess();
    }
    /***
     * 获取选择树页面
     * */
    public void getSelectOneOrgPage(){
    	String orgid = getPara("orgid");
    	String ifAllChild = getPara("ifAllChild");
    	String ifOpen = getPara("ifOpen");
    	String ifOnlyLeaf = getPara("ifOnlyLeaf");
    	setAttr("orgid", orgid);
    	setAttr("ifAllChild", ifAllChild);
    	setAttr("ifOpen", ifOpen);
    	setAttr("ifOnlyLeaf", ifOnlyLeaf);
    	renderIframe("/WEB-INF/admin/org/selectOneOrg.html");
    }
    public void getSelectManyOrgPage(){
    	String checkedNode = getPara("checkedNode");
    	setAttr("checkedNode", checkedNode);
    	renderIframe("/WEB-INF/admin/org/selectManyOrg.html");
    }
    
    /***
     * 删除
     * @throws Exception
     */
    public void delete() throws Exception{
		String ids = getPara("ids");
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		List<DjOrg> list = DjOrg.dao.getChildrenByPid(id,null);
    		if(list.size()>0){
    			renderError("有子党组织，不允许删除！");
    			return;
    		}
    		//组织机构下是否有党员
    		List<DjPartyMember> pmList = DjPartyMember.dao.find("select * from dj_party_member where org_id='" + id + "'");
    		if(pmList.size()>0){
    			renderError("党组织下有党员信息，不允许删除！");
    			return;
    		}
    	}
    	//执行删除
    	DjOrg.dao.deleteByIds(ids);
    	renderSuccess("删除成功!");
    }
    /***
     * 同级别的组织结构下，不允许重名
     */
    public void validOrgname(){
    	DjOrg org = getModel(DjOrg.class);
    	if(org!=null){
    		String parentId = getPara("parentId");
    		String orgId = getPara("orgId");
    		if(StrKit.isBlank(orgId)){//新增
    			String name = org.getName();
        		if(StrKit.notBlank(parentId)&&StrKit.notBlank(name)){
        			List<DjOrg> list = DjOrg.dao.find("select * from dj_org o where o.parent_id='"+parentId+"' and o.name='"+name+"' ");
        			if(list!=null&&list.size()>0){//如果有值，则校验失败
        				renderValidFail();
        			}else{
        				renderValidSuccess();
        			}
        		}else{
        			renderValidFail();
        		}
    		}else{//更新
    			String name = org.getName();//当前存入的名字
        		if(StrKit.notBlank(parentId)&&StrKit.notBlank(name)){
        			List<DjOrg> list = DjOrg.dao.find("select * from dj_org o where o.parent_id='"+parentId+"' and o.name='"+name+"' and o.id!='"+orgId+"' ");
        			if(list!=null&&list.size()>0){//如果有值，则校验失败
        				renderValidFail();
        			}else{
        				renderValidSuccess();
        			}
        		}else{
        			renderValidFail();
        		}
    		}
    		
    	}else{
    		renderValidSuccess();
    	}
    }

	/***
	 * 根据机构名称查询机构
	 */
	public void getOrgNameByName() {
		String name = getPara("q");
		Object[] para = new Object[1];
		para[0] = "%" + name + "%";
		List<Record> list = service.getOrgNameByName(name);
    	setAttr("code", "200");
    	setAttr("data", list);
    	renderJson(list);
	}

	public void getOrgSubListByParentId()
	{
		String orgid = ShiroKit.getUserOrgId();
		List<Record> list = service.getOrgSubListByParentId(orgid);
		setAttr("code", "200");
		setAttr("data", list);
		renderJson(list);
	}
}
