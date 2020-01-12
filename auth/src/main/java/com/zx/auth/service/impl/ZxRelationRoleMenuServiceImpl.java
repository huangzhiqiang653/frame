package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxRelationRoleMenu;
import com.zx.auth.entity.ZxRelationRoleResource;
import com.zx.auth.mapper.ZxRelationRoleMenuMapper;
import com.zx.auth.service.IZxRelationRoleMenuService;
import com.zx.auth.service.IZxRelationRoleResourceService;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色菜单关联表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxRelationRoleMenuServiceImpl extends ServiceImpl<ZxRelationRoleMenuMapper, ZxRelationRoleMenu> implements IZxRelationRoleMenuService {
    @Resource
    IZxRelationRoleResourceService zxRelationRoleResourceService;

    @Override
    public ResponseBean base(RequestBean requestBean) {
        switch (requestBean.getHandle()) {
            case ADD:
                return add(requestBean);
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
        // 获取其他数据
        Map object = (LinkedHashMap) requestBean.getInfo();
        List<String> menuIds = (ArrayList) object.get("menuIds");
        Map<String, List<String>> menuResourceIds = (Map) object.get("menuResourceIds");
        String roleId = (String) object.get("roleId");
        //构建角色菜单关系对象
        if (CollectionUtils.isEmpty(menuIds)) {
            return new ResponseBean(CommonConstants.FAIL.getCode(),
                    "菜单信息为空");
        }

        List<ZxRelationRoleMenu> roleMenuList = new ArrayList<>();
        for (String menuId : menuIds) {
            ZxRelationRoleMenu zxRelationRoleMenu = new ZxRelationRoleMenu();
            zxRelationRoleMenu.setRoleId(roleId);
            zxRelationRoleMenu.setMenuId(menuId);
            roleMenuList.add(zxRelationRoleMenu);
        }

        //删除旧关系
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role_id", roleId);
        this.remove(queryWrapper);
        if (!CollectionUtils.isEmpty(menuResourceIds)) {
            List<ZxRelationRoleResource> roleResourceArrayList = new ArrayList<>();
            for (String menuId : menuResourceIds.keySet()) {
                List<String> resourceList = menuResourceIds.get(menuId);
                if (CollectionUtils.isEmpty(resourceList)) {
                    continue;
                }

                for (String resourceId : resourceList) {
                    ZxRelationRoleResource zxRelationRoleResource = new ZxRelationRoleResource();
                    zxRelationRoleResource.setRoleId(roleId);
                    zxRelationRoleResource.setMenuId(menuId);
                    zxRelationRoleResource.setResourceId(resourceId);
                    roleResourceArrayList.add(zxRelationRoleResource);
                }

                //删除已存在的关系
                zxRelationRoleResourceService.remove(queryWrapper);
                //保存
                zxRelationRoleResourceService.saveBatch(roleResourceArrayList);
            }
        }
        //保存
        boolean saveFlag = this.saveBatch(roleMenuList);
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
        ZxRelationRoleMenu zxRelationRoleMenu = BaseHzq.convertValue(requestBean.getInfo(), ZxRelationRoleMenu.class);
        return new ResponseBean(this.updateById(zxRelationRoleMenu));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxRelationRoleMenu.class)));
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
        ZxRelationRoleMenu zxRelationRoleMenu = BaseHzq.convertValue(requestBean.getInfo(), ZxRelationRoleMenu.class);
        QueryWrapper<ZxRelationRoleMenu> queryWrapper = new QueryWrapper<ZxRelationRoleMenu>();
        if (!StringUtils.isEmpty(zxRelationRoleMenu.getMenuId())) {
            queryWrapper.eq("menu_id", zxRelationRoleMenu.getMenuId());
        }

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
        QueryWrapper<ZxRelationRoleMenu> queryWrapper = new QueryWrapper<ZxRelationRoleMenu>();
        // TODO 添加查询条件

        return new ResponseBean(this.page(page, queryWrapper));
    }
}
