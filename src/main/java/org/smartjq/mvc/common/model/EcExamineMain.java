package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcExamineMain;
@SuppressWarnings("serial")
public class EcExamineMain extends BaseEcExamineMain<EcExamineMain> {
	public static final EcExamineMain dao = new EcExamineMain();
	public static final String tableName = "ec_examine_main";
	
	/***
	 * query by id
	 */
	public EcExamineMain getById(String id){
		return EcExamineMain.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcExamineMain o = EcExamineMain.dao.getById(id);
    		o.delete();
    	}
	}
	
}