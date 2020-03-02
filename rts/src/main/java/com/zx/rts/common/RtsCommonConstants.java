package com.zx.rts.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * Rts系统共享的参数枚举
 *
 * @author huamei09
 */
@Getter
public enum RtsCommonConstants {
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

    //数据来源手机端
    DATA_TO_APP("1", "数据来源手机端"),

    //数据来源WEB端
    DATA_TO_WEB("0", "数据来源WEB端"),

    //成功
    SUCCESS("success", "成功"),

    //失败
    ERROR("error", "失败"),

    //是
    YSE("0", "成功"),

    //否
    NO("1", "失败"),


    NOT_LIKE_ROLE("notListRole", "除去当前角色的其他用户"),

    //分派状态 --已分派
    ASSIGN_STATUS_ASSIGNED("assigned", "已分派"),

    //分派状态 --已结办
    ASSIGN_STATUS_FINISH("finish", "已办结"),

    //分派状态--未分派
    ASSIGN_STATUS_UNASSIGNED("unassigned", "未分派"),

    //报修标识
    BX_FLAG("0", "报修"),

    //报抽标识
    BC_FLAG("1", "保抽"),
    ;

    @EnumValue
    private final String code;
    private final String message;

    private RtsCommonConstants(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
