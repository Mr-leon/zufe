package org.smartjq.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseDjOrgRelation<M extends BaseDjOrgRelation<M>> extends Model<M> implements IBean {

	public void setId(java.lang.String id) {
		set("id", id);
	}
	
	public java.lang.String getId() {
		return getStr("id");
	}

	public void setTitle(java.lang.String title) {
		set("title", title);
	}
	
	public java.lang.String getTitle() {
		return getStr("title");
	}

	public void setPubDate(java.lang.String pubDate) {
		set("pub_date", pubDate);
	}
	
	public java.lang.String getPubDate() {
		return getStr("pub_date");
	}

	public void setImg(java.lang.String img) {
		set("img", img);
	}
	
	public java.lang.String getImg() {
		return getStr("img");
	}

	public void setContent(java.lang.String content) {
		set("content", content);
	}
	
	public java.lang.String getContent() {
		return getStr("content");
	}

	public void setIfDel(java.lang.String ifDel) {
		set("if_del", ifDel);
	}
	
	public java.lang.String getIfDel() {
		return getStr("if_del");
	}

	public void setDelTime(java.lang.String delTime) {
		set("del_time", delTime);
	}
	
	public java.lang.String getDelTime() {
		return getStr("del_time");
	}

	public void setDelUser(java.lang.String delUser) {
		set("del_user", delUser);
	}
	
	public java.lang.String getDelUser() {
		return getStr("del_user");
	}

	public void setPubStatus(java.lang.String pubStatus) {
		set("pub_status", pubStatus);
	}
	
	public java.lang.String getPubStatus() {
		return getStr("pub_status");
	}

	public void setPubTime(java.lang.String pubTime) {
		set("pub_time", pubTime);
	}
	
	public java.lang.String getPubTime() {
		return getStr("pub_time");
	}

	public void setCreateTime(java.lang.String createTime) {
		set("create_time", createTime);
	}
	
	public java.lang.String getCreateTime() {
		return getStr("create_time");
	}

	public void setCreateUser(java.lang.String createUser) {
		set("create_user", createUser);
	}
	
	public java.lang.String getCreateUser() {
		return getStr("create_user");
	}

	public void setViewCount(java.lang.Integer viewCount) {
		set("view_count", viewCount);
	}
	
	public java.lang.Integer getViewCount() {
		return getInt("view_count");
	}

}
