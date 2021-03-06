package org.smartjq.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSchJob<M extends BaseSchJob<M>> extends Model<M> implements IBean {

	public void setID(String ID) {
		set("ID", ID);
	}
	
	public String getID() {
		return getStr("ID");
	}

	public void setJobName(String jobName) {
		set("JOB_NAME", jobName);
	}
	
	public String getJobName() {
		return getStr("JOB_NAME");
	}

	public void setJobClass(String jobClass) {
		set("JOB_CLASS", jobClass);
	}
	
	public String getJobClass() {
		return getStr("JOB_CLASS");
	}

	public void setJobMethod(String jobMethod) {
		set("JOB_METHOD", jobMethod);
	}
	
	public String getJobMethod() {
		return getStr("JOB_METHOD");
	}

	public void setTYPE(String TYPE) {
		set("TYPE", TYPE);
	}
	
	public String getTYPE() {
		return getStr("TYPE");
	}

	public void setCreateTime(java.util.Date createTime) {
		set("CREATE_TIME", createTime);
	}
	
	public java.util.Date getCreateTime() {
		return get("CREATE_TIME");
	}

}
