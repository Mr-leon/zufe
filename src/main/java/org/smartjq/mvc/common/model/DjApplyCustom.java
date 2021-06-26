package org.smartjq.mvc.common.model;

import org.smartjq.mvc.common.model.base.BaseDjApplyCustom;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

@SuppressWarnings("serial")
public class DjApplyCustom extends BaseDjApplyCustom<DjApplyCustom> {
	public static final DjApplyCustom dao = new DjApplyCustom();
	public static final String tableName = "dj_apply_custom";
	
	/***
	 * 根据主键查询
	 */
	public DjApplyCustom getById(String id){
		return DjApplyCustom.dao.findById(id);
	}
	
	/***
	 * 获取分页
	 */
	public Page<Record> getPage(int pnum,int psize){
		String sql  = " from "+tableName+" o ";
		return Db.paginate(pnum, psize, " select * ", sql);
	}
	
	/***
	 * 删除
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		DjApplyCustom o = DjApplyCustom.dao.getById(id);
    		o.delete();
    	}
	}
	
}