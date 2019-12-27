package com.zx.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 业务类型枚举类
 *
 * @author huangzhiqiang
 */
@Getter
public enum BusinessEnum {
    // 诉讼类、非诉讼类、风险、邮箱
    CASES("cases", "诉讼类案件模块"),
    NONE_CASES("noneCases", "非诉讼类案件模块"),
    RISK("ibeas", "风险模块"),
    CASES_EMAIL("casesEmail", "诉讼类案件模块(提醒邮件)"),
    NONE_CASES_EMAIL("noneCasesEmail", "非诉讼类案件模块(提醒邮件)"),
    MAIL_NOT_SEND("0", "邮件发送标记(未发送)"),
    MAIL_SEND("1", "邮件发送标记(已发送)"),
    ACCOUNT_NORMAL("0", "账号状态正常"),
    ACCOUNT_PROHIBIT("1", "账号状态禁用"),
    USER_NORMAL("0", "用户状态正常"),
    USER_PROHIBIT("1", "用户状态禁用"),

    ;

    BusinessEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @EnumValue
    private String value;
    private String desc;
}
