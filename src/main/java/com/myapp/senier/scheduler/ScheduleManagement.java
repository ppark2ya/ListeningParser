package com.myapp.senier.scheduler;

import javax.annotation.PostConstruct;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.scheduler.jobs.ZabbixJob;
import com.myapp.senier.scheduler.jobs.PostmanJob;
import com.myapp.senier.scheduler.jobs.SefilcareJob;
import com.myapp.senier.scheduler.jobs.CheckServerJob;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Component;

@Component
public class ScheduleManagement {

    private Scheduler scheduler;
    private SchedulerFactory schedulerFactory;

    @PostConstruct
    public void start() throws SchedulerException {
        schedulerFactory = new StdSchedulerFactory();
        scheduler = schedulerFactory.getScheduler();
        scheduler.start();
        
        JobDetail ZabbixJob = JobBuilder.newJob(ZabbixJob.class).build();
        // JobDetail PostmanJob = JobBuilder.newJob(PostmanJob.class).build();
        // JobDetail SefilcareJob = JobBuilder.newJob(SefilcareJob.class).build();
        // JobDetail CheckServerJob = JobBuilder.newJob(CheckServerJob.class).build();

        // QUARTZ TRIGGER
        Trigger zabbixTrigger = TriggerBuilder.newTrigger()
            .withIdentity(new TriggerKey("zabbixKey"))
            .withSchedule(CronScheduleBuilder.cronSchedule(CommonConstant.CRON_EXPRESSION_ZABBIX)).build();

        // Trigger postmanTrigger = TriggerBuilder.newTrigger()
        //     .withIdentity(new TriggerKey("postmanKey"))
        //     .withSchedule(CronScheduleBuilder.cronSchedule(CommonConstant.CRON_EXPRESSION_POSTMAN)).build();

        // Trigger sefilcareTrigger = TriggerBuilder.newTrigger()
        //     .withIdentity(new TriggerKey("sefilcareKey"))
        //     .withSchedule(CronScheduleBuilder.cronSchedule(CommonConstant.CRON_EXPRESSION_SEFILCARE)).build();

        // Trigger checkserverTrigger = TriggerBuilder.newTrigger()
        //     .withIdentity(new TriggerKey("checkserverKey"))
        //     .withSchedule(CronScheduleBuilder.cronSchedule(CommonConstant.CRON_EXPRESSION_CHECKSERVER)).build();

        scheduler.scheduleJob(ZabbixJob, zabbixTrigger);
        // scheduler.scheduleJob(PostmanJob, postmanTrigger);
        // scheduler.scheduleJob(SefilcareJob, sefilcareTrigger);
        // scheduler.scheduleJob(CheckServerJob, checkserverTrigger);
    }
}