package com.drools.controller;

import com.drools.model.entity.*;
import com.drools.model.usda.FoodIngredient;
import com.drools.model.usda.FoodItem;
import com.drools.model.usda.FoodsSearchCriteria;
import com.drools.model.usda.SimpleFoodItem;
import com.drools.repository.IngredientRepository;
import com.drools.repository.ProductNutritionRepository;
import com.drools.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.functionalmodel.RuleEngine;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usda")
public class UsdaController {

    private static final int PAGE_SIZE = 200;
    private static final Integer[] MAIN_NUTRITION_IDS = {203, 204, 205, 208};

    @Autowired
    private ProductNutritionRepository productNutritionRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RuleEngine ruleEngine;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/test/{userId}")
    public User test(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElse(new User());

        ruleEngine.fireAllRulesForOneUser(user);

        return user;
    }

    @GetMapping("/foods")
    public List<FoodItem> getFoodsList() {

        List<Integer> surveyFoods = this.getSurveyFoodsIds();

        List<FoodItem> detailedFoodItemsInfo = this.getDetailedFoodItemsInfo(surveyFoods);

        List<RelationProductNutrition> productNutritions = this.fillProductNutritionsList(detailedFoodItemsInfo);

        List<Ingredient> distinctIngredients = productNutritions.stream()
                .map(RelationProductNutrition::getProduct)
                .flatMap(product -> product.getComposition().stream())
                .distinct()
                .collect(Collectors.toList());
        ingredientRepository.saveAll(distinctIngredients);

        productNutritionRepository.saveAll(productNutritions);

        return detailedFoodItemsInfo;
    }

    private List<RelationProductNutrition> fillProductNutritionsList(List<FoodItem> detailedFoodItemsInfo) {
        List<RelationProductNutrition> productNutritions = new ArrayList<>();
        for (FoodItem foodItem : detailedFoodItemsInfo) {
            Product product = new Product(foodItem.getFdcId(), foodItem.getFoodCode(), foodItem.getDescription());

            for (FoodIngredient foodIngredient : foodItem.getInputFoods()) {
                product.getComposition().add(new Ingredient(foodIngredient.getIngredientCode(),
                        foodIngredient.getFoodDescription()));
            }

            for (NutritionFact nutritionFact : NutritionFact.NutritionFacts.getNutritionFacts()) {
                foodItem.getFoodNutrients()
                        .stream()
                        .filter(n -> n.getNutrient().getNumber().equals(nutritionFact.getId().toString()))
                        .findFirst()
                        .ifPresent(foodNutrient ->
                                productNutritions.add(new RelationProductNutrition(product, nutritionFact,
                                        foodNutrient.getAmount())));

            }
        }

        return productNutritions;
    }

    private List<FoodItem> getDetailedFoodItemsInfo(List<Integer> foodItemsIds) {
        List<FoodItem> foodItems = new ArrayList<>();

        try {
            for (int i = 0; i <= foodItemsIds.size(); i += 20) {
                HttpClient client = HttpClient.newHttpClient();

                URIBuilder builder = new URIBuilder();
                builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods")
                        .setParameter("api_key", "8ym093tKGYh1r2iecUggVFyU1zveNHcr9eRE6yWZ");
                URI uri = builder.build();

                FoodsSearchCriteria foodsSearchCriteria = new FoodsSearchCriteria(
                        foodItemsIds.subList(i, i + 20 >= foodItemsIds.size() ? foodItemsIds.size() : i + 20),
                        "full", MAIN_NUTRITION_IDS);
                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(foodsSearchCriteria);

                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json")
                        .uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                String res = client.send(request,
                        HttpResponse.BodyHandlers.ofString())
                        .body();
                ObjectMapper mapper = new ObjectMapper();

                foodItems.addAll(mapper.readValue(res, new TypeReference<>() {
                }));
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return foodItems;
    }

    private List<Integer> getSurveyFoodsIds() {
        List<SimpleFoodItem> simpleFoodItems = new ArrayList<>();

        try {
            //TODO: make api call to be universal
            for (int i = 1; i <= 44; i++) {
                HttpClient client = HttpClient.newHttpClient();

                URIBuilder builder = new URIBuilder();
                builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods/list")
                        .setParameter("dataType", "Survey (FNDDS)")
                        .setParameter("pageSize", String.valueOf(PAGE_SIZE))
                        .setParameter("pageNumber", String.valueOf(i))
                        .setParameter("api_key", "8ym093tKGYh1r2iecUggVFyU1zveNHcr9eRE6yWZ");
                URI uri = builder.build();

                HttpRequest request = HttpRequest.newBuilder()
                        .uri(uri)
                        .GET()
                        .build();

                String res = client.send(request,
                        HttpResponse.BodyHandlers.ofString())
                        .body();
                ObjectMapper mapper = new ObjectMapper();

                simpleFoodItems.addAll(mapper.readValue(res, new TypeReference<>() {
                }));
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return simpleFoodItems.stream()
                .map(i -> Integer.valueOf(i.getFdcId()))
                .collect(Collectors.toList());
    }
}
