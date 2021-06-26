package org.smartjq.mvc.admin.noticeuser;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.DjNoticeUser;
import org.smartjq.mvc.common.utils.DateUtil;

public class DjNoticeUserService{
	public static final DjNoticeUserService me = new DjNoticeUserService();
	public static final String TABLE_NAME = DjNoticeUser.tableName;
	
	/***
	 * query by id
	 */
	public DjNoticeUser getById(String id){
		return DjNoticeUser.dao.findById(id);
	}
	
	/***
	 * get page
	 */
	public Page<Record> getPage(int pnum,int psize,String startTime,String endTime,String userName, String isSign, String id){
//		String sql  = " n.title,n.sender_name,n.sender_orgname,n.publish_time,u.username,nu.if_sign,nu.sign_time from dj_notice as n, dj_notice_user as nu,sys_user as u where nu.notice_id=n.id and u.id=nu.user_id ";
		String sql  = " from dj_notice as n, dj_notice_user as nu,dj_party_member as u where nu.notice_id=n.id and u.id=nu.user_id and n.if_publish='1' ";
		if(StrKit.notBlank(startTime)){
			sql = sql + " and nu.sign_time>='"+ DateUtil.formatSearchTime(startTime,"0")+"'";
		}
		if(StrKit.notBlank(endTime)){
			sql = sql + " and nu.sign_time<='"+DateUtil.formatSearchTime(endTime,"1")+"'";
		}
		if(StrKit.notBlank(userName)){
			sql = sql + " and u.name like '%"+userName+"%'";
		}
		if(StrKit.notBlank(id)){
			sql = sql + " and n.id='"+id+"'";
		}
		if(StrKit.notBlank(isSign)){
			sql = sql + " and nu.if_sign='"+isSign+"'";
		}
		sql = sql + " order by n.publish_time desc";
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
    		DjNoticeUser o = me.getById(id);
    		o.delete();
    	}
	}

	public List<Record> findUserNotice(String userId, String noticeId) {
		return Db.find("select * from dj_notice_user u where u.user_id='"+userId+"' and u.notice_id='" + noticeId + "'");
	}
	
}