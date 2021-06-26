package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseSysUserCommonFunction;
@SuppressWarnings("serial")
public class SysUserCommonFunction extends BaseSysUserCommonFunction<SysUserCommonFunction> {
	public static final SysUserCommonFunction dao = new SysUserCommonFunction();
	public static final String tableName = "sys_user_common_function";
	
	/***
	 * query by id
	 */
	public SysUserCommonFunction getById(String id){
		return SysUserCommonFunction.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		SysUserCommonFunction o = SysUserCommonFunction.dao.getById(id);
    		o.delete();
    	}
	}
	
}