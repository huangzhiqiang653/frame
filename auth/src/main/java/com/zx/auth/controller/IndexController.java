package com.zx.auth.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.zx.auth.entity.*;
import com.zx.auth.service.*;
import com.zx.common.common.CheckMobileUtil;
import com.zx.common.common.MD5Utils;
import com.zx.common.common.RedisUtil;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @program: law-risk->IndexController
 * @description: 系统类
 * @author: 黄智强
 * @create: 2019-11-18 20:39
 **/
@RestController
@RequestMapping("/auth/index")
public class IndexController {
    protected Log log = LogFactory.getLog(this.getClass());

    @Resource
    IZxDictionaryService zxDictionaryService;
    @Resource
    IZxAccountService zxAccountService;
    @Resource
    IZxUserService zxUserService;
    @Resource
    IZxRoleService zxRoleService;
    @Resource
    IZxOrganizationService organizationService;
    @Resource
    IZxMenuService zxMenuService;
    @Resource
    RedisUtil redisUtil;

    @Value("${login.session.timeout}")
    private String loginSessionTimeOut;

    private final static String ticket_ = "ticket_";

    /**
     * 登陆校验
     *
     * @param request
     */
    @GetMapping("/toLogin")
    public ResponseBean loginValidate(HttpServletRequest request) {
        //判断请求是手机端还是pc端发出
        String userAgent = request.getHeader("USER-AGENT");
        boolean isPhone = CheckMobileUtil.check(userAgent);
        Map<String, Object> map = null;
        if (isPhone) {
            String ticket = (String) request.getAttribute("ticket");
            if (StringUtils.isEmpty(ticket)) {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.ENTITY_IS_NULL.getValue());
            }

            String key = ticket_ + ticket;
            Object values = redisUtil.get(key);
            if (values == null) {
                return new ResponseBean(CommonConstants.NOT_LOGIN.getCode(), CommonConstants.NOT_LOGIN.getMessage());
            }

            map = (Map<String, Object>) JSONUtils.parse((String) values);
        } else {
            HttpSession session = request.getSession();
            ZxUser user = (ZxUser) session.getAttribute("userInfo");
            ZxOrganization orgInfo = (ZxOrganization) session.getAttribute("orgInfo");
            List<ZxRole> authRoles = (List<ZxRole>) session.getAttribute("authRoles");
            map = new HashMap<>();
            map.put("userInfo", user);
            map.put("orgInfo", orgInfo);
            map.put("authRoles", authRoles);
        }

        return new ResponseBean(map);
    }

    /**
     * 账户登录
     *
     * @param request
     */
    @PostMapping("/login")
    public ResponseBean toLogin(@RequestBody ZxAccount account, HttpServletRequest request) {
        //获取参数
        String accountName = account.getAccountName();
        String accountPassword = account.getAccountPassword();
        if (StringUtils.isEmpty(accountName) || StringUtils.isEmpty(accountPassword)) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.ENTITY_IS_NULL.getValue());
        }
        //校验账号密码是否正确
        try {
            ZxAccount zxAccount = new ZxAccount();
            zxAccount.setAccountName(accountName);
            List<ZxAccount> list = zxAccountService.listByAccount(zxAccount);
            if (CollectionUtils.isEmpty(list)) {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.ACCOUNT_IS_NULL.getValue());
            }

            zxAccount = list.get(0);
            //校验密码：密码MD5加密
            if (!MD5Utils.md5(accountPassword).equals(zxAccount.getAccountPassword())) {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.ACCOUNT_PASSWORD_IS_ERROR.getValue());
            }

            //(用户、角色、权限、组织等)
            ZxUser user = zxUserService.getById(zxAccount.getUserId());
            ZxOrganization org = null;
            if (user != null && !StringUtils.isEmpty(user.getOrganizationId())) {
                org = organizationService.getById(user.getOrganizationId());
            }

            List<ZxRole> roleList = zxRoleService.listRoleByAccountId(zxAccount.getId());
            if (!CollectionUtils.isEmpty(roleList)) {
                for (ZxRole role : roleList) {
                    List<ZxMenu> authMenuList = zxMenuService.listMenuByRoleId(role.getId());
                    role.setAuthMenuList(authMenuList);
                }
            }

            //判断请求是手机端还是pc端发出
            String userAgent = request.getHeader("USER-AGENT");
            boolean isPhone = CheckMobileUtil.check(userAgent);
            if (isPhone) {
                Map<String, Object> map = new HashMap<>();
                map.put("userInfo", user);
                map.put("orgInfo", org);
                map.put("authRoles", roleList);
                String ticket = UUID.randomUUID().toString();
                String key = ticket_ + ticket;
                redisUtil.set(key, JSONUtils.toJSONString(map));
                redisUtil.expire(key, Long.parseLong(loginSessionTimeOut));
                return new ResponseBean(ticket);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("userInfo", user);
                session.setAttribute("orgInfo", org);
                session.setAttribute("authRoles", roleList);
                return new ResponseBean();
            }
        } catch (Exception e) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), e.getMessage());
        }
    }

    /**
     * 退出校验
     *
     * @param request
     * @return
     */
    @GetMapping("/logout")
    public ResponseBean loginOut(HttpServletRequest request) {
        //清除局部session
        request.getSession().invalidate();
        log.debug("退出成功～");
        return new ResponseBean();
    }
}
