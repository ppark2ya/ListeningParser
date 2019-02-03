package com.myapp.senier.common;

import org.quartz.CronScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

public class CommonConstant {
    // SERVER IP
    public static final String ZABBIX_SERVER_IP = "http://146.148.76.237";
    public static final String POSTMAN_SERVER_IP = "http://146.148.76.237";
    public static final String FIREBASE_SERVER_IP = "http://146.148.76.237";
    public static final String CHECKSERVER_SERVER_IP = "http://146.148.76.237";

    // SERVER PORT
    public static final String ZABBIX_SERVER_PORT = "3000";
    public static final String POSTMAN_SERVER_PORT = "3000";
    public static final String FIREBASE_SERVER_PORT = "3000";
    public static final String CHECKSERVER_SERVER_PORT = "3000";

    // SERVER API ADDRESS
    public static final String API_ZABBIX = "/api/zabbix";
    public static final String API_POSTMAN = "/api/postman";
    public static final String API_FIREBASE = "/api/firebase";
    public static final String API_CHECKSERVER = "/api/checkServer";

    // HTTP METHOD
    public static final String POST = "POST";
    public static final String GET = "GET";

    // SMS API KEY
    public static final String SMS_API_KEY = "NCSOHUBPGSDVMUCD";
    public static final String SMS_API_SECRET = "3GAFDNTCWWMUPAYJHUZEEYUBWR1BFEJG";

    // QUARTZ SCHEDULE
    public static Scheduler scheduler;
    public static final String CRON_EXPRESSION_ZABBIX = "0 0/1 * * * ?";
    public static final String CRON_EXPRESSION_POSTMAN = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_SERVER1 = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_SERVER2 = "0 0/2 * * * ?";
    public static final String CRON_EXPRESSION_STOP = "0 0 23 * * ? 2099";

    // QUARTZ TRIGGER
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
