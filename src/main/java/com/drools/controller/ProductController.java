package com.drools.controller;

import com.drools.model.entity.Product;
import com.drools.model.entity.User;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    private List<Product> products;

    @Autowired
    private KieSession session;

    ProductController() {
        products = new ArrayList<>();
//        products.add(new Product("milk", Collections.singletonList(new Item(1L, "milk", false))));
//        products.add(new Product("coffee", Collections.singletonList(new Item(2L, "coffee", false))));
    }

    @PostMapping("/order")
    public List<Product> orderNow(@RequestBody User user) {
        session.insert(user);

        for (Product product : products) {
            session.insert(product);
        }

        session.fireAllRules();

        return products;
    }
}
