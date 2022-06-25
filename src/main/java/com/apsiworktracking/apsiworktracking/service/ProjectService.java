package com.apsiworktracking.apsiworktracking.service;

import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.repository.ProjectDetailsRepository;
import com.apsiworktracking.apsiworktracking.repository.ProjectReposioty;
import com.apsiworktracking.apsiworktracking.repository.TaskRepository;
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

import static java.util.Collections.emptySet;

@Service
@RequiredArgsConstructor
public class ProjectService {

    @Autowired
    private ProjectReposioty projectReposioty;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectDetailsRepository projectDetailsRepository;

    public List<Project> getAllProjects() {
        return projectReposioty.findAll();
    }

    public Project createProject(Project project) {
        if(project.getClient() != null ) {
            User client = userRepository.getById(project.getClient().getId());
            if(!UserRoleEnum.CLIENT.equals(client.getRole())) {
                throw new IllegalArgumentException("Client must have correct role");
            }
        }
        projectReposioty.save(project);
        System.out.println(project.getProjectId());
        if(project.getSignedUsers()!=null) {
            for (ProjectDetail projectDetail: project.getSignedUsers()) {
                ProjectDetailKey key = new ProjectDetailKey(projectDetail.getProjectDetailId().getUserId(), project.getProjectId());
                User user = userRepository.getById(projectDetail.getProjectDetailId().getUserId());
                ProjectDetail pro = new ProjectDetail(key, user, project, projectDetail.getRole(), projectDetail.getStartDate(), projectDetail.getEndDate());
                projectDetailsRepository.save(pro);
            }
        }
        return project;
    }

    public Project getProject(Long id) {
        return projectReposioty.findById(id).orElse(null);
    }

//    public void addUserToProject(Long projectId, Long userId){
//        Project project = projectReposioty.getById(projectId);
//        User user = userRepository.getById(userId);
//
//        user.getProjects().add(project);
//        project.getSignedUsers().add(user);
//
//        userRepository.save(user);
//        projectReposioty.save(project);
//    }

    public void deleteProject(Long id) {

        projectReposioty.deleteById(id);

    }

    public void updateProject(Long id, Project project) {
        Project projectToUpdate = projectReposioty.getById(id);
        if (projectToUpdate == null) {
            throw new EntityExistsException("Projects with this id does not exist");
        }
        if(project.getClient() != null ) {
            User client = userRepository.getById(project.getClient().getId());
            if(!UserRoleEnum.CLIENT.equals(client.getRole())) {
                throw new IllegalArgumentException("Client must have correct role");
            }
        }
        projectToUpdate.setName(project.getName());
        projectToUpdate.setDescription(project.getDescription());
        if(projectToUpdate.getSignedUsers()!=null) {
            for (ProjectDetail projectDetail: projectToUpdate.getSignedUsers()) {
                System.out.println(projectDetail.getRole());
                ProjectDetailKey key = new ProjectDetailKey(projectDetail.getProjectDetailId().getUserId(), id);
                ProjectDetail pro = projectDetailsRepository.getById(key);
                projectDetailsRepository.delete(pro);
            }
        }
        projectToUpdate.setSignedUsers(emptySet());
        if(project.getSignedUsers()!=null) {
            for (ProjectDetail projectDetail: project.getSignedUsers()) {
                ProjectDetailKey key = new ProjectDetailKey(projectDetail.getProjectDetailId().getUserId(), projectDetail.getProjectDetailId().getProjectId());
                User user = userRepository.getById(projectDetail.getProjectDetailId().getUserId());
                Project project1 = projectReposioty.getById(id);
                ProjectDetail pro = new ProjectDetail(key, user, project1, projectDetail.getRole(), projectDetail.getStartDate(), projectDetail.getEndDate());


                projectDetailsRepository.save(pro);
                user.setProjects(projectDetailsRepository.findProjectDetailsByUser(user));
                userRepository.save(user);
                System.out.println(user.getProjects());



            }
        }
        projectToUpdate.setSignedUsers(project.getSignedUsers());

        projectToUpdate.setTasks(project.getTasks());
        projectToUpdate.setClient(project.getClient());
        projectReposioty.save(projectToUpdate);
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
