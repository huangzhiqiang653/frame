package com.zx.auth.controller;


import com.zx.auth.entity.ZxMenu;
import com.zx.auth.entity.ZxResource;
import com.zx.auth.entity.ZxRole;
import com.zx.auth.mapper.ZxMenuMapper;
import com.zx.auth.mapper.ZxResourceMapper;
import com.zx.auth.service.IZxRoleService;
import com.zx.common.common.BaseController;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色表 前端控制器
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@RestController
@RequestMapping("/auth/zx-role")
public class ZxRoleController extends BaseController {

    @Resource
    IZxRoleService zxRoleService;
    /**
     * 公共基础方法
     *
     * @param requestBean 请求参数实体类
     * @return 返回参数
     */
    @Override
    public ResponseBean base(RequestBean requestBean) {
        return zxRoleService.base(requestBean);
    }
}
