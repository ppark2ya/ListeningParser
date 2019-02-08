package com.myapp.senier.scheduler.jobs;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.common.utils.HttpClient;
import com.myapp.senier.model.DataModel;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZabbixJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(ZabbixJob.class);
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("ZabbixJob START !!!");
        try {
            HttpClient client = HttpClient
                            .Builder()
                            .setIp(CommonConstant.ZABBIX_SERVER_IP)
                            .setApi(CommonConstant.API_ZABBIX)
                            .setMethod(CommonConstant.GET)
                            .build();
    
            DataModel response = client.send();
            logger.info("ZabbixJob Response Log Message - {}", response.get("message"));
    
            HttpClient local = HttpClient
                            .Builder()
                            .setIp(CommonConstant.OWN_SERVER_IP)
                            .setApi(CommonConstant.API_OWNSERVER + CommonConstant.ZABBIX_CODE)
                            .setMethod(CommonConstant.POST)
                            .setParams(response)
                            .build();
                            
            DataModel result = local.send();
            logger.info("ZabbixJob Natural Language Parser Result - {}", result);
            
            logger.info("ZabbixJob END !!!");

        } catch(Exception e) {
            logger.error("ZabbixJob ERROR - {}", e.getMessage());
            e.printStackTrace();
        }
    }
}