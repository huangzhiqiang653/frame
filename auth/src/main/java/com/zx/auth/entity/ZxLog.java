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
 * 系统日志表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_log")
@ApiModel(value = "ZxLog对象", description = "系统日志表")
public class ZxLog extends BaseSimpleEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作人主键")
    private String userId;

    @ApiModelProperty(value = "请求地址")
    private String requestUrl;

    @ApiModelProperty(value = "请求类型 GET、POST、DELETE、PUT等")
    private String requestType;

    @ApiModelProperty(value = "菜单主键")
    private String menuId;

    @ApiModelProperty(value = "资源主键")
    private String resourceId;

    @ApiModelProperty(value = "请求数据")
    private String requestData;

    @ApiModelProperty(value = "返回数据")
    private String responseData;


}
