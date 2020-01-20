package com.zx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.auth.entity.ZxRole;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface IZxRoleService extends IService<ZxRole> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);

    /*
     * 根据角色获取菜单（包含菜单下资源）信息
     * @param requestBean
     * @return
     */
    public ResponseBean getMenuByRole(RequestBean requestBean);
}
