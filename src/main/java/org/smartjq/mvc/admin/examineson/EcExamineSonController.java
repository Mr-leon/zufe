package org.smartjq.mvc.admin.examineson;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import com.jfinal.render.RenderException;
import org.apache.http.HttpResponse;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.admin.workflow.WorkFlowService;
import org.smartjq.mvc.common.model.*;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.plugin.shiro.ShiroKit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;


/**
 * @author CYZ
 */
public class EcExamineSonController extends BaseController {
    public static final EcExamineSonService service = EcExamineSonService.me;
    public static WorkFlowService wfservice = WorkFlowService.me;

    /***
     * save data
     */
    public void save() {
        EcExamineSon o = getModel(EcExamineSon.class);
//        String eid = o.getEid();
//        String mid = o.getMid();
//        EcExamineAverage examineAverage = EcExamineAverage.dao.getAverageByEMid(eid, mid);
//        examineAverage.setFraction(0.0);
//        examineAverage.setFrequency(examineAverage.getFrequency() + 1);
//        examineAverage.setDateNew(DateUtil.getCurrentTime());
//        examineAverage.update();

//        EcExamineSon ecExamineSon = EcExamineSon.dao.getSonByEMid(eid, mid);
//        if (ecExamineSon != null) {
//            ecExamineSon.setIsend("0");
//            ecExamineSon.update();
//        }

        if (o.getOid().split("_").length > 1) {
            String oid = o.getOid().split("_")[0];
            Integer frequency = Integer.valueOf(o.getOid().split("_")[1]);
            o.setOid(oid);
            o.setFrequency(frequency);
        }
        o.setId(UuidUtil.getUUID());
        o.setUidWrite(ShiroKit.getUserId());
        o.setDateWrite(DateUtil.getCurrentTime());
        o.save();
        renderSuccess();
    }

    /**
     * 审核
     */
    public void toExamine() {
        EcExamineSon o = getModel(EcExamineSon.class);
        String eid = o.getEid();
        String mid = o.getMid();
        Double fraction = o.getFraction();
        o.setUidExamine(ShiroKit.getUserId());
        o.setDateExamine(DateUtil.getCurrentTime());
        o.update();
        renderSuccess();
    }

}