package org.smartjq.mvc.admin.examine;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.EcExamine;
import org.smartjq.mvc.common.model.EcIndicatrix;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.utils.DateUtil;

public class EcExamineService {
    public static final EcExamineService me = new EcExamineService();
    public static final String TABLE_NAME = EcExamine.tableName;

    /***
     * query by id
     */
    public EcExamine getById(String id) {
        return EcExamine.dao.findById(id);
    }

    /***
     * get page
     */
    public Page<Record> getPage(int pnum, int psize, String iid) {
        String sql = " from " + TABLE_NAME + " o ";
        sql += " LEFT JOIN ec_indicatrix AS i ON o.iid = i.id where 1=1";
        sql = sql + " and o.iid ='" + iid + "'";
        return Db.paginate(pnum, psize, " select i.name iname,o.* ", sql);
    }

    /***
     * get page
     */
    public Page<Record> getPageIndicatrixBranch(int pnum, int psize, String uid) {
        String sql = " from ec_examine_main a ";
        sql += " left join ec_user_organization b on a.oid = b.oid";
        sql += " left join ec_indicatrix c on a.iid = c.id";
        sql += " left join ec_examine_son d on a.id=d.mid";
        sql += " where b.uid='" + uid + "' and d.statue='0' group by a.id,c.name";
        return Db.paginate(pnum, psize, " select count(1) ecount,a.id,c.name ", sql);

    }

    /***
     * get page
     */
    public Page<Record> getPageOrgBranch(int pnum, int psize) {
        String sql = " from ec_examine_main a ";
        sql += " left join ec_user_organization b on a.oid = b.oid";
        sql += " left join ec_indicatrix c on a.iid = c.id";
        sql += " left join ec_examine_son d on a.id=d.mid";
        sql += " where  d.statue='1' group by a.id,c.name";
        return Db.paginate(pnum, psize, " select count(1) ecount,a.id,c.name ", sql);

    }

    public Page<Record> getPageExamineBranch(int pnum, int psize, String mid) {
        String sql = " from ec_examine_son a ";
        sql += " left join ec_examine b on a.eid = b.id";
        sql += " left join ec_organization c on a.oid=c.id";
        sql += " left join sys_user d on a.uid_write=d.id";
        sql += " where a.mid='" + mid + "' and a.statue='0' order by a.date_write ";
        return Db.paginate(pnum, psize, " select a.id,a.mid,substring(a.date_write,1,10) date_write ,b.name ename,b.id eid,b.mold,b.e_text,b.fraction,a.file_name,a.memo,b.target,c.name oname,d.name uname ", sql);

    }

    public Page<Record> getPageOrgExamineBranch(int pnum, int psize, String mid) {
        String sql = " from ec_examine_son a ";
        sql += " left join ec_examine b on a.eid = b.id";
        sql += " left join ec_organization c on a.oid=c.id";
        sql += " left join sys_user d on a.uid_write=d.id";
        sql += " left join sys_user e on a.uid_examine = e.id ";
        sql += " where a.mid='" + mid + "' and a.statue='1' order by a.date_write ";
        return Db.paginate(pnum, psize, " select a.id,a.mid,substring(a.date_write,1,10) date_write ,b.name ename,b.id eid,b.mold,b.e_text,b.fraction,a.file_name,a.memo,b.target,c.name oname,d.name uname,e.`name` euname,substring(a.date_examine,1,10) date_examine ", sql);

    }

    /***
     * del
     * @param ids
     */
    @Before(Tx.class)
    public void deleteByIds(String ids) {
        String idarr[] = ids.split(",");
        for (String id : idarr) {
            EcExamine o = me.getById(id);
            o.delete();
        }
    }

    public Page<Record> getARPage(Integer pnum, Integer psize, String year) {
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
        sql += " where uo.oid = '" + ShiroKit.getUserOrgId() + "' and a.year='2021' ";
        sql += " ) a";
        return Db.paginate(pnum, psize, " SELECT f,mid,oid,iid,iname,oid_son,oname   ", sql);
    }

    public Page<Record> getARFPage(Integer pnum, Integer psize, String mid, String iid, String oid) {
        String select = " select m.id,m.name,m.cycle,m.mold,m.e_frequency,IFNULL(n.s_frequency,0) s_frequency ";
        String sql = "from (select  " +
                " e.id,e.name,e.cycle,e.mold, " +
                " (case  " +
                "  when e.cycle='2' and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then e.frequency " +
                "  when e.cycle='3' and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then e.frequency " +
                "  when e.cycle='3' and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then e.frequency * 2 " +
                "  when e.cycle='3' and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then e.frequency * 3 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='02' then e.frequency  " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='03' then e.frequency * 2 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='04' then e.frequency * 3 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='05' then e.frequency * 4 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='06' then e.frequency * 5 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='07' then e.frequency * 6 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='08' then e.frequency * 7 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='09' then e.frequency * 8 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='10' then e.frequency * 9 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='11' then e.frequency * 10 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='12' then e.frequency * 11 " +
                "  else 0 " +
                " end) e_frequency " +
                "from ec_indicatrix i  " +
                "left join ec_examine e on e.iid=i.id " +
                "where i.id = '"+iid+"' and e.target='0' and e.cycle in ('2','3','4')) m " +
                "left join ( " +
                "select  " +
                " s.eid,count(1) s_frequency " +
                "from ec_examine_son s " +
                "left join ec_examine e on s.eid=e.id " +
                "where s.mid='"+mid+"'  " +
                " and s.oid='"+ShiroKit.getUserOrgId()+"' " +
                " and ((e.cycle='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0) " +
                " or (e.cycle='3' and find_in_set(substring(s.date_write,6,2),'01,02,02')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0) " +
                " or (e.cycle='3' and find_in_set(substring(s.date_write,6,2),'01,02,02,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0) " +
                " or (e.cycle='3' and find_in_set(substring(s.date_write,6,2),'01,02,02,04,05,06,07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0) " +
                " or (e.cycle='4' and substring(SYSDATE(),6,2)='01' and substring(SYSDATE(),6,2)='02') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02')>0 and substring(SYSDATE(),6,2)='03') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and substring(SYSDATE(),6,2)='04') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04')>0 and substring(SYSDATE(),6,2)='05') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05')>0 and substring(SYSDATE(),6,2)='06') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07')>0 and substring(SYSDATE(),6,2)='07') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08')>0 and substring(SYSDATE(),6,2)='08') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09')>0 and substring(SYSDATE(),6,2)='09') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09,10')>0 and substring(SYSDATE(),6,2)='10') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09,10,11')>0 and substring(SYSDATE(),6,2)='11') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09,10,11,12')>0 and substring(SYSDATE(),6,2)='12') " +
                " ) " +
                "group by s.eid) n on m.id=n.eid " +
                "where m.e_frequency > IFNULL(n.s_frequency,0)";
        return Db.paginate(pnum, psize, select, sql);
    }

    public Page<Record> getARDPage1(Integer pnum, Integer psize, String mid, String iid, String oid) {
        String select = " select m.id,m.name,m.cycle,m.mold,m.e_frequency,IFNULL(n.s_frequency,0) s_frequency ";
        String sql = " from (select " +
                " e.id,e.name,e.cycle,e.mold, " +
                " (case  " +
                "  when e.cycle='2' and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then e.frequency " +
                "  when e.cycle='3' and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then e.frequency " +
                "  when e.cycle='3' and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then e.frequency * 2 " +
                "  when e.cycle='3' and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then e.frequency * 3 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='02' then e.frequency  " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='03' then e.frequency * 2 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='04' then e.frequency * 3 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='05' then e.frequency * 4 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='06' then e.frequency * 5 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='07' then e.frequency * 6 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='08' then e.frequency * 7 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='09' then e.frequency * 8 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='10' then e.frequency * 9 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='11' then e.frequency * 10 " +
                "  when e.cycle='4' and substring(SYSDATE(),6,2)='12' then e.frequency * 11 " +
                "  else 0 " +
                " end) e_frequency " +
                "from ec_indicatrix i  " +
                "left join ec_examine e on e.iid=i.id " +
                "where i.id = '"+iid+"' and e.target='1' and e.cycle in ('2','3','4')) m " +
                "left join ( " +
                "select  " +
                " s.eid,count(1) s_frequency " +
                "from ec_examine_son s " +
                "left join ec_examine e on s.eid=e.id " +
                "where s.mid='"+mid+"'  " +
                " and s.oid='"+ShiroKit.getUserOrgId()+"' " +
                " and ((e.cycle='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0) " +
                " or (e.cycle='3' and find_in_set(substring(s.date_write,6,2),'01,02,02')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0) " +
                " or (e.cycle='3' and find_in_set(substring(s.date_write,6,2),'01,02,02,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0) " +
                " or (e.cycle='3' and find_in_set(substring(s.date_write,6,2),'01,02,02,04,05,06,07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0) " +
                " or (e.cycle='4' and substring(SYSDATE(),6,2)='01' and substring(SYSDATE(),6,2)='02') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02')>0 and substring(SYSDATE(),6,2)='03') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and substring(SYSDATE(),6,2)='04') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04')>0 and substring(SYSDATE(),6,2)='05') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05')>0 and substring(SYSDATE(),6,2)='06') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07')>0 and substring(SYSDATE(),6,2)='07') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08')>0 and substring(SYSDATE(),6,2)='08') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09')>0 and substring(SYSDATE(),6,2)='09') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09,10')>0 and substring(SYSDATE(),6,2)='10') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09,10,11')>0 and substring(SYSDATE(),6,2)='11') " +
                " or (e.cycle='4' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06,07,08,09,10,11,12')>0 and substring(SYSDATE(),6,2)='12') " +
                " ) " +
                "group by s.eid) n on m.id=n.eid " +
                "where m.e_frequency > IFNULL(n.s_frequency,0)";
        return Db.paginate(pnum, psize, select, sql);
    }
}