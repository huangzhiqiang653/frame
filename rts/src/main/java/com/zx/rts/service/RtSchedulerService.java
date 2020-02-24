package com.zx.rts.service;

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
