package com.myapp.senier.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Job1 implements Job {
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[job1]============");
        //CommonConstant.scheduler.rescheduleJob(new TriggerKey("stop"), CommonConstant.stopTrigger); // 스케줄 변경
    }
}