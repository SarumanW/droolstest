package lavka.functionalmodel;


import lavka.drools.model.entity.Product;
import lavka.drools.model.entity.User;
import lavka.drools.repository.ProductRepository;
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
