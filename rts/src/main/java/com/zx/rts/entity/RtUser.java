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
 * 用户表
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("t_rt_user")
@ApiModel(value = "RtUser对象", description = "用户表")
public class RtUser extends BaseEntityBean {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "姓名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "微信号")
    private String wechatCode;

    @ApiModelProperty(value = "微信主键")
    private String wechatId;

    @ApiModelProperty(value = "微信参数姓名")
    private String wechatNickName;

    @ApiModelProperty(value = "微信参数头像地址")
    private String wechatAvatarUrl;

    @ApiModelProperty(value = "微信参数性别 0：未知、1：男、2：女")
    private Integer wechatGender;

    @ApiModelProperty(value = "微信参数城市")
    private String wechatCity;

    @ApiModelProperty(value = "微信参数省份")
    private String wechatProvince;

    @ApiModelProperty(value = "微信参数国家")
    private String wechatCountry;

    @ApiModelProperty(value = "用户类型 0村名，1村管，2系统管理员，3维修人员，4司机")
    private Integer userType;

    @ApiModelProperty(value = "审批状态 0待审批，1审批通过，2审批未过")
    private Integer approvalStatus;

    @ApiModelProperty(value = "工作状态 0在职，1离职")
    private Integer workStatus;

    @ApiModelProperty(value = "所属村居编码")
    private String villageCode;

    @ApiModelProperty(value = "所属乡镇编码")
    private String townCode;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "备注")
    private String remark;


}
