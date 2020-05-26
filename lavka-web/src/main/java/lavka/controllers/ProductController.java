package lavka.controllers;


import lavka.drools.integrationservice.IntegrationService;
import lavka.drools.model.entity.Category;
import lavka.drools.model.entity.RelationUserProduct;
import lavka.drools.model.entity.User;
import lavka.drools.repository.CategoryRepository;
import lavka.drools.repository.UserRepository;
import lavka.functionalmodel.RuleEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import responsemodel.ProductResponse;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/food")
public class ProductController {

    @Qualifier("listexIntegrationService")
    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/test/{userId}")
    public User test(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(new User());

        ruleEngine.fireAllRulesForOneUser(user);

        return user;
    }

    @GetMapping(value = "/categories")
    public ResponseEntity<List<Category>> getCategories() {
        //integrationService.importCategories();

        //integrationService.importProductBase();

        List<Category> all = (List<Category>) categoryRepository.findAll();
        all.add(new Category(0L));

        return ResponseEntity.ok(all);
    }

    @GetMapping(value = "/{categoryId}/products")
    public ResponseEntity<List<ProductResponse>> getProducts(@PathVariable Long categoryId) {
        User user = userRepository.findById(1L).orElse(new User());

        List<ProductResponse> products = user.getUserProducts().stream()
                .map(RelationUserProduct::getProduct)
                .filter(p -> p.getCategoryId().equals(categoryId))
                .distinct()
                .map(ProductResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

}
