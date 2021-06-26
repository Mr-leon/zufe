package org.smartjq.mvc.admin.examinemain;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.EcExamineMain;
import org.smartjq.mvc.common.model.EcOrganization;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class EcExamineMainService {
    public static final EcExamineMainService me = new EcExamineMainService();
    public static final String TABLE_NAME = EcExamineMain.tableName;

    /***
     * query by id
     */
    public EcExamineMain getById(String id) {
        return EcExamineMain.dao.findById(id);
    }


    /***
     * get first page
     */
    public Page<Record> getFirstPage(int pnum, int psize, String year) {
        String userId = ShiroKit.getUserId();
        String sql = " FROM ";
        sql += "( select 0 f,a.id mid,a.oid oid,b.id iid,b.name iname,0 oid_son,d.name oname   ";
        sql += " from ec_examine_main a ";
        sql += " left join ec_indicatrix b on a.iid=b.id ";
        sql += " left join ec_user_organization c on a.oid =c.oid ";
        sql += " left join ec_organization d on a.oid =d.id  ";
        sql += " where a.year='" + year + "'  and c.uid='" + userId + "' ";
        sql += " UNION ";
        sql += " SELECT 1 f,a.id mid,a.oid oid,b.id iid,b.name iname,c.id oid_son,c.name oname   ";
        sql += " from ec_examine_main a ";
        sql += " left join ec_indicatrix b on a.iid=b.id ";
        sql += " left join ec_organization c on a.oid =c.fid ";
        sql += " left join ec_user_organization d on c.id =d.oid ";
        sql += " where a.year='" + year + "'  and d.uid='" + userId + "' ";
        sql += " UNION ";
        sql += " select 2 f, a.id mid,a.oid oid,b.id iid,b.name iname ,0 oid_son,c.name oname  ";
        sql += " from ec_user_organization uo ";
        sql += " left join ec_organization c on uo.oid =c.fid  ";
        sql += " LEFT JOIN ec_examine_main a on c.id = a.oid";
        sql += " left join ec_indicatrix b on a.iid=b.id  ";
        sql += " where uo.oid = '"+ShiroKit.getUserOrgId()+"' and a.year='2021' ";
        sql += " ) a";
        return Db.paginate(pnum, psize, " SELECT f,mid,oid,iid,iname,oid_son,oname   ", sql);
    }

    /***
     * get page
     */
    public Page<Record> getPage(int pnum, int psize, String iid) {
        Calendar calendar = new GregorianCalendar();
        int yearInt = calendar.get(Calendar.YEAR);
        String sql = " from ";
        sql += " (select count(*) c,p.id,p.name from ";
        sql += " (SELECT 1 a,id,name from ec_organization where fid='1' ";
        sql += " UNION";
        sql += " SELECT 2 a,b.id,b.name from ec_examine_main a ";
        sql += " left join ec_organization b on a.oid=b.id ";
        sql += " where a.year='" + yearInt + "' and a.iid='" + iid + "') p ";
        sql += " group by p.id,p.name ) q ";
        sql += " where q.c=1 ";
        return Db.paginate(pnum, psize, " select id,name ", sql);
    }

}