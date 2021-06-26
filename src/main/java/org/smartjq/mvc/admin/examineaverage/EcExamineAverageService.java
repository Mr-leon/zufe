package org.smartjq.mvc.admin.examineaverage;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.EcExamineAverage;
import org.smartjq.plugin.shiro.ShiroKit;
import org.smartjq.mvc.common.model.SysRoleOrg;
import org.smartjq.mvc.common.utils.DateUtil;

/**
 * @author CYZ
 */
public class EcExamineAverageService {
    public static final EcExamineAverageService me = new EcExamineAverageService();
    public static final String TABLE_NAME = EcExamineAverage.tableName;

    /***
     * query by id
     */
    public EcExamineAverage getById(String id) {
        return EcExamineAverage.dao.findById(id);
    }


    /***
     * get page
     */
    public Page<Record> getPage(Integer currentPage, Integer pageSize, String mid, String iid, String oid) {
        String select = "select o.id,o.name,o.mold,o.target,o.score,  (case when o.target ='0' then IFNULL(p.cumulative,0) else round(IFNULL(p.cumulative,0)/r.ocount,2) end ) cumulative, (case when o.target ='0' then IFNULL(q.fraction,0) else round(IFNULL(q.fraction,0)/r.ocount,2) end ) fraction,IFNULL(q.frequency,0) + 1 frequency, (case when o.frequency > IFNULL(q.frequency,0) then 1 else 0 end ) isadd ";
        String sql = "from (  ";
        sql += " select e.id,e.name,e.score,e.mold,e.sequence,e.target,e.frequency ";
        sql += " from ec_examine_main m ";
        sql += " left join ec_examine e on e.iid=m.iid ";
        sql += " where m.id = '" + mid + "' and m.iid='" + iid + "' and find_in_set('" + oid + "',e.oids) is NULL ";
        sql += " ) o  ";
        sql += " left join (select s.eid,SUM(s.fraction) cumulative from ec_examine_son s where s.mid='" + mid + "' group by s.eid) p on o.id=p.eid  ";
        sql += " left join (select x.id,x.frequency,sum(y.fraction) fraction ";
        sql += " from (select e.id,max(s.frequency) frequency from ec_examine_son s  ";
        sql += " left join ec_examine e on e.id=s.eid  ";
        sql += " where s.mid='" + mid + "' ";
        sql += " and (  ";
        sql += " case   ";
        sql += " when e.cycle='1' and substring(date_write,1,4)=substring(SYSDATE(),1,4) then 1  ";
        sql += " when e.cycle='2' and find_in_set(substring(date_write,6,2),'01,02,03,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03,04,05,06')>0 then 1  ";
        sql += " when e.cycle='2' and find_in_set(substring(date_write,6,2),'07,08,09,10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then 1  ";
        sql += " when e.cycle='3' and find_in_set(substring(date_write,6,2),'01,02,03')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03')>0 then 1  ";
        sql += " when e.cycle='3' and find_in_set(substring(date_write,6,2),'04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then 1  ";
        sql += " when e.cycle='3' and find_in_set(substring(date_write,6,2),'07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then 1  ";
        sql += " when e.cycle='3' and find_in_set(substring(date_write,6,2),'10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then 1  ";
        sql += " else 0 end ) = 1  ";
        sql += " group by e.id) x ";
        sql += " left join ( ";
        sql += " select s.eid,s.fraction,s.frequency from ec_examine_son s  ";
        sql += " where s.mid='" + mid + "' ) y on x.id=y.eid and x.frequency=y.frequency ";
        sql += " group by x.id,x.frequency) q on o.id=q.id ";
        sql += " left join (select count(1) ocount from ec_examine_main m left join ec_organization o on m.oid=o.fid where m.id = '" + mid + "') r on 1=1 ";
        sql += " order by o.sequence ";
        return Db.paginate(currentPage, pageSize, select, sql);
    }

    /***
     * get page
     */
    public Page<Record> getPage1(Integer currentPage, Integer pageSize, String mid, String iid, String oid) {
        String select = " select id,name,score,frequency,target,mold,cumulative,fraction,x_frequency ";
        String sql = " from (" +
                " select o.id,o.name,o.score,o.frequency, o.target,o.mold,IFNULL(p.cumulative,0) cumulative,IFNULL(q.fraction,0) fraction, 0 x_frequency " +
                "  from (select e.id,e.name,e.score,e.frequency,e.target,e.mold from ec_examine_main m left join ec_examine e on e.iid=m.iid " +
                " where m.id='"+mid+"' and e.target='0') o  " +
                " left join (select e.id,sum(s.fraction) cumulative from ec_examine_son s " +
                " left join ec_examine e on s.eid=e.id where s.mid='"+mid+"' and e.target='0' group by e.id) p on o.id=p.id " +
                " left join (select m.id,n.fraction from (select e.id,max(s.frequency) frequency    " +
                " from ec_examine_son s left join ec_examine e on s.eid=e.id where e.target='0' and s.mid='"+mid+"'  " +
                " and (case when s.date_write is NULL then 0 when e.cycle ='1' then 1      " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03,04,05,06')>0 then 1 " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then 1 " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then 1  " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then 1    " +
                " when e.cycle ='4' and substring(s.date_write,1,7) = substring(SYSDATE(),1,7) then 1 else 0 end )=1 group by e.id ) m " +
                "  left join (select e.id,s.fraction,s.frequency from ec_examine_son s left join ec_examine e on s.eid=e.id " +
                "  where e.target='0' and s.mid='"+mid+"' and (case when s.date_write is NULL then 0 when e.cycle ='1' then 1      " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03,04,05,06')>0 then 1 " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then 1 " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then 1  " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then 1    " +
                " when e.cycle ='4' and substring(s.date_write,1,7) = substring(SYSDATE(),1,7) then 1   " +
                " else 0 end )=1) n on m.id=n.id and m.frequency=n.frequency) q on o.id=q.id " +
                " union  " +
                " select o.id,o.name,o.score,o.frequency, o.target,o.mold,ROUND(IFNULL(p.cumulative,0)/q.ocount,2) cumulative,IFNULL(y.fraction,0) fraction,IFNULL(x.frequency,0) x_frequency " +
                "  from (select e.id,e.name,e.score,e.frequency, e.target,e.mold from ec_examine_main m " +
                " left join ec_examine e on e.iid=m.iid where m.id='"+mid+"' and e.target='1') o " +
                "  left join (select e.id,sum(s.fraction) cumulative from ec_examine_son s left join ec_examine e on s.eid=e.id " +
                " where s.mid='"+mid+"' and e.target='1' group by e.id) p on o.id=p.id " +
                "  left join (select count(*) ocount from ec_organization where fid=(select fid from ec_organization where id='"+oid+"')) q on 1=1 " +
                "  left join (select e.id,max(s.frequency) frequency from ec_examine_son s left join ec_examine e on s.eid=e.id " +
                "  where e.target='1' and s.mid='"+mid+"' and s.oid='"+oid+"' " +
                " and (case when s.date_write is NULL then 0 when e.cycle ='1' then 1      " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03,04,05,06')>0 then 1 " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then 1 " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then 1  " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then 1    " +
                " when e.cycle ='4' and substring(s.date_write,1,7) = substring(SYSDATE(),1,7) then 1 else 0 end )=1 group by e.id) x on o.id=x.id " +
                " left join (select u.id,v.fraction from (select e.id,max(s.frequency) frequency from ec_examine_son s left join ec_examine e on s.eid=e.id " +
                " where e.target='1' and s.mid='"+mid+"' and s.oid='"+oid+"' and (case when s.date_write is NULL then 0 when e.cycle ='1' then 1      " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03,04,05,06')>0 then 1 " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then 1 " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then 1  " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then 1    " +
                " when e.cycle ='4' and substring(s.date_write,1,7) = substring(SYSDATE(),1,7) then 1 else 0 end )=1 group by e.id) u " +
                " left join (select e.id,s.frequency,sum(s.fraction) fraction " +
                " from ec_examine_son s left join ec_examine e on s.eid=e.id where e.target='1' and s.mid='"+mid+"'  " +
                " and (case when s.date_write is NULL then 0 when e.cycle ='1' then 1      " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'01,02,03,04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03,04,05,06')>0 then 1 " +
                " when e.cycle ='2' and find_in_set(substring(s.date_write,6,2),'07,08,09,10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09,10,11,12')>0 then 1 " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'01,02,03')>0 and find_in_set(substring(SYSDATE(),6,2),'01,02,03')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'04,05,06')>0 and find_in_set(substring(SYSDATE(),6,2),'04,05,06')>0 then 1  " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'07,08,09')>0 and find_in_set(substring(SYSDATE(),6,2),'07,08,09')>0 then 1    " +
                " when e.cycle ='3' and find_in_set(substring(s.date_write,6,2),'10,11,12')>0 and find_in_set(substring(SYSDATE(),6,2),'10,11,12')>0 then 1    " +
                " when e.cycle ='4' and substring(s.date_write,1,7) = substring(SYSDATE(),1,7) then 1   " +
                " else 0 end )=1 group by e.id,s.frequency) v on u.id=v.id and u.frequency=v.frequency) y on o.id=y.id) ab ";
        return Db.paginate(currentPage, pageSize, select, sql);
    }

    /***
     * del
     * @param ids
     */
    @Before(Tx.class)
    public void deleteByIds(String ids) {
        String idarr[] = ids.split(",");
        for (String id : idarr) {
            EcExamineAverage o = me.getById(id);
            o.delete();
        }
    }

}