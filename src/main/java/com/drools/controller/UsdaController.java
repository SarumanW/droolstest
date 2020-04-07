package com.drools.controller;

import com.drools.model.entity.NutritionFact;
import com.drools.model.entity.Product;
import com.drools.model.entity.RelationProductNutrition;
import com.drools.model.usda.FoodItem;
import com.drools.repository.ProductNutritionRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @Autowired
    private ProductNutritionRepository productNutritionRepository;

    @GetMapping("/foods")
    public List<String> getFoodsList() throws IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods/list")
                .setParameter("dataType", "Foundation")
                .setParameter("pageSize", "200")
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

        List<FoodItem> foodItemList = mapper.readValue(res, new TypeReference<>() {
        });

        List<RelationProductNutrition> productNutritions = new ArrayList<>();

        for (FoodItem foodItem : foodItemList) {
            Product product = new Product(foodItem.getFdcId(), foodItem.getDescription(),
                    Product.ProductType.FOUNDATION);

            for (NutritionFact nutritionFact : NutritionFact.NutritionFacts.getNutritionFacts()) {
                foodItem.getFoodNutrients()
                        .stream()
                        .filter(n -> n.getNumber().equals(nutritionFact.getId().toString()))
                        .findFirst()
                        .ifPresent(foodNutrient ->
                                productNutritions.add(new RelationProductNutrition(product, nutritionFact,
                                        Double.valueOf(foodNutrient.getAmount()))));

            }
        }

        productNutritionRepository.saveAll(productNutritions);

        return foodItemList.stream()
                .map(FoodItem::getDescription)
                .collect(Collectors.toList());
    }
}
