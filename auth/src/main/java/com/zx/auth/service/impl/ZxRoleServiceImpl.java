package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxRelationRoleMenu;
import com.zx.auth.entity.ZxRelationRoleResource;
import com.zx.auth.entity.ZxRole;
import com.zx.auth.mapper.ZxAccountMapper;
import com.zx.auth.mapper.ZxRoleMapper;
import com.zx.auth.service.IZxRelationRoleMenuService;
import com.zx.auth.service.IZxRelationRoleResourceService;
import com.zx.auth.service.IZxRoleService;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxRoleServiceImpl extends ServiceImpl<ZxRoleMapper, ZxRole> implements IZxRoleService {
    @Resource
    IZxRelationRoleMenuService relationRoleMenuService;
    @Resource
    IZxRelationRoleResourceService relationRoleResourceService;

    /**
     * 公共基础方法
     *
     * @param requestBean
     * @return
     */
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
            case GET_MENU_BY_ROLE:
                return getMenuByRole(requestBean);
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
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), ZxRole.class)));
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), ZxRole.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        return new ResponseBean(this.updateById(BaseHzq.convertValue(requestBean.getInfo(), ZxRole.class)));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxRole.class)));
    }

    /**
     * 单条逻辑删除
     *
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalSingle(RequestBean requestBean) {
        return new ResponseBean(this.removeById((String) requestBean.getInfo()));
    }

    /**
     * 批量逻辑删除
     *
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalBatch(RequestBean requestBean) {
        return new ResponseBean(this.removeByIds((Collection<String>) requestBean.getInfos()));
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
        QueryWrapper<ZxRole> queryWrapper = new QueryWrapper<ZxRole>();
        // TODO 添加查询条件

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
        QueryWrapper<ZxRole> queryWrapper = new QueryWrapper<ZxRole>();
        // TODO 添加查询条件

        return new ResponseBean(this.page(page, queryWrapper));
    }

    /**
     * 根据角色获取菜单信息（包含资源信息）
     *
     * @param requestBean
     * @return
     */
    @Override
    public ResponseBean getMenuByRole(RequestBean requestBean) {
        ZxRole role = BaseHzq.convertValue(requestBean.getInfo(), ZxRole.class);
        String roleId = role.getId();
        Map<String, Object> map = new HashMap<>();
        QueryWrapper<ZxRelationRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<ZxRelationRoleMenu> menuList = relationRoleMenuService.list(queryWrapper);
        if (!CollectionUtils.isEmpty(menuList)) {
            List<String> menuIds = new ArrayList<>();
            map.put("menuIds",menuIds);
            for (ZxRelationRoleMenu menu : menuList) {
                String menuId = menu.getMenuId();
                menuIds.add(menuId);
            }

            QueryWrapper<ZxRelationRoleResource> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("role_id", roleId);
            queryWrapper2.in("menu_id", menuIds);
            List<ZxRelationRoleResource> resourceList = relationRoleResourceService.list(queryWrapper2);
            if (!CollectionUtils.isEmpty(resourceList)) {
                Map<String, List<String>> subMap = new HashMap<>();
                map.put("menuResourceIds", subMap);
                for (ZxRelationRoleResource resource : resourceList) {
                    String menuId = resource.getMenuId();
                    List<String> resourceIds = subMap.get(menuId);
                    if(CollectionUtils.isEmpty(resourceIds)){
                        resourceIds = new ArrayList<>();
                        subMap.put(menuId, resourceIds);
                    }

                    resourceIds.add(resource.getResourceId());
                }
            }
        }

        return new ResponseBean(map);
    }


}
