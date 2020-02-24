package com.zx.rts.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zx.rts.entity.RtCars;
import com.zx.rts.entity.RtUser;
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

    //车辆表。关联用户信息
    public IPage<RtCars> selectPageRtCars(IPage<RtCars> page, @Param(Constants.WRAPPER) Wrapper<RtCars> queryWrapper);


    //分派车辆信息
    public IPage<RtCars> selectPageByPump(IPage<RtCars> page, @Param(Constants.WRAPPER) Wrapper<RtCars> queryWrapper);
}
