package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcPartyTeacher;
@SuppressWarnings("serial")
public class EcPartyTeacher extends BaseEcPartyTeacher<EcPartyTeacher> {
	public static final EcPartyTeacher dao = new EcPartyTeacher();
	public static final String tableName = "ec_party_teacher";
	
	/***
	 * query by id
	 */
	public EcPartyTeacher getById(String id){
		return EcPartyTeacher.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcPartyTeacher o = EcPartyTeacher.dao.getById(id);
    		o.delete();
    	}
	}
	
}