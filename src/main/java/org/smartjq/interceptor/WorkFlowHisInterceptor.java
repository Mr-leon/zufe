package org.smartjq.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.kit.StrKit;
import org.smartjq.mvc.admin.workflow.WorkFlowService;

public class WorkFlowHisInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
    	inv.invoke();
        String procInsId = inv.getController().getAttr("procInsId","");
        if(StrKit.notBlank(procInsId)){
        	inv.getController().setAttr("hislist",WorkFlowService.me.getHisTaskList(procInsId));
        	inv.getController().setAttr("procInsId",procInsId);
        }
    }
}
