package com.zx.rts.service;


/**
 * @author shenyang
 * @since 2020-02-27
 * 定时任务业务类
 */
public interface RtSchedulerService {

    /**
     * 根据配置，检查保修记录是否超时
     */
    public void SchedulerRepairOvertime();

    /**
     * 根据配置，检查保抽记录是否超时
     */
    public void SchedulerPumpOvertime();
}
