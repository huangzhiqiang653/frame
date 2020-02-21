package com.zx.rts.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.rts.entity.RtUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
public interface IRtUserService extends IService<RtUser> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);

    /**
     * 由用户id获取保修和报抽信息
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getPumpRepairInfo(RequestBean requestBean);

}
