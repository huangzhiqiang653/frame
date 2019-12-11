package com.zx.auth.service.impl;

import com.zx.auth.entity.ZxTask;
import com.zx.auth.mapper.ZxTaskMapper;
import com.zx.auth.service.IZxTaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 定时任务 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxTaskServiceImpl extends ServiceImpl<ZxTaskMapper, ZxTask> implements IZxTaskService {

}
