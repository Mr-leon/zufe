package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcPartyStudent;
@SuppressWarnings("serial")
public class EcPartyStudent extends BaseEcPartyStudent<EcPartyStudent> {
	public static final EcPartyStudent dao = new EcPartyStudent();
	public static final String tableName = "ec_party_student";
	
	/***
	 * query by id
	 */
	public EcPartyStudent getById(String id){
		return EcPartyStudent.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcPartyStudent o = EcPartyStudent.dao.getById(id);
    		o.delete();
    	}
	}
	
}