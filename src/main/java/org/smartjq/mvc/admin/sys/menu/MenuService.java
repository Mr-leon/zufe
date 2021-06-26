package org.smartjq.mvc.admin.sys.menu;

import java.util.List;

import org.smartjq.mvc.common.model.SysMenu;

public class MenuService {
	public static final MenuService me = new MenuService();
	private final SysMenu dao = new SysMenu().dao();
	public SysMenu findByUrl(String menu) {
		List<SysMenu> list = dao.find("select * from sys_menu where if_show='1' and url='" + menu + "'");
		if(null != list && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

}
