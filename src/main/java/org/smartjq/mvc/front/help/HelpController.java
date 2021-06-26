/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.mvc.front.help;

import org.smartjq.mvc.common.base.BaseController;

/***
 * 帮助页面控制器
 * @author Administrator
 *
 */
public class HelpController extends BaseController {
	
	public void howToRunJFinalOA(){
		render("/WEB-INF/front/help/howToRunJFinalOA.html");
	}
}
