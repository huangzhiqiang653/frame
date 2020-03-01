package com.zx.rts.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zx.rts.entity.RtRecordRepair;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 报修记录表 Mapper 接口
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
public interface RtRecordRepairMapper extends BaseMapper<RtRecordRepair> {

    //自定义分页，关联用户表
    public IPage<RtRecordRepair> selectPageRtRecordRepair(IPage<RtRecordRepair> page, @Param(Constants.WRAPPER) Wrapper<RtRecordRepair> queryWrapper);


    //自定义分页，合并报修报抽记录，并关联用户信息
    public IPage<RtRecordRepair> selectPageMergeRepairAndPump(IPage<RtRecordRepair> page, @Param(Constants.WRAPPER) Wrapper<RtRecordRepair> queryWrapper);

    /**
     * 自定义分页,关联用户表,车辆表
     *
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<RtRecordRepair> selectPageRecordRepairCar(IPage<RtRecordRepair> page, @Param(Constants.WRAPPER) Wrapper<RtRecordRepair> queryWrapper);

    public int repairOvertimeTinspect(Integer dayTime);
}
