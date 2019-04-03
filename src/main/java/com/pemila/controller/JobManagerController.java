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

    //5.将任务从运行队列移除
    //6.更新数据库任务状态,必须不再运行中
    //3.删除数据库任务，必须不再运行中

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
    @GetMapping("/job/add/toRunning")
    public Result addJobToRunning(@RequestParam @NotNull int jobId) throws SchedulerException {
        return managerService.addJobToRunning(jobId);
    }



}
