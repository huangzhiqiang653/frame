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
 * 管理区域关联表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rt_manage_area")
@ApiModel(value="RtManageArea对象", description="管理区域关联表")
public class RtManageArea extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "车辆或人员主键")
    private String targetId;

    @ApiModelProperty(value = "行政区划主键")
    private String orgId;

    @ApiModelProperty(value = "行政区划编码")
    private String orgCode;


}
