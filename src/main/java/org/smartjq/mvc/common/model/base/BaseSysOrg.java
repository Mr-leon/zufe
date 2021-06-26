package org.smartjq.mvc.common.model.base;

import java.util.List;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;
import org.smartjq.mvc.common.model.SysOrg;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseSysOrg<M extends BaseSysOrg<M>> extends Model<M> implements IBean {
	private List<SysOrg> children;
	
	public List<SysOrg> getChildren() {
		return children;
	}
	public void setChildren(List<SysOrg> children) {
		this.children = children;
	}
	public void setId(java.lang.String id) {
		set("id", id);
	}

	public java.lang.String getId() {
		return get("id");
	}

	public void setVersion(java.lang.Long version) {
		set("version", version);
	}

	public java.lang.Long getVersion() {
		return get("version");
	}

	public void setLevel(java.lang.Long level) {
		set("level", level);
	}

	public java.lang.Long getLevel() {
		return get("level");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return get("type");
	}

	public void setArea(java.lang.String area) {
		set("area", area);
	}

	public java.lang.String getArea() {
		return get("area");
	}

	public void setDescription(java.lang.String description) {
		set("description", description);
	}

	public java.lang.String getDescription() {
		return get("description");
	}

	public void setImage(java.lang.String image) {
		set("image", image);
	}

	public java.lang.String getImage() {
		return get("image");
	}

	public void setIsparent(java.lang.String isparent) {
		set("isparent", isparent);
	}

	public java.lang.String getIsparent() {
		return get("isparent");
	}

	public void setName(java.lang.String name) {
		set("name", name);
	}

	public java.lang.String getName() {
		return get("name");
	}

	public void setSort(java.lang.Long sort) {
		set("sort", sort);
	}

	public java.lang.Long getSort() {
		return get("sort");
	}

	public void setUrl(java.lang.String url) {
		set("url", url);
	}

	public java.lang.String getUrl() {
		return get("url");
	}

	public void setParentId(java.lang.String parentId) {
		set("parent_id", parentId);
	}

	public java.lang.String getParentId() {
		return get("parent_id");
	}

	public void setOperate(java.lang.String operate) {
		set("operate", operate);
	}

	public java.lang.String getOperate() {
		return get("operate");
	}
	
	public void setParentChildCompanyId(java.lang.String parent_child_company_id) {
		set("parent_child_company_id", parent_child_company_id);
	}

	public java.lang.String getParentChildCompanyId() {
		return get("parent_child_company_id");
	}

	public void setUnionType(java.lang.String unionType) {
		set("union_type", unionType);
	}
	
	public java.lang.String getUnionType() {
		return get("union_type");
	}

	public void setIndustry(java.lang.String industry) {
		set("industry", industry);
	}
	
	public java.lang.String getIndustry() {
		return get("industry");
	}

	public void setEnterpriseType(java.lang.String enterpriseType) {
		set("enterprise_type", enterpriseType);
	}
	
	public java.lang.String getEnterpriseType() {
		return get("enterprise_type");
	}

	public void setOrgNum(java.lang.String orgNum) {
		set("org_num", orgNum);
	}
	
	public java.lang.String getOrgNum() {
		return get("org_num");
	}

	public void setEnterpriseName(java.lang.String enterpriseName) {
		set("enterprise_name", enterpriseName);
	}
	
	public java.lang.String getEnterpriseName() {
		return get("enterprise_name");
	}

	public void setSocialCreditCode(java.lang.String socialCreditCode) {
		set("social_credit_code", socialCreditCode);
	}
	
	public java.lang.String getSocialCreditCode() {
		return get("social_credit_code");
	}

	public void setDbUnion(java.lang.String dbUnion) {
		set("db_union", dbUnion);
	}
	
	public java.lang.String getDbUnion() {
		return get("db_union");
	}

	public void setUnionStreet(java.lang.String unionStreet) {
		set("union_street", unionStreet);
	}
	
	public java.lang.String getUnionStreet() {
		return get("union_street");
	}

	public void setAddress(java.lang.String address) {
		set("address", address);
	}
	
	public java.lang.String getAddress() {
		return get("address");
	}

	public void setCreateDate(java.lang.String createDate) {
		set("create_date", createDate);
	}
	
	public java.lang.String getCreateDate() {
		return get("create_date");
	}

	public void setChangeDate(java.lang.String changeDate) {
		set("change_date", changeDate);
	}
	
	public java.lang.String getChangeDate() {
		return get("change_date");
	}

	public void setPrincipal(java.lang.String principal) {
		set("principal", principal);
	}
	
	public java.lang.String getPrincipal() {
		return get("principal");
	}

	public void setContactNumber(java.lang.String contactNumber) {
		set("contact_number", contactNumber);
	}
	
	public java.lang.String getContactNumber() {
		return get("contact_number");
	}

	public void setStaffNum(java.lang.Integer staffNum) {
		set("staff_num", staffNum);
	}
	
	public java.lang.Integer getStaffNum() {
		return get("staff_num");
	}

	public void setMembership(java.lang.Integer membership) {
		set("membership", membership);
	}
	
	public java.lang.Integer getMembership() {
		return get("membership");
	}
}