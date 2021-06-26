package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.List;

import org.smartjq.mvc.common.model.base.BaseDjPartyMember;
@SuppressWarnings("serial")
public class DjPartyMember extends BaseDjPartyMember<DjPartyMember> {
	public static final DjPartyMember dao = new DjPartyMember();
	public static final String tableName = "dj_party_member";
	
	/***
	 * query by id
	 */
	public DjPartyMember getById(String id){
		return DjPartyMember.dao.findById(id);
	}
	
	/***
	 * del
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		DjPartyMember o = DjPartyMember.dao.getById(id);
    		o.delete();
    	}
	}

	public List<DjPartyMember> getUserListByOrgId(String orgid) {
		return DjPartyMember.dao.find("select * from dj_party_member where org_id='" + orgid + "'");//查询机构下所有人员;
	}
	
}