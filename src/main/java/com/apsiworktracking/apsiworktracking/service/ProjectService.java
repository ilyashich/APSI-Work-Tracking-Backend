package com.apsiworktracking.apsiworktracking.service;

import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.repository.ProjectReposioty;
import com.apsiworktracking.apsiworktracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private ProjectReposioty projectReposioty;

    @Autowired
    private UserRepository userRepository;

    public List<Project> getAllProjects() {
        return projectReposioty.findAll();
    }

    public Project createProject(Project project) {
        if(project.getClient() != null && !UserRoleEnum.CLIENT.equals(project.getClient().getRole())) {
            throw new IllegalArgumentException("Client must have correct role");
        }
        projectReposioty.save(project);
        return project;
    }

    public Project getProject(Long id) {
        return projectReposioty.findById(id).orElse(null);
    }

    public void addUserToProject(Long projectId, Long userId){
        Project project = projectReposioty.getById(projectId);
        User user = userRepository.getById(userId);

        user.getProjects().add(project);
        project.getSignedUsers().add(user);

        userRepository.save(user);
        projectReposioty.save(project);
    }

    public void deleteProject(Long id) {

        projectReposioty.deleteById(id);

    }

    public void updateProject(Long id, Project project) {
        Project projectTuUpdate = projectReposioty.getById(id);
        if (projectTuUpdate == null) {
            throw new EntityExistsException("Projects with this id does not exist");
        }
        if(project.getClient() != null && !UserRoleEnum.CLIENT.equals(project.getClient().getRole())) {
            throw new IllegalArgumentException("Client must have correct role");
        }
        projectTuUpdate.setName(project.getName());
        projectTuUpdate.setDescription(project.getDescription());
        projectTuUpdate.setSignedUsers(project.getSignedUsers());
        projectTuUpdate.setTasks(project.getTasks());
        projectReposioty.save(projectTuUpdate);
    }

    public List<Job> getAllJobsForProject(Long id) {
        Project project = projectReposioty.getById(id);
        List<Job> jobs = new ArrayList<Job>();
        Set<Task> taskIterator = project.getTasks();

        for (Task task: taskIterator) {
            Set<Job> jobIterator = task.getJobs();
            for (Job job: jobIterator) {
                jobs.add(job);
            }
        }
        return jobs;
    }

    public List<Job> getAllJobsToBeAcceptedForProject(Long id) {
        Project project = projectReposioty.getById(id);
        List<Job> jobs = new ArrayList<Job>();
        Set<Task> taskIterator = project.getTasks();

        for (Task task: taskIterator) {
            Set<Job> jobIterator = task.getJobs();
            for (Job job: jobIterator) {
                if(JobStateEnum.NEW.equals(job.getState())){
                    jobs.add(job);
                }

            }
        }
        return jobs;
    }

    public List<Job> getAllJobsRejectedByClientForProject(Long id) {
        Project project = projectReposioty.getById(id);
        List<Job> jobs = new ArrayList<Job>();
        Set<Task> taskIterator = project.getTasks();

        for (Task task: taskIterator) {
            Set<Job> jobIterator = task.getJobs();
            for (Job job: jobIterator) {
                if(JobStateEnum.REJECTED_BY_CLIENT.equals(job.getState())){
                    jobs.add(job);
                }

            }
        }
        return jobs;
    }

    public List<Job> getAllJobsToBeAcceptByClientForProject(Long id) {
        Project project = projectReposioty.getById(id);
        List<Job> jobs = new ArrayList<Job>();
        Set<Task> taskIterator = project.getTasks();

        for (Task task: taskIterator) {
            Set<Job> jobIterator = task.getJobs();
            for (Job job: jobIterator) {
                if(JobStateEnum.ACCEPTED.equals(job.getState())){
                    jobs.add(job);
                }

            }
        }
        return jobs;
    }

}
