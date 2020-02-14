package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxAccount;
import com.zx.auth.entity.ZxUser;
import com.zx.auth.mapper.SystemMapper;
import com.zx.auth.mapper.ZxUserMapper;
import com.zx.auth.service.IZxAccountService;
import com.zx.auth.service.IZxUserService;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.BusinessEnum;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxUserServiceImpl extends ServiceImpl<ZxUserMapper, ZxUser> implements IZxUserService {
    @Resource
    SystemMapper systemMapper;
    @Resource
    IZxAccountService zxAccountService;

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
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), ZxUser.class)));
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), ZxUser.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        ZxUser zxUser = BaseHzq.convertValue(requestBean.getInfo(), ZxUser.class);
        zxUser.setUpdateTime(systemMapper.getNow());
        // 如果用户禁用，则该用户下的账号全部禁用
        if (StringUtils.isEmpty(zxUser.getStatus()) && zxUser.getStatus().equals(Integer.parseInt(BusinessEnum.USER_NORMAL.getValue()))) {
            QueryWrapper<ZxAccount> queryWrapper = new QueryWrapper<ZxAccount>();
            queryWrapper.eq("user_id", zxUser.getId());
            queryWrapper.eq("status", Integer.parseInt(BusinessEnum.ACCOUNT_NORMAL.getValue()));
            List<ZxAccount> accountList = zxAccountService.list(queryWrapper);
            if (accountList != null && accountList.size() > 0) {
                for (ZxAccount zxAccount : accountList) {
                    zxAccount.setStatus(Integer.parseInt(BusinessEnum.ACCOUNT_PROHIBIT.getValue()));
                    zxAccount.setUpdateTime(systemMapper.getNow());
                }
                zxAccountService.updateBatchById(accountList);
            }
        }
        return new ResponseBean(this.updateById(zxUser));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxUser.class)));
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
        QueryWrapper<ZxUser> queryWrapper = new QueryWrapper<ZxUser>();
        queryWrapper.orderByDesc("update_time");
        queryWrapper.eq("status", 0);
        Map queryMap = (Map) requestBean.getInfo();
        ZxUser zxUser = BaseHzq.convertValue(queryMap, ZxUser.class);
        // 查询条件
        if (!StringUtils.isEmpty(zxUser.getUserName())) {
            queryWrapper.like("user_name", zxUser.getUserName().trim());
        }
        if (!StringUtils.isEmpty(zxUser.getEmail())) {
            queryWrapper.like("email", zxUser.getEmail().trim());
        }
        if (!StringUtils.isEmpty(zxUser.getPhoneNumber())) {
            queryWrapper.like("phone_number", zxUser.getPhoneNumber().trim());
        }
        if (!StringUtils.isEmpty(zxUser.getSex())) {
            queryWrapper.eq("sex", zxUser.getSex());
        }
        if (!StringUtils.isEmpty(zxUser.getStatus())) {
            queryWrapper.eq("status", zxUser.getStatus());
        }
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
        QueryWrapper<ZxUser> queryWrapper = new QueryWrapper<ZxUser>();
        queryWrapper.orderByAsc("status");
        queryWrapper.orderByDesc("update_time");

        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        List<String> updateTimes = (List<String>) queryMap.get("updateTime");
        String keyWords = (String) queryMap.get("keyWords");
        boolean includeChildrenFlag = StringUtils.isEmpty(queryMap.get("childrenInclude")) ? false : (boolean) queryMap.get("childrenInclude");
        queryMap.remove("updateTime");
        queryMap.remove("keyWords");
        queryMap.remove("childrenInclude");
        ZxUser zxUser = BaseHzq.convertValue(queryMap, ZxUser.class);
        // 查询条件
        if (!StringUtils.isEmpty(zxUser.getUserName())) {
            queryWrapper.like("user_name", zxUser.getUserName().trim());
        }
        if (!StringUtils.isEmpty(zxUser.getEmail())) {
            queryWrapper.like("email", zxUser.getEmail().trim());
        }
        if (!StringUtils.isEmpty(zxUser.getPhoneNumber())) {
            queryWrapper.like("phone_number", zxUser.getPhoneNumber().trim());
        }
        if (!StringUtils.isEmpty(zxUser.getSex())) {
            queryWrapper.eq("sex", zxUser.getSex());
        }
        if (!StringUtils.isEmpty(zxUser.getStatus())) {
            queryWrapper.eq("status", zxUser.getStatus());
        }
        if (updateTimes != null && updateTimes.size() == 2) {
            queryWrapper.between("update_time", updateTimes.get(0), updateTimes.get(1));
        }
        if (!StringUtils.isEmpty(zxUser.getOrganizationId())) {
            if (includeChildrenFlag) {
                String[] orgs = zxUser.getOrganizationId().split(",");
                String selectId = orgs[orgs.length - 2];
                queryWrapper.like("organization_id", selectId);
            } else {
                queryWrapper.eq("organization_id", zxUser.getOrganizationId());
            }
        }
        if (!StringUtils.isEmpty(keyWords)) {
            queryWrapper.and(new Consumer<QueryWrapper<ZxUser>>() {
                @Override
                public void accept(QueryWrapper<ZxUser> zxUserQueryWrapper) {
                    zxUserQueryWrapper.like("user_name", keyWords.trim()).or()
                            .like("email", keyWords.trim()).or()
                            .like("phone_number", keyWords.trim());
                }
            });
        }
        return new ResponseBean(this.page(page, queryWrapper));
    }

}
