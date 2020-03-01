package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.entity.RtRecordRepair;
import com.zx.rts.mapper.RtRecordRepairMapper;
import com.zx.rts.service.IRtRecordRepairService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 报修记录表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Service
public class RtRecordRepairServiceImpl extends ServiceImpl<RtRecordRepairMapper, RtRecordRepair> implements IRtRecordRepairService {

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
            case APP_ADD:
                return appAdd(requestBean);
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
            case GET_MY_PAGE:
                return getMyPage(requestBean);
            case GET_PAGE_RESPAIR_CAR:
                return getPageRecordRepairCar(requestBean);
            case GET_PAGE_PUMP_CAR:
                return getPageRecordPumpCar(requestBean);

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
        return new ResponseBean(this.save(BaseHzq.convertValue(requestBean.getInfo(), RtRecordRepair.class)));
    }

    /**
     * 手机端单个新增
     * 报修
     *
     * @param requestBean
     * @return
     */
    public ResponseBean appAdd(RequestBean requestBean) {
        RtRecordRepair repair = BaseHzq.convertValue(requestBean.getInfo(), RtRecordRepair.class);
        if (!StringUtils.isEmpty(repair.getId())) {
            this.updateById(repair);
        }
        //报修申请检查
        if (StringUtils.isEmpty(repair.getSubmitUserId()) || StringUtils.isEmpty(repair.getTargetUserId())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), "报修人或待修人不能为空~");
        }
        if (StringUtils.isEmpty(repair.getProblem())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), "报修问题描述不能为空~");
        }
        return new ResponseBean(this.save(repair));
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), RtRecordRepair.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        return new ResponseBean(this.updateById(BaseHzq.convertValue(requestBean.getInfo(), RtRecordRepair.class)));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), RtRecordRepair.class)));
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
        QueryWrapper<RtRecordRepair> queryWrapper = new QueryWrapper<>();
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
        //车辆维修条件构造
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtRecordRepair> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(queryMap)) {
            //报修人主键
            if (!StringUtils.isEmpty(queryMap.get("submitUserId"))) {
                queryWrapper.eq("submit_user_id", queryMap.get("submitUserId"));
            }
            ;
            //待修人主键
            if (!StringUtils.isEmpty(queryMap.get("targetUserId"))) {
                queryWrapper.eq("target_user_id", queryMap.get("targetUserId"));
            }
            ;
            //待修人姓名
            if (!StringUtils.isEmpty(queryMap.get("targetUserName"))) {
                queryWrapper.like("target_user_name", queryMap.get("targetUserName"));
            }
            ;

            //待修人手机号
            if (!StringUtils.isEmpty(queryMap.get("targetUserPhoneNumber"))) {
                queryWrapper.eq("target_user_phone_number", queryMap.get("targetUserPhoneNumber"));

            }
            ;
            //待修人所属村居编码
            if (!StringUtils.isEmpty(queryMap.get("targetUserVillageCode"))) {
                queryWrapper.eq("target_user_village_code", queryMap.get("targetUserVillageCode"));

            }
            ;
            //待修人所属乡镇编码
            if (!StringUtils.isEmpty(queryMap.get("targetUserTownCode"))) {
                queryWrapper.eq("target_user_town_code", queryMap.get("targetUserTownCode"));

            }
            ;

            //维修人主键
            if (!StringUtils.isEmpty(queryMap.get("operationUserId"))) {
                queryWrapper.eq("operation_user_id", queryMap.get("operationUserId"));

            }
            ;
            //维修状态 0未上门，1维修中，2已维修
            if (!StringUtils.isEmpty(queryMap.get("repairStatus"))) {
                queryWrapper.eq("repair_status", queryMap.get("repairStatus"));
            }
            ;
            //是否超时 0未超时，1已超时
            if (!StringUtils.isEmpty(queryMap.get("overtimeFlag"))) {
                queryWrapper.eq("overtime_flag", queryMap.get("overtimeFlag"));
            }
            ;

        }
        return new ResponseBean(baseMapper.selectPageRtRecordRepair(page, queryWrapper));
    }


    /**
     * 管理工作太分页列表，合并报修记录和报抽记录
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getMyPage(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }
        //车辆维修条件构造
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtRecordRepair> queryWrapper = new QueryWrapper<>();
        if (!CollectionUtils.isEmpty(queryMap)) {
            //报修人主键
            if (!StringUtils.isEmpty(queryMap.get("submitUserId"))) {
                queryWrapper.eq("submit_user_id", queryMap.get("submitUserId"));
            }
            ;
            //待修人主键
            if (!StringUtils.isEmpty(queryMap.get("targetUserId"))) {
                queryWrapper.eq("target_user_id", queryMap.get("targetUserId"));
            }
            ;
            //待修人姓名
            if (!StringUtils.isEmpty(queryMap.get("targetUserName"))) {
                queryWrapper.like("target_user_name", queryMap.get("targetUserName"));
            }
            ;

            //待修人手机号
            if (!StringUtils.isEmpty(queryMap.get("targetUserPhoneNumber"))) {
                queryWrapper.eq("target_user_phone_number", queryMap.get("targetUserPhoneNumber"));

            }
            ;
            //待修人所属村居编码
            if (!StringUtils.isEmpty(queryMap.get("targetUserVillageCode"))) {
                queryWrapper.eq("target_user_village_code", queryMap.get("targetUserVillageCode"));

            }
            ;
            //待修人所属乡镇编码
            if (!StringUtils.isEmpty(queryMap.get("targetUserTownCode"))) {
                queryWrapper.eq("target_user_town_code", queryMap.get("targetUserTownCode"));

            }
            ;

            //维修人主键
            if (!StringUtils.isEmpty(queryMap.get("operationUserId"))) {
                queryWrapper.eq("operation_user_id", queryMap.get("operationUserId"));

            }
            ;
            //维修状态 0未上门，1维修中，2已维修
            if (!StringUtils.isEmpty(queryMap.get("repairStatus"))) {
                queryWrapper.eq("repair_status", queryMap.get("repairStatus"));
            }
            ;
            //是否超时 0未超时，1已超时
            if (!StringUtils.isEmpty(queryMap.get("overtimeFlag"))) {
                queryWrapper.eq("overtime_flag", queryMap.get("overtimeFlag"));
            }
            ;

            //类型 0报修，1报抽
            if (!StringUtils.isEmpty(queryMap.get("type"))) {
                queryWrapper.eq("type", queryMap.get("type"));
            }
            ;

        }
        return new ResponseBean(baseMapper.selectPageMergeRepairAndPump(page, queryWrapper));
    }

    /**
     * 获取分页人员,报修,以及报修车辆信息
     * 王志成
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getPageRecordRepairCar(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }
        //车辆维修条件构造
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtRecordRepair> queryWrapper = new QueryWrapper<>();
        commonQueryWrapper(queryMap, queryWrapper);
        return new ResponseBean(baseMapper.selectPageRecordRepairCar(page, queryWrapper));
    }

    /**
     * 获取分页人员,报抽,以及报修车辆信息
     * 王志成
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getPageRecordPumpCar(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }
        //车辆维修条件构造
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtRecordRepair> queryWrapper = new QueryWrapper<>();
        commonQueryWrapper(queryMap, queryWrapper);
        return new ResponseBean(baseMapper.selectPageRecordPumpCar(page, queryWrapper));
    }

    /**
     * 封装查询条件
     *
     * @param queryWrapper
     */
    public void commonQueryWrapper(Map queryMap, QueryWrapper<?> queryWrapper) {

        if (!CollectionUtils.isEmpty(queryMap)) {
            //报修人主键
            if (!StringUtils.isEmpty(queryMap.get("submitUserId"))) {
                queryWrapper.eq("submit_user_id", queryMap.get("submitUserId"));
            }
            ;
            //待修人主键
            if (!StringUtils.isEmpty(queryMap.get("targetUserId"))) {
                queryWrapper.eq("target_user_id", queryMap.get("targetUserId"));
            }
            ;
            //待修人姓名
            if (!StringUtils.isEmpty(queryMap.get("targetUserName"))) {
                queryWrapper.like("target_user_name", queryMap.get("targetUserName"));
            }
            ;

            //待修人手机号
            if (!StringUtils.isEmpty(queryMap.get("targetUserPhoneNumber"))) {
                queryWrapper.eq("target_user_phone_number", queryMap.get("targetUserPhoneNumber"));

            }
            ;
            //待修人所属村居编码
            if (!StringUtils.isEmpty(queryMap.get("targetUserVillageCode"))) {
                queryWrapper.eq("target_user_village_code", queryMap.get("targetUserVillageCode"));

            }
            ;
            //待修人所属乡镇编码
            if (!StringUtils.isEmpty(queryMap.get("targetUserTownCode"))) {
                queryWrapper.eq("target_user_town_code", queryMap.get("targetUserTownCode"));

            }
            ;

            //维修人主键
            if (!StringUtils.isEmpty(queryMap.get("operationUserId"))) {
                queryWrapper.eq("operation_user_id", queryMap.get("operationUserId"));

            }
            ;
            //维修状态 0未上门，1维修中，2已维修
            if (!StringUtils.isEmpty(queryMap.get("repairStatus"))) {
                queryWrapper.eq("repair_status", queryMap.get("repairStatus"));
            }
            //状态 0未抽，1已抽
            if (!StringUtils.isEmpty(queryMap.get("pumpStatus"))) {
                queryWrapper.eq("pump_status", queryMap.get("pumpStatus"));
            }
            ;
            //是否超时 0未超时，1已超时
            if (!StringUtils.isEmpty(queryMap.get("overtimeFlag"))) {
                queryWrapper.eq("overtime_flag", queryMap.get("overtimeFlag"));
            }
            ;

            //类型 0报修，1报抽
            if (!StringUtils.isEmpty(queryMap.get("type"))) {
                queryWrapper.eq("type", queryMap.get("type"));
            }
            ;

        }


    }


}
