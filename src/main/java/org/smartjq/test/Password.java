package org.smartjq.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

public class Password {
	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String sql = "INSERT INTO sys_user (id, username, password, name, sex, status, orgid, is_admin, type, year_holiday) VALUES ('9cabfc71c62b4ad69f80a4d05582b1a9', '13272143450', '$shiro1$SHA-256$500000$l2BZM1Rz2ASYkeomRTVUXA==$S0dV71qpdZQAG0g+w7GcPubxT1JF+zewFU0cY+Bz/ss=', 'shang1', '1', '1', '68b2bb2026e54c5186f75a05156abb0f', '0', '1', '0');" ;
		PasswordService svc = new DefaultPasswordService();
		System.out.println(svc.encryptPassword("123456"));
		try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
			 
			/* 读入TXT文件 */
			String pathname = "e:\\new11.txt"; // 绝对路径或相对路径都可以，这里是绝对路径，写入文件时演示相对路径
			File filename = new File(pathname); // 要读取以上路径的input。txt文件
			InputStreamReader reader = new InputStreamReader(
					new FileInputStream(filename)); // 建立一个输入流对象reader
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			String line = "";
			line = br.readLine();
			while (line != null) {
				line = br.readLine(); // 一次读入一行数据]
				if(line != null) {
					String[] str = line.split(",");
					String password = str[0].substring(5);
					password = svc.encryptPassword(password);
					String id = UUID.randomUUID().toString().replace("-", "");
					System.out.println("INSERT INTO sys_user (id, username, password, name, sex, status, orgid, is_admin, type, year_holiday) VALUES ('" + id + "', '" + str[0] + "', '" + password + "', '" + str[1] + "', '1', '1', '" + str[2] + "', '0', '1', '0');");
				}
			}
			reader.close();
			br.close();
 
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
