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
import com.zx.rts.entity.RtRecordPump;
import com.zx.rts.entity.RtRecordRepair;
import com.zx.rts.entity.RtUser;
import com.zx.rts.mapper.RtUserMapper;
import com.zx.rts.service.IRtOrganizationService;
import com.zx.rts.service.IRtRecordPumpService;
import com.zx.rts.service.IRtRecordRepairService;
import com.zx.rts.service.IRtUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Service
public class RtUserServiceImpl extends ServiceImpl<RtUserMapper, RtUser> implements IRtUserService {

    @Resource
    IRtOrganizationService rtOrganizationService;


    @Resource
    IRtRecordPumpService rtRecordPumpService;


    @Resource
    IRtRecordRepairService rtRecordRepairService;
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
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), RtUser.class)));
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), RtUser.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        return new ResponseBean(this.updateById(BaseHzq.convertValue(requestBean.getInfo(), RtUser.class)));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), RtUser.class)));
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
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
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
     * 2020-2-21
     * @param requestBean
     * @return
     */
    public ResponseBean getPage(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        // TODO 添加查询条件  name
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        //获取区域主键id,由id获取code  phone_number
        String   quId=(String)queryMap.get("quId");
        RtOrganization rtOrganization1= rtOrganizationService.getById(quId);
        if(!StringUtils.isEmpty(rtOrganization1)){
            queryWrapper.eq("village_code", rtOrganization1.getCode()).or().eq("town_code", rtOrganization1.getCode());
        }
        RtUser rtUser = BaseHzq.convertValue(queryMap, RtUser.class);
        if (!StringUtils.isEmpty(rtUser.getName())) {
            queryWrapper.like("name", rtUser.getName());
        }
        if (!StringUtils.isEmpty(rtUser.getPhoneNumber())) {
            queryWrapper.like("phone_number", rtUser.getPhoneNumber());
        }

        return new ResponseBean(this.page(page, queryWrapper));
    }

    /**
     * 由保修人的主键id获取报修和报抽信息
     *
     * @param requestBean
     * @return
     */
    @Override
    public ResponseBean getPumpRepairInfo(RequestBean requestBean){
        RtUser rtUser = BaseHzq.convertValue(requestBean.getInfo(), RtUser.class);
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        QueryWrapper<RtRecordPump> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<RtRecordRepair> queryWrapper2 = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<String, Object>();

        if (!StringUtils.isEmpty(rtUser.getPhoneNumber())) {
            queryWrapper.like("phone_number", rtUser.getPhoneNumber());
        }
        if (!StringUtils.isEmpty(rtUser.getAddress())) {
            queryWrapper.like("address", rtUser.getAddress());
        }
        if (!StringUtils.isEmpty(rtUser.getId())) {
            queryWrapper1.eq("submit_user_id", rtUser.getId());
        }
        if (!StringUtils.isEmpty(rtUser.getId())) {
            queryWrapper2.eq("submit_user_id", rtUser.getId());
        }
          if (!StringUtils.isEmpty(rtUser.getId())) {
            queryWrapper1.eq("submit_user_id", rtUser.getId());
        }
        List<RtRecordPump> listPump = rtRecordPumpService.list(queryWrapper1);

        List<RtRecordRepair> listRepair = rtRecordRepairService.list(queryWrapper2);

        map.put("pump",listPump);

        map.put("repair",listRepair);
        return    new   ResponseBean(map);
    }




}
