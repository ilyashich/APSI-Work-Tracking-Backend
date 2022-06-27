package com.apsiworktracking.apsiworktracking.service;

import com.apsiworktracking.apsiworktracking.model.Job;
import com.apsiworktracking.apsiworktracking.model.JobStateEnum;
import com.apsiworktracking.apsiworktracking.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;


@Component
public class ScheduledTasks {

    @Autowired
    private JobService jobService;

    @Autowired
    private JobRepository jobRepository;


    @Scheduled(fixedRate = 1000*60*60*24)
    public void reportCurrentTime() {
        Calendar c = Calendar.getInstance();
        Calendar curr = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -7);
        List<Job> jobs = jobService.getAllJobs();
        for(Job job: jobs) {
            if(job.getStateDate() != null && job.getStateDate().before(c.getTime()) ) {
                if(JobStateEnum.NEW.equals(job.getState())) {
                    job.setState(JobStateEnum.ACCEPTED);
                    job.setStateDate(curr.getTime());
                    jobRepository.save(job);
                } else if(JobStateEnum.ACCEPTED.equals(job.getState())) {
                    job.setState(JobStateEnum.ACCEPTED_BY_CLIENT);
                    jobRepository.save(job);
                }
            }
        }

    }
}