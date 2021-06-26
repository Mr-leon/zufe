package org.smartjq.config.routes;

import com.jfinal.config.Routes;
import org.smartjq.mvc.admin.sys.attachment.AttachmentController;
import org.smartjq.mvc.admin.sys.chat.ChatController;
import org.smartjq.mvc.admin.sys.cms.CmsController;
import org.smartjq.mvc.admin.sys.cstmsetting.CustomSettingController;
import org.smartjq.mvc.admin.sys.dataauth.SysDataAuthController;
import org.smartjq.mvc.admin.sys.dbbackup.DatabaseBackupController;
import org.smartjq.mvc.admin.sys.dct.SysDctController;
import org.smartjq.mvc.admin.sys.generator.GeneratorController;
import org.smartjq.mvc.admin.sys.home.HomeController;
import org.smartjq.mvc.admin.sys.log.SysLogController;
import org.smartjq.mvc.admin.sys.login.LoginController;
import org.smartjq.mvc.admin.sys.menu.MenuController;
import org.smartjq.mvc.admin.sys.org.OrgController;
import org.smartjq.mvc.admin.sys.role.RoleController;
import org.smartjq.mvc.admin.sys.schjob.SchJobController;
import org.smartjq.mvc.admin.sys.test.TestController;
import org.smartjq.mvc.admin.sys.tool.file.FileController;
import org.smartjq.mvc.admin.sys.uidemo.UIDemoController;
import org.smartjq.mvc.admin.sys.upload.UploadController;
import org.smartjq.mvc.admin.sys.user.UserController;
import org.smartjq.mvc.front.help.HelpController;

public class SysRoutes extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/WEB-INF/admin/sys");//设置根目录
		add("/front/help",HelpController.class);//帮助
		
		add("/admin/uidemo", UIDemoController.class);//UIdemo
		add("/admin/upload",UploadController.class);//文件上传
		add("/admin/home", HomeController.class);//首页
		add("/admin/login", LoginController.class);//登录
		//系统管理
		add("/admin/sys/customsetting",CustomSettingController.class);//个性化设置
		add("/admin/sys/org", OrgController.class,"/org");//组织结构
		add("/admin/sys/chat",ChatController.class,"/chat");//即时聊天
		add("/admin/sys/log",SysLogController.class,"/log");//系统日志
		add("/admin/sys/cms",CmsController.class,"/cms");//内容管理
		add("/admin/sys/user", UserController.class,"/user");//用户
		add("/admin/sys/menu", MenuController.class,"/menu");//菜单
		add("/admin/sys/role",RoleController.class,"/role");//角色
		add("/admin/sys/attachment",AttachmentController.class,"/attachment");//系统附件管理
		add("/admin/sys/dataauth",SysDataAuthController.class,"/dataauth");//数据权限控制器
		add("/admin/sys/dct",SysDctController.class,"/dct");//系统字典管理
		add("/admin/sys/schjob", SchJobController.class,"/schjob");//定时任务可视化配置
		//代码生成器
		add("/admin/sys/generator",GeneratorController.class,"/generator");//代码生成器
		//数据库备份
		add("/admin/sys/dbbackup",DatabaseBackupController.class,"");


		//客户端工具类
		add("/admin/sys/tool/file", FileController.class,"/tool/file");

		
		//***测试类
		add("/admin/sys/test", TestController.class,"/test");
	}

}
