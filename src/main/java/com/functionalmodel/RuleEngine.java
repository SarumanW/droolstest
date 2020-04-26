package com.functionalmodel;

import com.drools.model.entity.Product;
import com.drools.model.entity.User;
import com.drools.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RuleEngine {

    @Autowired
    private ProductRepository productRepository;

    static List<Rule> rules;

    static {
        rules = new ArrayList<>();

        rules.addAll(KetoDietRules.getRules());

        rules.addAll(GeneralRules.getRules());
    }


    public void fireAllRulesForOneUser(User user) {
        List<Product> productList = (List<Product>) productRepository.findAll();

        for (Product product : productList) {
            UserProductPair userProductPair = new UserProductPair(user, product);

            rules.get(0).getFire().accept(userProductPair);
        }
    }
}
