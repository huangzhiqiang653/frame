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
 * 配置表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rt_config")
@ApiModel(value = "RtConfig对象", description = "配置表")
public class RtConfig extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "配置名称")
    private String configName;

    @ApiModelProperty(value = "配置编码")
    private String configCode;

    @ApiModelProperty(value = "配置参数")
    private String configParam;


}
