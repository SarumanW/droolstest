package lavka.drools.service;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lavka.drools.model.entity.*;
import lavka.drools.model.integration.usda.UsdaFoodIngredient;
import lavka.drools.model.integration.usda.UsdaFoodItem;
import lavka.drools.model.integration.usda.UsdaFoodsSearchCriteria;
import lavka.drools.model.integration.usda.UsdaSimpleFoodItem;
import lavka.drools.repository.CategoryRepository;
import lavka.drools.repository.IngredientRepository;
import lavka.drools.repository.ProductNutritionRepository;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@PropertySource("keys.properties")
public class UsdaIntegrationService implements IntegrationService {

    private static final int PAGE_SIZE = 200;
    private static final Integer[] MAIN_NUTRITION_IDS = {203, 204, 205, 208};
    private static Gson gson = new Gson();

    @Autowired
    private ProductNutritionRepository productNutritionRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${usda.key}")
    private String apiKey;

    @Override
    public void importProductBase() {
        List<Integer> surveyFoods = this.getSurveyFoodsIds();

        List<UsdaFoodItem> detailedUsdaFoodItemsInfo = this.getDetailedFoodItemsInfo(surveyFoods);

        List<RelationProductNutrition> productNutritions = this.fillProductNutritionsList(detailedUsdaFoodItemsInfo);

        List<Ingredient> distinctIngredients = productNutritions.stream()
                .map(RelationProductNutrition::getProduct)
                .flatMap(product -> product.getComposition().stream())
                .distinct()
                .collect(Collectors.toList());
        ingredientRepository.saveAll(distinctIngredients);

        productNutritionRepository.saveAll(productNutritions);
    }

    @Override
    public void importCategories() {
        List<Category> categories = new ArrayList<>();

        try (InputStream resource = new ClassPathResource(
                "files/categories.json").getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(resource))) {
            String json = reader.lines()
                    .collect(Collectors.joining());

            Type categoryType = new TypeToken<ArrayList<Category>>() {
            }.getType();

            categories = gson.fromJson(json, categoryType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loopCategories(categories, null);

        categoryRepository.saveAll(categories);
    }

    private static void loopCategories(List<Category> categories, Category parentCategory) {
        for (Category category : categories) {
            category.setParentCategory(parentCategory);

            if (category.getSubCategories().size() > 0) {
                loopCategories(category.getSubCategories(), category);
            }
        }
    }

    private List<RelationProductNutrition> fillProductNutritionsList(List<UsdaFoodItem> detailedUsdaFoodItemsInfo) {
        List<RelationProductNutrition> productNutritions = new ArrayList<>();
        for (UsdaFoodItem usdaFoodItem : detailedUsdaFoodItemsInfo) {
            Product product = new Product(usdaFoodItem.getFdcId(), usdaFoodItem.getFoodCode(), usdaFoodItem.getDescription());

            for (UsdaFoodIngredient usdaFoodIngredient : usdaFoodItem.getInputFoods()) {
                product.getComposition().add(new Ingredient(usdaFoodIngredient.getIngredientCode(),
                        usdaFoodIngredient.getFoodDescription()));
            }

            for (NutritionFact nutritionFact : NutritionFact.NutritionFacts.getNutritionFacts()) {
                usdaFoodItem.getUsdaFoodNutrients()
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

    private List<UsdaFoodItem> getDetailedFoodItemsInfo(List<Integer> foodItemsIds) {
        List<UsdaFoodItem> usdaFoodItems = new ArrayList<>();

        try {
            for (int i = 0; i <= foodItemsIds.size(); i += 20) {
                HttpClient client = HttpClient.newHttpClient();

                URIBuilder builder = new URIBuilder();
                builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods")
                        .setParameter("api_key", "8ym093tKGYh1r2iecUggVFyU1zveNHcr9eRE6yWZ");
                URI uri = builder.build();

                UsdaFoodsSearchCriteria usdaFoodsSearchCriteria = new UsdaFoodsSearchCriteria(
                        foodItemsIds.subList(i, i + 20 >= foodItemsIds.size() ? foodItemsIds.size() : i + 20),
                        "full", MAIN_NUTRITION_IDS);
                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(usdaFoodsSearchCriteria);

                HttpRequest request = HttpRequest.newBuilder()
                        .header("Content-Type", "application/json")
                        .uri(uri)
                        .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                        .build();

                String res = client.send(request,
                        HttpResponse.BodyHandlers.ofString())
                        .body();
                ObjectMapper mapper = new ObjectMapper();

                usdaFoodItems.addAll(mapper.readValue(res, new TypeReference<>() {
                }));
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return usdaFoodItems;
    }

    private List<Integer> getSurveyFoodsIds() {
        List<UsdaSimpleFoodItem> usdaSimpleFoodItems = new ArrayList<>();

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

                usdaSimpleFoodItems.addAll(mapper.readValue(res, new TypeReference<>() {
                }));
            }
        } catch (URISyntaxException | InterruptedException | IOException e) {
            e.printStackTrace();
        }

        return usdaSimpleFoodItems.stream()
                .map(i -> Integer.valueOf(i.getFdcId()))
                .collect(Collectors.toList());
    }
}
