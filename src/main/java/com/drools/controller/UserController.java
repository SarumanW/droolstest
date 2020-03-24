package com.drools.controller;

import com.drools.model.entity.User;
import com.drools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{login}")
    public User getUserByLogin(@PathVariable String login) {
        return userRepository.findByLogin(login);
    }
}
