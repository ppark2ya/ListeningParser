package com.myapp.senier.scheduler.jobs;

import com.myapp.senier.common.CommonConstant;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

public class Job1 implements Job {
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            System.out.println("[job1]============");
            CommonConstant.scheduler.rescheduleJob(new TriggerKey("job1Key"), CommonConstant.stopTrigger);
        } catch(SchedulerException se) {
            se.printStackTrace();
        }

    }
}