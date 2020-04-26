package com.functionalmodel;

import com.drools.model.entity.Diet;
import com.drools.model.entity.Ingredient;

import java.util.List;
import java.util.function.Consumer;

public enum Operation {
    CHECK_CARBS(userProductPair -> {
        System.out.println("carbs");

        Diet.DietType diet = (Diet.DietType) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aDIE);

        if (diet.equals(Diet.DietType.KETO)) {

        }
    }),

    CHECK_CATEGORIES(userProductPair -> {
        System.out.println("check");

        List<Long> restrictedCategoriesId = (List<Long>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aRC);

        Long categoryId = userProductPair.getProduct().getFoodCode();

        boolean isAllowed = true;

        for (Long restrictedCategoryId : restrictedCategoriesId) {
            if (String.valueOf(categoryId).startsWith(String.valueOf(restrictedCategoryId))) {
                isAllowed = false;
            }
        }

        if (isAllowed) {
            userProductPair.getUser().addProductToList(userProductPair.getProduct());
        }

    }),

    CHECK_WORDS(userProductPair -> {
        System.out.println("check words");

        List<String> restrictedWords = (List<String>) userProductPair.getUser().getAttributeObjectMap().get(Attribute.aRW);

        boolean isAllowed = true;

        for (String restrictedItem : restrictedWords) {
            if (userProductPair.getProduct().getName().toLowerCase().contains(restrictedItem.toLowerCase())) {
                isAllowed = false;
            }

            for (Ingredient ingredient : userProductPair.getProduct().getComposition()) {
                if (ingredient.getName().toLowerCase().contains(restrictedItem.toLowerCase())) {
                    isAllowed = false;
                }
            }
        }

        if (isAllowed) {
            userProductPair.getUser().addProductToList(userProductPair.getProduct());
        }
    });

    private Consumer<UserProductPair> consumerFunction;

    Operation(Consumer<UserProductPair> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    public Consumer<UserProductPair> getConsumerFunction() {
        return consumerFunction;
    }
}
