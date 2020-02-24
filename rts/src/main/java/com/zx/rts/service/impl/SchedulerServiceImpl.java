package com.zx.rts.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zx.rts.entity.RtConfig;
import com.zx.rts.mapper.RtConfigMapper;
import com.zx.rts.mapper.RtRecordPumpMapper;
import com.zx.rts.mapper.RtRecordRepairMapper;
import com.zx.rts.service.RtSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

@Service
public class SchedulerServiceImpl implements RtSchedulerService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private RtRecordRepairMapper rtRecordRepairMapper;
    @Resource
    private RtRecordPumpMapper rtRecordPumpMapper;
    @Resource
    private RtConfigMapper rtConfigMapper;

    //获取报修字典类别
    @Value("${scheduler.repairOvertimeParamName}")
    private String repairOvertimeParamName;

    //获取报抽字典类别
    @Value("${scheduler.pumpOvertimeParamName}")
    private String pumpOvertimeParamName;

    /**
     * 根据配置，检查保修记录是否超时
     */
    @Override
    @Transactional
    public void SchedulerRepairOvertime() {
        try {
            logger.info("报修定时任务执行~~~~~~~");
            //获取执行天数
            if (!StringUtils.isEmpty(repairOvertimeParamName)) {
                QueryWrapper<RtConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("config_param", this.repairOvertimeParamName);
                RtConfig rtConfig = rtConfigMapper.selectOne(queryWrapper);
                int i = rtRecordRepairMapper.repairOvertimeTinspect(Integer.parseInt(rtConfig.getConfigCode()));
                logger.info("检查结束，更新记录：" + i + "条");
            } else {
                logger.info("系统未配置超时报修参数,定时任务不执行");
            }
        } catch (Exception e) {
            logger.info("报修定时任务执行异常：" + e.getMessage());
        }
    }

    /**
     * 根据配置，检查保抽记录是否超时
     */
    @Override
    @Transactional
    public void SchedulerPumpOvertime() {
        try {
            logger.info("报抽定时任务执行~~~~~~~");
            if (!StringUtils.isEmpty(this.pumpOvertimeParamName)) {
                QueryWrapper<RtConfig> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("config_param", this.pumpOvertimeParamName);
                RtConfig rtConfig = rtConfigMapper.selectOne(queryWrapper);
                int i = rtRecordPumpMapper.pumpOvertimeTinspect(Integer.parseInt(rtConfig.getConfigCode()));
                logger.info("检查结束，更新记录：" + i + "条");
            } else {
                logger.info("系统未配置超时报抽参数,定时任务不执行");
            }
        } catch (Exception e) {
            logger.info("报抽定时任务执行异常：" + e.getMessage());
        }
    }

}
