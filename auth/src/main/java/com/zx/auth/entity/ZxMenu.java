package com.zx.auth.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zx.common.common.BaseEntityBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_menu")
@ApiModel(value = "ZxMenu对象", description = "菜单表")
public class ZxMenu extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "父主键")
    private String parentId;

    @ApiModelProperty(value = "层级")
    private Integer level;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "编码")
    private String code;

    @ApiModelProperty(value = "类型")
    private String menuType;

    @ApiModelProperty(value = "图标")
    private String icon;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "地址")
    private String url;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @Transient
    private transient List<ZxResource> zxResourceList;

}
