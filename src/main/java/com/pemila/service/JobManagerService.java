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
     * @throws SchedulerException e
     */
    Result addJobToRunning(int jobId) throws SchedulerException;

    /**
     * 暂停任务
     * @param jobId jobId
     * @return Result
     * @throws SchedulerException e
     */
    Result pauseJob(Integer jobId) throws SchedulerException;

    /**
     * 终止运行中的任务，从运行队列中删除
     * @param jobId jobId
     * @return Result
     * @throws SchedulerException e
     */
    Result stopRunningJob(Integer jobId) throws SchedulerException;



    /**
     * 恢复任务
     * @param jobId JobInfo
     * @return result
     * @throws SchedulerException e
     */
    Result resumeJob(Integer jobId) throws SchedulerException;

    /**
     * 将不在运行队列中的任务从数据库中删除
     * @param jobId jobId
     * @return result
     * @throws SchedulerException e
     */
    Result deleteJobInfo(Integer jobId) throws SchedulerException;

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
     * 立即执行
     * @param job JobInfo
     * @throws SchedulerException e
     */
    void runAJobNow(JobInfo job) throws SchedulerException;


}


