package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseDjDangfeiView;
@SuppressWarnings("serial")
public class DjDangfeiView extends BaseDjDangfeiView<DjDangfeiView> {
	public static final DjDangfeiView dao = new DjDangfeiView();
	public static final String tableName = "dj_dangfei_view";
	
	/***
	 * query by id
	 */
	public DjDangfeiView getById(String id){
		return DjDangfeiView.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		DjDangfeiView o = DjDangfeiView.dao.getById(id);
    		o.delete();
    	}
	}
	
}