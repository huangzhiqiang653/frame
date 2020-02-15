package com.zx.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.rts.entity.RtRecordPump;

/**
 * <p>
 * 报抽记录表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
public interface IRtRecordPumpService extends IService<RtRecordPump> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);
}
