package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcIndicatrix;
@SuppressWarnings("serial")
public class EcIndicatrix extends BaseEcIndicatrix<EcIndicatrix> {
	public static final EcIndicatrix dao = new EcIndicatrix();
	public static final String tableName = "ec_indicatrix";
	
	/***
	 * query by id
	 */
	public EcIndicatrix getById(String id){
		return EcIndicatrix.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcIndicatrix o = EcIndicatrix.dao.getById(id);
    		o.delete();
    	}
	}
	
}