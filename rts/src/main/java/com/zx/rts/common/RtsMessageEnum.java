package com.zx.rts.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * Rts系统提示枚举
 *
 * @author huamei09
 */
@Getter
public enum RtsMessageEnum {
    PHONE_NUMBER_IS_EMPTY("手机号不能为空"),
    USER_REPEAT("新增失败，用户号码已存在"),
    USER_NUMBER("用户号码存在请修改"),
    CARS_REPEAT("车牌号已存在，操作失败"),
    CARS_NUMBER_IS_EMPTY("车牌号不为空"),
    CARS_FORMAT_ERROR("车牌号格式不正确"),
    ADD_USER_SUCCESS("添加驾驶员信息成功"),
    CODE_LIVE("code已有请修改"),
    SAVE_FALURE("保存失败"),
    DISTRICT_LIVE_PEOPLE("请先删除该区域下的人员数据"),
    TREE_POINT("获取树根节点数据失败");

    RtsMessageEnum(String value) {
        this.value = value;
    }

    @EnumValue
    private String value;
}