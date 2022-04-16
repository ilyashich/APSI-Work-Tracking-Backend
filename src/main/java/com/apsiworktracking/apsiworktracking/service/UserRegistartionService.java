package com.apsiworktracking.apsiworktracking.service;

import com.apsiworktracking.apsiworktracking.model.User;
import com.apsiworktracking.apsiworktracking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.management.openmbean.KeyAlreadyExistsException;

@Service
public class UserRegistartionService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    public User createPerson(User user)
    {

        if(userRepository.findByUsername(user.getUsername()) != null) {
            throw new KeyAlreadyExistsException("User with this username already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}
