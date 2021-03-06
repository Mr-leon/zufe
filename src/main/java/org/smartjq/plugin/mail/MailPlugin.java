package org.smartjq.plugin.mail;

import java.util.Properties;

import com.jfinal.kit.LogKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;

/**
 * 发送邮件插件
 * @author farmer
 *
 */
public class MailPlugin implements IPlugin{
	
	static final String MAIN_CONFIG = "main";
	
	String configName = MAIN_CONFIG;
	
	Properties props;
	
	public MailPlugin(Properties props) {
		this(MAIN_CONFIG, props);
	}
	
	public MailPlugin(String configName,Properties props) {
		this.configName = configName;
		this.props = props;
	}
	
	@Override
	public boolean start() {
		if(StrKit.isBlank(props.getProperty("host"))){
			throw new RuntimeException("未设置邮箱服务器");
		}
		if(StrKit.isBlank(props.getProperty("protocol"))){
			props.setProperty("protocol", "smtp");
			LogKit.warn("未设置协议protocol，使用默认值:"+"smtp");
		}
		if(StrKit.isBlank(props.getProperty("port"))){
			throw new RuntimeException("未设置端口port");
		}
		if(StrKit.isBlank(props.getProperty("mail.smtp.ssl.enable"))){
			props.setProperty("mail.smtp.ssl.enable", "false");
			LogKit.warn("未设置mail.smtp.ssl.enable，使用默认值:"+"false");
		}
		if(StrKit.isBlank(props.getProperty("mail.smtp.auth"))){
			props.setProperty("mail.smtp.auth", "true");
			LogKit.warn("未设置mail.smtp.auth，使用默认值:"+"true");
		}
		if(StrKit.isBlank(props.getProperty("username"))){
			throw new RuntimeException("未设置用户名");
		}
		if(StrKit.isBlank(props.getProperty("password"))){
			LogKit.warn("未设置密码");
		}
		if(StrKit.isBlank(props.getProperty("mail.smtp.timeout"))){
			props.setProperty("mail.smtp.timeout", "10000");
			LogKit.warn("未设置超时时间，使用默认值mail.smtp.timeout:"+"10000");
		}
		MailKit.init(configName, new MailPro(props));
		return true;
	}

	@Override
	public boolean stop() {
		return true;
	}

}
