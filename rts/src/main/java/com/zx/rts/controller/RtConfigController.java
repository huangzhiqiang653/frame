package com.zx.rts.controller;


import com.zx.common.common.BaseController;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.rts.service.IRtConfigService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 配置表 前端控制器
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@RestController
@RequestMapping("/rts/rt-config")
public class RtConfigController extends BaseController {

    @Resource
    IRtConfigService baseService;

    /**
     * 公共基础方法
     *
     * @param requestBean 请求参数实体类
     * @return 返回参数
     */
    @Override
    public ResponseBean base(RequestBean requestBean) {
        return baseService.base(requestBean);
    }
}
