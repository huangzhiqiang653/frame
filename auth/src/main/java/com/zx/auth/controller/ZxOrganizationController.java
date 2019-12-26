package com.zx.auth.controller;


import com.zx.auth.service.IZxOrganizationService;
import com.zx.common.common.BaseController;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 组织表 前端控制器
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/auth/zx-organization")
public class ZxOrganizationController extends BaseController {

    @Resource
    IZxOrganizationService zxOrganizationService;

    /**
     * 公共基础方法
     *
     * @param requestBean 请求参数实体类
     * @return 返回参数
     */
    @Override
    public ResponseBean base(RequestBean requestBean) {
        return zxOrganizationService.base(requestBean);
    }
}
