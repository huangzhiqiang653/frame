package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.entity.RtOrganization;
import com.zx.rts.entity.RtUser;
import com.zx.rts.mapper.RtOrganizationMapper;
import com.zx.rts.service.IRtOrganizationService;
import com.zx.rts.service.IRtUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 单位表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Service
public class RtOrganizationServiceImpl extends ServiceImpl<RtOrganizationMapper, RtOrganization> implements IRtOrganizationService {

    @Resource
    IRtUserService rtUserService;

    @Resource
    IRtOrganizationService rtOrganizationService;

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
                return getTree(requestBean);

            default:
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(),
                        SystemMessageEnum.HANDLE_NOT_IN.getValue()
                );

        }
    }

    /**
     * 单个新增
     * 2020/2/20
     * @param requestBean
     * @return
     */
    public ResponseBean add(RequestBean requestBean) {
        RtOrganization rtOrganization = BaseHzq.convertValue(requestBean.getInfo(), RtOrganization.class);
        //检测code是否唯一
        QueryWrapper<RtOrganization> queryWrapper = new QueryWrapper<RtOrganization>();
        queryWrapper.eq("code", rtOrganization.getCode());
        List<RtOrganization> list = rtOrganizationService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            return new ResponseBean(
                    CommonConstants.FAIL.getCode(),
                    "code已有请修改");
        }
        boolean saveFlag = this.save(rtOrganization);
        return saveFlag ? new ResponseBean(rtOrganization) : new ResponseBean(CommonConstants.FAIL.getCode(), "保存失败");
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), RtOrganization.class)));
    }

    /**
     * 更新单条数据所有字段
     * 2020/2/20
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {

        RtOrganization rtOrganization = BaseHzq.convertValue(requestBean.getInfo(), RtOrganization.class);
        rtOrganization.setUpdateTime(new Date());
        return new ResponseBean(this.updateById(rtOrganization));

    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), RtOrganization.class)));
    }

    /**
     * 单条逻辑删除
     * 2020/2/20  王志成
     * @param requestBean
     * @return
     */
    public ResponseBean deleteLogicalSingle(RequestBean requestBean) {
        String orgId = (String) requestBean.getInfo();
        //由主键id获取区域对象
        RtOrganization rtOrganization1=this.getById((String) requestBean.getInfo());
        // 校验该区域下可有人员数据，无可以删
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<RtUser>();
        // 2020-2-20
        queryWrapper.eq("village_code", rtOrganization1.getCode()).or().eq("town_code", rtOrganization1.getCode());
        List<RtUser> list = rtUserService.list(queryWrapper);
        if (list != null && list.size() > 0) {
            return new ResponseBean(
                    CommonConstants.FAIL.getCode(),
                    "请先删除该区域下的人员数据");
        }
        return new ResponseBean(this.removeById(orgId));
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
     * 2020/2/20
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
        QueryWrapper<RtOrganization> queryWrapper = new QueryWrapper<>();
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
        QueryWrapper<RtOrganization> queryWrapper = new QueryWrapper<>();
        // TODO 添加查询条件

        return new ResponseBean(this.page(page, queryWrapper));
    }



    /**
     * 获取区域树数据
     * 2020-2-19  王志成
     * @param requestBean
     * @return
     */
    public ResponseBean getTree(RequestBean requestBean) {
        QueryWrapper<RtOrganization> queryWrapper = new QueryWrapper<RtOrganization>();
        queryWrapper.isNull("parent_id");
        List<RtOrganization> organizationList = this.list(queryWrapper);
        if (organizationList != null && organizationList.size() == 1) {
            RtOrganization root = organizationList.get(0);
            Map resultMap = BaseHzq.beanToMap(root);
            this.dGGetOrg(resultMap);
            List<Map> treeList = new ArrayList<Map>();
            treeList.add(resultMap);
            return new ResponseBean(treeList);
        } else {
            return new ResponseBean(
                    CommonConstants.FAIL.getCode(),
                    "获取树根节点数据失败"
            );
        }
    }
    /**
     * 递归获取区域数据
     * 2020-2-19  王志成
     * @return
     */
    public void dGGetOrg(Map map) {
        QueryWrapper<RtOrganization> queryWrapper = new QueryWrapper<RtOrganization>();
        queryWrapper.orderByAsc("sort").eq("parent_id", map.get("id"));
        List<RtOrganization> rtOrganizations = this.list(queryWrapper);
        if (rtOrganizations != null && rtOrganizations.size() > 0) {
            List<Map> children = new ArrayList<Map>();
            for (RtOrganization rtOrganization : rtOrganizations) {
                Map child = BaseHzq.beanToMap(rtOrganization);
                this.dGGetOrg(child);
                children.add(child);
            }
            map.put("children", children);
        }
    }

}
