package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseDjOrgRelation;
@SuppressWarnings("serial")
public class DjOrgRelation extends BaseDjOrgRelation<DjOrgRelation> {
	public static final DjOrgRelation dao = new DjOrgRelation();
	public static final String tableName = "dj_org_relation";
	
	/***
	 * query by id
	 */
	public DjOrgRelation getById(String id){
		return DjOrgRelation.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		DjOrgRelation o = DjOrgRelation.dao.getById(id);
    		o.delete();
    	}
	}
	
}