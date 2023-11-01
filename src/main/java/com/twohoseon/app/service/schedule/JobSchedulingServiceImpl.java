package com.twohoseon.app.service.schedule;

import com.twohoseon.app.jobs.PostExpireJob;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author : hyunwoopark
 * @version : 1.0.0
 * @package : twohoseon
 * @name : JobSchedulingServiceImpl
 * @date : 11/1/23 5:29 AM
 * @modifyed : $
 **/
@Slf4j
@RequiredArgsConstructor
@Service
public class JobSchedulingServiceImpl implements JobSchedulingService {
    private final Scheduler scheduler;

    public void schedulePostExpireJob(Long memberId, Long postId) throws SchedulerException {

        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("postId", postId);
        jobDataMap.put("memberId", memberId);

        Date startAtDate = DateBuilder.futureDate(5, DateBuilder.IntervalUnit.SECOND);  // 현재로부터 5초 후
//        Date startAtDate = DateBuilder.futureDate(24, DateBuilder.IntervalUnit.HOUR);  // 현재로부터 24시간 후

        JobDetail jobDetail = JobBuilder.newJob(PostExpireJob.class)
                .usingJobData(jobDataMap)
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startAt(startAtDate)
                .build();

        scheduler.scheduleJob(jobDetail, trigger);
    }

}