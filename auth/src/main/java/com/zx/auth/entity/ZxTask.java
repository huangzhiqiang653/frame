package com.zx.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_task")
@ApiModel(value="ZxTask对象", description="定时任务")
public class ZxTask extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务名称")
    private String taskName;

    @ApiModelProperty(value = "目标类")
    private String targetClass;

    @ApiModelProperty(value = "目标方法")
    private String targetMethod;

    @ApiModelProperty(value = "执行时间")
    private LocalDateTime executeTime;

    @ApiModelProperty(value = "时间正则")
    private String timeRegular;

    @ApiModelProperty(value = "执行规则 0定时执行(默认)，1执行一次")
    private Integer exeuteRule;

    @ApiModelProperty(value = "任务状态 0待执行(默认)，1执行中，2已执行")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;


}
