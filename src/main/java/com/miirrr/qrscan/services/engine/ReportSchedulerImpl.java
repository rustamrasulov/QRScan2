package com.miirrr.qrscan.services.engine;

import com.miirrr.qrscan.config.Config;
import lombok.extern.log4j.Log4j2;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

@Log4j2
public class ReportSchedulerImpl implements ReportScheduler{

    private static Config config;

    private static ReportSchedulerImpl INSTANCE;

    private ReportSchedulerImpl() {
        config = Config.getConfig();
    }

    public static ReportSchedulerImpl getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ReportSchedulerImpl();
        }
        return INSTANCE;
    }

    public void starUp() throws Exception {
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler scheduler = sf.getScheduler();

        JobDetail weeklyReport = JobBuilder.newJob(ReportExport.class)
                .withIdentity("weeklyReport", "group1")
                .build();


        Trigger weeklyTrigger = TriggerBuilder.newTrigger()
                .withIdentity("weeklyTrigger", "group1")
                // "SEC MIN HOUR DAY-OF-MONTH MONTH DAY-OF-WEEK YEAR(Optional)"
                .withSchedule(CronScheduleBuilder.cronSchedule(config.getSchedulerCron()))
                .build();

        scheduler.start();

        scheduler.scheduleJob(weeklyReport, weeklyTrigger);
    }
}
