package com.zx.auth.service.impl;

import com.zx.auth.entity.ZxAccount;
import com.zx.auth.mapper.ZxAccountMapper;
import com.zx.auth.service.IZxAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 账户表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxAccountServiceImpl extends ServiceImpl<ZxAccountMapper, ZxAccount> implements IZxAccountService {

}
