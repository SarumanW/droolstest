package com.functionalmodel;

import java.util.function.Consumer;

public enum Operation {
    CHECK_CARBS(userProductPair -> {
        System.out.println("carbs");
    }),

    CHECK_CATEGORIES(userProductPair -> {
        System.out.println("check");
    }),

    CHECK_WORDS(userProductPair -> {

    });

    private Consumer<UserProductPair> consumerFunction;

    private Operation(Consumer<UserProductPair> consumerFunction) {
        this.consumerFunction = consumerFunction;
    }

    public Consumer<UserProductPair> getConsumerFunction() {
        return consumerFunction;
    }
}
