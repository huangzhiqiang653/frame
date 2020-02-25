package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.common.RtsCommonConstants;
import com.zx.rts.common.RtsMessageEnum;
import com.zx.rts.config.ExportExcel;
import com.zx.rts.dto.RtUserDto;
import com.zx.rts.entity.RtOrganization;
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
    static final String[] INFO_EXPORT_ROWNAME = new String[]{"序号", "所属区划", "手机号码"};

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
            case TELL_REPAIRED_PAGE:
                return getRepairPage(requestBean);
            case GET_USER_IDS:
                return getUserIds(requestBean);
            case GET_PAGE_DRIVER:
                return getAllDriver(requestBean);
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
                return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.PHONE_NUMBER_IS_EMPTY.getValue());
            }
            //第一步，检查数据来源
            if ("1".equals(rtUser.getLy())) {
                // 数据来源手机用户
            } else {
                // 数据来源平台，平台新增，无需审核
                rtUser.setApprovalStatus(Integer.parseInt(CommonConstants.AUDIT_STATUS_TG.getCode()));
                //王志成 2020-2-24 保存用户区域信息编号 由village_code获取town_code编号
                QueryWrapper<RtOrganization> queryWrapperOrgan = new QueryWrapper<>();
                if (!StringUtils.isEmpty(rtUser) && !StringUtils.isEmpty(rtUser.getVillageCode())) {
                    queryWrapperOrgan.eq("code", rtUser.getVillageCode());
                }
                List<RtOrganization> list = rtOrganizationService.list(queryWrapperOrgan);
                RtOrganization rtOrgan = new RtOrganization();
                if (!StringUtils.isEmpty(list) && list.size() == 1) {
                    rtOrgan = rtOrganizationService.getById(list.get(0).getParentId());
                }
                rtUser.setTownCode(rtOrgan.getCode());
            }
            //第二步：校验用户是否存在
            QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());
            queryWrapper.eq("phone_number", rtUser.getPhoneNumber());

            Integer integer = baseMapper.selectCount(queryWrapper);
            if (integer > 0) {
                //用户存在，不允许注册
                return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.USER_REPEAT.getValue());
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
     * 王志成
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        //检查用户号码唯一
        RtUser rtUser = BaseHzq.convertValue(requestBean.getInfo(), RtUser.class);
        //获取区域上级编码
        QueryWrapper<RtOrganization> queryWrapperOrgan = new QueryWrapper<>();
        if (!StringUtils.isEmpty(rtUser) && !StringUtils.isEmpty(rtUser.getVillageCode())) {
            queryWrapperOrgan.eq("code", rtUser.getVillageCode());
        }
        List<RtOrganization> listOrgan = rtOrganizationService.list(queryWrapperOrgan);
        RtOrganization rtOrgan = new RtOrganization();
        if (!StringUtils.isEmpty(listOrgan) && listOrgan.size() == 1) {
            rtOrgan = rtOrganizationService.getById(listOrgan.get(0).getParentId());
        }
        rtUser.setTownCode(rtOrgan.getCode());
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(rtUser) && !StringUtils.isEmpty(rtUser.getPhoneNumber())) {
            queryWrapper.eq("phone_number", rtUser.getPhoneNumber());
            queryWrapper.ne("id", rtUser.getId());
            List<RtUser> list = list(queryWrapper);
            if (list != null && list.size() > 0) {
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(), RtsMessageEnum.USER_NUMBER.getValue());
            }
        }
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
     *
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

            //根据村居编码查询村名信息2020/2/24   --王志成
            if (!StringUtils.isEmpty(queryMap.get("villageCode"))) {
                QueryWrapper<RtOrganization> queryWrapperOrgan = new QueryWrapper<RtOrganization>();
                queryWrapperOrgan.eq("code", queryMap.get("villageCode"));
                List<RtOrganization> list = rtOrganizationService.list(queryWrapperOrgan);
                if (!StringUtils.isEmpty(list) && list.size() == 1 && !StringUtils.isEmpty(list.get(0).getParentId())) {
                    queryWrapper.eq("village_code", queryMap.get("villageCode")).or().eq("town_code", queryMap.get("villageCode"));
                } else {
                    //展示所有数据
                    return new ResponseBean(this.page(page, queryWrapper));
                }
            }
            //根据乡镇编码查询村名信息2020/2/24
            /*if (!StringUtils.isEmpty(queryMap.get("townCode"))) {
                queryWrapper.eq("town_code", queryMap.get("townCode"));
            }*/

        }
        return new ResponseBean(this.page(page, queryWrapper));
    }


    /**
     * 导出人员信息
     * wangzhicheng
     *
     * @param response
     * @return
     */
    @Override
    public void ExportRtUser(HttpServletResponse response) {
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("delete_flag", 1);
        List<RtUser> list = this.list(queryWrapper);
        ExportExcel ee = new ExportExcel();
        ee.setTitle(INFO_EXPORT_TITLE);
        ee.setRowName(INFO_EXPORT_ROWNAME);
        List<Map<String, Object>> expList = new ArrayList<Map<String, Object>>();
        int i = 1;
        for (RtUser rtUser : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            //序号
            map.put(INFO_EXPORT_ROWNAME[0], i++);
            //所属区划 //待修改
            map.put(INFO_EXPORT_ROWNAME[1], rtUser.getTownCode());
            //手机号码
            map.put(INFO_EXPORT_ROWNAME[2], rtUser.getPhoneNumber());

            expList.add(map);
        }
        ee.setDataList(expList);
        exportExcelService.export(response, ee);
    }

    /**
     * 获取驾驶员全部信息
     * wangzhicheng
     *
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
        queryWrapper.eq("user_type", "driver").or().like("user_type", "driver");
        return new ResponseBean(this.page(page, queryWrapper));
    }

    /**
     * 获取用户id信息,添加驾驶员类型
     * 王志成
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getUserIds(RequestBean requestBean) {
        //获取用户id
        String ids = (String) requestBean.getInfo();
        String[] orgs = ids.split(",");
        for (String e : orgs) {
            if (!StringUtils.isEmpty(e)) {
                RtUser rtUser = this.getById(e);
                rtUser.setUserType(rtUser.getUserType() + "," + "driver");
                this.updateById(rtUser);

            }
        }
        return new ResponseBean(CommonConstants.SUCCESS.getCode(), RtsMessageEnum.ADD_USER_SUCCESS.getValue());

    }


    //获取可分派维修人员数据
    public ResponseBean getRepairPage(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }

        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode())
                //指定维修人员
                .like("user_type", RtsCommonConstants.USER_ROLE_REPAIRPERSONNEL.getCode())
                //指定审核通过人
                .eq("approval_status", CommonConstants.AUDIT_STATUS_TG.getCode());

        if (!StringUtils.isEmpty(queryMap.get("name"))) {
            queryWrapper.like("name", queryMap.get("name"));
        }

        if (!StringUtils.isEmpty(queryMap.get("phoneNumber"))) {
            queryWrapper.eq("phone_number", queryMap.get("phoneNumber"));
        }

        if (!StringUtils.isEmpty(queryMap.get("villageCode"))) {
            queryWrapper.eq("village_code", queryMap.get("villageCode"));
        }

        if (!StringUtils.isEmpty(queryMap.get("townCode"))) {
            queryWrapper.eq("town_code", queryMap.get("townCode"));
        }

        return new ResponseBean(baseMapper.selectPageByRepair(page, queryWrapper));
    }

}
