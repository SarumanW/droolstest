package lavka.controllers;


import lavka.drools.integrationservice.IntegrationService;
import lavka.drools.model.entity.User;
import lavka.drools.repository.UserRepository;
import lavka.functionalmodel.RuleEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usda")
public class UsdaController {

    @Qualifier("listexIntegrationService")
    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test/{userId}")
    public User test(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(new User());

        ruleEngine.fireAllRulesForOneUser(user);

        return user;
    }

    @GetMapping("/categories")
    public void getFoodsList() {
        integrationService.importCategories();
    }

}
