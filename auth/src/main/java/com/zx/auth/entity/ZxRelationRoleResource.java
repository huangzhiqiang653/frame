package com.zx.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseSimpleEntityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 角色资源关联表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_relation_role_resource")
@ApiModel(value = "ZxRelationRoleResource对象", description = "角色资源关联表")
public class ZxRelationRoleResource extends BaseSimpleEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "角色主键")
    private String roleId;

    @ApiModelProperty(value = "菜单主键")
    private String menuId;

    @ApiModelProperty(value = "资源主键")
    private String resourceId;


}
