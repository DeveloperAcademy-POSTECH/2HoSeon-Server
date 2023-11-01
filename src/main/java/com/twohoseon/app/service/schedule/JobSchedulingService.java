package com.twohoseon.app.service.schedule;

import org.quartz.SchedulerException;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : JobSchedulingService
 * @date : 11/1/23 5:29 AM
 * @modifyed : $
 **/
public interface JobSchedulingService {
    void schedulePostExpireJob(Long memberId, Long postId) throws SchedulerException;
}
