package com.myapp.senier.scheduler.jobs;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.common.utils.HttpClient;
import com.myapp.senier.model.DataModel;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Job2 implements Job{
    private static final Logger logger = LoggerFactory.getLogger(Job2.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        int logCount = 1;
        logger.info("JOB2 start - {}", logCount++);
        
        HttpClient client = HttpClient
                        .Builder()
                        .setIp(CommonConstant.ZABBIX_SERVER_IP)
                        .setPort(CommonConstant.ZABBIX_SERVER_PORT)
                        .setApi(CommonConstant.API_ZABBIX)
                        .setMethod(CommonConstant.GET)
                        .build();

        DataModel dm = client.send();
        logger.info("Response Message - {}", dm.get("message"));
    }
}