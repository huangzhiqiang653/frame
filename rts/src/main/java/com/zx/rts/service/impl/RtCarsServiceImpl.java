package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zx.common.common.BaseHzq;
import com.zx.common.common.CommonUtil;
import com.zx.common.common.RequestBean;
import com.zx.common.common.ResponseBean;
import com.zx.common.enums.CommonConstants;
import com.zx.common.enums.SystemMessageEnum;
import com.zx.rts.common.RtsMessageEnum;
import com.zx.rts.dto.RtCarsDto;
import com.zx.rts.entity.RtCars;
import com.zx.rts.entity.RtManageArea;
import com.zx.rts.entity.RtUser;
import com.zx.rts.mapper.RtCarsMapper;
import com.zx.rts.service.IRtCarsService;
import com.zx.rts.service.IRtManageAreaService;
import com.zx.rts.service.IRtOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 单个新增
     *
     * @param requestBean
     * @return
     */
    public ResponseBean add(RequestBean requestBean) {

        RtCarsDto rtCars = BaseHzq.convertValue(requestBean.getInfo(), RtCarsDto.class);
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
        wrapper.eq("target_id",carsDto.getId() );
        if (!StringUtils.isEmpty(carsDto.getRemoveManageAreaFlag())) {
            //删除关联配置标识不为空时，表示删除
            iRtManageAreaService.remove(wrapper);
        }else{
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
                queryWrapper.eq("village_code", queryMap.get("villageCode"));
            }
            //所属乡镇编码
            if (!StringUtils.isEmpty(queryMap.get("townCode"))) {
                queryWrapper.eq("town_code", queryMap.get("townCode"));
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
