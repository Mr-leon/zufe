/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.config;

import org.smartjq.config.routes.AMSRoutes;
import org.smartjq.config.routes.AdminRoutes;
import org.smartjq.config.routes.CRMRoutes;
import org.smartjq.config.routes.GrapRoutes;
import org.smartjq.config.routes.JcdbpRoutes;
import org.smartjq.config.routes.MobileRoutes;
import org.smartjq.config.routes.SysRoutes;
import org.smartjq.handler.GlobalHandler;
import org.smartjq.interceptor.ExceptionInterceptor;
import org.smartjq.interceptor.IfLoginInterceptor;
import org.smartjq.interceptor.LogInterceptor;
import org.smartjq.interceptor.WorkFlowHisInterceptor;
import org.smartjq.mvc.admin.common.DjConstants;
import org.smartjq.mvc.admin.workflow.WorkFlowUtil;
import org.smartjq.mvc.common.model._MappingKit;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.plugin.flowable.FlowablePlugin;
import org.smartjq.plugin.mail.MailPlugin;
import org.smartjq.plugin.quartz.QuartzPlugin;
import org.smartjq.plugin.shiro.ShiroInterceptor;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.plugin.shiro.ShiroPlugin;

import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.core.Const;
import com.jfinal.ext.handler.UrlSkipHandler;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;

public class MainConfig extends JFinalConfig {
	protected static final Log LOG = Log.getLog(MainConfig.class);
	public static Routes routes;
	public static Constants constants;
	public static String webUrl;
	public static String fileDownloadPath;
	public static String jqeptUrl;
	private static String secret;
	public static String uploadPath;
	

    /** 腾讯云服务器AppId.*/
    public static String cosAppId;

    /** 腾讯云SecretId.*/
    public static String cosSecretId;

    /** 腾讯云SecretKey.*/
    public static String cosSecretKey;

    /** 存储桶名称.*/
    public static String cosBucketName;

    /** 访问静态资源路径前缀.*/
    public static String cosDomainName;

    /** 腾讯云存储桶地区.*/
    public static String cosRegion;
    
	/**
	 * 配置JFinal常量
	 * 加载顺序！！！！！！！！！  第一
	 */
	@Override
	public void configConstant(Constants me) {
		MainConfig.constants = me;
		//读取数据库配置文件
		PropKit.use("config_dev.properties");
		//设置当前是否为开发模式
		me.setDevMode(PropKit.getBoolean("devMode"));
		//设置默认上传文件保存路径 getFile等使用
		me.setBaseUploadPath(PropKit.get("baseUploadPath"));
		//设置上传最大限制尺寸
		me.setMaxPostSize(200*Const.DEFAULT_MAX_POST_SIZE);
		// 开启对 jfinal web 项目组件 Controller、Interceptor、Validator 的注入
		me.setInjectDependency(true);
		// 开启对超类的注入。不开启时可以在超类中通过 Aop.get(...) 进行注入
		me.setInjectSuperClass(true);
		setWebUrl(PropKit.get("webUrl"));
		setJqeptUrl(PropKit.get("jqeptUrl"));
		setSecret(PropKit.get("secret"));
		setFileDownloadPath(PropKit.get("fileDownloadPath"));
		setUploadPath(PropKit.get("uploadPath"));
		

		MainConfig.cosAppId = PropKit.get("COS.AppId");
		MainConfig.cosSecretId = PropKit.get("COS.SecretId");
		MainConfig.cosSecretKey = PropKit.get("COS.SecretKey");
		MainConfig.cosBucketName = PropKit.get("COS.BucketName");
		MainConfig.cosDomainName = PropKit.get("COS.DomainName");
		MainConfig.cosRegion = PropKit.get("COS.Region");

		//获取beetl模版引擎
//		me.setRenderFactory(new BeetlRenderFactory());
		me.setError404View("/error/404.html");
		// 获取GroupTemplate ,可以设置共享变量等操作
//        @SuppressWarnings("unused")
//		GroupTemplate groupTemplate = BeetlRenderFactory.groupTemplate ;
	}

	/**
	 * 配置JFinal插件
	 * 数据库连接池
	 * ORM
	 * 缓存等插件
	 *
	 *
	 * 自定义插件
	 *
	 * 加载顺序！！！！    第三
	 */
	@Override
	public void configPlugin(Plugins me) {
		//配置数据库连接池插件
		DruidPlugin druidPlugin = new DruidPlugin(PropKit.get("jdbcUrl"), PropKit.get("user"), PropKit.get("password"));
		//orm映射 配置ActiveRecord插件
		ActiveRecordPlugin arp=new ActiveRecordPlugin(druidPlugin);
		arp.setShowSql(PropKit.getBoolean("devMode"));
//		arp.setContainerFactory(new CaseInsensitiveContainerFactory(true));//jfinal将sql全部转为小写配置。activiti相关的表字段为大写。如要开启该配置，需要修改，模型管理，流程管理list等页面的列表信息。
		arp.setDialect(new MysqlDialect());
		_MappingKit.mapping(arp);
		FlowablePlugin acitivitiPlugin = new FlowablePlugin();
		ShiroPlugin shiroPlugin = new ShiroPlugin(MainConfig.routes);
		shiroPlugin.setLoginUrl("/admin/login");//登录url：未验证成功跳转
		shiroPlugin.setSuccessUrl("/admin/index");//登录成功url：验证成功自动跳转
		shiroPlugin.setUnauthorizedUrl("/admin/login/needPermission");//授权url：未授权成功自动跳转
		QuartzPlugin quatrZPlugin = new QuartzPlugin();
		MailPlugin mailPlugin = new MailPlugin(PropKit.use("mail.properties").getProperties());
		//添加到插件列表中
		me.add(druidPlugin);//数据库连接池插件
		me.add(arp);//ARP插件
		me.add(acitivitiPlugin);//流程插件
		me.add(shiroPlugin);//权限插件
		me.add(mailPlugin);//邮件发送插件
		me.add(quatrZPlugin);//定时任务插件
		LOG.info("插件启动成功");
	}
	
	/**
	 * 配置全局拦截器
	 *
	 * 加载顺序！！！！    第五
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		me.add(new ShiroInterceptor());//shiro拦截器
		me.add(new IfLoginInterceptor());//判断是否登录拦截器
		me.add(new WorkFlowHisInterceptor());//流程历史拦截器
		me.add(new LogInterceptor());//通用日志处理拦截器
		me.add(new ExceptionInterceptor());//通用异常处理拦截器
	}
	
	/**
	 * 配置全局处理器
	 *
	 * 加载顺序！！！！    第六
	 */
	@Override
	public void configHandler(Handlers handler) {
		//log.info("configHandler 全局配置处理器，设置跳过哪些URL不处理");
		handler.add(new UrlSkipHandler("/|/admin/friendchat/.*|/ca/.*|/se/.*|.*.htm|.*.html|.*.js|.*.css|.*.json|.*.png|.*.gif|.*.jpg|.*.jpeg|.*.bmp|.*.ico|.*.exe|.*.txt|.*.zip|.*.rar|.*.7z|.*.tff|.*.woff|.*.ttf|.*.map|.*.xml|.*.woff2|.*.pdf", false));
		handler.add(new GlobalHandler());
	}
	
	
	
	/**
	 * 配置JFinal路由映射
	 * 加载顺序！！！！！！！！     第二
	 */
	@Override
	public void configRoute(Routes me) {
		MainConfig.routes = me;//shiro使用
		me.add(new AdminRoutes());//办公路由
		me.add(new CRMRoutes());//客户路由
		me.add(new AMSRoutes());//资产路由
		me.add(new SysRoutes());//系统管理路由
		me.add(new GrapRoutes());//爬取网站数据路由
		me.add(new MobileRoutes());//手机端管理路由
		me.add(new JcdbpRoutes());//金彩大比拼路由
	}
	
	/****
	 * 加载顺序！！！！    第四
	 */
	@Override
	public void configEngine(Engine me) {
		me.addSharedObject("shiro",new ShiroKit());//提供给模板能使用Shiro权限校验
		me.addSharedObject("OAConstants", new DjConstants());//提供给模板能引用后台常量类
		me.addSharedObject("WorkFlowUtil", new WorkFlowUtil());//提供给模板使用能流程工具类
		me.addSharedObject("UuidUtil", new UuidUtil());//提供给模板能生成UUID
		//业务通用的工具函数。
		me.addSharedFunction("/WEB-INF/admin/sys/template/commonTemplate.html");
		//业务通用的工具函数
		me.addSharedFunction("/WEB-INF/admin/sys/attachment/businessIncludeBtn.html");
		//通用流程相关函数
		me.addSharedFunction("/WEB-INF/admin/workflow/commonFlowModule.html");
	}
	
	/***
	 * 项目启动之后执行
	 */
	@Override
	public void onStart(){
//		GrapDbSourceService service = GrapDbSourceService.me;
//		List<GrapDbSource> allDbsource = GrapDbSource.dao.getAllDbsource();
//		for(GrapDbSource dbs:allDbsource){
//			try{
//				if(service.testConnect(dbs)){
//					System.out.println("*********数据源："+dbs.getName()+"连接成功。！");
//				}
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//		}
	}

	public static String getWebUrl() {
		return webUrl;
	}
	
	public static void setWebUrl(String webUrl) {
		MainConfig.webUrl = webUrl;
	}

	public static String getFileDownloadPath() {
		return fileDownloadPath;
	}

	public static void setFileDownloadPath(String fileDownloadPath) {
		MainConfig.fileDownloadPath = fileDownloadPath;
	}

	public static String getJqeptUrl() {
		return jqeptUrl;
	}

	public static void setJqeptUrl(String jqeptUrl) {
		MainConfig.jqeptUrl = jqeptUrl;
	}

	public static String getSecret() {
		return secret;
	}

	private void setSecret(String secret) {
		MainConfig.secret = secret;
	}

	public static String getUploadPath() {
		return uploadPath;
	}

	private void setUploadPath(String uploadPath) {
		MainConfig.uploadPath = uploadPath;
	}
}
