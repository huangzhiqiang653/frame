package com.zx.rts.controller;

import com.zx.common.common.ResponseBean;
import com.zx.rts.service.IRtCarsService;
import com.zx.rts.service.IRtUserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 导出前端控制器
 * 王志成
 */
@RestController
@RequestMapping("/rts/rt-export")
public class ExportExcelController {

    @Resource
    private IRtUserService rtUserService;

    @Resource
    private IRtCarsService iRtCarsService;

    /**
     * 人员信息导出
     *
     * @param response
     */
    @GetMapping(value = "/exportRtUser")
    @ResponseBody
    public void exportRtUser(HttpServletResponse response) {

        rtUserService.exportRtUser(response);
    }


    /**
     * 车辆信息导出
     *
     * @param response
     */
    @GetMapping(value = "/exportRtCar")
    public void exportRtCar(HttpServletResponse response) {

        iRtCarsService.exportRtCar(response);
    }

    /**
     * 沈阳
     * 车辆导入
     *
     * @param file
     */
    @PostMapping(value = "/importRtCar")
    @ResponseBody
    public ResponseBean importRtCar(@RequestParam MultipartFile file) {

       return   iRtCarsService.importRtCar(file);
    }

    /**
     * wangzhicheng
     * 人员导入
     *
     * @param file
     */
    @PostMapping(value = "/importRtUser")
    public void importRtUser(@RequestParam MultipartFile file) {

        rtUserService.importRtUser(file);
    }


}
