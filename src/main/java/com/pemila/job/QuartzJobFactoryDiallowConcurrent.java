package com.pemila.job;

import com.pemila.model.JobInfo;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

/**
 * @author 月在未央
 * @date 2019/3/27 17:18
 */
@DisallowConcurrentExecution
public class QuartzJobFactoryDiallowConcurrent implements Job {
    @Override
    public void execute(JobExecutionContext context){
        JobInfo scheduleJob = (JobInfo) context.getMergedJobDataMap().get("scheduleJob");
        JobUtils.invokMethod(scheduleJob);
    }
}
