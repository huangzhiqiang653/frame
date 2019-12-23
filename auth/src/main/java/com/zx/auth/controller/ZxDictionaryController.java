package com.zx.auth.controller;


import com.zx.auth.service.IZxDictionaryService;
import com.zx.common.common.BaseController;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/auth/zx-dictionary")
public class ZxDictionaryController extends BaseController {

    @Resource
    IZxDictionaryService zxDictionaryService;

    /**
     * 公共基础方法
     *
     * @param requestBean 请求参数实体类
     * @return 返回参数
     */
    @Override
    public ResponseBean base(RequestBean requestBean) {
        return zxDictionaryService.base(requestBean);
    }
}
