package com.zx.auth.service.impl;

import com.zx.auth.entity.ZxLog;
import com.zx.auth.mapper.ZxLogMapper;
import com.zx.auth.service.IZxLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统日志表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxLogServiceImpl extends ServiceImpl<ZxLogMapper, ZxLog> implements IZxLogService {

}
