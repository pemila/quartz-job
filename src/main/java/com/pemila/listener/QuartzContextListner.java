package com.pemila.listener;

import com.pemila.util.Logs;
import org.quartz.Scheduler;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @author 月在未央
 * @date 2019/3/27 15:05
 */
public class QuartzContextListner implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try{
            Scheduler startQuartz = (Scheduler) WebApplicationContextUtils
                    .getWebApplicationContext(event.getServletContext())
                    .getBean("schedulerFactoryBean");
            startQuartz.shutdown(true);
            Thread.sleep(1000);//主线程睡眠1s
        }catch (Exception e){
            e.printStackTrace();
        }
        event.getServletContext().log("QuartzContextListener销毁成功！");
        Logs.info("QuartzContextListener销毁成功！");
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        Logs.info("QuartzContextListener启动成功！");
        event.getServletContext().log("QuartzContextListener启动成功！");
    }
}
