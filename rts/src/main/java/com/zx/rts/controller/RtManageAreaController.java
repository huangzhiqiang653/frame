package com.zx.rts.controller;


import com.zx.common.common.BaseController;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.rts.service.IRtManageAreaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 管理区域关联表 前端控制器
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-19
 */
@RestController
@RequestMapping("/rts/rt-manage-area")
public class RtManageAreaController extends BaseController {

    @Resource
    IRtManageAreaService baseService;

    @Override
    public ResponseBean base(RequestBean requestBean) {
        return baseService.base(requestBean);
    }
}
