package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.ExcelUtil;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.common.RtsCommonConstants;
import com.zx.rts.common.RtsMessageEnum;
import com.zx.rts.config.ExportExcel;
import com.zx.rts.dto.RtUserDto;
import com.zx.rts.entity.RtManageArea;
import com.zx.rts.entity.RtOrganization;
import com.zx.rts.entity.RtUser;
import com.zx.rts.mapper.RtUserMapper;
import com.zx.rts.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    private IRtManageAreaService iRtManageAreaService;


    @Value("${excel.type.users}")
    private String usersExcelType;

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
            if (RtsCommonConstants.DATA_TO_APP.getCode().equals(rtUser.getLy())) {
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
                if (!StringUtils.isEmpty(rtOrgan) && !StringUtils.isEmpty(rtOrgan.getCode())) {
                    rtUser.setTownCode(rtOrgan.getCode());
                }
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

            //保存用户数据
            this.save(rtUser);

            //添加关联配置
            if (!StringUtils.isEmpty(rtUser.getListManageArea()) && rtUser.getListManageArea().size() > 0) {
                for (RtManageArea bean : rtUser.getListManageArea()) {
                    bean.setTargetId(rtUser.getId());
                }
                //批量添加关联
                iRtManageAreaService.saveBatch(rtUser.getListManageArea());
            }

            return new ResponseBean(true);
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
        RtUserDto rtUser = BaseHzq.convertValue(requestBean.getInfo(), RtUserDto.class);
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
        if (!StringUtils.isEmpty(rtOrgan.getCode())) {
            rtUser.setTownCode(rtOrgan.getCode());
        }
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(rtUser) && !StringUtils.isEmpty(rtUser.getPhoneNumber())) {
            queryWrapper.eq("phone_number", rtUser.getPhoneNumber());
            queryWrapper.ne("id", rtUser.getId());
            queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());
            List<RtUser> list = list(queryWrapper);
            if (list != null && list.size() > 0) {
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(), RtsMessageEnum.USER_NUMBER.getValue());
            }
        }

        //关联配置
        QueryWrapper<RtManageArea> wrapper = new QueryWrapper();
        wrapper.eq("target_id", rtUser.getId());
        if (!StringUtils.isEmpty(rtUser.getRemoveManageAreaFlag())) {
            //删除关联配置标识不为空时，表示删除
            iRtManageAreaService.remove(wrapper);
        } else {
            //检查是否修改
            if (!StringUtils.isEmpty(rtUser.getListManageArea()) && rtUser.getListManageArea().size() > 0) {
                iRtManageAreaService.remove(wrapper);
                for (RtManageArea bean : rtUser.getListManageArea()) {
                    bean.setTargetId(rtUser.getId());
                }
                //添加关联
                iRtManageAreaService.saveBatch(rtUser.getListManageArea());
            }
        }
        return new ResponseBean(this.updateById(rtUser));
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
            if(!StringUtils.isEmpty(queryMap.get("remark"))  &&
               !StringUtils.isEmpty(queryMap.get("userType")) &&
               RtsCommonConstants.NOT_LIKE_ROLE.equals(queryMap.get("remark"))){

               //不等当前传入角色的其他用户
               queryWrapper.notLike("user_type", queryMap.get("userType"));

            }else if (!StringUtils.isEmpty(queryMap.get("userType"))) {
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

            //所属村居编码 -2020-2-26
            if (!StringUtils.isEmpty(queryMap.get("villageCode"))) {
                String sql = " SELECT a.code FROM t_rt_organization a " +
                        "  LEFT JOIN t_rt_organization b  " +
                        "  ON a.parent_code = b.code " +
                        "  WHERE a.`code`= " + queryMap.get("villageCode") +
                        "  OR a.parent_code=" + queryMap.get("villageCode") +
                        "  OR b.parent_code=" + queryMap.get("villageCode");
                queryWrapper.inSql("village_code", sql);

            }


        }
        return new ResponseBean(this.page(page, queryWrapper));
    }


    /**
     * 导出人员信息
     * 王志成
     *
     * @param response
     * @return
     */
    @Override
    public void exportRtUser(HttpServletResponse response) {
        //获取所有区划集合数据
        List<RtOrganization> listOrganization = rtOrganizationService.list();
        //将List集合数据转成Map集合
        Map<String, RtOrganization> organnizaMap = listOrganization.stream().collect(Collectors.toMap(RtOrganization::getCode, RtOrganization -> RtOrganization));
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
            //所属区划由人员villageCode乡村编码获取名称
            RtOrganization rtOrgan = new RtOrganization();
            if (!StringUtils.isEmpty(rtUser.getVillageCode())) {
                rtOrgan = organnizaMap.get(rtUser.getVillageCode());
                if (!StringUtils.isEmpty(rtOrgan) && !StringUtils.isEmpty(rtUser.getVillageCode())) {
                    map.put(INFO_EXPORT_ROWNAME[1], rtOrgan.getName());
                } else {
                    map.put(INFO_EXPORT_ROWNAME[1], "无");
                }

            } else {
                map.put(INFO_EXPORT_ROWNAME[1], "无");

            }
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


    /**
     * 人员信息导入
     *
     * @param file
     * @return
     */
    @Override
    public ResponseBean importRtUser(MultipartFile file) {
        //获取EXCEL数据
        ResponseBean responseBean = ExcelUtil.excelAnalysis(file, usersExcelType);
        //承载错误信息
        List<ResponseBean> errMessage = new ArrayList<>();
        if (CommonConstants.SUCCESS.getCode().equals(responseBean.getCode())) {
            List<Map<String, String>> list = (List<Map<String, String>>) responseBean.getData();
            for (int i = 0; i < list.size(); i++) {
                RtUser rtUser = new RtUser();
                rtUser.setName(list.get(i).get("name"));
                rtUser.setPhoneNumber(list.get(i).get("phoneNumber"));
                rtUser.setVillageCode(list.get(i).get("villageCode"));
                ResponseBean res = this.baseSaveUser(rtUser);
                if (CommonConstants.FAIL.getCode().equals(res.getCode())) {
                    res.setData(rtUser);
                    errMessage.add(res);
                }
            }
            return new ResponseBean(errMessage);
        } else {
            return responseBean;
        }


    }

    /**
     * 人员新增公共方法提取
     *
     * @param rtUser
     * @return
     */
    private ResponseBean baseSaveUser(RtUser rtUser) {
        //1.电话号码不为空
        if (StringUtils.isEmpty(rtUser.getPhoneNumber())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.CARS_NUMBER_IS_EMPTY.getValue());
        }

        //2.号码不能重复
        QueryWrapper<RtUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("phone_number", rtUser.getPhoneNumber());
        queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());
        List<RtUser> list = list(queryWrapper);
        if (null != list && list.size() > 0) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.CARS_REPEAT.getValue());
        }
        //3.保存人员基本信息
        this.save(rtUser);

        return new ResponseBean(true);
    }


}
