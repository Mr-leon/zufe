package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcUserOrganization;
/**
 * @author CYZ
 */
@SuppressWarnings("serial")
public class EcUserOrganization extends BaseEcUserOrganization<EcUserOrganization> {
	public static final EcUserOrganization dao = new EcUserOrganization();
	public static final String tableName = "ec_user_organization";
	
	/***
	 * oid by uid
	 */
	public EcUserOrganization getOidByUid(String uid){
		return EcUserOrganization.dao.findFirst("select uid,oid from ec_user_organization where uid ='"+uid+"'");
	}

	
}