package com.pemila.model;


import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    /** 任务名称*/
    @NotNull
    @Column(length = 20)
    private String jobName;
    /** 定时任务所属组*/
    @NotNull
    @Column(length = 50)
    private String jobGroup;
    /** 任务状态   1= 可执行 0=不执行*/
    @NotNull
    @Column(length = 1)
    private int jobStatus;
    /** cron表达式  定义任务执行频率*/
    @NotNull
    @Column(length = 50)
    private String cronExpression;
    @NotNull
    /** 任务描述*/
    @Column(length = 100)
    private String jobDesc;
    /** 任务执行时调用的类的方法 包名+类名*/
    @NotNull
    @Column(length = 100)
    private String beanClass;
    /** 首次执行未结束，而已到二次执行时间点，则需要等待首次执行结束再执行第二次*/
    @NotNull
    @Column(length = 1)
    private String isConcurrent;
    /** spring bean Id*/
    @Column(length = 50)
    private String springId;
    /** 调用执行的方法名*/
    @NotNull
    @Column(length = 50)
    private String methodName;
    /** 任务创建时间*/
    @Column
    private long createTime;
    /** 更新时间*/
    @Column
    private long updateTime;
}
