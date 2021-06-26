package org.smartjq.mvc.admin.sys.org;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.model.SysOrg;

public class OrgService {
	public static final OrgService me = new OrgService();
	private final SysOrg dao = new SysOrg().dao();

	public SysOrg findParentOrg(String orgId) {
		String sql = "select * from sys_org where id='" + orgId + "'";
		List<SysOrg> list = dao.find(sql);
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/***
	 * query by id
	 */
	public SysOrg getById(String id){
		return SysOrg.dao.findById(id);
	}

	public List<Record> getOrgNameByName(String name) {
		Object[] para = new Object[1];
		para[0] = "%" + name + "%";
		String sql  = "select o.id,o.name from sys_org as o where o.name like ? order by o.name asc";
		return Db.find(sql, para);
	}
	
}
