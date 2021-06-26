package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcExamineAverage;

import java.util.List;

@SuppressWarnings("serial")
public class EcExamineAverage extends BaseEcExamineAverage<EcExamineAverage> {
	public static final EcExamineAverage dao = new EcExamineAverage();
	public static final String tableName = "ec_examine_average";
	
	/***
	 * query by id
	 */
	public EcExamineAverage getById(String id){
		return EcExamineAverage.dao.findById(id);
	}

	/***
	 * query by id
	 */
	public EcExamineAverage getCumulative(String eid,String mid){
		return EcExamineAverage.dao.findFirst(" select sum(fraction) cumulative from ec_examine_son where mid = '"+mid+"' and eid = '"+eid+"' ");
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		EcExamineAverage o = EcExamineAverage.dao.getById(id);
    		o.delete();
    	}
	}

	public EcExamineAverage getAverageByEMid(String eid,String mid){
		return  EcExamineAverage.dao.findFirst("select * from ec_examine_average where eid = '"+eid+"' and mid = '"+mid+"'");
	}

	public List<EcExamineAverage> getEdit(String eid,String mid){
		return  EcExamineAverage.dao.find(" select avg(a.fraction) fraction,a.frequency,b.cycle, " +
				" case when b.cycle=1 then substring(a.date_write, 1, 4) " +
				"  when b.cycle=2 and find_in_set(substring(a.date_write, 6, 2),'01,02,03,04,05,06') then 'b1' " +
				"  when b.cycle=2 and find_in_set(substring(a.date_write, 6, 2),'07,08,09,10,11,12') then 'b2' " +
				"  when b.cycle=3 and find_in_set(substring(a.date_write, 6, 2),'01,02,03') then 'j1' " +
				"  when b.cycle=3 and find_in_set(substring(a.date_write, 6, 2),'04,05,06') then 'j2' " +
				"  when b.cycle=3 and find_in_set(substring(a.date_write, 6, 2),'07,08,09') then 'j3' " +
				"  when b.cycle=3 and find_in_set(substring(a.date_write, 6, 2),'10,11,12') then 'j4' " +
				"  when b.cycle=4 then substring(a.date_write, 6, 2) " +
				" else '0' " +
				" end s " +
				" from ec_examine_son a  " +
				" left join ec_examine b on a.eid=b.id " +
				" where a.mid='"+mid+"'  " +
				" and a.eid='"+eid+"' " +
				" and a.statue>0 " +
				" GROUP BY a.frequency,b.cycle,s " +
				" order by s,b.cycle ");
	}
	
}