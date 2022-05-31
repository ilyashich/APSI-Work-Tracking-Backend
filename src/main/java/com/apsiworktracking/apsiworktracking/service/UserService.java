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

    public List<User> getUsers()
    {
        return userRepository.findAll();
    }

    public User getPerson(Long id)
    {
        return userRepository.findById(id).orElse(null);
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

    public Set<Project> getProjectsForUser(Long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user.getProjects();
    }

    public List<Job> getAllJobsToBeAccepted (Long id) {
        User user = userRepository.getById(id);
        Set<Project> projects = user.getProjects();
        List<Job> jobs = new ArrayList<Job>();
        for(Project project: projects) {
            jobs.addAll(projectService.getAllJobsToBeAcceptedForProject(project.getProjectId()));
        }
        return jobs;
    }

    public List<Job> getAllJobsToBeAcceptedByClient (Long id) {
        User user = userRepository.getById(id);
        Set<Project> projects = user.getClientProject();
        List<Job> jobs = new ArrayList<Job>();
        for(Project project: projects) {
            jobs.addAll(projectService.getAllJobsToBeAcceptByClientForProject(project.getProjectId()));
        }
        return jobs;
    }

    public void acceptJobByManager(Long userId, Long jobId) {
        User user = userRepository.getById(userId);
        if(!UserRoleEnum.MANAGER.equals(user.getRole())) {
            throw new NotAuthorizedException("Only manager can accept job");
        }
        Job job = jobRepository.getById(jobId);
        if(!JobStateEnum.NEW.equals(job.getState())) {
            throw new IllegalArgumentException("Job needs to be NEW to be accepted");
        }
        job.setState(JobStateEnum.ACCEPTED);
        jobRepository.save(job);
    }

    public void acceptJobByClient(Long userId, Long jobId) {
        User user = userRepository.getById(userId);
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

    public void rejectJob (Long userId, Long jobId, String reason) {
        User user = userRepository.getById(userId);
        if(UserRoleEnum.EMPLOYEE.equals(user.getRole()) ) {
            throw new NotAuthorizedException("Only manager or client can reject job");
        }
        Job job = jobRepository.getById(jobId);
        if(!(JobStateEnum.NEW.equals(job.getState()) || JobStateEnum.ACCEPTED.equals(job.getState()))) {
            throw new IllegalArgumentException("Job needs to be NEW or ACCEPTED to be rejected");
        }
        job.setState(JobStateEnum.REJECTED);
        job.setRejectionReason(reason);
        jobRepository.save(job);
    }

}
