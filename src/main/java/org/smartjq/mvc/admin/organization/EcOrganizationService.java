package org.smartjq.mvc.admin.organization;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.EcOrganization;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.utils.DateUtil;

/**
 * @author CYZ
 */
public class EcOrganizationService{
	public static final EcOrganizationService me = new EcOrganizationService();
	public static final String TABLE_NAME = EcOrganization.tableName;
	
	/***
	 * query by id
	 */
	public EcOrganization getById(String id){
		return EcOrganization.dao.findById(id);
	}
	
	/***
	 * get page
	 */
	public Page<Record> getPage(int pnum,int psize,String fid,String name){
		String userId = ShiroKit.getUserId();
		String sql  = " from "+TABLE_NAME+" o where 1=1";
		if(StrKit.notBlank(fid)){
			sql = sql + " and o.fid = "+fid;
		}
		if(StrKit.notBlank(name)){
			sql = sql + " and o.name like '%"+name+"%'";
		}
		return Db.paginate(pnum, psize, " select * ", sql);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcOrganization o = me.getById(id);
    		o.delete();
    	}
	}
	
}