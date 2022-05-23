package com.apsiworktracking.apsiworktracking.controller;

import com.apsiworktracking.apsiworktracking.model.Project;
import com.apsiworktracking.apsiworktracking.model.User;
import com.apsiworktracking.apsiworktracking.service.UserRegistartionService;
import com.apsiworktracking.apsiworktracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpSession;
import javax.ws.rs.NotAuthorizedException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Map;

@CrossOrigin(origins={"https://ashy-ground-0223e9e03.1.azurestaticapps.net", "http://localhost:4200"})
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

    @GetMapping("/person/{id}")
    public User getPerson(@PathVariable("id") Long id)
    {
        return userService.getPerson(id);
    }

    @GetMapping("/person/{username}/projects")
    public Set<Project> getProjectsForUser(@PathVariable("username") String username)
    {
        return userService.getUserProjects(username);
    }


    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }

    @DeleteMapping("/user/delete/{id}")
    public void deleteUser (@PathVariable("id") Long id){
        userService.deletePerson(id);
    }

    @PutMapping("/user/update/{id}")
    public void updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @GetMapping("/user/{id}/projects")
    public Set<Project> getProjectsForUser(@PathVariable("id") Long id){
        return userService.getProjectsForUser(id);
    }


    @GetMapping("/register")
    public User createUser(@RequestBody User user)
    {
        return userRegistartionService.createPerson(user);
    }

    @RequestMapping("/token")
    public Map<String,String> token(HttpSession session) {

        return Collections.singletonMap("token", session.getId());

    }

    private void authorize(String authorization) {
        if (authorization.isEmpty() || RequestContextHolder.currentRequestAttributes().getSessionId().equals(authorization)) {
            throw new NotAuthorizedException("Session id is not valid");
        }
    }

}
