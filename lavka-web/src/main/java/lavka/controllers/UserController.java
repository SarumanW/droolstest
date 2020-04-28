package lavka.controllers;


import lavka.drools.model.entity.User;
import lavka.drools.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
