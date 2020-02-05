package com.zx.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.auth.entity.ZxAccount;
import com.zx.auth.mapper.SystemMapper;
import com.zx.auth.mapper.ZxAccountMapper;
import com.zx.auth.service.IZxAccountService;
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
 * 账户表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2019-12-04
 */
@Service
public class ZxAccountServiceImpl extends ServiceImpl<ZxAccountMapper, ZxAccount> implements IZxAccountService {

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
            case LIST_ACCOUNT_BY_ROLE:
                return listAccountByRole(requestBean);
            default:
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(),
                        SystemMessageEnum.HANDLE_NOT_IN.getValue()
                );

        }
    }

    @Override
    public ResponseBean listAccountByRole(RequestBean requestBean) {
        String roleId = (String) requestBean.getInfo();
        if (StringUtils.isEmpty(roleId)) {
            return new ResponseBean();
        }

        List<ZxAccount> zxAccounts = getZxRoleAccounts(roleId);
        return new ResponseBean(zxAccounts);
    }

    private List<ZxAccount> getZxRoleAccounts(String roleId) {
        if (StringUtils.isEmpty(roleId)) {
            return new ArrayList<>(0);
        }

        ZxAccountMapper accountMapper = this.getBaseMapper();
        return accountMapper.listAccountByRoleId(roleId);
    }

    /**
     * 单个新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean add(RequestBean requestBean) {
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), ZxAccount.class)));
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), ZxAccount.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        ZxAccount zxAccount = BaseHzq.convertValue(requestBean.getInfo(), ZxAccount.class);
        zxAccount.setUpdateTime(systemMapper.getNow());
        return new ResponseBean(this.updateById(zxAccount));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), ZxAccount.class)));
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
        QueryWrapper<ZxAccount> queryWrapper = new QueryWrapper<ZxAccount>();
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
        QueryWrapper<ZxAccount> queryWrapper = new QueryWrapper<ZxAccount>();
        queryWrapper.orderByAsc("status");
        queryWrapper.orderByDesc("update_time");

        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        List<String> updateTimes = null;
        if (!CollectionUtils.isEmpty(queryMap)) {
            if (queryMap.get("updateTime") != null) {
                updateTimes = (List<String>) queryMap.get("updateTime");
                queryMap.remove("updateTime");
            }
            //授权标志：false 只查询未授权，true 只查询已授权，未空则查询全部
            if (queryMap.get("authFlag") != null) {
                String roleId = (String) queryMap.get("roleId");
                boolean authFlag = Boolean.getBoolean((String)queryMap.get("authFlag"));
                queryMap.remove("roleId");
                queryMap.remove("authFlag");
                List<ZxAccount> zxAccounts = getZxRoleAccounts(roleId);
                if (CollectionUtils.isEmpty(zxAccounts)) {
                    Set<String> authAccountIds = new HashSet<>();
                    for (ZxAccount account : zxAccounts) {
                        authAccountIds.add(account.getId());
                    }

                    if (authFlag) {
                        queryWrapper.in("id", authAccountIds);
                    } else {
                        queryWrapper.notIn("id", authAccountIds);
                    }
                }
            }
        }

        ZxAccount zxAccount = BaseHzq.convertValue(queryMap, ZxAccount.class);
        // 查询条件
        if (!StringUtils.isEmpty(zxAccount.getAccountName())) {
            queryWrapper.like("account_name", zxAccount.getAccountName().trim());
        }
        if (!StringUtils.isEmpty(zxAccount.getStatus())) {
            queryWrapper.eq("status", zxAccount.getStatus());
        }
        if (updateTimes != null && updateTimes.size() == 2) {
            queryWrapper.between("update_time", updateTimes.get(0), updateTimes.get(1));
        }
        return new ResponseBean(this.page(page, queryWrapper));
    }
}
