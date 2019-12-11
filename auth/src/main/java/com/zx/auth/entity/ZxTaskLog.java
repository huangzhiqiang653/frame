package com.zx.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseSimpleEntityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 任务日志记录表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_task_log")
@ApiModel(value = "ZxTaskLog对象", description = "任务日志记录表")
public class ZxTaskLog extends BaseSimpleEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务主键")
    private String taskId;

    @ApiModelProperty(value = "执行时间")
    private LocalDateTime executeTime;

    @ApiModelProperty(value = "执行结果 0成功(默认)，1失败，2未知")
    private Integer executeResult;

    @ApiModelProperty(value = "相关数据")
    private String data;


}
