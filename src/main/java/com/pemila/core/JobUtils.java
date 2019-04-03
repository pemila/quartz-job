package com.pemila.core;

import com.pemila.model.JobInfo;
import com.pemila.util.Logs;
import com.pemila.util.SpringUtil;
import org.springframework.util.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 月在未央
 * @date 2019/3/27 16:50
 */
public class JobUtils {

    public static void invokMethod(JobInfo scheduleJob) {
        Object object = null;
        Class<?> clazz = null;
        // springId不为空先按springId查找bean
        if (!StringUtils.isEmpty(scheduleJob.getSpringId())) {
            object = SpringUtil.getBean(scheduleJob.getSpringId());
        } else if (!StringUtils.isEmpty(scheduleJob.getBeanClass())) {
            try {
                clazz = Class.forName(scheduleJob.getBeanClass());
                object = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (object == null) {
            Logs.error("任务 = [" + scheduleJob.getJobName() + "]---------------启动失败，请检查是否配置正确！！！");
            return;
        }
        clazz = object.getClass();
        Method method = null;
        try {
            method =  clazz.getDeclaredMethod(scheduleJob.getMethodName());
        } catch (NoSuchMethodException e) {
            Logs.error("任务 = [" + scheduleJob.getJobName() + "]---------------启动失败，方法名设置错误！！！");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (method != null) {
            try {
                method.invoke(object);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
