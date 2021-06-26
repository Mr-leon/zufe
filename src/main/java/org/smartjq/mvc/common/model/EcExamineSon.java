package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcExamineSon;
@SuppressWarnings("serial")
public class EcExamineSon extends BaseEcExamineSon<EcExamineSon> {
	public static final EcExamineSon dao = new EcExamineSon();
	public static final String tableName = "ec_examine_son";
	
	/***
	 * query by id
	 */
	public EcExamineSon getById(String id){
		return EcExamineSon.dao.findById(id);
	}

	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcExamineSon o = EcExamineSon.dao.getById(id);
    		o.delete();
    	}
	}

	public EcExamineSon getSonByEMid(String eid,String mid){
		return EcExamineSon.dao.findFirst("select * from ec_examine_son where isend = '1' and eid = '"+eid+"' and mid = '"+mid+"'");
	}
	
}