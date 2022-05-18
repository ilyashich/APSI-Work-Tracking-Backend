package com.apsiworktracking.apsiworktracking.service;

import com.apsiworktracking.apsiworktracking.model.Project;
import com.apsiworktracking.apsiworktracking.model.User;
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

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;

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

}
