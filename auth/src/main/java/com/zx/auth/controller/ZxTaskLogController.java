package com.zx.auth.controller;


import com.zx.common.common.BaseController;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 任务日志记录表 前端控制器
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/auth/zx-task-log")
public class ZxTaskLogController extends BaseController {

    /**
     * 公共基础方法
     *
     * @param requestBean 请求参数实体类
     * @return 返回参数
     */
    @Override
    public ResponseBean base(RequestBean requestBean) {
        return null;
    }
}
