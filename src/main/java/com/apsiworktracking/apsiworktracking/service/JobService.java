package com.apsiworktracking.apsiworktracking.service;


import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ProjectReposioty projectReposioty;

    public Job createJob(Job job) {
        Calendar calendar = Calendar.getInstance();
        job.setStateDate(calendar.getTime());
        job.setTime(roundToHalf(job.getTime()));
        if(!JobTypeEnum.DOCUMENT.equals(job.getType())) {
            job.setDocumentUrl(null);
        } else if(!JobTypeEnum.PROBLEM.equals(job.getType())){
            job.setProblem(null);
        }

        if(!JobStateEnum.REJECTED.equals(job.getState())) {
            job.setRejectionReason(null);
        }
        job.setState(JobStateEnum.NEW);
        job.setRejectionReason(null);
        if(job.getUser().getId()!=null) {
            User user = userRepository.getById(job.getUser().getId());
            job.setUser(user);
        }



        if(job.getTask()!=null & job.getTask().getTaskId() != null) {
            taskService.updateTime(job.getTask().getTaskId(), job.getTime());
        }

        Calendar c = Calendar.getInstance();
        c.setTime(job.getDate());

        LocalDate currentdate = LocalDate.now();
        if(c.get(Calendar.MONTH)+1 != currentdate.getMonth().getValue()) {
//            System.out.println("nierówne");
            if(c.get(Calendar.MONTH)+2 == currentdate.getMonth().getValue()) {
//                System.out.println("+2");
                if(currentdate.getDayOfMonth()>7) {
                    throw new IllegalArgumentException("Date must be until 7 day of next month");
                }
            }
            if(c.get(Calendar.MONTH)+2 < currentdate.getMonth().getValue()) {
                    throw new IllegalArgumentException("Month too old");
            }
        }
        boolean found = false;
        if (job.getTask()!=null){
            Task task = taskRepository.getById(job.getTask().getTaskId());
            job.setTask(task);
            Long projectId = task.getProject().getProjectId();
            System.out.println(projectId);
            User user = userRepository.getById(job.getUser().getId());
            Set<ProjectDetail> projectDetails = user.getProjects();
            for (ProjectDetail detail: projectDetails) {
                if (projectId == detail.getProject().getProjectId()) {
                    Calendar end = Calendar.getInstance();
                    end.setTime(detail.getEndDate());
                    end.add(Calendar.DAY_OF_MONTH, 7);
                    Date endTime = end.getTime();
                    if(job.getDate().after(detail.getStartDate()) && job.getDate().before(endTime)) {
                        found = true;
                    }
                }
            }
        } else throw new IllegalArgumentException("Job must be signed for task and project");
        if (!found) {
            throw new IllegalArgumentException("User is not signed for this project");
        }

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

        Calendar c = Calendar.getInstance();
        c.setTime(job.getDate());

        LocalDate currentdate = LocalDate.now();
        if(c.get(Calendar.MONTH)+1 != currentdate.getMonth().getValue()) {
//            System.out.println("nierówne");
            if(c.get(Calendar.MONTH)+2 == currentdate.getMonth().getValue()) {
//                System.out.println("+2");
                if(currentdate.getDayOfMonth()>7) {
                    throw new IllegalArgumentException("Date must be until 7 day of next month");
                }
            }
            if(c.get(Calendar.MONTH)+2 < currentdate.getMonth().getValue()) {
                throw new IllegalArgumentException("Month too old");
            }
        }
        boolean found = false;
        if (job.getTask()!=null){
            Task task = taskRepository.getById(job.getTask().getTaskId());
            job.setTask(task);
            Long projectId = task.getProject().getProjectId();
            System.out.println(projectId);
            User user = userRepository.getById(job.getUser().getId());
            Set<ProjectDetail> projectDetails = user.getProjects();
            for (ProjectDetail detail: projectDetails) {
                if (projectId == detail.getProject().getProjectId()) {
                    Calendar end = Calendar.getInstance();
                    end.setTime(detail.getEndDate());
                    end.add(Calendar.DAY_OF_MONTH, 7);
                    Date endTime = end.getTime();
                    if(job.getDate().after(detail.getStartDate()) && job.getDate().before(endTime)) {
                        found = true;
                    }
                }
            }
        } else throw new IllegalArgumentException("Job must be signed for task and project");
        if (!found) {
            throw new IllegalArgumentException("User is not signed for this project");
        }

        if(job.getUser().getId()!=null) {
            User user = userRepository.getById(job.getUser().getId());
            job.setUser(user);
        }

        job.setTime(roundToHalf(job.getTime()));
        if(!JobTypeEnum.DOCUMENT.equals(job.getType())) {
            job.setDocumentUrl(null);
        } else if(!JobTypeEnum.PROBLEM.equals(job.getType())){
            job.setProblem(null);
        }

        if(!JobStateEnum.REJECTED.equals(job.getState())) {
//            System.out.println(job.getState());
            job.setRejectionReason(null);
        }

        Double diff =job.getTime()-jobToUpdate.getTime();

        jobToUpdate.setName(job.getName());
        jobToUpdate.setDescription(job.getDescription());
        jobToUpdate.setTime(job.getTime());
        jobToUpdate.setDate(job.getDate());
        jobToUpdate.setState(job.getState());
        jobToUpdate.setRejectionReason(job.getRejectionReason());
        jobToUpdate.setType(job.getType());
        jobToUpdate.setDocumentUrl(job.getDocumentUrl());
        jobToUpdate.setProblem(job.getProblem());
        jobToUpdate.setUser(job.getUser());
        jobToUpdate.setTask(job.getTask());
        if(job.getTask()!=null) {
            taskService.updateTime(job.getTask().getTaskId(), diff);
        }

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
        System.out.println("start");
        Job job = jobRepository.getById(id);
        System.out.println("job");
        if(JobStateEnum.NEW.equals(job.getState()) || JobStateEnum.REJECTED_BY_CLIENT.equals(job.getState()) || JobStateEnum.ACCEPTED.equals(job.getState())) {

            System.out.println(job.getState());
            if(JobStateEnum.ACCEPTED.equals(job.getState())) {
                job.setState(JobStateEnum.NEW);
            } else if( JobStateEnum.REJECTED_BY_CLIENT.equals(job.getState())) {
                job.setState(JobStateEnum.ACCEPTED_BY_CLIENT);
            } else {
                job.setState(JobStateEnum.ACCEPTED);
            }

            Calendar calendar = Calendar.getInstance();
            job.setStateDate(calendar.getTime());
            jobRepository.save(job);


        } else {
            throw new IllegalArgumentException("Job needs to be NEW to be accepted");
        }


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
