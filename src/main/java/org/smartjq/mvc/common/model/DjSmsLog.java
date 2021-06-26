package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseDjSmsLog;
@SuppressWarnings("serial")
public class DjSmsLog extends BaseDjSmsLog<DjSmsLog> {
	public static final DjSmsLog dao = new DjSmsLog();
	public static final String tableName = "dj_sms_log";
	
	/***
	 * query by id
	 */
	public DjSmsLog getById(String id){
		return DjSmsLog.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		DjSmsLog o = DjSmsLog.dao.getById(id);
    		o.delete();
    	}
	}
	
}