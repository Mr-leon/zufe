
package org.smartjq.mvc.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		//系统设置的
		arp.addMapping("sys_user", "id", SysUser.class);//用户
		arp.addMapping("sys_menu", "id", SysMenu.class);//菜单
		arp.addMapping("sys_role", "id", SysRole.class);//角色
		arp.addMapping("sys_role_menu", "id", SysRoleMenu.class);//角色对应功能权限
		arp.addMapping("sys_role_user", "id", SysRoleUser.class);//用户角色
		arp.addMapping("sys_role_org", "id", SysRoleOrg.class);//用户组织结构-数据权限
		arp.addMapping("sys_org", "id", SysOrg.class);//组织结构
		arp.addMapping("sys_friend", "id", SysFriend.class);//用户好友
		arp.addMapping("sys_custom_setting", "id", SysCustomSetting.class);//自定义设置
		arp.addMapping("sys_point", "id", SysPoint.class);//积分
		arp.addMapping("sys_point_user", "id", SysPointUser.class);//用户积分
		arp.addMapping("sys_attachment", "id", SysAttachment.class);//系统附件
		arp.addMapping("sys_mobile_message", "id", SysMobileMessage.class);//系统短信模块
		arp.addMapping("sys_data_auth_rule", "id", SysDataAuth.class);//数据权限配置信息表
		arp.addMapping("sys_log", "id", SysLog.class);//系统日志表
		arp.addMapping("sys_dct", "id", SysDct.class);//系统字典表
		arp.addMapping("sys_dct_group", "id", SysDctGroup.class);//系统字典分组表
		arp.addMapping("SCH_JOB_EXECUTE", "ID", SchJobExecute.class);//定时任务执行管理
		arp.addMapping("SCH_JOB", "ID", SchJob.class);//定时任务字典管理
		//内容管理的
		//即时通讯的
		arp.addMapping("chat_history", "id", ChatHistory.class);//群聊
		arp.addMapping("act_re_model", "ID_", ActReModel.class);//流程模型
		arp.addMapping("act_re_procdef", "ID_", ActReProcdef.class);
		arp.addMapping("v_tasklist", "TASKID", VTasklist.class);//任务--视图
		
		//短信通知
		arp.addMapping("dj_sms_log", "id", DjSmsLog.class);
		
		//用户常用功能
		arp.addMapping("sys_user_common_function", "id", SysUserCommonFunction.class);

		arp.addMapping("ec_indicatrix", "id", EcIndicatrix.class);
		arp.addMapping("ec_examine", "id", EcExamine.class);
		arp.addMapping("ec_organization", "id", EcOrganization.class);
		arp.addMapping("ec_examine_main", "id", EcExamineMain.class);
		arp.addMapping("ec_party_student", "id", EcPartyStudent.class);
		arp.addMapping("ec_party_teacher", "id", EcPartyTeacher.class);
		arp.addMapping("ec_examine_son", "id", EcExamineSon.class);
		arp.addMapping("ec_user_organization", "uid", EcUserOrganization.class);
		arp.addMapping("ec_examine_average", "id", EcExamineAverage.class);
	}
}

