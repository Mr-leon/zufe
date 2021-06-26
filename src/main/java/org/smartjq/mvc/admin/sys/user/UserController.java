/**
 * @author Lion
 * @date 2017年1月24日 下午12:02:35
 * @qq 439635374
 */
package org.smartjq.mvc.admin.sys.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import org.smartjq.mvc.admin.workflow.WorkFlowIdentityService;
import org.smartjq.mvc.common.base.BaseController;
import org.smartjq.mvc.common.model.*;
import org.smartjq.mvc.common.utils.DateUtil;
import org.smartjq.mvc.common.utils.UuidUtil;
import org.smartjq.plugin.shiro.ShiroKit;

/***
 * 用户管理控制器
 *
 * @author Administrator
 *
 */

public class UserController extends BaseController {
    public static final WorkFlowIdentityService idService = WorkFlowIdentityService.me;
    public static final SysUserService service = SysUserService.me;

    /***
     * 获取管理首页
     */
    public void getListPage() {
        setAttr("orgid", "#root");
        renderIframe("list.html");
    }

    /***
     * 获取通讯录首页
     */
    public void getUserListPage() {
        HttpServletRequest request = getRequest();
        String url = request.getRequestURL().toString();
        String userId = ShiroKit.getUserId();
        String context = request.getContextPath();
        String menu = url.substring(url.indexOf(context) + context.length());
        SysMenu sm = smService.findByUrl(menu);
        if (null != sm) {
            SysUserCommonFunction o = cfService.getByMenuId(sm.getId(), userId);
            if (null != o) {
                o.setUseTime(new Date());
                o.update();
            } else {
                o = new SysUserCommonFunction();
                o.setCreateTime(DateUtil.dateToString(new Date(), 0));
                o.setIfDel("0");
                o.setIfFixed("0");
                o.setMenuId(sm.getId());
                o.setUserid(userId);
                o.setId(UuidUtil.getUUID());
                o.setUseTime(new Date());
                o.save();
            }
        }
        setAttr("orgid", ShiroKit.getUserOrgId());
        renderIframe("userList.html");
    }

    /***
     * 获取分页数据
     **/
    public void listData() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String orgid = getPara("orgid");
        String groupid = getPara("groupid");
        String usernameSearch = getPara("usernameSearch", "");
        String nameSearch = getPara("nameSearch", "");
        if (StrKit.notBlank(usernameSearch) || StrKit.notBlank(nameSearch)) {
            orgid = "";
        }

        Page<Record> page = null;
        if (null != groupid && !"".equals(groupid)) {
            page = SysUser.dao.getPageByGroupId(Integer.valueOf(curr), Integer.valueOf(pageSize), groupid, usernameSearch,
                    nameSearch);
        } else {
            page = SysUser.dao.getPage(Integer.valueOf(curr), Integer.valueOf(pageSize), orgid, usernameSearch,
                    nameSearch);
        }
        List<Record> pageList = page.getList();
        for (Record r : pageList) {
            List<SysRole> list = SysRole.dao.getAllRoleByUserid(r.getStr("id"));
            List<String> nameList = new ArrayList<String>();
            for (SysRole role : list) {
                nameList.add(role.getName());
            }
            r.set("userRoleName", StringUtils.join(nameList, ","));
        }
        renderPage(page.getList(), "", page.getTotalRow());
    }

    /***
     * 根据角色获取用户
     */
    public void getListDataByRoleid() {
        String curr = getPara("pageNumber");
        String pageSize = getPara("pageSize");
        String roleid = getPara("roleid");
        if (StrKit.isBlank(roleid)) {
            renderPage(null, "", 0);
        } else {
            Page<Record> page = SysUser.dao.getPageByRoleid(Integer.valueOf(curr), Integer.valueOf(pageSize), roleid);
            renderPage(page.getList(), "", page.getTotalRow());
        }
    }

    /***
     * 保存
     */
    public void save() {
        SysUser newUser = getModel(SysUser.class);
        String userRoleId = getPara("userRoleId", "");
        if (StrKit.notBlank(newUser.getId())) {
            String password = newUser.getPassword();
            SysUser old = SysUser.dao.findById(newUser.getId());
            if (StrKit.isBlank(password)) {// 如果没传递密码
                password = old.getPassword();// 获取原始密码
                newUser.setPassword(password);// 设置为原始密码
            } else {// 传递了密码 , 设置新密码
                PasswordService svc = new DefaultPasswordService();
                newUser.setPassword(svc.encryptPassword(password));// 加密新密码
            }
            newUser.getOrgid();
            if (StrKit.notBlank(newUser.getOrgid())) {
                EcUserOrganization o = EcUserOrganization.dao.getOidByUid(newUser.getId());
                if (o != null) {
                    o.setOid(newUser.getOrgid());
                    o.update();
                } else {
                    EcUserOrganization userOrganization = new EcUserOrganization();
                    userOrganization.setOid(newUser.getOrgid());
                    userOrganization.setUid(newUser.getId());
                    userOrganization.save();
                }
            }
            newUser.update();
            SysUser.dao.giveUserRole(newUser.getId(), userRoleId);
        } else {
            newUser.setId(UuidUtil.getUUID());
            PasswordService svc = new DefaultPasswordService();
            newUser.setPassword(svc.encryptPassword(newUser.getPassword()));// 加密新密码
            if (StrKit.notBlank(newUser.getOrgid())) {
                EcUserOrganization userOrganization = new EcUserOrganization();
                userOrganization.setOid(newUser.getOrgid());
                userOrganization.setUid(newUser.getId());
                userOrganization.save();
            }
            newUser.save();
            SysUser.dao.giveUserRole(newUser.getId(), userRoleId);// 这里面有添加用户，添加分组
        }
        renderSuccess();
    }

    /***
     * 修改密码
     */
    public void changePassword() {
        SysUser user = SysUser.dao.findById(ShiroKit.getUserId());
        String oldPass = getPara("oldPass");
        String newPass = getPara("newPass");
        PasswordService svc = new DefaultPasswordService();
        System.out.println("oldPass=" + oldPass);
        //验证密码
        if (svc.passwordsMatch(oldPass, user.getPassword())) {
            newPass = svc.encryptPassword(newPass);
            user.setPassword(newPass);
            user.update();
            setAttr("code", "200");
            renderJson();
        } else {
            setAttr("code", "500");
            renderJson();
        }
    }


    /***
     * 根据用户姓名查询用户
     */
    public void getUserNameByName() {
        String name = getPara("q");
        Object[] para = new Object[1];
        para[0] = "%" + name + "%";
        List<Record> list = service.findUserByName(name);
        setAttr("code", "200");
        setAttr("data", list);
        renderJson(list);
    }

    /***
     * 获取编辑页面
     */
    public void getEditPage() {
        // 添加和修改
        String id = getPara("id");
        if (StrKit.notBlank(id)) {// 修改
            SysUser o = SysUser.dao.getById(id);
            String orgid = o.getOrgid();
            EcOrganization org = EcOrganization.dao.getById(orgid);
            setAttr("org", org);
            setAttr("o", o);
            List<SysRole> list = SysRole.dao.getAllRoleByUserid(o.getId());
            List<String> idList = new ArrayList<String>();
            List<String> nameList = new ArrayList<String>();
            for (SysRole role : list) {
                idList.add(role.getId());
                nameList.add(role.getName());
            }
            setAttr("userRoleId", StringUtils.join(idList, ","));
            setAttr("userRoleName", StringUtils.join(nameList, ","));
        } else {
            String orgid = getPara("orgid");
            if (StrKit.notBlank(orgid)) {
                EcOrganization org = EcOrganization.dao.getById(orgid);
                setAttr("org", org);
            }
        }
        renderIframe("edit.html");
    }

    /***
     * 获取详细信息页面
     */
    public void getDetailPage() {
        // 添加和修改
        String id = getPara("id");
        if (StrKit.notBlank(id)) {// 修改
            SysUser o = SysUser.dao.getById(id);
            String orgid = o.getOrgid();
            SysOrg org = SysOrg.dao.getById(orgid);
            setAttr("org", org);
            setAttr("o", o);
            List<SysRole> list = SysRole.dao.getAllRoleByUserid(o.getId());
            List<String> idList = new ArrayList<String>();
            List<String> nameList = new ArrayList<String>();
            for (SysRole role : list) {
                idList.add(role.getId());
                nameList.add(role.getName());
            }
            setAttr("userRoleId", StringUtils.join(idList, ","));
            setAttr("userRoleName", StringUtils.join(nameList, ","));
        } else {
            String orgid = getPara("orgid");
            if (StrKit.notBlank(orgid)) {
                SysOrg org = SysOrg.dao.getById(orgid);
                setAttr("org", org);
            }
        }
        renderIframe("userDetail.html");
    }

    public void userSetting() {

        String id = getPara("id");
        if (StrKit.notBlank(id)) {// 修改
            SysUser o = SysUser.dao.getById(id);
            String orgid = o.getOrgid();
            SysOrg org = SysOrg.dao.getById(orgid);
            setAttr("org", org);
            setAttr("o", o);
            List<SysRole> list = SysRole.dao.getAllRoleByUserid(o.getId());
            List<String> idList = new ArrayList<String>();
            List<String> nameList = new ArrayList<String>();
            for (SysRole role : list) {
                idList.add(role.getId());
                nameList.add(role.getName());
            }
            setAttr("userRoleId", StringUtils.join(idList, ","));
            setAttr("userRoleName", StringUtils.join(nameList, ","));
        }
        renderIframe("userSetting.html");
    }

    /***
     * 验证用户 , 是否被注册
     */
    public void validUsername() {
        SysUser user = getModel(SysUser.class);
        if (user != null) {
            String username = user.getUsername();
            if (StrKit.notBlank(username)) {
                SysUser o = SysUser.dao.getByUsername(username);
                if (o == null) {// 用户不存在
                    renderValidSuccess();
                } else {
                    renderValidFail();
                }
            } else {
                renderValidSuccess();
            }
        } else {
            renderValidSuccess();
        }
    }

    /***
     * 给用户赋值角色
     */
    public void getGiveUserRolePage() {
        String userid = getPara("userid");
        setAttr("userid", userid);
        renderIframe("giveUserRole.html");
    }

    /***
     * 给用户赋值角色
     */
    public void giveUserRole() {
        String userid = getPara("userid");
        String data = getPara("data");
        SysUser.dao.giveUserRole(userid, data);
        renderSuccess();
    }

    /***
     * 删除
     *
     * @throws Exception
     */
    public void delete() throws Exception {
        String ids = getPara("ids");
        // 执行删除
        SysUser.dao.deleteByIds(ids);
        renderSuccess("删除成功!");
    }

    /***
     * 打开选择多个人员的页面
     */
    public void openSelectManyUserPage() {
        String orgid = getPara("orgid");
        String oldData = getPara("oldData");
        setAttr("selectedId", "");
        setAttr("selectedName", "");
        if (StrKit.notBlank(oldData)) {
            String[] old = oldData.split(",");
            List<String> username = new ArrayList<String>();
            List<String> name = new ArrayList<String>();
            List<String> org = new ArrayList<String>();
            for (String userid : old) {
                SysUser user = SysUser.dao.getById(userid);
                if (user != null) {
                    username.add(user.getUsername());
                    name.add(user.getName());
                    org.add(SysOrg.dao.getOrgNameById(user.getOrgid()));
                }
            }
            setAttr("selectedId", oldData);
            setAttr("selectedUsername", StringUtils.join(username, ","));
            setAttr("selectedName", StringUtils.join(name, ","));
            setAttr("selectedOrgName", StringUtils.join(org, ","));
        }
        setAttr("orgid", orgid);
        renderIframe("selectManyUser.html");
    }

    /***
     * 打开选择单个人员的页面
     */
    public void openSelectOneUserPage() {
        String orgid = getPara("orgid");
        setAttr("orgid", orgid);
        renderIframe("selectOneUser.html");
    }

    /***
     * 使用角色打开选择人员的页面
     */
    public void openSelectOneUserUseRolePage() {
        String roleKey = getPara("roleKey");
        if (StrKit.notBlank(roleKey)) {
            String firstRoleKey = roleKey.split(",")[0];
            SysRole firstRole = SysRole.dao.getRoleByRoleKey(firstRoleKey);
            if (firstRole != null) {
                setAttr("firstRoleId", firstRole.getId());
            }
        }
        setAttr("roleKey", roleKey);
        renderIframe("selectOneUserUseRole.html");
    }

}
