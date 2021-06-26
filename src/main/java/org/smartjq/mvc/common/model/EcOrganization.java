package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.dto.ZtreeNode;
import org.smartjq.mvc.common.model.base.BaseEcOrganization;

import java.util.ArrayList;
import java.util.List;

/**
 * @author CYZ
 */
@SuppressWarnings("serial")
public class EcOrganization extends BaseEcOrganization<EcOrganization> {
    public static final EcOrganization dao = new EcOrganization();
    public static final String tableName = "ec_organization";


    /***
     * 根据id 查询父id
     * @param uid
     * @return
     */
    public EcOrganization getFidById(String uid) {
        return EcOrganization.dao.findFirst("select * from " + tableName + " o left join  ec_user_organization uo on o.id= uo.oid left join sys_user u on uo.uid = u.id where u.id = '" + uid + "'");
    }

    /***
     * query by id
     */
    public EcOrganization getById(String id) {
        return EcOrganization.dao.findById(id);
    }

    /***
     * 获取所有组织结构
     * @return
     */
    public List<EcOrganization> getAllMenu() {
        List<EcOrganization> list = getChildrenAll("1");
        return list;
    }

    /***
     * 递归
     * 查询父节点
     * @param fid
     * @return
     */
    public List<EcOrganization> getParentAll(String fid) {
        //根据pid查询父级
        List<EcOrganization> organizationList = getChildrenByPid(fid);
        return organizationList;
    }

    /***
     * 递归
     * 查询子节点
     * @param id
     * @return
     */
    public List<EcOrganization> getChildrenAll(String id) {
        //根据id查询孩子
        List<EcOrganization> organizationList = getChildrenByPid(id);
        for (EcOrganization o : organizationList) {
            o.setChildren(getChildrenAll(o.getId()));
        }
        return organizationList;
    }

    /***
     * 根据父id 查询组织结构
     * @param id
     * @return
     */
    public List<EcOrganization> getChildrenByPid(String id) {
        return EcOrganization.dao.find("select * from " + tableName + " o where o.fid='" + id + "'");
    }

    /***
     * 根据父id 查询组织结构数量
     * @param id
     * @return
     */
    public Integer getChildrenCountByPid(String id) {
        return EcOrganization.dao.find("select * from " + tableName + " o where o.fid='" + id + "'").size();
    }


    /**
     * 根据eid查询对应的未涉及分党委
     */
    public List<EcOrganization> getAllOrgByEid(String oids) {
        String sql = " select o.id,o.`name` from ec_organization o where 1=1 ";
        sql += " and o.id in (" + oids + ") ";
        return EcOrganization.dao.find(sql);
    }

    /***
     * 菜单转成ZtreeNode
     * @param organization
     * @return
     */
    public ZtreeNode toZTreeNode(EcOrganization organization) {
        ZtreeNode node = new ZtreeNode();
        node.setId(organization.getId());
        node.setName(organization.getName());
        return node;
    }

    /***
     * 菜单转成ZTreeNode
     * @param organizationList
     * @param open
     * @return
     */
    public List<ZtreeNode> toZTreeNode(List<EcOrganization> organizationList, Boolean open) {
        List<ZtreeNode> list = new ArrayList<ZtreeNode>();
        for (EcOrganization organization : organizationList) {
            ZtreeNode node = toZTreeNode(organization);
//            if(organization.getChildren()!=null&&organization.getChildren().size()>0){
//                node.setChildren(toZTreeNode(organization.getChildren(),open));
//            }
            node.setOpen(open);
            list.add(node);
        }
        return list;
    }

    /***
     * del
     * @param ids
     */
    @Before(Tx.class)
    public void deleteByIds(String ids) {
        String idarr[] = ids.split(",");
        for (String id : idarr) {
            EcOrganization o = EcOrganization.dao.getById(id);
            o.delete();
        }
    }

    /**
     * 根据当前用户组织机构查询未录入的下级
     *
     * @param mid
     * @param eid
     * @return
     */
    public List<EcOrganization> getSonOrgByUid(String mid, String eid) {
        String sql = "select CONCAT(m.id,'_',(case when IFNULL(n.oid,0)=0 then 1 when n.e_frequency>n.s_frequency then n.s_frequency+1 else 0 end)) id,m.name ";
        sql += " from (select id,name from ec_organization where fid='12345678910') m ";
        sql += " left join (select s.oid,e.frequency e_frequency,max(s.frequency) s_frequency from ec_examine_son s left join ec_examine e on s.eid=e.id ";
        sql += " where s.mid='"+mid+"' and s.eid='"+eid+"' ";
        sql += " group by s.oid,e.frequency) n on m.id=n.oid ";
        sql += " where (case when IFNULL(n.oid,0)=0 then 1 when n.e_frequency>n.s_frequency then n.s_frequency+1 else 0 end)>0 ";
        return EcOrganization.dao.find(sql);
    }

    public List<EcOrganization> getChildrenAllTree(String id){
        //根据id查询孩子
        List<EcOrganization> list =  getChildrenByPid(id);
        for(EcOrganization o : list){
            o.setChildren(getChildrenAllTree(o.getId()));
        }
        return list;
    }
    /***
     * 菜单转成ZTreeNode
     * @param
     * olist 数据
     * open  是否展开所有
     * ifOnlyLeaf 是否只选叶子
     * @return
     */
    public List<ZtreeNode> toZTreeNode(List<EcOrganization> olist,Boolean open,Boolean ifOnlyLeaf){
        List<ZtreeNode> list = new ArrayList<ZtreeNode>();
        for(EcOrganization o : olist){
            ZtreeNode node = toZTreeNode(o);
            if(o.getChildren()!=null&&o.getChildren().size()>0){//如果有孩子
                node.setChildren(toZTreeNode(o.getChildren(),open,ifOnlyLeaf));
                if(ifOnlyLeaf){//如果只选叶子
                    node.setNocheck(true);
                }
            }
            if("1".equals(node.getType())){
                node.setOpen(true);
            }else{
                node.setOpen(open);
            }
            list.add(node);
        }
        return list;
    }


}