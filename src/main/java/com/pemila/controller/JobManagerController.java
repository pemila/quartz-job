package com.pemila.controller;

import com.pemila.model.JobInfo;
import com.pemila.service.JobManagerService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 定时任务管理
 * @author 月在未央
 * @date 2019/3/28 9:58
 */
@RestController
public class JobManagerController {

    @Autowired
    private JobManagerService managerService;

    /** 向数据库中新增定时任务计划*/
    @PostMapping("/job/add/jobInfo")
    public void addJob(@RequestBody JobInfo jobInfo) throws Exception {
            managerService.addJob(jobInfo);
    }

    /** 更新执行任务状态，对于执行中的任务同时修改调度计划*/
    @GetMapping("/job/update/status")
    public void updateJobStatus(@RequestParam @NotNull Integer jobId,@RequestParam @NotNull Integer jobStatus )
            throws Exception {
        managerService.updateJobStatus(jobId,jobStatus);
    }

    /** 更新任务执行频率*/
    @GetMapping("/job/update/cron")
    public void updateJobCron(@RequestParam @NotNull Integer jobId,@RequestParam @NotNull String cron )
            throws SchedulerException {
        managerService.updateJobCron(jobId,cron);
    }

}
