package com.zx.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * 系统提示枚举
 *
 * @author huangzhiqiang
 */
@Getter
public enum SystemMessageEnum {
    // type匹配失败提示
    HANDLE_NOT_IN("type类型匹配失败，可选项(add,addBatch,updateAll,updateAllBatch,updateSelect,updateSelectBatch,deletePhysical,deletePhysicalBatch,deleteLogical,deleteLogicalBatch,getInfoById,getListByCondition,getAll,getPage)"),
    // 参数为空提示
    ENTITY_IS_NULL("参数为空"),
    //账号不存在
    ACCOUNT_IS_NULL("账号不存在"),
    ACCOUNT_IS_LIVE("账号已存在"),
    ACCOUNT_PASSWORD_IS_ERROR("密码错误"),
    ;

    SystemMessageEnum(String value) {
        this.value = value;
    }

    @EnumValue
    private String value;
}
