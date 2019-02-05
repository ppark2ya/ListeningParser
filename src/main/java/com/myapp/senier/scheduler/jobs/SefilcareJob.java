package com.myapp.senier.scheduler.jobs;

import java.util.Set;

import com.myapp.senier.common.CommonConstant;
import com.myapp.senier.common.utils.HttpClient;
import com.myapp.senier.model.DataModel;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SefilcareJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(SefilcareJob.class);

    public void execute(JobExecutionContext context) throws JobExecutionException {
        logger.info("SefilcareJob START !!!");
        try {
            HttpClient client = HttpClient
                            .Builder()
                            .setIp(CommonConstant.SEFILCARE_SERVER_IP)
                            .setApi(CommonConstant.API_SEFILCARE)
                            .setMethod(CommonConstant.GET)
                            .build();
    
            DataModel dm = client.send();
            dm.putStrNull("serviceCd", CommonConstant.SEFILCARE_CODE);
            logger.info("SefilcareJob Response Log Message - {}", dm.get("message"));
    
            // StanfordNLP nlp = new StanfordNLP();
            // DataModel words = nlp.executeLogAnalyzer(dm);
            // logger.info("SefilcareJob Natural Language Parser Result - {}", words);
            
            logger.info("SefilcareJob END !!!");
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}