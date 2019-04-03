package com.pemila.service.impl;

import com.pemila.core.QuartzJobFactory;
import com.pemila.core.QuartzJobFactoryDisallowConcurrent;
import com.pemila.model.JobInfo;
import com.pemila.repository.JobInfoRepository;
import com.pemila.service.JobManagerService;
import com.pemila.util.Logs;
import com.pemila.util.Result;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author 月在未央
 * @date 2019/3/27 17:52
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class JobManagerServiceImpl implements JobManagerService {

    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @Override
    public List<JobInfo> queryAllJob() throws SchedulerException {
        //查询数据库中所有任务
        List<JobInfo> infoList = jobInfoRepository.findAll();
        Map<String,JobInfo> map = new HashMap<>();
        infoList.forEach(e-> map.put(e.getJobName(),e));
        //获取调度器
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        //创建一个matcher
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        //使用matcher从调度器中获取匹配的JobKey
        Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);
        for (JobKey jobKey : jobKeys) {
            //获取trigger
            List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
            for (Trigger trigger : triggers) {
                String key = jobKey.getName();
                if(map.containsKey(key)){
                    JobInfo job = map.get(key);
                    //根据trigger从调度器中获取状态
                    Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
                    //获取任务实时状态
                    job.setIsRunning(triggerState.ordinal());
                }
            }
        }
        infoList = new ArrayList<>(map.values());
        return infoList;
    }

    @Override
    public Result addJob(JobInfo job) {
        job.setCreateTime(System.currentTimeMillis()/1000);
        job.setUpdateTime(job.getCreateTime());
        job = jobInfoRepository.save(job);
        return job.getId()>0?Result.success():Result.fail("新增任务失败");
    }

    @Override
    public Result addJobToRunning(int jobId) throws SchedulerException {
        //获取任务信息
        JobInfo info = jobInfoRepository.findById(jobId).get();
        if(info.getJobStatus()==0){
            return Result.fail("任务状态为不可执行");
        }
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(info.getJobName(), info.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger==null) {
            Class clazz = "1".equals(info.getIsConcurrent())
                    ? QuartzJobFactory.class
                    : QuartzJobFactoryDisallowConcurrent.class;
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(info.getJobName(), info.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", info);
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(info.getJobName(), info.getJobGroup()).withSchedule(scheduleBuilder).build();
            scheduler.scheduleJob(jobDetail, trigger);
        }else {
            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(info.getCronExpression());
            trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
        }
        return Result.success();
    }

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
                job.setJobStatus(1);
                job.setUpdateTime(System.currentTimeMillis()/1000);
                addJob(job);
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
