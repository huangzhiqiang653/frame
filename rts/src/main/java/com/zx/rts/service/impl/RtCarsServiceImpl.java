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
import com.zx.rts.entity.RtCars;
import com.zx.rts.mapper.RtCarsMapper;
import com.zx.rts.service.IRtCarsService;
import com.zx.rts.service.IRtOrganizationService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
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

        RtCars rtCars = BaseHzq.convertValue(requestBean.getInfo(), RtCars.class);
        //车辆新增，需要验证车牌号是否存在，及车牌号是否正确
        //第一步:校验车牌号
        if (StringUtils.isEmpty(rtCars.getCarNo())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.CARS_FORMAT_ERROR.getValue());
        } else {
            //忽略车牌大小写
            rtCars.setCarNo(rtCars.getCarNo().toUpperCase().trim());
        }
        ;
        if (!CommonUtil.checkPlateNumberFormat(rtCars.getCarNo())) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.CARS_FORMAT_ERROR.getValue());
        }

        //第二步：校验车牌是否存在
        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("car_no", rtCars.getCarNo());
        queryWrapper.eq("delete_flag",  CommonConstants.DELETE_NO.getCode());
        Integer integer = baseMapper.selectCount(queryWrapper);
        if (integer > 0) {
            return new ResponseBean(CommonConstants.FAIL.getCode(), SystemMessageEnum.CARS_REPEAT.getValue());
        }
        return new ResponseBean(this.save(rtCars));
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
        return new ResponseBean(this.updateById(BaseHzq.convertValue(requestBean.getInfo(), RtCars.class)));
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
     * @param requestBean
     * @return
     */
    public ResponseBean getPumpPage(RequestBean requestBean){
        Page page = BaseHzq.convertValue(requestBean.getInfo(), Page.class);
        if (StringUtils.isEmpty(page)) {
            page = new Page();
        }

        Map queryMap = page.getRecords().size() > 0 ? (HashMap) page.getRecords().get(0) : null;

        QueryWrapper<RtCars> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("delete_flag", CommonConstants.DELETE_NO.getCode());

        if(!StringUtils.isEmpty(queryMap.get("carNo"))){
            queryWrapper.like("car_no",queryMap.get("carNo").toString().toUpperCase().trim());
        }
        return new ResponseBean(baseMapper.selectPageByPump(page, queryWrapper));
    }
}
