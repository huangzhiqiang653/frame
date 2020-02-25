package com.zx.rts.scheduler;

import com.zx.rts.service.IRtRecordRepairService;
import com.zx.rts.service.RtSchedulerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * 定时任务
 * @author shenyang
 * @version 1.0
 */
@Configuration
@EnableScheduling
public class RoadScheduler {

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RtSchedulerService rtSchedulerService;

    //@Scheduled(cron = "0/10 * * * * ?")//每隔10秒执行一次
    @Scheduled(cron = "0 0 10 * * ?")//每隔10分钟 执行一次
    public void schedule() {
        logger.info("开启定时任务，检查超时状态");
        rtSchedulerService.SchedulerRepairOvertime();
        rtSchedulerService.SchedulerPumpOvertime();
    }

}
