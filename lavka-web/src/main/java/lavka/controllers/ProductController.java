package lavka.controllers;


import lavka.drools.integrationservice.IntegrationService;
import lavka.drools.model.entity.Category;
import lavka.drools.model.entity.Product;
import lavka.drools.model.entity.RelationUserProduct;
import lavka.drools.model.entity.User;
import lavka.drools.repository.CategoryRepository;
import lavka.drools.repository.ProductRepository;
import lavka.drools.repository.UserRepository;
import lavka.functionalmodel.RuleEngine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import responsemodel.ProductResponse;
import responsemodel.UserResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
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
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/products")
    public List<ProductResponse> getAllProducts() {
        List<Product> products = (List<Product>) productRepository.findAll();

        log.info("ProductController.getAllProducts | products retrieved");

        return products.stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/categories")
    public List<Category> getCategories() {
//        integrationService.importCategories();
//
//        integrationService.importProductBase();

        List<Category> all = (List<Category>) categoryRepository.findAll();
        all.add(new Category(0L));

        log.info("ProductController.getCategories | categories retrieved");

        return all;
    }

    @PostMapping(value = "/like/{productId}")
    public UserResponse likeProduct(@RequestBody User currentUser, @PathVariable Long productId) {
        User user = userRepository.findById(currentUser.getId()).orElse(new User());

        Product product = productRepository.findById(productId).orElse(new Product());

        user.getLikedProducts().add(new RelationUserProduct(product, user, true, true));

        User savedUser = userRepository.save(user);

        log.info("ProductController.likeProduct | User {userId} liked product {productId}", savedUser.getId(), productId);

        return new UserResponse(savedUser);
    }

    @PostMapping(value = "/unlike/{productId}")
    public UserResponse unlikeProduct(@RequestBody User currentUser, @PathVariable Long productId) {
        User user = userRepository.findById(currentUser.getId()).orElse(new User());

        RelationUserProduct relationUserProduct = user.getLikedProducts().stream()
                .filter(r -> r.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);

        user.getLikedProducts().remove(relationUserProduct);

        User savedUser = userRepository.save(user);

        log.info("ProductController.unlikeProduct | User {userId} unliked product {productId}", savedUser.getId(), productId);

        return new UserResponse(savedUser);
    }

}
