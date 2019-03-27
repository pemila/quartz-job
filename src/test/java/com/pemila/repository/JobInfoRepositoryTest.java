package com.pemila.repository;

import com.pemila.model.JobInfo;
import com.pemila.util.Logs;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author 月在未央
 * @date 2019/3/27 16:18
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobInfoRepositoryTest {

    @Autowired
    private JobInfoRepository jobInfoRepository;
    @Test
    public void Test1(){
        List<JobInfo> infos = jobInfoRepository.findAll();
        infos.forEach( e ->Logs.info(e.toString()));
    }


}
