package com.zx.auth.controller;

import com.zx.auth.service.IZxDictionaryService;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.HandleEnum;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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

    /**
     * 登陆校验
     *
     * @param request
     */
    @GetMapping("/login")
    public ResponseBean loginValidate(HttpServletRequest request) {
        // TODO 登陆校验
        log.debug("已登陆～");
        Map<String, Object> map = new HashMap<String, Object>();
        // map.put("userInfo", ssoUser);
        map.put("dictionaryList", zxDictionaryService.base(new RequestBean("getAll", HandleEnum.GET_ALL, null, null, null)).getData());
        return new ResponseBean(map);
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
        //
        return new ResponseBean(String.format("%s/logout?service=%s", null, null));
    }
}
