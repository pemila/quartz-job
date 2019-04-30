package com.pemila.controller;

import com.pemila.model.JobInfo;
import com.pemila.service.JobManagerService;
import com.pemila.util.Result;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 定时任务管理
 * @author 月在未央
 * @date 2019/3/28 9:58
 */
@RestController
public class JobManagerController {

    @Autowired
    private JobManagerService managerService;

    /** 查询所有任务*/
    @GetMapping("/job/query/all")
    public Result<List<JobInfo>> queryAllJob() throws SchedulerException {
        return Result.success(managerService.queryAllJob());
    }

    /** 新增数据库任务。不加入运行队列*/
    @PostMapping("/job/add/jobInfo")
    public Result addJob(@RequestBody JobInfo jobInfo) throws SchedulerException {
        return managerService.addJob(jobInfo);
    }

    /** 将任务加入运行队列，即启动任务*/
    @GetMapping("/job/start/toRunning")
    public Result addJobToRunning(@RequestParam @NotNull int jobId) throws SchedulerException {
        return managerService.addJobToRunning(jobId);
    }

    /** 暂停运行中的任务*/
    @GetMapping("/job/pause/runningJob")
    public Result pauseRunningJob(@RequestParam @NotNull Integer jobId) throws SchedulerException {
        return managerService.pauseJob(jobId);
    }

    /** 恢复运行暂停的任务*/
    @GetMapping("/job/resume/pauseJob")
    public Result resumeJobToRunning(@RequestParam @NotNull Integer jobId) throws SchedulerException {
        return managerService.resumeJob(jobId);
    }

    /** 终止运行中的任务，从运行队列中移除*/
    @GetMapping("/job/stop/runningJob")
    public Result stopRunningJob(@RequestParam @NotNull Integer jobId) throws SchedulerException {
        return managerService.stopRunningJob(jobId);
    }

    /** 将不在运行队列中的任务从数据库中删除*/
    @GetMapping("/job/delete/jobInfo")
    public Result deleteJobInfo(@RequestParam @NotNull Integer jobId) throws SchedulerException {
        return managerService.deleteJobInfo(jobId);
    }
}
