package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxMenu;
import com.zx.auth.entity.ZxResource;
import com.zx.auth.mapper.SystemMapper;
import com.zx.auth.mapper.ZxMenuMapper;
import com.zx.auth.service.IZxMenuService;
import com.zx.auth.service.IZxResourceService;
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
 * 菜单表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxMenuServiceImpl extends ServiceImpl<ZxMenuMapper, ZxMenu> implements IZxMenuService {
    @Resource
    SystemMapper systemMapper;

    @Resource
    IZxResourceService resourceService;

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
            case GET_TREE:
                return getMenuTree(requestBean);
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
        ZxMenu zxMenu = BaseHzq.convertValue(requestBean.getInfo(), ZxMenu.class);
        boolean saveFlag = this.save(zxMenu);
        return saveFlag ? new ResponseBean(zxMenu) : new ResponseBean(CommonConstants.FAIL.getCode(), "保存失败");
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), ZxMenu.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        ZxMenu zxMenu = BaseHzq.convertValue(requestBean.getInfo(), ZxMenu.class);
        zxMenu.setUpdateTime(systemMapper.getNow());
        return new ResponseBean(this.updateById(zxMenu));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxMenu.class)));
    }

    /**
     * 单条逻辑删除
     *
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalSingle(RequestBean requestBean) {
        String menuId = (String) requestBean.getInfo();
        //删除菜单资源信息
        QueryWrapper<ZxResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("relation_id", menuId);
        resourceService.remove(queryWrapper);
        return new ResponseBean(this.removeById(menuId));
    }

    /**
     * 批量逻辑删除
     *
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalBatch(RequestBean requestBean) {
        List<String> menuIds = (List<String>) requestBean.getInfo();
        //删除菜单资源信息
        QueryWrapper<ZxResource> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("relation_id", menuIds);
        resourceService.remove(queryWrapper);
        return new ResponseBean(this.removeByIds(menuIds));
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
        QueryWrapper<ZxMenu> queryWrapper = new QueryWrapper<ZxMenu>();
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
        QueryWrapper<ZxMenu> queryWrapper = new QueryWrapper<ZxMenu>();
        // TODO 添加查询条件

        return new ResponseBean(this.page(page, queryWrapper));
    }

    /**
     * 获取菜单树
     *
     * @param requestBean
     * @return
     */
    @Override
    public ResponseBean getMenuTree(RequestBean requestBean) {
        Map object = (LinkedHashMap) requestBean.getInfo();
        boolean showResource = false;
        if (object != null && !StringUtils.isEmpty(object.get("showResource"))) {
            showResource = (Boolean) object.get("showResource");
        }

        QueryWrapper<ZxMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("parent_id");
        queryWrapper.last("limit 1");
        ZxMenu root = this.getOne(queryWrapper);
        if (root == null) {
            return new ResponseBean(
                    CommonConstants.FAIL.getCode(),
                    "获取树根节点数据失败"
            );
        }

        Map resultMap = BaseHzq.beanToMap(root);
        this.recursionGetMenu(resultMap, showResource);
        List<Map> treeList = new ArrayList<>();
        treeList.add(resultMap);
        return new ResponseBean(treeList);
    }

    /**
     * 递归获取菜单数据
     *
     * @return
     */
    public void recursionGetMenu(Map map, boolean isShowResource) {
        QueryWrapper<ZxMenu> queryWrapper = new QueryWrapper<ZxMenu>();
        queryWrapper.orderByAsc("sort").eq("parent_id", map.get("id"));
        List<ZxMenu> zxMenuList = this.list(queryWrapper);
        List<Map> children = new ArrayList<>();
        if (CollectionUtils.isEmpty(zxMenuList)) {
            if (isShowResource) {
                QueryWrapper<ZxResource> subQueryWrapper = new QueryWrapper<>();
                subQueryWrapper.in("relation_id", map.get("id"));
                List<ZxResource> resourceList = resourceService.list(subQueryWrapper);
                if (!CollectionUtils.isEmpty(resourceList)) {
                    for (ZxResource resource : resourceList) {
                        Map child = BaseHzq.beanToMap(resource);
                        child.put("name", resource.getResourceName());
                        child.put("isResource", true);
                        children.add(child);
                    }

                    map.put("children", children);
                }
            }
            return;
        }

        for (ZxMenu zxMenu : zxMenuList) {
            Map child = BaseHzq.beanToMap(zxMenu);
            this.recursionGetMenu(child, isShowResource);
            children.add(child);
        }
        map.put("children", children);
    }


    /**
     * 根据角色Id获取角色下的菜单及资源权限
     *
     * @param roleId 角色id
     * @return
     */
    @Override
    public List<ZxMenu> listMenuByRoleId(String roleId) {
        ZxMenuMapper zxMenuMapper = this.getBaseMapper();
        List<ZxMenu> menuList = zxMenuMapper.listMenuByRoleId(roleId);
        if (CollectionUtils.isEmpty(menuList)) return new ArrayList<>(0);
        List<ZxResource> resourceList = zxMenuMapper.listResourceByRoleId(roleId);
        if (CollectionUtils.isEmpty(resourceList)) return menuList;
        for (ZxMenu menu : menuList) {
            for (ZxResource resource : resourceList) {
                if (menu.getId().equals(resource.getRelationId())) {
                    List<ZxResource> list = menu.getZxResourceList();
                    if (list == null) {
                        list = new ArrayList<>();
                        menu.setZxResourceList(list);
                    }

                    list.add(resource);
                }
            }
        }

        return menuList;
    }
}
