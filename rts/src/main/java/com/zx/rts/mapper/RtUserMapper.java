package com.zx.rts.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.zx.rts.entity.RtCars;
import com.zx.rts.entity.RtUser;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
public interface RtUserMapper extends BaseMapper<RtUser> {

    /**
     * 获取维修人员及相关维修数量
     * @param page
     * @param queryWrapper
     * @return
     */
    public IPage<RtUser> selectPageByRepair(IPage<RtUser> page, @Param(Constants.WRAPPER) Wrapper<RtUser> queryWrapper);

    /**
     * 批量修改驾驶员
     * @param userType
     * @param carNo
     * @param ids
     * @return
     */
    public int updateBatctPepairPersonnel(@Param("userType")String userType,@Param("carNo")String carNo,@Param("ids")String ids);
}
