package com.zx.auth.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @program: law-ibeas->DynamicScheduleTaskSecond
 * @description: 动态定时任务配置类
 * @author: 黄智强
 * @create: 2019-11-27 10:50
 **/
@Configuration
public class AutoInitSchedule {
    private static final Logger logger = LoggerFactory.getLogger(SchedulingRunnable.class);
    @Resource
    private CronTaskRegistrar cronTaskRegistrar;

    /**
     * 初始化定时任务
     */
    @PostConstruct
    public void initSystemTask() {
        // SchedulingRunnable task = new SchedulingRunnable("remindTodoServiceImpl", "sendMail", remindId);
        // cronTaskRegistrar.addCronTask(task, cron);
        // cronTaskRegistrar.addCronTask(task, "0/5 * * * * ? ");
    }

}
