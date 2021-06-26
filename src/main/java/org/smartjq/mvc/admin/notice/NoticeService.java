package org.smartjq.mvc.admin.notice;

import java.util.List;

import org.smartjq.mvc.common.model.DjNotice;
import org.smartjq.mvc.common.model.DjNoticeUser;
import org.smartjq.mvc.common.model.DjPartyMember;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.mvc.common.utils.UuidUtil;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/***
 * web端通知公告调用服务
 * @author Administrator
 *
 */
public class NoticeService {
	
	public static final NoticeService me = new NoticeService();
	private final DjNotice dao = new DjNotice().dao();
	private final DjNoticeUser nudao = new DjNoticeUser().dao();
	
	@Before(Tx.class)
	public void save(DjNotice notice){
//		notice.setTextContent(StringUtil.delHTMLTag(notice.getContent()));//设置纯文本
		DjNotice o = dao.findById(notice.getId());
		if(null != o){//更新
			notice.update();//更新公共
		}else{//保存
			notice.setCreateTime(DateUtil.getCurrentTime());
			notice.save();//保存公告
		}
		//根据推送单位，保存通知到的人
		deleteNoticeUserByNoticeId(notice.getId());//删除该通知所有通知到人
		String orgidarr[] = notice.getToOrgId().split(",");
		for(String orgid : orgidarr){
			List<DjPartyMember> userlist = DjPartyMember.dao.getUserListByOrgId(orgid);//查询机构下所有人员
			for(DjPartyMember user : userlist){
				DjNoticeUser noticeuser = new DjNoticeUser();
				noticeuser.setId(UuidUtil.getUUID());
				noticeuser.setUserId(user.getId());
				noticeuser.setUserName(user.getName());
				noticeuser.setNoticeId(notice.getId());
				noticeuser.save();
			}
		}
	}
	
	/***
	 * 删除公告下所有通知到人
	 * @param noticeid
	 */
	public void deleteNoticeUserByNoticeId(String noticeid){
		Db.update("delete from dj_notice_user where notice_id='"+noticeid+"'");//删除该通知所有通知到人
	} 
	
	/***
	 * 删除通知公告
	 * @param id
	 */
	@Before(Tx.class)
	public void deleteNotice(String id){
		DjNotice notice = DjNotice.dao.findById(id);
		if(notice!=null){
			notice.delete();
		}
		deleteNoticeUserByNoticeId(id);
	}
	
	/***
	 * 发布
	 */
	public void publish(String id){
		DjNotice notice = DjNotice.dao.findById(id);
		notice.setIfPublish(NoticeConstants.NOTICE_IF_PUBLISH_YES);
		notice.setPublicTime(DateUtil.getCurrentTime());
		notice.update();
	}
	
	/***
	 * 取回
	 */
	@Before(Tx.class)
	public void callBack(String id){
		DjNotice notice = DjNotice.dao.findById(id);
		notice.setIfPublish(NoticeConstants.NOTICE_IF_PUBLISH_NO);
		notice.setPublicTime("");
		notice.update();
		//将所有被通知人签收状态改为未签收
		List<DjNoticeUser> list = DjNoticeUser.dao.getNoticeUserListByNoticeId(id);
		for(DjNoticeUser o : list){
			o.setIfSign(NoticeConstants.NOTICE_IF_SIGN_NO);
			o.setSignTime("");
			o.update();
		}
	}
	
	/***
	 * 获取我的通知公告
	 * @param userid
	 * @return
	 */
	public List<DjNotice> getMyNotice(String userid,String ifsign){
		return dao.find("select DISTINCT n.*,u.if_sign from dj_notice n ,dj_notice_user u where n.id=u.notice_id and u.user_id='"+userid+"' and n.if_publish='"+NoticeConstants.NOTICE_IF_PUBLISH_YES+"' and u.if_sign='"+ifsign+"'");
	}
	
	/**
	 * 首页活动列表数据，查询最新6条记录
	 * @param userid
	 * @return
	 */
	public List<DjNotice> getMyNotice(String userid){
		return dao.find("select DISTINCT n.*,u.if_sign from dj_notice n ,dj_notice_user u where n.id=u.notice_id and u.user_id='"+userid+"' and n.if_publish='"+NoticeConstants.NOTICE_IF_PUBLISH_YES+"' order by n.publish_time desc limit 6");
	}
	public Page<DjNotice> getMyNoticePage(int pnum,int psize,String userid){
		return DjNotice.dao.paginate(pnum, psize, "select DISTINCT n.*,u.if_sign,u.sign_time "," from dj_notice n ,dj_notice_user u where n.id=u.notice_id and u.user_id='"+userid+"' and n.if_publish='"+NoticeConstants.NOTICE_IF_PUBLISH_YES+"' order by n.publish_time desc");
	}
	/***
	 * 签收公告
	 * @param userid
	 * @param noticeid
	 */
	public void sign(String userid,String noticeid){
		DjNoticeUser u = nudao.findFirst("select * from dj_notice_user u where u.notice_id = '"+noticeid+"' and u.user_id='"+userid+"'");
		u.setIfSign(NoticeConstants.NOTICE_IF_SIGN_YES);
		u.setSignTime(DateUtil.getCurrentTime());
		u.update();
	}

	public List<Record> findUserNotice(String userId, String noticeId) {
		return Db.find("select * from dj_notice_user u where u.user_id='"+userId+"' and u.notice_id='" + noticeId + "'");
	}
}
