package com.zx.rts.controller;

import com.zx.rts.service.IRtUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 导出前端控制器
 * 王志成
 */
@RestController
@RequestMapping("/rts/rt-user")
public class ExportExcelController {

    @Resource
    IRtUserService rtUserService;


    @GetMapping(value = "/exportRtUser")
    @ResponseBody
    public void ExportRtUser(HttpServletResponse response) {
        rtUserService.ExportRtUser(response);
    }

}
