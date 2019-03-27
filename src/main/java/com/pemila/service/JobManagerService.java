package com.pemila.service;

import com.pemila.model.JobInfo;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author 月在未央
 * @date 2019/3/27 17:24
 */
public interface JobManagerService {

    /**
     * 新增任务
     * @param info jobinfo
     */
    public void addJobInfo(JobInfo info);

    /**
     * 更新任务执行频率,如果任务在执行计划中则同时更新调度计划
     * @param jobId job_id
     * @param cron cron表达式
     * @throws SchedulerException e
     */
    public void updateJobCron(int jobId, String cron) throws SchedulerException;

    /**
     * 查询在执行计划中的任务
     * @return List<JobInfo>
     * @throws SchedulerException e
     */
    public List<JobInfo> queryRunningJob() throws SchedulerException;

    /**
     * 查询全部计划中的任务
     * @return List<JobInfo>
     */
    public List<JobInfo> queryAllJob();

    /**
     * 添加任务
     * @param job JobInfo
     * @throws SchedulerException e
     */
    public void addJob(JobInfo job) throws SchedulerException;

    /**
     * 暂停任务
     * @param job JobInfo
     * @throws SchedulerException e
     */
    public void pauseJob(JobInfo job) throws SchedulerException;

    /**
     * 恢复任务
     * @param job JobInfo
     * @throws SchedulerException e
     */
    public void resumeJob(JobInfo job) throws SchedulerException;

    /**
     * 删除任务
     * @param job JobInfo
     * @throws SchedulerException e
     */
    public void deleteJob(JobInfo job) throws SchedulerException;

    /**
     * 立即执行
     * @param job JobInfo
     * @throws SchedulerException e
     */
    public void runAJobNow(JobInfo job) throws SchedulerException;
}


