package com.zx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.auth.entity.ZxMenu;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface IZxMenuService extends IService<ZxMenu> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);

    /**
     * 获取菜单树数据
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getMenuTree(RequestBean requestBean);

    /**
     * 根据角色Id获取角色下的菜单权限
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    public List<ZxMenu> listMenuByRoleId(String roleId);
}
