package org.smartjq.mvc.common.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.model.base.BaseSysPointUser;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SysPointUser extends BaseSysPointUser<SysPointUser> {
	public static final SysPointUser dao = new SysPointUser();
	
	/**
	 * 这个文章被多少人领取了
	 * @param id
	 * @return
	 */
	public List<SysPointUser> getPointUserListByContentId(String id){
		return SysPointUser.dao.find("select * from sys_point_user u where u.receive_content_id='"+id+"'");
	}
	
	/***
	 * 这个人是否领取过这个文章
	 * @param userid
	 * @param id
	 * @return
	 */
	public SysPointUser getPointUserListByUserIDAndContentId(String userid,String id){
		return SysPointUser.dao.findFirst("select * from sys_point_user u where u.receive_content_id='"+id+"' and u.userid='"+userid+"'");
	}
	
	
	
	/***************签到********************/
	/***
	 * 根据用户和日期查询
	 * @return
	 */
	public SysPointUser getSignByUserAndDate(String userid , String day){
		return SysPointUser.dao.findFirst("select * from sys_point_user d where d.userid='"+userid+"' and d.sign_day='"+day+"'");
	}
	
	/***
	 * 根据用户
	 * @return
	 */
	public List<SysPointUser> getSignByUser(String userid){
		return SysPointUser.dao.find("select * from sys_point_user d where d.userid='"+userid+"' and sign_day is not NULL ");
	}
	
	/***
	 * 查询用户签到获得总分
	 */
	public Integer getPointByDaySign(String userid){
		Record r = Db.findFirst("select SUM(u.point) s from sys_point_user u where u.sign_day is not null and u.userid='"+userid+"' ");
		if(r.getBigDecimal("s")!=null){
			return r.getBigDecimal("s").intValue();
		}else{
			return 0;
		}
	}
	
	/***
	 * 查询用户签收获取总分
	 */
	public Integer getPointByReceive(String userid){
		Record r = Db.findFirst("select SUM(u.point) s from sys_point_user u where u.receive_content_id is not null and u.userid='"+userid+"' ");
		if(r.getBigDecimal("s")!=null){
			return r.getBigDecimal("s").intValue();
		}else{
			return 0;
		}
	}
}