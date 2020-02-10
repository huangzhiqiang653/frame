package com.zx.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zx.auth.entity.ZxMenu;
import com.zx.auth.entity.ZxResource;

import java.util.List;

/**
 * <p>
 * 菜单表 Mapper 接口
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface ZxMenuMapper extends BaseMapper<ZxMenu> {
    /**
     * 根据角色id获取该角色下的菜单列表
     *
     * @param roleId 角色id
     * @return
     */
    List<ZxMenu> listMenuByRoleId(String roleId);

    /**
     * 根据角色id获取该角色下的资源列表
     *
     * @param roleId 角色id
     * @return
     */
    List<ZxResource> listResourceByRoleId(String roleId);
}
