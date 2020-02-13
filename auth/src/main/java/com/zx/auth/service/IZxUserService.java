package com.zx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.auth.entity.ZxUser;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface IZxUserService extends IService<ZxUser> {
    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);





    /**
     *2020-2-12
     * @param zxUser
     * @return
     */
    public ResponseBean addAccountUser(ZxUser zxUser)  ;


}
