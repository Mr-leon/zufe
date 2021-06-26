package org.smartjq.mvc.admin.sys.user;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.model.SysUser;


public class SysUserService {
	public static final SysUserService me = new SysUserService();
	
	/***
	 * 获取所有用户
	 * @return
	 */
	public List<SysUser> getAllUser(){
		return SysUser.dao.find("select * from sys_user");
	}

	/***
	 * query by id
	 */
	public SysUser getById(String id){
		return SysUser.dao.findById(id);
	}
	
	public List<Record> findUserByName(String name) {
		Object[] para = new Object[1];
		para[0] = "%" + name + "%";
		String sql  = "select u.id,u.username,u.name,o.name as orgName from sys_user as u,sys_org as o where u.orgid=o.id and u.name like ? order by u.name asc";
		return Db.find(sql, para);
	}
}