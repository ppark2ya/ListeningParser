package com.myapp.senier.scheduler.jobs;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.common.utils.HttpClient;
import com.myapp.senier.model.DataModel;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckServerJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(CheckServerJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("CheckServerJob START !!!");
        
        try {
            HttpClient client = HttpClient
                            .Builder()
                            .setIp(CommonConstant.CHECKSERVER_SERVER_IP)
                            .setApi(CommonConstant.API_CHECKSERVER)
                            .setMethod(CommonConstant.GET)
                            .build();
    
            DataModel dm = client.send();
            dm.putStrNull("serviceCd", CommonConstant.CHECKSERVER_CODE);
            logger.info("CheckServerJob Response Log Message - {}", dm.get("message"));
    
            // StanfordNLP nlp = new StanfordNLP();
            // DataModel words = nlp.executeLogAnalyzer(dm);
            // logger.info("CheckServerJob Natural Language Parser Result - {}", words);
            
            logger.info("CheckServerJob END !!!");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}