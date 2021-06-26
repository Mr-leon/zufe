package org.smartjq.mvc.admin.examineson;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.EcExamineSon;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.utils.DateUtil;

public class EcExamineSonService{
	public static final EcExamineSonService me = new EcExamineSonService();
	public static final String TABLE_NAME = EcExamineSon.tableName;
	
	/***
	 * query by id
	 */
	public EcExamineSon getById(String id){
		return EcExamineSon.dao.findById(id);
	}

	/***
	 * get page
	 */
	public Page<Record> getPage(int pnum,int psize,String startTime,String endTime,String applyUser){
		String userId = ShiroKit.getUserId();
		String sql  = " from "+TABLE_NAME+" o where 1=1";
		//sql = sql + SysRoleOrg.dao.getRoleOrgSql(userId) ;
		if(StrKit.notBlank(startTime)){
			sql = sql + " and o.create_time>='"+ DateUtil.formatSearchTime(startTime,"0")+"'";
		}
		if(StrKit.notBlank(endTime)){
			sql = sql + " and o.create_time<='"+DateUtil.formatSearchTime(endTime,"1")+"'";
		}
		if(StrKit.notBlank(applyUser)){
			sql = sql + " and o.applyer_name like '%"+applyUser+"%'";
		}
		sql = sql + " order by o.create_time desc";
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
    		EcExamineSon o = me.getById(id);
    		o.delete();
    	}
	}
	
}