package com.functionalmodel;

import com.drools.model.entity.Diet;
import com.drools.model.entity.Product;
import com.drools.model.entity.User;
import com.drools.utils.RestrictionImport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class RuleEngine {

    Consumer<UserProductPair> ketoDietIdIndicatePredicate = userProductPair -> {
        if (userProductPair.getUser().getFollowedDiets()
                .stream()
                .filter(d -> d.getId().equals(4L))
                .findAny()
                .orElse(null) != null) {
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aDI, 4);
        }
    };

    Consumer<UserProductPair> ketoDietIndicateAssertion = userProductPair -> userProductPair.getUser()
            .getAttributeObjectMap().put(Attribute.aDIE, Diet.DietType.KETO);

    Rule<UserProductPair> ketoDietIndicateRule = new Rule(1, 1,
            ketoDietIdIndicatePredicate, ketoDietIndicateAssertion, 2, 2);

    //-----

    Consumer<UserProductPair> ketoDietRestrictionPredicate = userProductPair -> {

    };

    Consumer<UserProductPair> ketoDietRestrictionAssertion = userProductPair -> {
        if (userProductPair.getUser()
                .getAttributeObjectMap().get(Attribute.aDIE).equals(Diet.DietType.KETO)) {

            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aRC,
                    RestrictionImport.getRestrictionsByDietId(Diet.DietType.KETO.getId()).getRestrictedCategoriesIds());

            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aRW,
                    RestrictionImport.getRestrictionsByDietId(Diet.DietType.KETO.getId()).getRestrictedItems());
        }
    };

    Rule<UserProductPair> ketoDietRestrictionRule = new Rule(2, 2,
            ketoDietRestrictionPredicate, ketoDietRestrictionAssertion, 3, 3);

    //------

    Consumer<UserProductPair> productCharsPredicate = userProductPair -> {
        if (userProductPair.getProduct() != null) {
            userProductPair.getUser()
                    .getAttributeObjectMap().put(Attribute.aPR, userProductPair.getProduct());
        }
    };

    Consumer<UserProductPair> productCharsAssertion = userProductPair -> {
        Product product = (Product) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aPR);

        if (product != null) {
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPC, product.getFoodCode());
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPN, product.getName());
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPI, product.getComposition());
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPNU, product.getNutritionFacts());
        }
    };

    Rule<UserProductPair> productCharsRule = new Rule(3, 3,
            productCharsPredicate, productCharsAssertion, 4, 4);

    //------

    Consumer<UserProductPair> carbsOperationPredicate = userProductPair -> {

    };

    Consumer<UserProductPair> carbsOperationAssertion = userProductPair -> {
        Object productNutrition = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aPNU);
        Object diet = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aDIE);

        if (diet.equals(Diet.DietType.KETO) && productNutrition != null) {
            List<Operation> operationList = (List<Operation>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aOP);

            if (operationList == null) {
                operationList = new ArrayList<>();
                operationList.add(Operation.CHECK_CARBS);
                userProductPair.getUser().getAttributeObjectMap().put(Attribute.aOP, operationList);
            } else {
                if (!operationList.contains(Operation.CHECK_CARBS)) {
                    operationList.add(Operation.CHECK_CARBS);
                }
            }
        }
    };

    Rule<UserProductPair> carbsOperationsRule = new Rule(4, 4,
            carbsOperationPredicate, carbsOperationAssertion, 4, 5);

    //------

    Consumer<UserProductPair> categoriesOperationPredicate = userProductPair -> {

    };

    Consumer<UserProductPair> categoriesOperationAssertion = userProductPair -> {
        Object restrictedCategories = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aRC);
        Object productCategories = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aPC);

        if (restrictedCategories != null && productCategories != null) {
            List<Operation> operationList = (List<Operation>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aOP);

            if (operationList == null) {
                operationList = new ArrayList<>();
                operationList.add(Operation.CHECK_CATEGORIES);
                userProductPair.getUser().getAttributeObjectMap().put(Attribute.aOP, operationList);
            } else {
                if (!operationList.contains(Operation.CHECK_CATEGORIES)) {
                    operationList.add(Operation.CHECK_CATEGORIES);
                }
            }
        }
    };

    Rule<UserProductPair> categoriesOperationsRule = new Rule(4, 5,
            categoriesOperationPredicate, categoriesOperationAssertion, 4, 6);


    //------

    Consumer<UserProductPair> wordsOperationPredicate = userProductPair -> {

    };

    Consumer<UserProductPair> wordsOperationAssertion = userProductPair -> {
        Object restrictedWords = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aRW);
        Object productName = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aPN);
        Object productIngredients = userProductPair.getUser().getAttributeObjectMap().get(Attribute.aPI);

        if (restrictedWords != null && productName != null && productIngredients != null) {
            List<Operation> operationList = (List<Operation>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aOP);

            if (operationList == null) {
                operationList = new ArrayList<>();
                operationList.add(Operation.CHECK_WORDS);
                userProductPair.getUser().getAttributeObjectMap().put(Attribute.aOP, operationList);
            } else {
                if (!operationList.contains(Operation.CHECK_WORDS)) {
                    operationList.add(Operation.CHECK_WORDS);
                }
            }
        }
    };

    Rule<UserProductPair> wordsOperationsRule = new Rule(4, 6,
            wordsOperationPredicate, wordsOperationAssertion, 5, 7);

    //------

    Consumer<UserProductPair> operationPredicate = userProductPair -> {

    };

    Consumer<UserProductPair> operationAssertion = userProductPair -> {
        List<Operation> operationList = (List<Operation>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aOP);

        for (Operation operation : operationList) {
            operation.getConsumerFunction().accept(userProductPair);
        }
    };

    Rule<UserProductPair> operationsRule = new Rule(5, 7,
            operationPredicate, operationAssertion, 0, 0);


    public void fireAllRulesForOneUser(User user) {
        List<Product> productList = new ArrayList<>();

        for (Product product : productList) {
            UserProductPair userProductPair = new UserProductPair(user, product);

            ketoDietIndicateRule.getFire().accept(userProductPair);
        }


    }

    public Map<Attribute, Object> getAttributesMap() {
        Map<Attribute, Object> attributeObjectMap = new HashMap<>();

        for (Attribute attribute : Attribute.values()) {
            attributeObjectMap.put(attribute, new Object());
        }

        return attributeObjectMap;
    }
}
