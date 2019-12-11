package com.zx.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 组织表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_organization")
@ApiModel(value="ZxOrganization对象", description="组织表")
public class ZxOrganization extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "名称全程")
    private String fullName;

    @ApiModelProperty(value = "简称")
    private String shortName;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "组织类型")
    private String type;

    @ApiModelProperty(value = "父主键")
    private String parentId;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "排序")
    private Integer sort;


}
