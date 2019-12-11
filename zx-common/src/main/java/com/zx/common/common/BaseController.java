package com.zx.common.common;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: law-ibeas->BaseController
 * @description: 公共控制层方法
 * @author: 黄智强
 * @create: 2019-11-12 12:11
 **/
public abstract class BaseController {

    @ApiOperation(value = "基础处理接口", notes = "基础处理接口")
    @PostMapping("/base")
    @ResponseBody
    public ResponseBean baseOrigin(@RequestBody RequestBean requestBean) {
        return this.base(requestBean);
    }

    /**
     * 公共基础方法
     *
     * @param requestBean 请求参数实体类
     * @return 返回参数
     */
    public abstract ResponseBean base(RequestBean requestBean);
}
