package com.apsiworktracking.apsiworktracking.controller;

import com.apsiworktracking.apsiworktracking.model.User;
import com.apsiworktracking.apsiworktracking.service.UserRegistartionService;
import com.apsiworktracking.apsiworktracking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    public List<User> getPersons()
    {
        return userService.getPersons();
    }

    @GetMapping("/person/{id}")
    public User getPerson(@PathVariable("id") Long id)
    {
        return userService.getPerson(id);
    }

//    @PostMapping("/persons")
//    public User createPerson(@RequestBody User user)
//    {
//        return userService.createPerson(user);
//    }
//
//
////    @PutMapping("/persons/{id}")
////    public User updatePerson(@PathVariable("id") Long id, @RequestBody User user)
////    {
////        return userService.updateUser(id, user);
////    }

//    @DeleteMapping("/persons/{id}")
//    public void deletePerson(@PathVariable("id") Long id)
//    {
//        userService.deletePerson(id);
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
