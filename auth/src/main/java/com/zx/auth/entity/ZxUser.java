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
 * 用户表
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("auth_zx_user")
@ApiModel(value="ZxUser对象", description="用户表")
public class ZxUser extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名")
    private String userName;

    @ApiModelProperty(value = "性别 0女，1男，2未知(默认)")
    private Integer sex;

    @ApiModelProperty(value = "出生年月日")
    private LocalDateTime birthDay;

    @ApiModelProperty(value = "头像地址")
    private String headUrl;

    @ApiModelProperty(value = "头像文件")
    private String headBlod;

    @ApiModelProperty(value = "所属机构")
    private String organizationId;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "手机号码")
    private String phoneNumber;

    @ApiModelProperty(value = "用户状态 0启用(默认)，1禁用")
    private Integer status;

    @ApiModelProperty(value = "备注")
    private String remark;


}
