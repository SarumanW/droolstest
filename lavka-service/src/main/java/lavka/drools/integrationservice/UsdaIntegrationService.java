package lavka.drools.integrationservice;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lavka.drools.model.entity.Category;
import lavka.drools.model.entity.NutritionFact;
import lavka.drools.model.entity.Product;
import lavka.drools.model.entity.RelationProductNutrition;
import lavka.drools.model.integration.usda.*;
import lavka.drools.repository.CategoryRepository;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private CategoryRepository categoryRepository;

    @Value("${usda.key}")
    private String apiKey;

    @Override
    public void importProductBase() {
        List<Integer> surveyFoods = this.getSurveyFoodsIds();

        List<UsdaFoodItem> detailedUsdaFoodItemsInfo = this.getDetailedFoodItemsInfo(surveyFoods);

        List<RelationProductNutrition> productNutritions = this.fillProductNutritionsList(detailedUsdaFoodItemsInfo);

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
        List<RelationProductNutrition> relationProductNutritionList = new ArrayList<>();

        for (UsdaFoodItem usdaFoodItem : detailedUsdaFoodItemsInfo) {
            Product product = new Product(usdaFoodItem.getFdcId(), usdaFoodItem.getFoodCode(), usdaFoodItem.getDescription());

            product.setComposition(usdaFoodItem.getInputFoods()
                    .stream()
                    .map(UsdaFoodIngredient::getFoodDescription)
                    .collect(Collectors.joining(",")));

            Map<String, String> productNutritions = new HashMap<>();
            for (UsdaFoodNutrient usdaFoodNutrient : usdaFoodItem.getUsdaFoodNutrients()) {
                switch (usdaFoodNutrient.getNutrient().getNumber()) {
                    case "203":
                        productNutritions.put(NutritionFact.NutritionFacts.PROTEIN.getCode(), String.valueOf(usdaFoodNutrient.getAmount()));
                        break;
                    case "204":
                        productNutritions.put(NutritionFact.NutritionFacts.FAT.getCode(), String.valueOf(usdaFoodNutrient.getAmount()));
                        break;
                    case "205":
                        productNutritions.put(NutritionFact.NutritionFacts.CARBS.getCode(), String.valueOf(usdaFoodNutrient.getAmount()));
                        break;
                    case "208":
                        productNutritions.put(NutritionFact.NutritionFacts.ENERGY.getCode(), String.valueOf(usdaFoodNutrient.getAmount()));
                        break;
                }
            }

            for (NutritionFact nutritionFact : NutritionFact.NutritionFacts.getNutritionFacts()) {
                String nutritionValue = productNutritions.get(nutritionFact.getId().toString());

                if (nutritionValue != null) {
                    relationProductNutritionList.add(new RelationProductNutrition(product, nutritionFact, nutritionValue));
                }
            }
        }

        return relationProductNutritionList;
    }

    private List<UsdaFoodItem> getDetailedFoodItemsInfo(List<Integer> foodItemsIds) {
        List<UsdaFoodItem> usdaFoodItems = new ArrayList<>();

        for (int i = 0; i <= foodItemsIds.size(); i += 20) {

            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods")
                    .setParameter("api_key", apiKey);

            UsdaFoodsSearchCriteria usdaFoodsSearchCriteria = new UsdaFoodsSearchCriteria(
                    foodItemsIds.subList(i, i + 20 >= foodItemsIds.size() ? foodItemsIds.size() : i + 20),
                    "full", MAIN_NUTRITION_IDS);

            usdaFoodItems.addAll(sendListPostHttpRequest(builder, usdaFoodsSearchCriteria, UsdaFoodItem.class));
        }

        return usdaFoodItems;
    }

    private List<Integer> getSurveyFoodsIds() {
        List<UsdaSimpleFoodItem> usdaSimpleFoodItems = new ArrayList<>();

        //TODO: make api call to be universal
        for (int i = 1; i <= 44; i++) {
            URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods/list")
                    .setParameter("dataType", "Survey (FNDDS)")
                    .setParameter("pageSize", String.valueOf(PAGE_SIZE))
                    .setParameter("pageNumber", String.valueOf(i))
                    .setParameter("api_key", apiKey);

            usdaSimpleFoodItems.addAll(sendListGetHttpRequest(builder, UsdaSimpleFoodItem.class));
        }

        return usdaSimpleFoodItems.stream()
                .map(i -> Integer.valueOf(i.getFdcId()))
                .collect(Collectors.toList());
    }
}
