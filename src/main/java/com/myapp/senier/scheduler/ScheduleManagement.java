package com.myapp.senier.scheduler;

import javax.annotation.PostConstruct;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.scheduler.jobs.Job1;
import com.myapp.senier.scheduler.jobs.Job2;
import com.myapp.senier.scheduler.jobs.Job3;
import com.myapp.senier.scheduler.jobs.Job4;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManagement {

    private SchedulerFactory schedulerFactory;

    @PostConstruct
    public void start() throws SchedulerException {
        schedulerFactory = new StdSchedulerFactory();

        CommonConstant.scheduler = schedulerFactory.getScheduler();
        CommonConstant.scheduler.start();

        JobDetail job1 = JobBuilder.newJob(Job1.class).build();
        JobDetail job2 = JobBuilder.newJob(Job2.class).build();
        JobDetail job3 = JobBuilder.newJob(Job3.class).build();
        JobDetail job4 = JobBuilder.newJob(Job4.class).build();

        CommonConstant.scheduler.scheduleJob(job1, CommonConstant.oneSecTrigger);
        CommonConstant.scheduler.scheduleJob(job2, CommonConstant.twoSecTrigger);
        CommonConstant.scheduler.scheduleJob(job3, CommonConstant.threeSecTrigger);
        CommonConstant.scheduler.scheduleJob(job4, CommonConstant.fourSecTrigger);
    }
}