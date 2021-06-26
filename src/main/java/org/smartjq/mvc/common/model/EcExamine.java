package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcExamine;

import java.util.List;

/**
 * @author CYZ
 */
@SuppressWarnings("serial")
public class EcExamine extends BaseEcExamine<EcExamine> {
	public static final EcExamine dao = new EcExamine();
	public static final String tableName = "ec_examine";
	
	/***
	 * query by id
	 */
	public EcExamine getById(String id){
		return EcExamine.dao.findById(id);
	}

//	public EcExamine getByEdit(String sid){
//		return EcExamine.dao.findFirst("SELECT b.id,c.fraction FROM ec_examine_son a left join ec_examine_average b on a.mid=b.mid and a.eid=b.eid left join ec_examine c on a.eid=c.id where a.id ='"+sid+"' and (CASE    WHEN c.cycle='1' and a.frequency>=b.frequency THEN 1 WHEN c.cycle='2' and a.frequency>=b.frequency and INSTR('01,02,03,04,05,06',substring(a.date_write,6,2)) and INSTR('01,02,03,04,05,06',substring(sysdate(),6,2)) THEN 1 " +
//				" WHEN c.cycle='2' and a.frequency>=b.frequency and INSTR('07,08,09,10,11,12',substring(a.date_write,6,2)) and INSTR('07,08,09,10,11,12',substring(sysdate(),6,2)) THEN 1 " +
//				"   WHEN c.cycle='3' and a.frequency>=b.frequency and INSTR('01,02,03',substring(a.date_write,6,2)) and INSTR('01,02,03',substring(sysdate(),6,2)) THEN 1 " +
//				"   WHEN c.cycle='3' and a.frequency>=b.frequency and INSTR('04,05,06',substring(a.date_write,6,2)) and INSTR('01,02,03',substring(sysdate(),6,2)) THEN 1 " +
//				"   WHEN c.cycle='3' and a.frequency>=b.frequency and INSTR('07,08,09',substring(a.date_write,6,2)) and INSTR('01,02,03',substring(sysdate(),6,2)) THEN 1 " +
//				"   WHEN c.cycle='3' and a.frequency>=b.frequency and INSTR('10,11,12',substring(a.date_write,6,2)) and INSTR('01,02,03',substring(sysdate(),6,2)) THEN 1 " +
//				"   WHEN c.cycle='4' and a.frequency>=b.frequency and substring(a.date_write,6,2) = substring(sysdate(),6,2) THEN 1 " +
//				" ELSE " +
//				"  0 " +
//				"END)=1; ");
//	}

	/***
	 * query by id
	 */
	public List<EcExamine> getByIId(String iid){
		return EcExamine.dao.find("SELECT * FROM `ec_examine` where iid = '"+iid+"'");
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcExamine o = EcExamine.dao.getById(id);
    		o.delete();
    	}
	}
	
}