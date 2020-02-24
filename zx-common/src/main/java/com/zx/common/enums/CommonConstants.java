package com.zx.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @author huangzhiqiang
 */
@Getter
public enum CommonConstants {
    // 成功
    SUCCESS("0", "成功"),
    // 失败
    FAIL("1", "失败"),
    // 无权操作
    NOAUTH("2", "无权操作~"),
    // 不可重复操作，请刷新页面
    FLUSHPAGE("3", "不可重复操作，请刷新页面~"),
    // 未登陆
    NOT_LOGIN("4", "未登录"),

    //未删除标识
    DELETE_NO("0", "未删除"),

    //已删除标识
    DELETE_YES("1", "已删除"),

    //审核状态--待审
    AUDIT_STATUS_DS("0", "待审核"),

    //审核状态--审核通过
    AUDIT_STATUS_TG("1", "审核通过"),

    //审核状态--审核不通过
    AUDIT_STATUS_BTG("2", "审核不通过"),

    //用户身份--村名
    USER_ROLE_VILLAGER("villager", "村名"),

    //用户身份--村管
    USER_ROLE_VILLAGEMANAGE("villageManage", "村管"),

    //用户身份--系统管理员
    USER_ROLE_SYSTEMMANAGE("systemManage", "系统管理员"),

    //用户身份--维修人员
    USER_ROLE_REPAIRPERSONNEL("repairPersonnel", "维修人员"),

    //用户身份--司机
    USER_ROLE_DRIVER("driver", "司机"),
    ;


    @EnumValue
    private final String code;
    private final String message;

    private CommonConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
