package org.smartjq.mvc.common.model;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import org.smartjq.mvc.common.model.base.BaseEcAuxiliary;
import org.smartjq.plugin.shiro.ShiroKit;

import java.util.List;

@SuppressWarnings("serial")
public class EcAuxiliary extends BaseEcAuxiliary<EcAuxiliary> {
    public static final EcAuxiliary dao = new EcAuxiliary();
    public static final String tableName = "ec_auxiliary";

    /***
     * query by id
     */
    public EcAuxiliary getById(String id) {
        return EcAuxiliary.dao.findById(id);
    }

    /***
     * del
     * @param ids
     */
    @Before(Tx.class)
    public void deleteByIds(String ids) {
        String idarr[] = ids.split(",");
        for (String id : idarr) {
            EcAuxiliary o = EcAuxiliary.dao.getById(id);
            o.delete();
        }
    }

    public List<EcAuxiliary> findJDAllByOid(String eid, String mid) {
        String sql = " select" +
                " o.auxiliary,IFNULL(q.frequency,0) + 1 frequency" +
                " from ( " +
                " select " +
                "  auxiliary" +
                " from ec_auxiliary " +
                " where type='1' and id < (case " +
                "   when find_in_set(substring(SYSDATE(),6,2),'04,05,06') then 2" +
                "   when find_in_set(substring(SYSDATE(),6,2),'07,08,09') then 3" +
                "   when find_in_set(substring(SYSDATE(),6,2),'10,11,12') then 4" +
                "   else 0" +
                "  end )) o" +
                " " +
                " left join (select frequency from ec_examine where id='" + eid + "') p on 1=1" +
                " left join (select " +
                " max(frequency) frequency," +
                " (case" +
                "  when find_in_set(substring(date_write,6,2),'01,02,03') then '1'" +
                "  when find_in_set(substring(date_write,6,2),'04,05,06') then '2'" +
                "  when find_in_set(substring(date_write,6,2),'07,08,09') then '3'" +
                "  else 'jd4'" +
                " end) jd" +
                " from ec_examine_son" +
                " where mid='" + mid + "'" +
                " and eid='" + eid + "'" +
                " and oid='" + ShiroKit.getUserOrgId() + "'" +
                " and find_in_set(substring(date_write,6,2),'01,02,03,04,05,06,07,08,09')>0" +
                " group by jd) q on o.auxiliary=q.jd" +
                " where p.frequency>IFNULL(q.frequency,0) order by o.auxiliary";
        return EcAuxiliary.dao.find(sql);
    }

    public List<EcAuxiliary> findMouthAllByOid(String eid, String mid) {
        String sql = " select " +
                " o.auxiliary,IFNULL(q.frequency,0) + 1 frequency " +
                "from (  " +
                " select  " +
                "  auxiliary " +
                " from ec_auxiliary  " +
                " where type='2' and id < (case  " +
                "   when substring(SYSDATE(),6,2)='02' then 2 " +
                "   when substring(SYSDATE(),6,2)='03' then 3 " +
                "   when substring(SYSDATE(),6,2)='04' then 4 " +
                "   when substring(SYSDATE(),6,2)='05' then 5 " +
                "   when substring(SYSDATE(),6,2)='06' then 6 " +
                "   when substring(SYSDATE(),6,2)='07' then 7 " +
                "   when substring(SYSDATE(),6,2)='08' then 8 " +
                "   when substring(SYSDATE(),6,2)='09' then 9 " +
                "   when substring(SYSDATE(),6,2)='10' then 10 " +
                "   when substring(SYSDATE(),6,2)='11' then 11 " +
                "   when substring(SYSDATE(),6,2)='12' then 12 " +
                "   else 0 " +
                "  end )) o " +
                "  " +
                "left join (select frequency from ec_examine where id='" + eid + "') p on 1=1 " +
                "left join (select  " +
                " max(frequency) frequency, " +
                " substring(date_write,6,2) yf " +
                " from ec_examine_son " +
                " where mid='" + mid + "' " +
                " and eid='" + eid + "' " +
                " and oid='" + ShiroKit.getUserOrgId() + "' " +
                " and find_in_set(substring(date_write,6,2),'01,02,03,04,05,06,07,08,09,10,11')>0 " +
                " group by yf) q on o.auxiliary=q.yf " +
                " where p.frequency>IFNULL(q.frequency,0) order by o.auxiliary";
        return EcAuxiliary.dao.find(sql);
    }
}