package com.pemila.model;


import lombok.Data;
import javax.persistence.*;

/**
 * 任务信息表
 * 指定id为主键且自增，指定jobName唯一，排除isRunning字段
 * @author 月在未央
 * @date 2019/3/27 15:36
 */
@Data
@Entity
@Table(name = "job_info",uniqueConstraints = {@UniqueConstraint(columnNames="jobName")})

public class JobInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,columnDefinition = "varchar(20) comment'任务名称'")
    private String jobName;

    @Column(nullable = false,columnDefinition = "varchar(50) comment'定时任务所属组'")
    private String jobGroup;

    @Column(nullable = false,columnDefinition = "int(1) comment'任务状态  1= 可执行 0=不执行'")
    private int jobStatus;

    @Column(nullable = false,columnDefinition = "varchar(50) comment'cron表达式  定义任务执行频率'")
    private String cronExpression;

    @Column(nullable = false,columnDefinition = "varchar(100) comment'任务描述'")
    private String jobDesc;

    @Column(nullable = false,columnDefinition = "varchar(100) comment'任务执行时调用的类的方法 包名+类名'")
    private String beanClass;

    @Column(nullable = false,columnDefinition = "varchar(1) comment '等于0时，首次执行未结束，而已到二次执行时间点，则需要等待首次执行结束再执行第二次'")
    private String isConcurrent;

    @Column(nullable = false,columnDefinition = "varchar(50) comment'spring bean Id'")
    private String springId;

    @Column(nullable = false,columnDefinition = "varchar(50) comment'调用执行的方法名'")
    private String methodName;

    @Column(nullable = false,columnDefinition = "bigint(20) comment'任务创建时间'")
    private long createTime;

    @Column(nullable = false,columnDefinition = "bigint(20) comment'更新时间'")
    private long updateTime;


    /** 是否在执行计划中 1=是 0=否*/
    @Transient
    private int isRunning;
}
