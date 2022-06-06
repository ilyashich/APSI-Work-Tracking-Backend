package com.apsiworktracking.apsiworktracking.service;

import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.repository.JobRepository;
import com.apsiworktracking.apsiworktracking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.ws.rs.NotAuthorizedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobService jobService;

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public User getPerson(String username)
    {
        return userRepository.findByUsername(username);
    }

    public void deletePerson(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserPrinciple(user);

    }

    public Set<Project> getUserProjects(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getProjects();
    }

    public void updateUser(Long id, User user) {
        User userToUpdate = userRepository.getById(id);
        if (userToUpdate == null) {
            throw new UsernameNotFoundException("User not found");
        }
        userToUpdate.setPassword(user.getPassword());
        userToUpdate.setUsername(user.getUsername());
        userToUpdate.setRole(user.getRole());
        userToUpdate.setName(user.getName());
        userToUpdate.setSurname(user.getSurname());
        userToUpdate.setProjects(user.getProjects());
        userRepository.save(userToUpdate);
    }

    public Set<Project> getProjectsForUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getProjects();
    }

    public List<Job> getAllJobsToBeAccepted (String username) {
        User user = userRepository.findByUsername(username);
        Set<Project> projects = getProjectsForUser(username);
        List<Job> jobs = new ArrayList<Job>();
        for(Project project: projects) {
            jobs.addAll(projectService.getAllJobsToBeAcceptedForProject(project.getProjectId()));
        }
        return jobs;
    }

    public List<Job> getAllJobsToBeAcceptedByClient (String username) {
        User user = userRepository.findByUsername(username);
        Set<Project> projects = user.getClientProject();
        List<Job> jobs = new ArrayList<Job>();
        for(Project project: projects) {
            jobs.addAll(projectService.getAllJobsToBeAcceptByClientForProject(project.getProjectId()));
        }
        return jobs;
    }

    public Set<Job> getAllJobsForUser (String username) {
        User user = userRepository.findByUsername(username);
        return user.getJobs();
    }

    public List<Task> getAllTasksForProjects (String username) {
        User user = userRepository.findByUsername(username);
        Set<Project> projects = user.getProjects();
        List<Task> task = new ArrayList<Task>();
        for(Project project: projects) {
            task.addAll(project.getTasks());
        }
        return task;
    }

    public void acceptJobByManager(String username, Long jobId) {
        User user = userRepository.findByUsername(username);
//        if(!UserRoleEnum.MANAGER.equals(user.getRole())) {
//            throw new NotAuthorizedException("Only manager can accept job");
//        }
//        jobService.acceptJob(jobId);
    }

    public void acceptJobByClient(String username, Long jobId) {
        User user = userRepository.findByUsername(username);
        if(!UserRoleEnum.CLIENT.equals(user.getRole())) {
            throw new NotAuthorizedException("Only client can accept this job");
        }
        Job job = jobRepository.getById(jobId);
        if(!JobStateEnum.ACCEPTED.equals(job.getState())) {
            throw new IllegalArgumentException("Job needs to be ACCEPTED to be accepted by client");
        }
        job.setState(JobStateEnum.ACCEPTED_BY_CLIENT);
        jobRepository.save(job);
    }

    public void rejectJob (String username, Long jobId, String reason) {
        User user = userRepository.findByUsername(username);
        if(!UserRoleEnum.MANAGER.equals(user.getRole())) {
            throw new NotAuthorizedException("Only manager can reject this job");
        }
        Job job = jobRepository.getById(jobId);
        if(!JobStateEnum.NEW.equals(job.getState())) {
            throw new IllegalArgumentException("Job needs to be NEW to be rejested");
        }
        job.setState(JobStateEnum.REJECTED);
        job.setRejectionReason(reason);
        jobRepository.save(job);
    }

    public void rejectJobByClient (String username, Long jobId, String reason) {
        User user = userRepository.findByUsername(username);
        if(!UserRoleEnum.CLIENT.equals(user.getRole())) {
            throw new NotAuthorizedException("Only client can reject this job");
        }
        Job job = jobRepository.getById(jobId);
        if(!JobStateEnum.ACCEPTED.equals(job.getState())) {
            throw new IllegalArgumentException("Job needs to be ACCEPTED to be rejected by client");
        }
        job.setState(JobStateEnum.REJECTED_BY_CLIENT);
        job.setRejectionReason(reason);
        jobRepository.save(job);
    }

}
