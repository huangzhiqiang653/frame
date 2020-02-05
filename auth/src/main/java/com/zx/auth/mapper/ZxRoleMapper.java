package com.zx.auth.mapper;

import com.zx.auth.entity.ZxAccount;
import com.zx.auth.entity.ZxRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 角色表 Mapper 接口
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface ZxRoleMapper extends BaseMapper<ZxRole> {
    List<ZxRole> listRoleByAccountId(String roleId);
}
