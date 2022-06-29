package com.apsiworktracking.apsiworktracking.controller;

import com.apsiworktracking.apsiworktracking.model.*;
import com.apsiworktracking.apsiworktracking.service.UserRegistartionService;
import com.apsiworktracking.apsiworktracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import java.security.Principal;
import java.util.*;

@CrossOrigin(origins={"https://jolly-field-05989fe03.1.azurestaticapps.net", "http://localhost:4200"})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController
{

    private String headerName = "X-Auth-Token";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegistartionService userRegistartionService;

    @GetMapping("/persons")
    public List<User> getUsers()
    {
//        authorize(authorization);
        return userService.getUsers();
    }

    @GetMapping("/person/{username}")
    public User getPerson(@PathVariable("username") String username)
    {
        return userService.getPerson(username);
    }

    @GetMapping("/person/{username}/projects")
    public List<Project> getProjectsForUser(@PathVariable("username") String username)
    {
        return userService.getUserProjects(username);
    }

    @GetMapping("/person/{username}/projects_manager")
    public List<Project> getProjectsForUserManager(@PathVariable("username") String username)
    {
        return userService.getProjectsForUserManager(username);
    }


//    @GetMapping("/person/{username}/projects/id")
//    public List<Long> getProjectsForUser2(@PathVariable("username") String username)
//    {
//        List<Project> pro = userService.getUserProjects(username);
//        List<Long> list = new ArrayList<>();
//        for(Project project: pro){
//            list.add(project.getProjectId());
//        }
//        return list;
//    }


    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

//    @DeleteMapping("/user/delete/{id}")
//    public void deleteUser (@PathVariable("id") Long id){
//        userService.deletePerson(id);
//    }
//
//    @PutMapping("/user/update/{id}")
//    public void updateUser(@PathVariable("id") Long id, @RequestBody User user) {
//        userService.updateUser(id, user);
//    }

//    @GetMapping("/user/{id}/projects")
//    public Set<Project> getProjectsForUser(@PathVariable("id") Long id){
//        return userService.getProjectsForUser(id);
//    }


    @GetMapping("/register")
    public User createUser(@RequestBody User user)
    {
        return userRegistartionService.createPerson(user);
    }

//    @RequestMapping("/token")
//    public Map<String,String> token(HttpSession session) {
//
//        return Collections.singletonMap("token", session.getId());
//
//    }

    @GetMapping("/user/{username}/jobs_to_accept")
    public List<Job> getAllJobsToAccept(@PathVariable("username") String username)
    {
        return userService.getAllJobsToBeAccepted(username);
    }

    @GetMapping("/user/{username}/jobs_to_accept_by_client")
    public List<Job> getAllJobsToAcceptByClient(@PathVariable("username") String username)
    {
        return userService.getAllJobsToBeAcceptedByClient(username);
    }

    @PutMapping("/user/{username}/job_to_accept/{jobId}")
    public void acceptJob(@PathVariable("username") String username, @PathVariable("jobId") Long jobId)
    {
        userService.acceptJobByManager(username, jobId);
    }

    @PutMapping("/user/{username}/job_to_accept_by_client/{jobId}")
    public void acceptJobByClient(@PathVariable("username") String username, @PathVariable("jobId") Long jobId)
    {
        userService.acceptJobByClient(username, jobId);
    }

    @PutMapping("/user/{username}/job_to_reject/{jobId}")
    public void rejectJob(@PathVariable("username") String username, @PathVariable("jobId") Long jobId, @RequestBody String reason)
    {
        userService.rejectJob(username, jobId, reason);
    }

    @PutMapping("/user/{username}/job_to_reject_by_client/{jobId}")
    public void rejectJobByClient(@PathVariable("username") String username, @PathVariable("jobId") Long jobId, @RequestBody String reason)
    {
        userService.rejectJobByClient(username, jobId, reason);
    }


//    private void authorize(String authorization) {
//        if (authorization.isEmpty() || RequestContextHolder.currentRequestAttributes().getSessionId().equals(authorization)) {
//            throw new NotAuthorizedException("Session id is not valid");
//        }
//    }

    @GetMapping("/user/{username}/tasks")
    public List<Task> getTasksForUser(@PathVariable("username") String username)
    {
        return userService.getAllTasksForProjects(username);
    }

    @GetMapping("/user/{username}/jobs")
    public Set<Job> getJobsForUser(@PathVariable("username") String username)
    {
        return userService.getAllJobsForUser(username);
    }

    @GetMapping("/user/{username}/calendar/jobs")
    public List<CalendarJob> getJobsForUserCalendar(@PathVariable("username") String username)
    {
        return userService.getAllJobsForUserCalendar(username);
    }

    @GetMapping("/clients")
    public List<User> getClients()
    {
        return userService.getClients();
    }




}
