package com.apsiworktracking.apsiworktracking.service;


import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.repository.JobRepository;
import com.apsiworktracking.apsiworktracking.repository.ProjectReposioty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectReposioty projectReposioty;

    public Job createJob(Job job) {
        job.setTime(roundToHalf(job.getTime()));
        if(JobTypeEnum.DOCUMENT.equals(job.getType())) {
            job.setProblem(null);
        } else {
            job.setDocumentUrl(null);
        }

        if(!JobStateEnum.REJECTED.equals(job.getState())) {
            System.out.println(job.getState());
            job.setRejectionReason(null);
        }
        job.setState(JobStateEnum.NEW);
        job.setRejectionReason(null);
        jobRepository.save(job);
        return job;
    }

    public List<Job> getAllJobs () {
        return jobRepository.findAll();
    }

    public Job getJob (Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    public void deleteJob (Long id) {
        jobRepository.deleteById(id);
    }

    public Job updateJob(Long id, Job job) {
        Job jobToUpdate = jobRepository.getById(id);
        if (jobToUpdate == null) {
            throw new EntityExistsException("Projects with this id does not exist");
        }
        job.setTime(roundToHalf(job.getTime()));
        if(JobTypeEnum.DOCUMENT.equals(job.getType())) {
            job.setProblem(null);
        } else {
            job.setDocumentUrl(null);
        }

        if(!JobStateEnum.REJECTED.equals(job.getState())) {
            System.out.println(job.getState());
            job.setRejectionReason(null);
        }
        jobToUpdate.setName(job.getName());
        jobToUpdate.setDescription(job.getDescription());
        jobToUpdate.setTime(job.getTime());
        jobToUpdate.setDate(job.getDate());
        jobToUpdate.setState(job.getState());
        jobToUpdate.setRejectionReason(job.getRejectionReason());
        jobToUpdate.setType(job.getType());
        jobToUpdate.setDocumentUrl(job.getDocumentUrl());
        jobToUpdate.setProblem(job.getProblem());
        jobToUpdate.setEmployee(job.getEmployee());
        jobToUpdate.setTask(job.getTask());

        jobRepository.save(jobToUpdate);
        return jobToUpdate;
    }

    public List<Job> getAllJobsaToBeAccepted() {
        List<Project> projects = projectReposioty.findAll();
        List<Job> jobs = new ArrayList<Job>();
        for(Project project: projects) {
            jobs.addAll(projectService.getAllJobsToBeAcceptedForProject(project.getProjectId()));
        }
        return jobs;
    }

    public void acceptJob(Long id) {
        Job job = jobRepository.getById(id);
        if(!JobStateEnum.NEW.equals(job.getState())) {
            throw new IllegalArgumentException("Job needs to be NEW to be accepted");
        }
        job.setState(JobStateEnum.ACCEPTED);
        jobRepository.save(job);
    }


    public List<Job> getAllJobsRejectedByClient() {
        List<Project> projects = projectReposioty.findAll();
        List<Job> jobs = new ArrayList<Job>();
        for(Project project: projects) {
            jobs.addAll(projectService.getAllJobsRejectedByClientForProject(project.getProjectId()));
        }
        return jobs;
    }

    public static Double roundToHalf(Double d) {
        return Math.round(d * 2) / 2.0;
    }


}
