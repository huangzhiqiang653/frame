package com.zx.rts.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Scheduled(cron = "0/2 * * * * ?")//每隔2秒执行一次
    public void schedule() {

       logger.info("定时任务");
    }

}
