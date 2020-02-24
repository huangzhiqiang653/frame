package com.zx.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;
import org.springframework.util.StringUtils;

/**
 * 操作枚举
 *
 * @author huangzhiqiang
 */
@Getter
public enum HandleEnum {
    //默认操作
    ADD("add", "新增"),
    ADD_BATCH("addBatch", "批量新增"),
    UPDATE_ALL("updateAll", "更新所有字段"),
    UPDATE_SELECT("updateSelect", "更新有值字段"),
    UPDATE_ALL_BATCH("updateAllBatch", "批量更新所有字段"),
    UPDATE_SELECT_BATCH("updateSelectBatch", "批量更新有值字段"),
    DELETE_PHYSICAL("deletePhysical", "物理删除"),
    DELETE_LOGICAL("deleteLogical", "逻辑删除"),
    DELETE_PHYSICAL_BATCH("deletePhysicalBatch", "物理删除"),
    DELETE_LOGICAL_BATCH("deleteLogicalBatch", "逻辑删除"),
    GET_INFO_BY_ID("getInfoById", "获取单条数据"),
    GET_LIST_BY_CONDITION("getListByCondition", "根据条件查询多个"),
    GET_ALL("getAll", "查询全部"),
    GET_PAGE("getPage", "查询全部"),
    EMPTY("", "空"),
    // 根据业务需要，增加积累
    DELETE_AND_ADD("deleteAndAdd", "删除之前的数据，并新增"),
    UPDATE_SELF_INFO("updateSelfInfo", "更新主表自己"),
    GET_TREE("getTree", "获取树数据"),
    GET_PUMP("getPump", "获取树数据"),
    GET_MENU_BY_ROLE("getAuthMenu", "获取有权限的菜单"),
    LIST_ACCOUNT_BY_ROLE("listAccountByRole", "根据指定角色下的账号"),
    LIST_ROLE_BY_ACCOUNT("listRoleByAccountId", "获取指定账号设定的角色信息"),
    ADD_ROLE_ACCOUNTS_RELATION("addRoleAccountsRelation", "指定角色添加账号信息"),
    ADD_ACCOUNT_ROLES_RELATION("addAccountRolesRelation", "指定账号添加角色信息"),
    INIT_ACCOUNT_PWD("initialAccountPwd", "初始化账户密码"),
    //手机端的接口
    APP_ADD("appAdd","手机端报抽报修申请接口"),
    APP_GET_INFO_BY_ID("appGetInfoById","手机端查看详情接口")
    ;

    HandleEnum(String value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    @EnumValue
    private String value;
    private String desc;

    public static HandleEnum getTypeByValue(String value) {
        if (StringUtils.isEmpty(value)) {
            return EMPTY;
        }
        for (HandleEnum enums : HandleEnum.values()) {
            if (enums.getValue().equals(value)) {
                return enums;
            }
        }
        return EMPTY;
    }
}
