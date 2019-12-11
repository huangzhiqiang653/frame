package com.zx.auth.service.impl;

import com.zx.auth.entity.ZxUser;
import com.zx.auth.mapper.ZxUserMapper;
import com.zx.auth.service.IZxUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxUserServiceImpl extends ServiceImpl<ZxUserMapper, ZxUser> implements IZxUserService {

}
