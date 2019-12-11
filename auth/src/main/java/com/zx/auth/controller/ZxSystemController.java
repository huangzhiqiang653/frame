package com.zx.auth.controller;

import com.zx.auth.service.IZxCommonService;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: law-risk->SystemController
 * @description: 系统公共方法
 * @author: 黄智强
 * @create: 2019-11-25 10:03
 **/
@RestController
@RequestMapping("/auth/zx-system")
public class ZxSystemController {
    @Resource
    IZxCommonService zxCommonService;

    /**
     * 上传文件
     *
     * @param file 文件对象
     * @return 0:组名；1: 文件路径
     * @throws IOException
     */
    @PostMapping(value = "/upload")
    @ResponseBody
    public ResponseBean uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        return zxCommonService.uploadFile(file, request);
    }

    /**
     * fastDFS文件下载
     *
     * @param response  响应对象
     * @param groupName 组名称
     * @param path      文件路径
     */
    @RequestMapping(value = "/downFastDFSFile")
    @ResponseBody
    public void downFastDFSFile(HttpServletResponse response, String groupName, String path, String fileName) throws Exception {
        zxCommonService.downFastDFSFile(response, groupName, path, fileName);
    }

    /**
     * 文件下载
     *
     * @param requestBean
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/downFile")
    @ResponseBody
    public void downFile(@RequestBody RequestBean requestBean, HttpServletRequest request, HttpServletResponse response) throws Exception {
        zxCommonService.downFile(requestBean, request, response);
    }

    /**
     * 数据库链接校验
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(value = "/validateDbConnect")
    @ResponseBody
    public ResponseBean validateDbConnect(@RequestBody RequestBean requestBean) {
        return zxCommonService.validateDbConnect(requestBean);
    }

    /**
     * 获取表结果数据
     *
     * @param requestBean
     * @return
     */
    @RequestMapping(value = "/getTableData")
    @ResponseBody
    public ResponseBean getTableData(@RequestBody RequestBean requestBean) {
        return zxCommonService.getTableData(requestBean);
    }
}
