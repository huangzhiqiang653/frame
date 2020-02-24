package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.config.ExportExcel;
import com.zx.rts.dto.RtUserDto;
import com.zx.rts.entity.RtOrganization;
import com.zx.rts.entity.RtRecordPump;
import com.zx.rts.entity.RtRecordRepair;
import com.zx.rts.entity.RtUser;
import com.zx.rts.mapper.RtUserMapper;
import com.zx.rts.service.*;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
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

    @Resource
    ExportExcelService exportExcelService;
    /**
     * 导出excel需使用的表头标记
     */
    static final String INFO_EXPORT_TITLE = "村民信息";

    /**
     * 导出excel需使用的表头标记
     */
    static final String[] INFO_EXPORT_ROWNAME = new String[]{"序号","所属区划", "手机号码"};
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
            case GET_PUMP:
                return getPumpRepairInfo(requestBean);
            case ADD_DRIVER:
                return addDriver(requestBean);

            case TELL_REPAIRED_PAGE:
                return getRepairPage(requestBean);
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
        try {
            RtUserDto rtUser = BaseHzq.convertValue(requestBean.getInfo(), RtUserDto.class);
            if (StringUtils.isEmpty(rtUser.getPhoneNumber())) {
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.PHONE_NUMBER_IS_EMPTY.getValue());
            }
            //第一步，检查数据来源
            if ("1".equals(rtUser.getLy())) {
                // 数据来源手机用户

            } else {
                // 数据来源平台，平台新增，无需审核
                rtUser.setApprovalStatus(Integer.parseInt(CommonConstants.AUDIT_STATUS_TG.getCode()));
            }
            //第二步：校验用户是否存在
            QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());
            queryWrapper.eq("phone_number", rtUser.getPhoneNumber());

            Integer integer = baseMapper.selectCount(queryWrapper);
            if (integer > 0) {
                //用户存在，不允许注册
                return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.USER_REPEAT.getValue());
            }

            return new ResponseBean(this.save(rtUser));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return new ResponseBean(CommonConstants.FAIL.getCode(), e.getMessage());
        }
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
        RtUser rtUser = BaseHzq.convertValue(requestBean.getInfo(), RtUser.class);
        //根据车牌号查询
        if (!StringUtils.isEmpty(rtUser.getCarNo())) {
            queryWrapper.eq("car_no", rtUser.getCarNo());
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
     * 2020-2-21
     * @param requestBean
     * @return
     */
    public ResponseBean getPage(RequestBean requestBean) {

        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }

        // TODO 添加查询条件
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        //获取区域主键id,由id获取code
        String   quId=(String)queryMap.get("quId");
        RtOrganization rtOrganization1= rtOrganizationService.getById(quId);
        if(!StringUtils.isEmpty(rtOrganization1)){
            queryWrapper.eq("village_code", rtOrganization1.getCode()).or().eq("town_code", rtOrganization1.getCode());
        }

        //条件构造
        if (!CollectionUtils.isEmpty(queryMap)) {
            //车牌号等值查询
            if (!StringUtils.isEmpty(queryMap.get("carNo"))) {
                queryWrapper.eq("car_no", queryMap.get("carNo"));
            }
            //用户类型 villager村名(默认)，villageManage村管，systemManage系统管理员，repairPersonnel维修人员，driver司机
            if (!StringUtils.isEmpty(queryMap.get("userType"))) {
                queryWrapper.like("user_type", queryMap.get("userType"));
            }
            //姓名
            if (!StringUtils.isEmpty(queryMap.get("name"))) {
                queryWrapper.like("name", queryMap.get("name"));
            }
            //手机号
            if (!StringUtils.isEmpty(queryMap.get("phoneNumber"))) {
                queryWrapper.eq("phone_number", queryMap.get("phoneNumber"));
            }
            //审核信息
            if (!StringUtils.isEmpty(queryMap.get("approvalStatus"))) {
                queryWrapper.eq("approval_status", queryMap.get("approvalStatus"));
            }
        }
        return new ResponseBean(this.page(page, queryWrapper));
    }

    /**
     * 由村民的主键id获取报修和报抽信息基本信息
     * wangzhicheng
     * @param requestBean
     * @return
     */
    @Override
    public ResponseBean getPumpRepairInfo(RequestBean requestBean) {
        RtUser rtUser = BaseHzq.convertValue(requestBean.getInfo(), RtUser.class);
        QueryWrapper<RtRecordPump> queryWrapper1 = new QueryWrapper<>();
        QueryWrapper<RtRecordRepair> queryWrapper2 = new QueryWrapper<>();
        Map<String, Object> map = new HashMap<String, Object>();
        if (!StringUtils.isEmpty(rtUser.getId())) {
            queryWrapper1.eq("submit_user_id", rtUser.getId());
        }
        if (!StringUtils.isEmpty(rtUser.getId())) {
            queryWrapper2.eq("submit_user_id", rtUser.getId());
        }
        //获取村民报抽信息
        List<RtRecordPump> listPump = rtRecordPumpService.list(queryWrapper1);
        //获取村民报修信息
        List<RtRecordRepair> listRepair = rtRecordRepairService.list(queryWrapper2);
        map.put("pump", listPump);
        map.put("repair", listRepair);
        map.put("cunmin", rtUser);
        return new ResponseBean(map);
    }

    /**
     * 导出人员信息
     * wangzhicheng
     * @param response
     * @return
     */
    @Override
    public  void ExportRtUser(HttpServletResponse response){
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("delete_flag",1);
        List<RtUser> list = this.list(queryWrapper);
        ExportExcel ee = new ExportExcel();
        ee.setTitle(INFO_EXPORT_TITLE);
        ee.setRowName(INFO_EXPORT_ROWNAME);
        List<Map<String, Object>> expList = new ArrayList<Map<String, Object>>();
        int  i=1;
        for(RtUser rtUser:list){
            Map<String, Object> map = new HashMap<String, Object>();
            //序号
            map.put(INFO_EXPORT_ROWNAME[0],i++);
            //所属区划 //待修改
            map.put(INFO_EXPORT_ROWNAME[1],rtUser.getTownCode());
            //手机号码
            map.put(INFO_EXPORT_ROWNAME[2],rtUser.getPhoneNumber());

            expList.add(map);
        }
        ee.setDataList(expList);
        exportExcelService.export(response, ee);
    }

    /**
     * 新增单个驾驶员
     * wang
     * @param requestBean
     * @return
     */
    public ResponseBean addDriver(RequestBean requestBean) {

        RtUser  rtUser=  BaseHzq.convertValue(requestBean.getInfo(), RtUser.class);
        rtUser.setUserType("driver");
        return new ResponseBean(this.save(rtUser));
    }

    /**
     * 获取驾驶员全部信息
     * wangzhicheng
     * @param requestBean
     * @return
     */
    public ResponseBean getAllDriver(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        // TODO 添加查询条件
        // Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        queryWrapper.eq("user_type","driver").or().like("user_type","driver");
        return new ResponseBean(this.page(page, queryWrapper));
    }


    //获取可分派维修人员数据
    public ResponseBean getRepairPage(RequestBean requestBean){
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }

        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode())
        //指定维修人员
        .like("user_type", CommonConstants.USER_ROLE_REPAIRPERSONNEL.getCode())
        //指定审核通过人
        .eq("approval_status",CommonConstants.AUDIT_STATUS_TG.getCode());

        if(!StringUtils.isEmpty(queryMap.get("name"))){
            queryWrapper.like("name",queryMap.get("name"));
        }

        if(!StringUtils.isEmpty(queryMap.get("phoneNumber"))){
            queryWrapper.eq("phone_number",queryMap.get("phoneNumber"));
        }

        if(!StringUtils.isEmpty(queryMap.get("villageCode"))){
            queryWrapper.eq("village_code",queryMap.get("villageCode"));
        }

        if(!StringUtils.isEmpty(queryMap.get("townCode"))){
            queryWrapper.eq("town_code",queryMap.get("townCode"));
        }

        return new ResponseBean(baseMapper.selectPageByRepair(page, queryWrapper));
    }

}
