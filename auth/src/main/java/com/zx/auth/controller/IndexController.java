package com.zx.auth.controller;

import com.zx.auth.entity.*;
import com.zx.auth.service.*;
import com.zx.common.common.*;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.HandleEnum;
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

    @Value("${redis.session.timeout}")
    private String redisSessionTimeout;
    @Value("${redis.dictionary.timeout}")
    private String redisDictionaryTimeout;

    private final static String ticket_ = "ticket_";

    /**
     * 登陆校验
     *
     * @param request
     */
    @GetMapping("/toLogin")
    public ResponseBean loginValidate(HttpServletRequest request) {
        Map<String, Object> map;
        //判断请求是手机端还是pc端发出
        String userAgent = request.getHeader("USER-AGENT");
        boolean isPhone = CheckMobileUtil.check(userAgent);
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

            map = JSONUtil.json2Object((String) values, Map.class);
        } else {
            HttpSession session = request.getSession();
            ZxUser user = (ZxUser) session.getAttribute("userInfo");
            Map<String, ZxOrganization> orgInfoMap = (Map<String, ZxOrganization>) session.getAttribute("orgInfoMap");
            List<ZxRole> authRoles = (List<ZxRole>) session.getAttribute("authRoles");
            map = new HashMap<>();
            map.put("userInfo", user);
            map.put("orgInfoMap", orgInfoMap);
            map.put("authRoles", authRoles);
        }

        List<ZxDictionary> allDictionary;
        String dictionary = (String) redisUtil.get("dictionaryList");
        if (StringUtils.isEmpty(dictionary)) {
            allDictionary = (List<ZxDictionary>) zxDictionaryService.base(new RequestBean("getAll", HandleEnum.GET_ALL, null, null, null)).getData();
            redisUtil.set("dictionaryList", JSONUtil.object2Json(allDictionary));
            redisUtil.expire("dictionaryList", Long.parseLong(redisDictionaryTimeout));
        } else {
            allDictionary = (List<ZxDictionary>) JSONUtil.json2Object((String) dictionary, List.class);
        }

        map.put("dictionaryList", allDictionary);
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
            ZxUser user = null;
            if (!StringUtils.isEmpty(zxAccount.getUserId())) {
                user = zxUserService.getById(zxAccount.getUserId().split(",")[0]);
            }

            Map<String, ZxOrganization> orgMap = null;
            if (user != null && !StringUtils.isEmpty(user.getOrganizationId())) {
                orgMap = new HashMap<>();
                String[] orgIds = user.getOrganizationId().split(",");
                int len = orgIds.length;
                for (int i = 0; i < (len > 1 ? len - 1 : len); i++) {
                    ZxOrganization org = organizationService.getById(orgIds[i]);
                    orgMap.put(orgIds[i], org);
                }
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
                map.put("orgInfoMap", orgMap);
                map.put("authRoles", roleList);
                String ticket = UUID.randomUUID().toString();
                String key = ticket_ + ticket;
                redisUtil.set(key, JSONUtil.object2Json(map));
                redisUtil.expire(key, Long.parseLong(redisSessionTimeout));
                return new ResponseBean(ticket);
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("userInfo", user);
                session.setAttribute("orgInfoMap", orgMap);
                session.setAttribute("authRoles", roleList);
                return new ResponseBean();
            }
        } catch (Exception e) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), e.getMessage());
        }
    }
    /**
     * 注册新用户
     *
     *@param account
     */
    @PostMapping("/register")
    public ResponseBean toRegister(@RequestBody ZxAccount account ) {
       //生成随机不重复的字符串
        String result= UUID.randomUUID().toString().replace("-", "").toUpperCase();
        //保存注册账号用户信息
        ZxUser user1=new  ZxUser();
        user1.setId(MD5Utils.md5(result));
        user1.setUserName(account.getUserAccountNameZhu());
        user1.setBirthDay(account.getUserAccountBirthdayZhu());
        user1.setSex(account.getUserAccountSexZhu());
        user1.setCreateTime(new  Date());
        user1.setPhoneNumber(account.getUserAccountPhone());
        //保存用户信息
        zxUserService.addAccountUser(user1);
        //获取用户账号
        String accountName = account.getAccountName();
        //获取用户密码
        String accountPassword = account.getAccountPassword();
        //验证账号密码非空
        if (StringUtils.isEmpty(accountName) || StringUtils.isEmpty(accountPassword)) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.ENTITY_IS_NULL.getValue());
        }
        //2015-2-13重新new个对象
        ZxAccount accountNew =new  ZxAccount();
        accountNew.setUserId(user1.getId());
        accountNew.setAccountName(accountName);
        accountNew.setAccountPassword(MD5Utils.md5(accountPassword));
        //将注册的新用户信息进行保存
        return new ResponseBean(zxAccountService.addRegisterAccount(accountNew));

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
