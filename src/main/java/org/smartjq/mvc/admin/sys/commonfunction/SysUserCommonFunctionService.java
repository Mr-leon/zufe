package org.smartjq.mvc.admin.sys.commonfunction;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.model.SysUserCommonFunction;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.plugin.shiro.ShiroKit;

public class SysUserCommonFunctionService{
	public static final SysUserCommonFunctionService me = new SysUserCommonFunctionService();
	public static final String TABLE_NAME = SysUserCommonFunction.tableName;
	
	/***
	 * query by id
	 */
	public SysUserCommonFunction getById(String id){
		return SysUserCommonFunction.dao.findById(id);
	}
	
	/***
	 * get page
	 */
	public Page<Record> getPage(int pnum,int psize,String startTime,String endTime,String applyUser){
		String userId = ShiroKit.getUserId();
		String sql  = " from "+TABLE_NAME+" o LEFT JOIN act_hi_procinst p ON o.proc_ins_id=p.ID_  where 1=1";
		sql = sql + SysRoleOrg.dao.getRoleOrgSql(userId) ;
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
	 * @param userId 
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteById(String menuId, String userId){

		List<SysUserCommonFunction> list = SysUserCommonFunction.dao.find("select * from "+TABLE_NAME+" where if_del='0' and userid='" + userId + "' and menu_id='" + menuId + "'");
		if(null != list && 0 < list.size()) {
			SysUserCommonFunction o =  list.get(0);
			o.setIfDel("1");
	    	o.update();
		}
	}

	public List<Record> getCommonFunctions(String userId) {
		return Db.find("select cf.menu_id,sm.name, sm.url from "+TABLE_NAME+" as cf, sys_menu as sm where if_del='0' and cf.menu_id=sm.id and cf.userid='" + userId + "' order by cf.use_time desc limit 8");
	}

	public SysUserCommonFunction getByMenuId(String menuId, String userId) {
		List<SysUserCommonFunction> list = SysUserCommonFunction.dao.find("select * from "+TABLE_NAME+" where if_del='0' and userid='" + userId + "' and menu_id='" + menuId + "'");
		if(null != list && 0 < list.size()) {
			return list.get(0);
		}
		return null;
	}
}