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
 * 数据扩展表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_data_extend")
@ApiModel(value="ZxDataExtend对象", description="数据扩展表")
public class ZxDataExtend extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "表名")
    private String systemTableName;

    @ApiModelProperty(value = "数据主键")
    private String dataId;

    @ApiModelProperty(value = "字段名称")
    private String fieldName;

    @ApiModelProperty(value = "字段类型")
    private String fieldType;

    @ApiModelProperty(value = "字段值")
    private String fieldValue;


}
