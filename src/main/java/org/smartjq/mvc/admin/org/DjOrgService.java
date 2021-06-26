package org.smartjq.mvc.admin.org;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.model.DjOrg;

import java.util.List;

public class DjOrgService {
	public static final DjOrgService me = new DjOrgService();
	private final DjOrg dao = new DjOrg().dao();

	public DjOrg findParentOrg(String orgId) {
		String sql = "select * from dj_org where id='" + orgId + "'";
		List<DjOrg> list = dao.find(sql);
		if(null != list && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
	/***
	 * query by id
	 */
	public DjOrg getById(String id){
		return DjOrg.dao.findById(id);
	}

	public List<Record> getOrgNameByName(String name) {
		Object[] para = new Object[1];
		para[0] = "%" + name + "%";
		String sql  = "select o.id,o.name from dj_org as o where o.name like ? order by o.name asc";
		return Db.find(sql, para);

	}

	public List<Record> getOrgSubListByParentId(String pid) {
		String sql  = "SELECT *  FROM (SELECT t1.*,IF(FIND_IN_SET(parent_id, @pids) > 0, @pids := CONCAT(@pids, ',', id), '0') AS ischild " +
				" FROM (SELECT * FROM dj_org AS t  ORDER BY t.id ASC) t1,(SELECT @pids := '"+pid+"') t2 ) t3 WHERE ischild != '0'";
		return Db.find(sql);
	}
	
}
