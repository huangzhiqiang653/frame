package com.zx.rts.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 报抽记录表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rt_record_pump")
@ApiModel(value = "RtRecordPump对象", description = "报抽记录表")
public class RtRecordPump extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "报抽人主键")
    private String submitUserId;

    @ApiModelProperty(value = "待抽人主键")
    private String targetUserId;

    @ApiModelProperty(value = "抽取司机主键")
    private String operationUserId;

    @ApiModelProperty(value = "抽取车主键")
    private String pumpCarId;

    @ApiModelProperty(value = "报修时间")
    private LocalDateTime reportTime;

    @ApiModelProperty(value = "冗余字段1 0未上门，1维修中，2已维修")
    private Integer repairStatus;

    @ApiModelProperty(value = "完成时间")
    private LocalDateTime finishTime;

    @ApiModelProperty(value = "状态 0未抽，1已抽")
    private Integer pumpStatus;

    @ApiModelProperty(value = "问题描述")
    private String problem;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "类型 0报修，1报抽")
    private Integer type;

    @ApiModelProperty(value = "是否超时 0未超时，1已超时")
    private Integer overtimeFlag;


}
