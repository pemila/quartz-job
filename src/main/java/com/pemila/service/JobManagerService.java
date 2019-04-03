package com.pemila.service;

import com.pemila.model.JobInfo;
import com.pemila.util.Result;
import org.quartz.SchedulerException;

import java.util.List;

/**
 * @author 月在未央
 * @date 2019/3/27 17:24
 */
public interface JobManagerService {

    /**
     * 查询所有任务，包含执行中以及不在执行计划中的所有任务
     * @return List<JobInfoEx> list
     * @throws SchedulerException e
     */
    List<JobInfo> queryAllJob() throws SchedulerException;

    /**
     * 添加任务到数据库
     * @param job jobInfo
     * @return Result
     * @throws SchedulerException e
     */
    Result addJob(JobInfo job) throws SchedulerException;

    /**
     * 根据jobId将任务添加到执行计划中
     * @param jobId id
     * @return Result
     */
    Result addJobToRunning(int jobId) throws SchedulerException;

    /**
     * 更新任务执行频率,如果任务在执行计划中则同时更新调度计划
     * @param jobId job_id
     * @param cron cron表达式
     * @throws SchedulerException e
     */
    void updateJobCron(int jobId, String cron) throws SchedulerException;

    /**
     *  更改任务状态
     * @param jobId jobId
     * @param status 任务状态
     * @throws SchedulerException e
     */
    void updateJobStatus(int jobId, int status) throws SchedulerException;

    /**
     * 查询在执行计划中的任务
     * @return List<JobInfo>
     * @throws SchedulerException e
     */
    List<JobInfo> queryRunningJob() throws SchedulerException;


    /**
     * 删除任务
     * @param jobId JobId
     * @throws SchedulerException e
     */
    void deleteJob(int jobId) throws SchedulerException;

    /**
     * 暂停任务
     * @param job JobInfo
     * @throws SchedulerException e
     */
    void pauseJob(JobInfo job) throws SchedulerException;

    /**
     * 恢复任务
     * @param job JobInfo
     * @throws SchedulerException e
     */
    void resumeJob(JobInfo job) throws SchedulerException;


    /**
     * 立即执行
     * @param job JobInfo
     * @throws SchedulerException e
     */
    void runAJobNow(JobInfo job) throws SchedulerException;


}


