package com.drools;

import com.drools.model.Product;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class MegaOfferController {

    @Autowired
    private KieSession session;

    @PostMapping("/order")
    public Order orderNow(@RequestBody Order order) {
        List<Product> list = new ArrayList<>();
        session.setGlobal("userProductsList", list);
        session.insert(order);
        session.fireAllRules();
        return order;
    }

    public void test(){}
}
