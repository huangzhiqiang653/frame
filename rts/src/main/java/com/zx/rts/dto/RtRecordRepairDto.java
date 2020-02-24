package com.zx.rts.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import com.zx.rts.entity.RtRecordRepair;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 报修记录表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RtRecordRepairDto extends RtRecordRepair {

    @TableField(exist = false)
    @ApiModelProperty(value = "待修人姓名")
    private String targetUserName;


    @TableField(exist = false)
    @ApiModelProperty(value = "待修人手机号")
    private String targetUserPhoneNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "待修人所属村居编码")
    private String targetUserVillageCode;

    @TableField(exist = false)
    @ApiModelProperty(value = "待修人所属乡镇编码")
    private String targetUserTownCode;

}
