package com.twohoseon.app.config;

import com.twohoseon.app.common.AutowiringSpringBeanJobFactory;
import lombok.RequiredArgsConstructor;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : QuartzConfig
 * @date : 11/1/23 2:04 AM
 * @modifyed : $
 **/
@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory;

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setTransactionManager(transactionManager);
        schedulerFactoryBean.setJobFactory(autowiringSpringBeanJobFactory);
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler() {
        return schedulerFactoryBean().getScheduler();
    }
}
