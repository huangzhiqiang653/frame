package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxOrganization;
import com.zx.auth.entity.ZxRelaitonAccountRole;
import com.zx.auth.entity.ZxRelationRoleMenu;
import com.zx.auth.entity.ZxRelaitonAccountRole;
import com.zx.auth.mapper.ZxRelaitonAccountRoleMapper;
import com.zx.auth.service.IZxRelaitonAccountRoleService;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色资源关联表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxRelaitonAccountRoleServiceImpl extends ServiceImpl<ZxRelaitonAccountRoleMapper, ZxRelaitonAccountRole> implements IZxRelaitonAccountRoleService {

    @Override
    public ResponseBean base(RequestBean requestBean) {
        switch (requestBean.getHandle()) {
            case ADD:
                return add(requestBean);
            case ADD_ROLE_ACCOUNTS_RELATION:
                return addRoleAccountsRelation(requestBean);
            case ADD_ACCOUNT_ROLES_RELATION:
                return addAccountRolesRelation(requestBean);
            case ADD_BATCH:
                return addBatch(requestBean);
            case UPDATE_ALL:
                return updateAllField(requestBean);
            case UPDATE_ALL_BATCH:
                return updateAllFieldBatch(requestBean);
            case DELETE_LOGICAL:
                return deleteLogicalSingle(requestBean);
            case DELETE_LOGICAL_BATCH:
                return deleteLogicalBatch(requestBean);
            case GET_INFO_BY_ID:
                return getInfoById(requestBean);
            case GET_LIST_BY_CONDITION:
                return getListByCondition(requestBean);
            case GET_ALL:
                return getAll();
            case GET_PAGE:
                return getPage(requestBean);
            default:
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(),
                        SystemMessageEnum.HANDLE_NOT_IN.getValue()
                );

        }
    }
    /**
     * 单个新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean add(RequestBean requestBean) {
        ZxRelaitonAccountRole zxRelaitonAccountRole = BaseHzq.convertValue(requestBean.getInfo(), ZxRelaitonAccountRole.class);
        boolean saveFlag = this.save(zxRelaitonAccountRole);
        return saveFlag ? new ResponseBean(zxRelaitonAccountRole) : new ResponseBean(CommonConstants.FAIL.getCode(), "保存失败");
    }

    /**
     * 指定角色添加账号信息
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addRoleAccountsRelation(RequestBean requestBean) {
        // 获取其他数据
        Map object = (LinkedHashMap) requestBean.getInfo();
        String roleId = (String) object.get("roleId");
        List<String> accountIds = (List<String>) object.get("accountIds");
        //构建角色菜单关系对象
        List<ZxRelaitonAccountRole> accountRoleResourceList = null;
        if(!CollectionUtils.isEmpty(accountIds)) {
            accountRoleResourceList = new ArrayList<>();
            for (String accountId : accountIds) {
                ZxRelaitonAccountRole zxRelationRoleResource = new ZxRelaitonAccountRole();
                zxRelationRoleResource.setRoleId(roleId);
                zxRelationRoleResource.setAccountId(accountId);
                accountRoleResourceList.add(zxRelationRoleResource);
            }
        }
        //删除旧关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", roleId);
        this.remove(queryWrapper);
        //保存
        if(CollectionUtils.isEmpty(accountRoleResourceList)){
           return new ResponseBean(CommonConstants.SUCCESS.getCode());
        }

        boolean saveFlag = this.saveBatch(accountRoleResourceList);
        return saveFlag ? new ResponseBean(CommonConstants.SUCCESS.getCode()) : new ResponseBean(CommonConstants.FAIL.getCode(), "保存失败");
    }

    /**
     * 指定账号添加角色信息
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addAccountRolesRelation(RequestBean requestBean) {
        // 获取其他数据
        Map object = (LinkedHashMap) requestBean.getInfo();
        String accountId = (String) object.get("accountId");
        List<String> roleIds = (List<String>) object.get("roleIds");
        //构建角色菜单关系对象
        List<ZxRelaitonAccountRole> accountRoleResourceList = null;
        if(!CollectionUtils.isEmpty(roleIds)) {
            accountRoleResourceList = new ArrayList<>();
            for (String roleId : roleIds) {
                ZxRelaitonAccountRole zxRelationRoleResource = new ZxRelaitonAccountRole();
                zxRelationRoleResource.setRoleId(roleId);
                zxRelationRoleResource.setAccountId(accountId);
                accountRoleResourceList.add(zxRelationRoleResource);
            }
        }
        //删除旧关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("account_id", accountId);
        this.remove(queryWrapper);
        //保存
        if(CollectionUtils.isEmpty(accountRoleResourceList)){
           return new ResponseBean(CommonConstants.SUCCESS.getCode());
        }

        boolean saveFlag = this.saveBatch(accountRoleResourceList);
        return saveFlag ? new ResponseBean(CommonConstants.SUCCESS.getCode()) : new ResponseBean(CommonConstants.FAIL.getCode(), "保存失败");
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), ZxRelationRoleMenu.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        ZxRelaitonAccountRole zxRelationRoleResource = BaseHzq.convertValue(requestBean.getInfo(), ZxRelaitonAccountRole.class);
        return new ResponseBean(this.updateById(zxRelationRoleResource));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxRelaitonAccountRole.class)));
    }

    /**
     * 单条逻辑删除
     *
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalSingle(RequestBean requestBean) {
        String id = (String) requestBean.getInfo();
        return new ResponseBean(this.removeById(id));
    }

    /**
     * 批量逻辑删除
     *
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalBatch(RequestBean requestBean) {
        List<String> ids = (List<String>) requestBean.getInfo();
        return new ResponseBean(this.removeByIds(ids));
    }

    /**
     * 根据主键获取单条数据
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getInfoById(RequestBean requestBean) {
        return new ResponseBean(this.getById((String) requestBean.getInfo()));
    }

    /**
     * 根据条件查询数据
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getListByCondition(RequestBean requestBean) {
        ZxRelaitonAccountRole zxRelationRoleMenu = BaseHzq.convertValue(requestBean.getInfo(), ZxRelaitonAccountRole.class);
        QueryWrapper<ZxRelaitonAccountRole> queryWrapper = new QueryWrapper<ZxRelaitonAccountRole>();
        if (!StringUtils.isEmpty(zxRelationRoleMenu.getRoleId())) {
            queryWrapper.eq("role_id", zxRelationRoleMenu.getRoleId());
        }

        return new ResponseBean(this.list(queryWrapper));
    }

    /**
     * 获取全部数据
     *
     * @return
     */
    public ResponseBean getAll() {
        return new ResponseBean(this.list());
    }


    /**
     * 获取分页数据
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getPage(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }
        QueryWrapper<ZxRelaitonAccountRole> queryWrapper = new QueryWrapper<ZxRelaitonAccountRole>();
        // TODO 添加查询条件

        return new ResponseBean(this.page(page, queryWrapper));
    }
}
