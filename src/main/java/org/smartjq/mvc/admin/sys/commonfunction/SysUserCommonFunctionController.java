package org.smartjq.mvc.admin.sys.commonfunction;

import com.jfinal.kit.StrKit;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.model.SysUserCommonFunction;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.plugin.shiro.ShiroKit;



public class SysUserCommonFunctionController extends BaseController {
	public static final SysUserCommonFunctionService service = SysUserCommonFunctionService.me;
    /***
     * save data
     */
    public void save(){
    	SysUserCommonFunction o = getModel(SysUserCommonFunction.class);
    	if(StrKit.notBlank(o.getId())){
    		o.update();
    	}else{
    		o.setId(UuidUtil.getUUID());
    		o.setCreateTime(DateUtil.getCurrentTime());
    		o.save();
    	}
    	renderSuccess();
    }
    
    /***
     * del
     * @throws Exception
     */
    public void delete() throws Exception{
		String id = getPara("ids");
		service.deleteById(id, ShiroKit.getUserId());
    	renderSuccess("删除成功!");
    }
}