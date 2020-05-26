package lavka.controllers;


import lavka.drools.model.entity.User;
import lavka.drools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import responsemodel.UserResponse;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{login}")
    public UserResponse getUserByLogin(@PathVariable String login) {
        User user = userRepository.findByLogin(login);

        return new UserResponse(user);
    }

    @PostMapping("/save")
    public User updateUser(@RequestBody User user) {
        return userRepository.save(user);
    }
}
