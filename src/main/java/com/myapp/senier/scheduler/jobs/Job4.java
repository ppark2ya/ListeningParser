package com.myapp.senier.scheduler.jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class Job4 implements Job{
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("[job4]============");
    }
}