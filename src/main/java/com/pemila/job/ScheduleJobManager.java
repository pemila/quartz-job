package com.pemila.job;

import com.pemila.model.JobInfo;
import com.pemila.repository.JobInfoRepository;
import com.pemila.util.Logs;
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

    @PostConstruct
    public void init(){
        // 这里从数据库中获取任务信息数据
        Logs.info("Quartz 启动");
        List<JobInfo> jobList = jobInfoRepository.findAll();
        for (JobInfo job : jobList) {
            addJob(job);
        }
    }


    private void addJob(JobInfo job){
        //TODO
    }

}
