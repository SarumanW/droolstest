package com.drools.controller;

import com.drools.model.entity.User;
import com.drools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController {

    //TODO: implement logging

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode
                (user.getPassword()));

        userRepository.save(user);

        return user;
    }
}
