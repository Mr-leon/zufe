package org.smartjq.mvc.admin.indicatrix;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.EcIndicatrix;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.utils.DateUtil;

import java.util.List;

public class EcIndicatrixService{
	public static final EcIndicatrixService me = new EcIndicatrixService();
	public static final String TABLE_NAME = EcIndicatrix.tableName;
	
	/***
	 * query by id
	 */
	public EcIndicatrix getById(String id){
		return EcIndicatrix.dao.findById(id);
	}
	
	/***
	 * get page
	 */
	public Page<Record> getPage(int pnum,int psize,String name){
		String userId = ShiroKit.getUserId();
		String sql  = " from "+TABLE_NAME+" o where 1=1";
		//sql = sql + SysRoleOrg.dao.getRoleOrgSql(userId) ;
		if(StrKit.notBlank(name)){
			sql = sql + " and o.name like '%"+name+"%'";
		}
		return Db.paginate(pnum, psize, " select * ", sql);
	}


	public List<Record> listSubject() {
		String sql  = "select id,name from ec_indicatrix ";
		return Db.find(sql);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcIndicatrix o = me.getById(id);
    		o.delete();
    	}
	}
	
}