package lavka.controllers;


import lavka.drools.model.entity.User;
import lavka.drools.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import responsemodel.UserResponse;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{login}")
    public UserResponse getUserByLogin(@PathVariable String login) {
        User user = userRepository.findByLogin(login);

        log.info("UserController.getUserByLogin | User with login {} is logged in", login);

        return new UserResponse(user);
    }

    @PostMapping("/save")
    public UserResponse updateUser(@RequestBody User user) {
        userRepository.save(user);

        log.info("UserController.updateUser | User with login {} is updated", user.getLogin());

        return new UserResponse(user);
    }
}
