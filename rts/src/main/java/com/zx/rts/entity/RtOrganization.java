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
 * 单位表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rt_organization")
@ApiModel(value = "RtOrganization对象", description = "单位表")
public class RtOrganization extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "组织名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "父主键")
    private String parentId;

    @ApiModelProperty(value = "父编码")
    private String parentCode;

    @ApiModelProperty(value = "类型 0乡镇，1村居")
    private Integer type;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
