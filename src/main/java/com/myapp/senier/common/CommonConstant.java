package com.myapp.senier.common;

import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

public class CommonConstant {
    // GCP IP
    public static final String GCP_SERVER_IP = "146.148.76.237";

    public static Scheduler scheduler;
    public static final String CRON_EXPRESSION_ZABBIX = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_POSTMAN = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_SERVER1 = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_SERVER2 = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_STOP = "0 0 23 * * ? 2099";

    public static Trigger oneSecTrigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey("job1Key"))
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION_ZABBIX)).build();

    public static Trigger twoSecTrigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey("job2Key"))
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION_POSTMAN)).build();

    public static Trigger threeSecTrigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey("job3Key"))
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION_SERVER1)).build();

    public static Trigger fourSecTrigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey("job4Key"))
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION_SERVER2)).build();

    public static Trigger stopTrigger = TriggerBuilder.newTrigger()
                .withIdentity(new TriggerKey("stop"))
                .withSchedule(CronScheduleBuilder.cronSchedule(CRON_EXPRESSION_STOP)).build();
}
