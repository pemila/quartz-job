package com.pemila.core;

import com.pemila.model.JobInfo;
import com.pemila.repository.JobInfoRepository;
import com.pemila.service.JobManagerService;
import com.pemila.util.Logs;
import com.pemila.util.Result;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author 月在未央
 * @date 2019/3/27 17:20
 */
@Component
public class ScheduleJobManager {

    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Autowired
    private JobManagerService jobManagerService;

    @PostConstruct
    public void init() throws SchedulerException {
        Logs.info("start quartz");
        //从数据库中获取任务信息数据
        List<JobInfo> jobList = jobInfoRepository.findAll();
        for (JobInfo job : jobList) {
            try{
                Result res = jobManagerService.addJobToRunning(job.getId());
                if(0 == res.getResult()){
                    Logs.info("add scheduler ====== {}",job.getJobName());
                }
            }catch (Exception e){
                Logs.error("add scheduler error= {}",job.getJobName());
            }
        }
    }
}
