package com.zx.auth.service.impl;

import com.zx.auth.entity.ZxTaskLog;
import com.zx.auth.mapper.ZxTaskLogMapper;
import com.zx.auth.service.IZxTaskLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 任务日志记录表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxTaskLogServiceImpl extends ServiceImpl<ZxTaskLogMapper, ZxTaskLog> implements IZxTaskLogService {

}
