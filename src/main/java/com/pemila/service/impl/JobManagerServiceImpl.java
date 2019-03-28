package com.pemila.service.impl;

import com.pemila.job.QuartzJobFactory;
import com.pemila.job.QuartzJobFactoryDiallowConcurrent;
import com.pemila.model.JobInfo;
import com.pemila.repository.JobInfoRepository;
import com.pemila.service.JobManagerService;
import com.pemila.util.Logs;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 月在未央
 * @date 2019/3/27 17:52
 */
@Service
public class JobManagerServiceImpl implements JobManagerService {

    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public void updateJobCron(int jobId, String cron) throws SchedulerException {
        JobInfo job = jobInfoRepository.findById(jobId).get();
        job.setCronExpression(cron);
        job.setUpdateTime(System.currentTimeMillis()/1000);
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
    public void updateJobStatus(int jobId, int status) throws SchedulerException {
        JobInfo job = jobInfoRepository.findById(jobId).get();
        switch (status){
            case 0:
                deleteJob(jobId);
                job.setJobStatus(0);
                job.setUpdateTime(System.currentTimeMillis()/1000);
                break;
            case 1:
                addJob(job);
                job.setJobStatus(1);
                job.setUpdateTime(System.currentTimeMillis()/1000);
                break;
            default:
                break;
        }
        jobInfoRepository.saveAndFlush(job);
    }

    @Override
    public List<JobInfo> queryRunningJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<JobExecutionContext> executingJobs = scheduler.getCurrentlyExecutingJobs();
        List<JobInfo> jobList = new ArrayList<JobInfo>(executingJobs.size());
        for (JobExecutionContext executingJob : executingJobs) {
            JobInfo job = new JobInfo();
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            job.setJobName(jobKey.getName());
            job.setJobGroup(jobKey.getGroup());
            job.setJobDesc("触发器:" + trigger.getKey());
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            //TODO 测试triggerState.ordinal()
                job.setJobStatus(triggerState.ordinal());
            if (trigger instanceof CronTrigger) {
                CronTrigger cronTrigger = (CronTrigger) trigger;
                String cronExpression = cronTrigger.getCronExpression();
                job.setCronExpression(cronExpression);
            }
            jobList.add(job);
        }
        return jobList;
    }

    @Override
    public List<JobInfo> queryAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        List<JobInfo> jobList = new ArrayList<JobInfo>();
        for (JobKey jobKey : jobKeys) {
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                JobInfo job = new JobInfo();
                job.setJobName(jobKey.getName());
                job.setJobGroup(jobKey.getGroup());
                job.setJobDesc("触发器:" + trigger.getKey());
                Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                //TODO 测试triggerState.ordinal()
                    job.setJobStatus(triggerState.ordinal());
                if (trigger instanceof CronTrigger) {
                    CronTrigger cronTrigger = (CronTrigger) trigger;
                    String cronExpression = cronTrigger.getCronExpression();
                    job.setCronExpression(cronExpression);
                }
                jobList.add(job);
            }
        }
        return jobList;
    }

    @Override
    public void addJob(JobInfo job) throws SchedulerException {
        if(job!=null){
            job.setCreateTime(System.currentTimeMillis()/1000);
            job.setUpdateTime(job.getCreateTime());
            jobInfoRepository.save(job);
            if (1!=job.getJobStatus()){
                //不需要执行，则返回
                return;
            }
        }else {
            return;
        }
        //将任务添加到调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        Logs.info("====== add scheduler");
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger==null) {
            Class clazz = null;
            if("1".equals(job.getIsConcurrent())) {
                clazz =  QuartzJobFactory.class;
            }else {
                clazz = QuartzJobFactoryDiallowConcurrent.class;
            }
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }else {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }

    @Override
    public void deleteJob(int jobId) throws SchedulerException {
        JobInfo job = jobInfoRepository.findById(jobId).get();
        //从执行计划中删除任务
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.deleteJob(jobKey);
    }


    @Override
    public void pauseJob(JobInfo job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    @Override
    public void resumeJob(JobInfo job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    @Override
    public void runAJobNow(JobInfo job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.triggerJob(jobKey);
    }
}
