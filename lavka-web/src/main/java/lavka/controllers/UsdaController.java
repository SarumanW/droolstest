package lavka.controllers;


import lavka.drools.model.entity.User;
import lavka.drools.model.integration.usda.UsdaFoodItem;
import lavka.drools.repository.IngredientRepository;
import lavka.drools.repository.ProductNutritionRepository;
import lavka.drools.repository.UserRepository;
import lavka.functionalmodel.RuleEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/usda")
public class UsdaController {

    @Autowired
    private ProductNutritionRepository productNutritionRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

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

    @GetMapping("/foods")
    public List<UsdaFoodItem> getFoodsList() {
        return Collections.emptyList();
    }

}
