package org.smartjq.mvc.common.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.jfinal.log.Log;
import org.smartjq.mvc.admin.smslog.DjSmsLogService;
import org.smartjq.mvc.common.model.DjSmsLog;

/**
 * 公寓数据批处理
 * @author Shang 2019-01-04
 *
 */
@Component
@EnableScheduling
public class SendSmsTasks {
	public static final DjSmsLogService service = DjSmsLogService.me;
    private static final Log logger = Log.getLog(SendSmsTasks.class);

	/**
	 * 短信发送任务，每5秒执行一次
	 */
	@Scheduled(fixedRate = 1000 * 5 * 1)
	public void sendSmsCode() {
		logger.info("Start process send sms code");
		List<DjSmsLog> list = new ArrayList<DjSmsLog>();
		try {
			list = service.findAllUnsentSms();
			for(DjSmsLog log : list) {
				if(ToolsUtil.isMobile(log.getPhone())) {
					Gson gson = new Gson();
					String[] params = gson.fromJson(log.getParams(), String[].class);
					String ret = ToolsUtil.sendSms(log.getPhone(), "675118", params);
			        if(null != ret && "Ok".equals(ret)) {
			        	log.setStatus("1");
			        	log.setSendTime(DateUtil.getCurrentTime());
			        	log.update();
			        }else {
			        	log.setStatus("2");
			        	log.update();
			        }
				}
			}
		} catch (Exception e) {
			logger.error("短信发送异常", e);
		}		
	}	
}
