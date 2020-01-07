package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxMenu;
import com.zx.auth.mapper.ZxMenuMapper;
import com.zx.auth.service.IZxMenuService;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
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
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), ZxMenu.class)));
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
        return new ResponseBean(this.updateById(BaseHzq.convertValue(requestBean.getInfo(), ZxMenu.class)));
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
        QueryWrapper<ZxMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("parent_id");
        queryWrapper.last("limit 1");
        ZxMenu root = this.getOne(queryWrapper);
        if(root == null){
            return new ResponseBean(
                    CommonConstants.FAIL.getCode(),
                    "获取树根节点数据失败"
            );
        }

        Map resultMap = BaseHzq.beanToMap(root);
        this.recursionGetMenu(resultMap);
        List<Map> treeList = new ArrayList<>();
        treeList.add(resultMap);
        return new ResponseBean(treeList);
    }

    /**
     * 递归获取菜单数据
     *
     * @return
     */
    public void recursionGetMenu(Map map) {
        QueryWrapper<ZxMenu> queryWrapper = new QueryWrapper<ZxMenu>();
        queryWrapper.orderByAsc("sort").eq("parent_id", map.get("id"));
        List<ZxMenu> zxMenuList = this.list(queryWrapper);
        if (CollectionUtils.isEmpty(zxMenuList)) {
            return;
        }
        List<Map> children = new ArrayList<>();
        for (ZxMenu zxMenu : zxMenuList) {
            Map child = BaseHzq.beanToMap(zxMenu);
            this.recursionGetMenu(child);
            children.add(child);
        }

        map.put("children", children);
    }
}
