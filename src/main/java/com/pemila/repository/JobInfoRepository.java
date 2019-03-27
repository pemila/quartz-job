package com.pemila.repository;

import com.pemila.model.JobInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author 月在未央
 * @date 2019/3/27 16:09
 */
@Repository
public interface JobInfoRepository extends JpaRepository<JobInfo,Integer> {
    JobInfo findByJobName(String name);

}
