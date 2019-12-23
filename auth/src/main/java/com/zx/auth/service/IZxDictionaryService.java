package com.zx.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zx.auth.entity.ZxDictionary;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
public interface IZxDictionaryService extends IService<ZxDictionary> {

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
    public ResponseBean base(RequestBean requestBean);

}
