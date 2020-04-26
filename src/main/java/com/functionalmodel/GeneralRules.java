package com.functionalmodel;

import com.drools.model.entity.Diet;
import com.drools.model.entity.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GeneralRules {
    private static Consumer<UserProductPair> productCharsPredicate = userProductPair -> {
        if (userProductPair.getProduct() != null) {
            userProductPair.getUser()
                    .getAttributeObjectMap().put(Attribute.aPR, userProductPair.getProduct());
        }
    };

    private static Consumer<UserProductPair> productCharsAssertion = userProductPair -> {
        Product product = (Product) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aPR);

        if (product != null) {
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPC, product.getFoodCode());
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPN, product.getName());
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPI, product.getComposition());
            userProductPair.getUser().getAttributeObjectMap().put(Attribute.aPNU, product.getNutritionFacts());
        }
    };

    private static Rule<UserProductPair> productCharsRule = new Rule(3, 3,
            productCharsPredicate, productCharsAssertion, 4, 4);

    //------

    private static Consumer<UserProductPair> carbsOperationPredicate = userProductPair -> {

    };

    private static Consumer<UserProductPair> carbsOperationAssertion = userProductPair -> {
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

    private static Rule<UserProductPair> carbsOperationsRule = new Rule(4, 4,
            carbsOperationPredicate, carbsOperationAssertion, 4, 5);

    //------

    private static Consumer<UserProductPair> categoriesOperationPredicate = userProductPair -> {

    };

    private static Consumer<UserProductPair> categoriesOperationAssertion = userProductPair -> {
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

    private static Rule<UserProductPair> categoriesOperationRule = new Rule(4, 5,
            categoriesOperationPredicate, categoriesOperationAssertion, 4, 6);


    //------

    private static Consumer<UserProductPair> wordsOperationPredicate = userProductPair -> {

    };

    private static Consumer<UserProductPair> wordsOperationAssertion = userProductPair -> {
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

    private static Rule<UserProductPair> wordsOperationsRule = new Rule(4, 6,
            wordsOperationPredicate, wordsOperationAssertion, 5, 7);

    //------

    private static Consumer<UserProductPair> operationPredicate = userProductPair -> {

    };

    private static Consumer<UserProductPair> operationAssertion = userProductPair -> {
        List<Operation> operationList = (List<Operation>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aOP);

        for (Operation operation : operationList) {
            operation.getConsumerFunction().accept(userProductPair);
        }
    };

    private static Rule<UserProductPair> operationsRule = new Rule(5, 7,
            operationPredicate, operationAssertion, 0, 0);


    public static List<Rule> getRules() {
        return List.of(productCharsRule, carbsOperationsRule, categoriesOperationRule, wordsOperationsRule, operationsRule);
    }
}
