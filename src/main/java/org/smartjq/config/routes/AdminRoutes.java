package org.smartjq.config.routes;

import com.jfinal.config.Routes;
import org.smartjq.mvc.admin.auxiliary.EcAuxiliaryController;
import org.smartjq.mvc.admin.common.CommonBusinessController;
import org.smartjq.mvc.admin.examine.EcExamineController;
import org.smartjq.mvc.admin.examineaverage.EcExamineAverageController;
import org.smartjq.mvc.admin.examinemain.EcExamineMainController;
import org.smartjq.mvc.admin.examineson.EcExamineSonController;
import org.smartjq.mvc.admin.indicatrix.EcIndicatrixController;
import org.smartjq.mvc.admin.notice.NoticeController;
import org.smartjq.mvc.admin.noticeuser.DjNoticeUserController;
import org.smartjq.mvc.admin.org.DjOrgController;
import org.smartjq.mvc.admin.organization.EcOrganizationController;
import org.smartjq.mvc.admin.partystudent.EcPartyStudentController;
import org.smartjq.mvc.admin.partyteacher.EcPartyTeacherController;
import org.smartjq.mvc.admin.smslog.DjSmsLogController;
import org.smartjq.mvc.admin.sys.commonfunction.SysUserCommonFunctionController;
import org.smartjq.mvc.admin.workflow.WorkFlowController;
import org.smartjq.mvc.admin.workflow.flowimg.FlowImgController;
import org.smartjq.mvc.admin.workflow.flowtask.FlowTaskController;
import org.smartjq.mvc.admin.workflow.model.ModelController;
import org.smartjq.mvc.admin.workflow.model.ModelEditorJsonRestResource;
import org.smartjq.mvc.admin.workflow.model.ModelSaveRestResource;
import org.smartjq.mvc.admin.workflow.rest.ProcessDefinitionDiagramLayoutResource;
import org.smartjq.mvc.admin.workflow.rest.ProcessInstanceDiagramLayoutResource;
import org.smartjq.mvc.admin.workflow.rest.ProcessInstanceHighlightsResource;
import org.smartjq.mvc.admin.workflow.rest.StencilsetRestResource;

public class AdminRoutes extends Routes{

	@Override
	public void config() {
		setBaseViewPath("/WEB-INF/admin");
		//通用业务控制器
		add("/admin/common/business",CommonBusinessController.class);
		//流程图
		add("/admin/model",ModelController.class,"/workflow/model");//工作流-模型
		add("/admin/workflow",WorkFlowController.class,"/workflow");//工作流

		//短信通知
		add("/admin/smslog",DjSmsLogController.class,"/smslog");//


		//在线办公---各种各样的申请申请
		add("/admin/workflow/flowtask",FlowTaskController.class,"/workflow/flowtask");//处理通用任务

		//流程在线编辑器和流程跟踪所用路由
		add("/admin/process-instance/highlights",ProcessInstanceHighlightsResource.class);//modeler
		add("/admin/process-instance/diagram-layout",ProcessInstanceDiagramLayoutResource.class);//modeler
		add("/admin/process-definition/diagram-layout",ProcessDefinitionDiagramLayoutResource.class);//modeler
		add("/admin/workflow/flowimg", FlowImgController.class);//流程图片展示
		add("/admin/modelEditor/save",ModelSaveRestResource.class);
		add("/admin/modelEditor/json",ModelEditorJsonRestResource.class);
		add("/admin/editor/stencilset",StencilsetRestResource.class);

		//用户常用功能
		add("/admin/commonFunction",SysUserCommonFunctionController.class,"/commonFunction");//
		add("/admin/notice",NoticeController.class,"/notice");//通知公告
		add("/admin/noticeuser",DjNoticeUserController.class,"/noticeuser");//通知公告明细列表
		add("/admin/org",DjOrgController.class,"/org");//通知公告明细列表

		//考核项管理
		add("/admin/examine", EcExamineController.class,"/examine");

		//指标管理
		add("/admin/indicatrix", EcIndicatrixController.class,"/indicatrix");

		//指标管理
		add("/admin/organization", EcOrganizationController.class,"/organization");

		//考核项主表管理
		add("/admin/examinemain", EcExamineMainController.class,"/examinemain");

		//录入数据
		add("/admin/examineson", EcExamineSonController.class,"/examineson");

		//导入数据
		add("/admin/partyteacher", EcPartyTeacherController.class,"/partyteacher");

		//导入数据
		add("/admin/partystudent", EcPartyStudentController.class,"/partystudent");

		//
		add("/admin/examineaverage", EcExamineAverageController.class,"/examineaverage");

		add("/admin/auxiliary", EcAuxiliaryController.class,"/auxiliary");
	}

}
