package com.zx.rts.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zx.rts.entity.RtCars;
import com.zx.rts.entity.RtManageArea;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 车辆信息DTO
 *
 * @author huamei09
 * @since 2020年2月24日16:08:23
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RtCarsDto extends RtCars {
    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "待抽取数量")
    private String notPumpNum;



    @ApiModelProperty(value = "关联配置")
    @TableField(exist = false)
    private List<RtManageArea> listManageArea;


    @ApiModelProperty(value = "删除关联标识")
    @TableField(exist = false)
    private String removeManageAreaFlag;

}
