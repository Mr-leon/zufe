/**

 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.mvc.admin.sys.home;

import java.util.List;

import org.smartjq.mvc.admin.workflow.flowtask.FlowTaskService;
import org.smartjq.mvc.admin.sys.commonfunction.SysUserCommonFunctionService;
import org.smartjq.mvc.admin.sys.login.SessionUtil;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.model.*;
import org.smartjq.mvc.common.utils.Constants;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.plugin.shiro.ext.SimpleUser;

import com.jfinal.plugin.activerecord.Record;

//import org.smartjq.mvc.admin.apply.resget.OaResGetConstants;
//import org.smartjq.mvc.admin.bumph.BumphConstants;
//import org.smartjq.mvc.admin.login.SessionUtil;
//import org.smartjq.mvc.admin.notice.NoticeService;
//import org.smartjq.mvc.admin.workflow.WorkFlowService;

/***
 * 首页控制器
 */
public class HomeController extends BaseController {
//	static WorkFlowService workflowService = WorkFlowService.me;
	static FlowTaskService commonFlowService = FlowTaskService.me;
	static SysUserCommonFunctionService commonFunctionService = new SysUserCommonFunctionService();
    /***
     * 首页
     */
    public void getHomePage(){
		SimpleUser user = ShiroKit.getLoginUser();
    	//获取首页通知公告
//		setAttr("NoticeList",noticeService.getMyNotice(user.getId()));//获取首页通知公告
//		setAttr("LinksList",commonLinksService.getCommonLinks());//获取首页常用链接
		List<Record> functionList = commonFunctionService.getCommonFunctions(user.getId());
		setAttr("FunctionList", functionList);//获取首页常用链接
//		setAttr("activityList",activityService.getActivityList());//获取首页金彩活动列表
    	renderIframe("/WEB-INF/admin/home/homePage.html");
    }
	
	/***
	 * 登录成功获取首页
	 */
    public void index(){
    	SimpleUser user = ShiroKit.getLoginUser();
    	String username = user.getUsername();
    	SysUser u = SysUser.dao.getByUsername(username);
    	SessionUtil.setUsernameToSession(this.getRequest(), username);
		EcUserOrganization ecUserOrganization = EcUserOrganization.dao.getOidByUid(u.getId());
		setSessionAttr("oid",ecUserOrganization.getOid());
    	//加载个性化设置
	    String settingType = "tab";
    	SysCustomSetting setting = SysCustomSetting.dao.getCstmSettingByUsername(username);
    	if(setting==null){
		    setting = SysCustomSetting.dao.getDefaultCstmSetting();
    		setAttr("setting", setting);
    	}else{
		    settingType = setting.getIndexPageType();
    		setAttr("setting", setting);
    	}
    	this.getSession().setAttribute("setting",setting);//个性化设置放到sessiong里
    	List<SysUser> friends = SysFriend.dao.getUserFriend(u.getId());
//        setAttr("email_count", emailService.getUnreadEmailCount(u.getId()));
    	setAttr("friends", friends);//我的好友
    	setAttr("user", u);//当前用户
    	setAttr("userName", user.getName());//我的姓名
    	setAttr("userEmail", user.getEmail());//我的邮箱
    	List<SysMenu> mlist;
    	if(user.getUsername().equals(Constants.SUUUUUUUUUUUUUPER_USER_NAME)){//特殊入口
    		mlist=SysMenu.dao.getAllMenu();
    	}else{
    		//查询所有有权限的菜单
        	mlist = SysRole.dao.getRoleAuthByUserid(u.getId(), "1","#root");//规定只有四级菜单 在这里暂定为A,B,C,D
        	for(SysMenu a : mlist){
        		List<SysMenu> blist = SysRole.dao.getRoleAuthByUserid(u.getId(), "1",a.getId());//A下面的菜单
        		a.setChildren(blist);
        		for(SysMenu b : blist){
        			List<SysMenu> clist = SysRole.dao.getRoleAuthByUserid(u.getId(), "1",b.getId());//B下面的菜单
        			b.setChildren(clist);
        			for(SysMenu c : clist){
            			List<SysMenu> dlist = SysRole.dao.getRoleAuthByUserid(u.getId(), "1",c.getId());//B下面的菜单
            			c.setChildren(dlist);
            		}
        		}
        	}
    	}
    	setAttr("mlist", mlist);
    	if("single".equals(settingType)){
			render("/WEB-INF/admin/home/index_singlepage.html");
		}else{
			render("/WEB-INF/admin/home/index.html");
		}
    }


    public void getSingleCustomSettingPage(){
		render("/common/include/setting-single.html");
	}

    /***
     * 设定已办数据
     */
    public void setAttrHavedoneList(String username){
    	List<String> havedoneKeyList = commonFlowService.getHavedoneDefkeyList(ShiroKit.getUsername());
    	setAttr("havedoneKeyList", havedoneKeyList);
    }
    
    /***
     * 首页内容页
     */
    public void getMyHome(){
    	renderIframe("/WEB-INF/admin/home/myHome.html");
    }
    /**
     * 内容页
     * */
    public void content(){
    	renderIframe("/WEB-INF/admin/home/content.html");
    }
    /***
     * 获取消息中心最新消息
     */
    public void getSiteMessageTipPage(){
    	renderIframe("/WEB-INF/admin/home/siteMessageTip.html");
    }
    
}
