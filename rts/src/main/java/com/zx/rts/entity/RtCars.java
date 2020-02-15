package com.zx.rts.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 车辆表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rt_cars")
@ApiModel(value = "RtCars对象", description = "车辆表")
public class RtCars extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车牌号")
    private String carNo;

    @ApiModelProperty(value = "所属村居编码")
    private String villageCode;

    @ApiModelProperty(value = "所属乡镇编码")
    private String townCode;

    @ApiModelProperty(value = "当值司机")
    private String dutyUserId;


}
