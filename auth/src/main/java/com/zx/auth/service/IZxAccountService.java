package com.zx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.auth.entity.ZxAccount;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;

import java.util.List;

/**
 * <p>
 * 账户表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface IZxAccountService extends IService<ZxAccount> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);

    /**
     * 根据角色获取账号信息
     *
     * @param requestBean
     * @return
     */
    public ResponseBean listAccountByRole(RequestBean requestBean);

    /**
     * 根据参数获取有效的账号信息
     *
     * @param zxAccount 账号对象
     * @return 账号信息
     */
    public List<ZxAccount> listByAccount(ZxAccount zxAccount) throws Exception;





}
