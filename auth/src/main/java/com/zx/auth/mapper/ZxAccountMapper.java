package com.zx.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zx.auth.entity.ZxAccount;

import java.util.List;

/**
 * <p>
 * 账户表 Mapper 接口
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface ZxAccountMapper extends BaseMapper<ZxAccount> {
    List<ZxAccount> listAccountByRoleId(String roleId);
}
