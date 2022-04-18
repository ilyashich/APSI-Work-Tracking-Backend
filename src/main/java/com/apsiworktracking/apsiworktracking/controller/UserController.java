package com.apsiworktracking.apsiworktracking.controller;

import com.apsiworktracking.apsiworktracking.model.User;
import com.apsiworktracking.apsiworktracking.service.UserRegistartionService;
import com.apsiworktracking.apsiworktracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@CrossOrigin(origins="https://ashy-ground-0223e9e03.1.azurestaticapps.net")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserRegistartionService userRegistartionService;

    @GetMapping("/persons")
    public List<User> getUsers()
    {
        return userService.getUsers();
    }

//    @GetMapping("/person/{id}")
//    public User getPerson(@PathVariable("id") Long id)
//    {
//        return userService.getPerson(id);
//    }


    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }


    @GetMapping("/register")
    public User createUser(@RequestBody User user)
    {
        return userRegistartionService.createPerson(user);
    }
}
