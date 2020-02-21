package com.zx.rts.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zx.rts.entity.RtCars;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 车辆表 Mapper 接口
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
public interface RtCarsMapper extends BaseMapper<RtCars> {

    public IPage<RtCars> selectPageRtCars(IPage<RtCars> page, @Param(Constants.WRAPPER) Wrapper<RtCars> queryWrapper);
}
