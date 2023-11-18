package com.twohoseon.app.jobs;

import com.twohoseon.app.util.AppleUtility;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : PostExpireJob
 * @date : 11/1/23 2:20 AM
 * @modifyed : $
 **/

public class RefreshAppleSecretJob implements Job {

    @Autowired
    private AppleUtility appleUtility;

    @Override
    public void execute(JobExecutionContext context) {
        appleUtility.createAppleClientSecret();
    }
}
