package com.pemila.model;


import lombok.Data;
import javax.persistence.*;

/**
 * @author 月在未央
 * @date 2019/3/27 15:36
 */
@Data
@Entity
@Table(name = "job_info")
public class JobInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(length = 20)
    private String jobName;
    @Column(length = 50)
    private String jobGroup;
    @Column(length = 1)
    private int jobStatus;
    @Column(length = 50)
    private String cronExpression;
    @Column(length = 100)
    private String jobDesc;
    @Column(length = 100)
    private String beanClass;
    @Column(length = 1)
    private String isConcurrent;
    @Column(length = 50)
    private String springId;
    @Column(length = 50)
    private String methodName;
    @Column
    private long createTime;
    @Column
    private long updateTime;

}
