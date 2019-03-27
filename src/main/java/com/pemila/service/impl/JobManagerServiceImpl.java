package com.pemila.service.impl;

import com.pemila.model.JobInfo;
import com.pemila.repository.JobInfoRepository;
import com.pemila.service.JobManagerService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.List;

/**
 * @author 月在未央
 * @date 2019/3/27 17:52
 */
public class JobManagerServiceImpl implements JobManagerService {

    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void addJobInfo(JobInfo info) {
        info.setCreateTime(System.currentTimeMillis()/1000);
        info.setUpdateTime(info.getCreateTime());
        jobInfoRepository.save(info);
    }

    @Override
    public void updateJobCron(int jobId, String cron) throws SchedulerException {
        JobInfo job = jobInfoRepository.findById(jobId).get();
        job.setCronExpression(cron);
        if (1 == job.getJobStatus()) {
            //如果任务在执行计划中，则更新scheduler
            Scheduler scheduler = schedulerFactoryBean.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
            CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
        jobInfoRepository.saveAndFlush(job);
    }

    @Override
    public List<JobInfo> queryRunningJob() throws SchedulerException {



        return null;
    }

    @Override
    public List<JobInfo> queryAllJob() {
        return null;
    }

    @Override
    public void addJob(JobInfo job) throws SchedulerException {

    }

    @Override
    public void pauseJob(JobInfo job) throws SchedulerException {

    }

    @Override
    public void resumeJob(JobInfo job) throws SchedulerException {

    }

    @Override
    public void deleteJob(JobInfo job) throws SchedulerException {

    }

    @Override
    public void runAJobNow(JobInfo job) throws SchedulerException {

    }
}
