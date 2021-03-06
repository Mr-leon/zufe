package org.smartjq.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseEcAuxiliary<M extends BaseEcAuxiliary<M>> extends Model<M> implements IBean {

	public void setId(String id) {
		set("id", id);
	}
	
	public String getId() {
		return getStr("id");
	}

	public void setAuxiliary(String auxiliary) {
		set("auxiliary", auxiliary);
	}
	
	public String getAuxiliary() {
		return getStr("auxiliary");
	}

	public void setType(String type) {
		set("type", type);
	}
	
	public String getType() {
		return getStr("type");
	}

}
