package com.zx.rts.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import com.zx.rts.entity.RtManageArea;
import com.zx.rts.entity.RtUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * <p>
 * 用户表 DTO
 * </p>
 *
 * @author shenyang
 * @since 2020-02-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RtUserDto extends RtUser {

    @ApiModelProperty(value = "数据来源 0 平台 1手机用户")
    @TableField(exist = false)
    private String ly;

    @ApiModelProperty(value = "待维修数量")
    @TableField(exist = false)
    private String notRepairedNum;


    @ApiModelProperty(value = "关联配置")
    @TableField(exist = false)
    private List<RtManageArea> listManageArea;


    @ApiModelProperty(value = "删除关联标识")
    @TableField(exist = false)
    private String removeManageAreaFlag;


    @ApiModelProperty(value = "村民所属地")
    @TableField(exist = false)
    private String addressName;
}
