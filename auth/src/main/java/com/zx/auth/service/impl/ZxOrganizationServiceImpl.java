package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxOrganization;
import com.zx.auth.mapper.SystemMapper;
import com.zx.auth.mapper.ZxOrganizationMapper;
import com.zx.auth.service.IZxOrganizationService;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 组织表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxOrganizationServiceImpl extends ServiceImpl<ZxOrganizationMapper, ZxOrganization> implements IZxOrganizationService {

    @Resource
    SystemMapper systemMapper;

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
                return getOrgTree(requestBean);
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
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), ZxOrganization.class)));
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), ZxOrganization.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        ZxOrganization zxOrganization = BaseHzq.convertValue(requestBean.getInfo(), ZxOrganization.class);
        zxOrganization.setUpdateTime(systemMapper.getNow());
        return new ResponseBean(this.updateById(zxOrganization));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxOrganization.class)));
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
        QueryWrapper<ZxOrganization> queryWrapper = new QueryWrapper<ZxOrganization>();
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
        QueryWrapper<ZxOrganization> queryWrapper = new QueryWrapper<ZxOrganization>();
        // TODO 添加查询条件

        return new ResponseBean(this.page(page, queryWrapper));
    }

    /**
     * 获取组织树数据
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getOrgTree(RequestBean requestBean) {
        QueryWrapper<ZxOrganization> queryWrapper = new QueryWrapper<ZxOrganization>();
        queryWrapper.isNull("parent_id");
        List<ZxOrganization> organizationList = this.list(queryWrapper);
        if (organizationList != null && organizationList.size() == 1) {
            ZxOrganization root = organizationList.get(0);
            Map resultMap = BaseHzq.beanToMap(root);
            this.recursionGetOrg(resultMap);
            return new ResponseBean(resultMap);
        } else {
            return new ResponseBean(
                    CommonConstants.FAIL.getCode(),
                    "获取树根节点数据失败"
            );
        }
    }

    /**
     * 递归获取组织数据
     *
     * @return
     */
    public void recursionGetOrg(Map map) {
        QueryWrapper<ZxOrganization> queryWrapper = new QueryWrapper<ZxOrganization>();
        queryWrapper.orderByAsc("sort").eq("parent_id", map.get("id"));
        List<ZxOrganization> zxOrganizations = this.list(queryWrapper);
        if (zxOrganizations != null && zxOrganizations.size() > 0) {
            List<Map> children = new ArrayList<Map>();
            for (ZxOrganization zxOrganization : zxOrganizations) {
                Map child = BaseHzq.beanToMap(zxOrganization);
                this.recursionGetOrg(child);
                children.add(child);
            }
            map.put("children", children);
        }
    }
}
