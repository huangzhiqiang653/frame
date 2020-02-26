package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.*;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.common.RtsMessageEnum;
import com.zx.rts.config.ExportExcel;
import com.zx.rts.dto.RtCarsDto;
import com.zx.rts.entity.RtCars;
import com.zx.rts.entity.RtManageArea;
import com.zx.rts.mapper.RtCarsMapper;
import com.zx.rts.service.ExportExcelService;
import com.zx.rts.service.IRtCarsService;
import com.zx.rts.service.IRtManageAreaService;
import com.zx.rts.service.IRtOrganizationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>
 * 车辆表 服务实现类
 * </p>
 *
 * @author 黄智强
 * @since 2020-02-15
 */
@Service
public class RtCarsServiceImpl extends ServiceImpl<RtCarsMapper, RtCars> implements IRtCarsService {
    @Resource
    IRtOrganizationService rtOrganizationService;

    @Resource
    private IRtManageAreaService iRtManageAreaService;

    @Resource
    private ExportExcelService exportExcelService;

    @Value("${excel.type.cars}")
    private String carsExcelType;


    /**
     * 导出excel需使用的表头标记
     */
    public static final String[] INFO_EXPORT_ROWNAME = new String[]{"序号", "人员信息", "所属区划", "手机号码", "车牌号码"};

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
            case TELL_PUMP_PAGE:
                return getPumpPage(requestBean);
            default:
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(),
                        SystemMessageEnum.HANDLE_NOT_IN.getValue()
                );

        }
    }

    /**
     * 车辆信息导出
     *
     * @param response
     */
    @Override
    public void ExportRtCar(HttpServletResponse response) {

        List<RtCarsDto> list = baseMapper.selectCarList(new RtCars());
        ExportExcel ee = new ExportExcel();
        ee.setTitle(RtsMessageEnum.INFO_EXPORT_TITLE_CARS.getValue());
        ee.setRowName(INFO_EXPORT_ROWNAME);
        List<Map<String, Object>> expList = new ArrayList<Map<String, Object>>();
        int i = 1;
        for (RtCarsDto rtCars : list) {
            Map<String, Object> map = new HashMap<String, Object>();
            //序号
            map.put(INFO_EXPORT_ROWNAME[0], i++);
            //人员姓名
            map.put(INFO_EXPORT_ROWNAME[1], rtCars.getName());
            //所属区划
            String qh =
                    (StringUtils.isEmpty(rtCars.getTownCode()) ? "" : rtCars.getTownCode()) +
                            (StringUtils.isEmpty(rtCars.getVillageCode()) ? "" : rtCars.getVillageCode());
            map.put(INFO_EXPORT_ROWNAME[2], qh);
            //手机号
            map.put(INFO_EXPORT_ROWNAME[3], rtCars.getPhoneNumber());
            //车牌号
            map.put(INFO_EXPORT_ROWNAME[4], rtCars.getCarNo());
            expList.add(map);
        }
        ee.setDataList(expList);
        exportExcelService.export(response, ee);

    }

    /**
     * 车辆信息导入
     *
     * @param file
     */
    @Override
    public ResponseBean importRtCar(MultipartFile file) {
        //获取EXCEL数据
        ResponseBean responseBean = ExcelUtil.excelAnalysis(file, carsExcelType);
        //承载错误信息
        List<ResponseBean> errMessage =new ArrayList<>();

        if (CommonConstants.SUCCESS.getCode().equals(responseBean.getCode())) {
            List<Map<String, String>> list =(List<Map<String, String>>) responseBean.getData();
            for (int i = 0; i < list.size(); i++) {
                RtCarsDto rtCars = new RtCarsDto();
                rtCars.setCarNo(list.get(i).get("carNo"));
                rtCars.setVillageCode(list.get(i).get("villageCode"));
                ResponseBean res = this.baseSave(rtCars);
                if(CommonConstants.FAIL.getCode().equals(res.getCode())){
                    res.setData(rtCars);
                    errMessage.add(res);
                }
            }
            return new ResponseBean(errMessage);
        } else {
            return responseBean;
        }

    }

    /**
     * 单个新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean add(RequestBean requestBean) {

        RtCarsDto rtCars = BaseHzq.convertValue(requestBean.getInfo(), RtCarsDto.class);
        return this.baseSave(rtCars);
    }

    /**
     * 车辆新增公共方法提取
     * @param rtCars
     * @return
     */
    private ResponseBean baseSave(RtCarsDto rtCars) {
        //车辆新增，需要验证车牌号是否存在，及车牌号是否正确
        //第一步:校验车牌号
        if (StringUtils.isEmpty(rtCars.getCarNo())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.CARS_NUMBER_IS_EMPTY.getValue());
        } else {
            //忽略车牌大小写
            rtCars.setCarNo(rtCars.getCarNo().toUpperCase().trim());
        }
        ;
        if (!CommonUtil.checkPlateNumberFormat(rtCars.getCarNo())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.CARS_FORMAT_ERROR.getValue());
        }

        //第二步：校验车牌是否存在
        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_no", rtCars.getCarNo());
        queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());
        Integer integer = baseMapper.selectCount(queryWrapper);
        if (integer > 0) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), RtsMessageEnum.CARS_REPEAT.getValue());
        }
        //第三步保存车辆基本信息
        this.save(rtCars);

        //第四步保存车辆关联配置区域
        if (!StringUtils.isEmpty(rtCars.getListManageArea()) && rtCars.getListManageArea().size() > 0) {
            for (RtManageArea bean : rtCars.getListManageArea()) {
                bean.setTargetId(rtCars.getId());
            }
            //添加关联
            iRtManageAreaService.saveBatch(rtCars.getListManageArea());
        }

        return new ResponseBean(true);
    }

    /**
     * 批量新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean addBatch(RequestBean requestBean) {
        return new ResponseBean(this.saveBatch(BaseHzq.convertValue(requestBean.getInfos(), RtCars.class)));
    }

    /**
     * 更新单条数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllField(RequestBean requestBean) {
        //检查用户号码唯一
        RtCarsDto carsDto = BaseHzq.convertValue(requestBean.getInfo(), RtCarsDto.class);
        if (!StringUtils.isEmpty(carsDto) && !StringUtils.isEmpty(carsDto.getCarNo())) {
            carsDto.setCarNo(carsDto.getCarNo().toUpperCase().trim());//转换大小写
            QueryWrapper<RtCars> queryWrapper = new QueryWrapper();
            queryWrapper.eq("car_no", carsDto.getCarNo());
            queryWrapper.ne("id", carsDto.getId());
            List<RtCars> list = super.list(queryWrapper);
            if (list != null && list.size() > 0) {
                return new ResponseBean(
                        CommonConstants.FAIL.getCode(), RtsMessageEnum.CARS_REPEAT.getValue());
            }
        }


        //关联区划配置
        QueryWrapper<RtManageArea> wrapper = new QueryWrapper();
        wrapper.eq("target_id", carsDto.getId());
        if (!StringUtils.isEmpty(carsDto.getRemoveManageAreaFlag())) {
            //删除关联配置标识不为空时，表示删除
            iRtManageAreaService.remove(wrapper);
        } else {
            //检查是否修改
            if (!StringUtils.isEmpty(carsDto.getListManageArea()) && carsDto.getListManageArea().size() > 0) {
                iRtManageAreaService.remove(wrapper);
                for (RtManageArea bean : carsDto.getListManageArea()) {
                    bean.setTargetId(carsDto.getId());
                }
                //添加关联
                iRtManageAreaService.saveBatch(carsDto.getListManageArea());
            }
        }
        return new ResponseBean(this.updateById(carsDto));
    }

    /**
     * 更新批量数据所有字段
     *
     * @param requestBean
     * @return
     */
    public ResponseBean updateAllFieldBatch(RequestBean requestBean) {
        return new ResponseBean(this.updateBatchById(BaseHzq.convertValue(requestBean.getInfos(), RtCars.class)));
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
        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
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
        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;
        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        //条件构造
        if (!CollectionUtils.isEmpty(queryMap)) {
            //车牌号等值查询
            if (!StringUtils.isEmpty(queryMap.get("carNo"))) {
                queryWrapper.eq("car_no", queryMap.get("carNo").toString().toUpperCase().trim());
            }

            //所属村居编码
            if (!StringUtils.isEmpty(queryMap.get("villageCode"))) {
                String sql = " SELECT a.code FROM t_rt_organization a " +
                        "  LEFT JOIN t_rt_organization b  " +
                        "  ON a.parent_code = b.code " +
                        "  WHERE a.`code`= " + queryMap.get("villageCode") +
                        "  OR a.parent_code=" + queryMap.get("villageCode") +
                        "  OR b.parent_code=" + queryMap.get("villageCode");
                queryWrapper.inSql("village_code", sql);

            }

            //姓名
            if (!StringUtils.isEmpty(queryMap.get("name"))) {
                queryWrapper.like("name", queryMap.get("name"));
            }
            //手机号
            if (!StringUtils.isEmpty(queryMap.get("phoneNumber"))) {
                queryWrapper.eq("phone_number", queryMap.get("phoneNumber"));
            }


        }
        // TODO 添加查询条件
        return new ResponseBean(baseMapper.selectPageRtCars(page, queryWrapper));
    }

    /**
     * 获取可分派维修人员数据
     *
     * @param requestBean
     * @return
     */
    public ResponseBean getPumpPage(RequestBean requestBean) {
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }

        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;

        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());

        if (!StringUtils.isEmpty(queryMap.get("carNo"))) {
            queryWrapper.like("car_no", queryMap.get("carNo").toString().toUpperCase().trim());
        }
        return new ResponseBean(baseMapper.selectPageByPump(page, queryWrapper));
    }
}
