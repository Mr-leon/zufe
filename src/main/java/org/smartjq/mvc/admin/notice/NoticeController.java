/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.mvc.admin.notice;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.model.DjNotice;
import org.smartjq.mvc.common.model.SysMenu;
import org.smartjq.mvc.common.model.SysOrg;
import org.smartjq.mvc.common.model.SysUser;
import org.smartjq.mvc.common.model.SysUserCommonFunction;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.plugin.shiro.ShiroKit;

/***
 * 通知公告控制器（web）
 * @author Administrator
 *
 */
public class NoticeController extends BaseController {
	
	static NoticeService service =  NoticeService.me;
	
	/***************************通知公告---开始***********************/
	/***
	 * 获取通知公告发布列表页面
	 */
	public void getListPage(){
		HttpServletRequest request = getRequest();
		String url = request.getRequestURL().toString();
		String userId = ShiroKit.getUserId();
		String context = request.getContextPath();
		String menu = url.substring(url.indexOf(context) + context.length());
		SysMenu sm = smService.findByUrl(menu);
		if(null != sm) {
			SysUserCommonFunction o = cfService.getByMenuId(sm.getId(), userId);
			if(null != o) {
				o.setUseTime(new Date());
		    	o.update();
			}else {
				o = new SysUserCommonFunction();
				o.setCreateTime(DateUtil.dateToString(new Date(), 0));
				o.setIfDel("0");
				o.setIfFixed("0");
				o.setMenuId(sm.getId());
				o.setUserid(userId);
		    	o.setId(UuidUtil.getUUID());
				o.setUseTime(new Date());
		    	o.save();
			}
		}
		renderIframe("list.html");
    }
	/***
	 * 获取通知公告数据列表
	 */
	public void getListData(){
		String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
    	Page<DjNotice> page = DjNotice.dao.getPage(Integer.valueOf(curr),Integer.valueOf(pageSize));
    	renderPage(page.getList(),"" ,page.getTotalRow());
	}
	
	/***
	 * 获取通知公告起草页面
	 */
	public void getEditPage(){
//		String parentPath = this.getRequest().getServletPath().substring(0,this.getRequest().getServletPath().lastIndexOf("/")+1);

		//是否是查看详情页面
		String view = getPara("view");
		if("detail".equals(view)){
			setAttr("view", view);
		}
		String id = getPara("id");
		if(StrKit.notBlank(id)){
			setAttr("o", DjNotice.dao.findById(id));
		}else{
			DjNotice o = new DjNotice();
    		String userId = ShiroKit.getUserId();//用户主键
    		SysUser user = SysUser.dao.getById(userId);//用户对象
    		SysOrg org = SysOrg.dao.getById(user.getOrgid());//单位对象
			o.setId(UuidUtil.getUUID());
    		o.setUserId(userId);
    		o.setSenderName(user.getName());
    		o.setOrgId(org.getId());
    		o.setSenderOrgName(org.getName());
    		setAttr("o",o);
		}
		renderIframe("edit.html");
	}
	/***
	 * 保存
	 */
	public void save(){
		service.save(getModel(DjNotice.class));
		renderSuccess();
	}
	/***
	 * 删除
	 */
	public void delete(){
		String ids = getPara("ids");
		service.deleteNotice(ids);
		renderSuccess();
	}
	
	
	/***
	 * 发布
	 */
	public void publish(){
		service.publish(getPara("id"));
		renderSuccess();
	}
	
	/***
	 * 取回
	 */
	public void callBack(){
		service.callBack(getPara("id"));
		renderSuccess();
	}
	
	/*****************我收到的公告*********************************/
	public void myReceiveNoticePage(){

		renderIframe("myReceiveNotice.html");
	}
	public void myReceiveNotice(){
		String curr = getPara("pageNumber");
    	String pageSize = getPara("pageSize");
		String userid = ShiroKit.getUserId();
		Page<DjNotice> page = service.getMyNoticePage(Integer.valueOf(curr),Integer.valueOf(pageSize),userid);
		renderPage(page.getList(),"" ,page.getTotalRow());
	}
	/*****************管理通知公告结束*********************************/
	
	/***
	 * 首页查看通知公告
	 */
	public void viewNotice(){
		List<Record> list = service.findUserNotice(ShiroKit.getUserId(), getPara("id"));
		if(list != null && list.size() > 0) {
			setAttr("if_sign", list.get(0).getObject("if_sign"));
		}else {
			setAttr("if_sign", "0");
		}
		setAttr("notice", DjNotice.dao.findById(getPara("id")));
		renderIframe("view.html");
	}
	/***
	 * 签收公告
	 */
	public void sign(){
		service.sign(ShiroKit.getUserId(), getPara("id"));
		renderSuccess("签收成功");
	}
}
