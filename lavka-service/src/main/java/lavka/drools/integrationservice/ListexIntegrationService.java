package lavka.drools.integrationservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lavka.drools.model.entity.Category;
import lavka.drools.model.entity.NutritionFact;
import lavka.drools.model.entity.Product;
import lavka.drools.model.entity.RelationProductNutrition;
import lavka.drools.model.integration.listex.ListexCategoriesResponse;
import lavka.drools.model.integration.listex.ListexCategory;
import lavka.drools.model.integration.listex.ListexProduct;
import lavka.drools.model.integration.listex.ListexProductAttribute;
import lavka.drools.repository.CategoryRepository;
import lavka.drools.repository.ProductNutritionRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@PropertySource("keys.properties")
public class ListexIntegrationService implements IntegrationService {

    private static final String SCHEME = "https";
    private static final String LISTEX_HOST = "api.listex.info/v3";
    private static final String GET_CATEGORIES_URL = "/categories";
    private static final String GET_PRODUCT_URL = "/product";

    private static final String API_KEY_PARAM = "apikey";
    private static final String PRODUCT_ID_PARAM = "good_id";

    private static final Long ROOT_CATEGORY_ID = 14001L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductNutritionRepository productNutritionRepository;

    private static Gson gson = new Gson();

    @Value("${listex.key}")
    private String apiKey;

    @Override
    public void importProductBase() {
        List<ListexProduct> productResponses = readProductsFromFile();

        List<RelationProductNutrition> relationProductNutritionList =
                parseResponseToRelationProductNutritionList(productResponses);

        productNutritionRepository.saveAll(relationProductNutritionList);
    }

    private List<ListexProduct> readProductsFromFile() {
        List<ListexProduct> productResponses = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(Paths.get("D:\\diploma\\listex_db"))) {

            List<Path> collect = walk
                    .filter(Files::isRegularFile)
                    .collect(Collectors.toList());

            for (Path filePaths : collect) {
                try (InputStream resource = Files.newInputStream(filePaths);
                     BufferedReader reader = new BufferedReader(
                             new InputStreamReader(resource))) {
                    String json = reader.lines()
                            .collect(Collectors.joining());

                    Type listexProducts = new TypeToken<ArrayList<ListexProduct>>() {
                    }.getType();

                    productResponses.addAll(gson.fromJson(json, listexProducts));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return productResponses.stream()
                .filter(distinctByKey(ListexProduct::getProductId))
                .collect(Collectors.toList());
    }

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private List<RelationProductNutrition> parseResponseToRelationProductNutritionList(List<ListexProduct> productResponses) {
        List<Category> categories = (List<Category>) categoryRepository.findAll();

        List<RelationProductNutrition> relationProductNutritionList = new ArrayList<>();

        for (ListexProduct listexProduct : productResponses) {
            List<ListexProductAttribute> attributes = listexProduct.getAttributes();

            Product product = new Product(listexProduct.getProductId());

            Map<String, String> productNutritions = new HashMap<>();

            for (ListexProductAttribute attribute : attributes) {
                switch (attribute.getAttributeId().toString()) {
                    case "71":
                        product.setExpirationMonths(attribute.getAttributeValue());
                        break;
                    case "3969":
                        product.setExpirationDays(attribute.getAttributeValue());
                        break;
                    case "2481":
                        product.setShortName(attribute.getAttributeValue());
                        break;
                    case "2479":
                        product.setName(attribute.getAttributeValue());
                        break;
                    case "2484":
                        product.setComposition(attribute.getAttributeValue());
                        break;
                    case "5":
                        productNutritions.put(NutritionFact.NutritionFacts.PROTEIN.getCode(), attribute.getAttributeValue());
                        break;
                    case "1":
                        productNutritions.put(NutritionFact.NutritionFacts.FAT.getCode(), attribute.getAttributeValue());
                        break;
                    case "6":
                        productNutritions.put(NutritionFact.NutritionFacts.CARBS.getCode(), attribute.getAttributeValue());
                        break;
                    case "34":
                        if (attribute.getAttributeValueType().equals("ккал/100г")) {
                            productNutritions.put(NutritionFact.NutritionFacts.ENERGY.getCode(), attribute.getAttributeValue());
                        }
                        break;
                }
            }

            ListexCategory listexProductCategory = listexProduct.getCategories().get(0);
            Category category = categories.stream()
                    .filter(c -> c.getCategoryNumber().equals(listexProductCategory.getCategoryId()))
                    .findAny().orElse(new Category());

            product.setCategoryId(category.getCategoryNumber());
            product.setFoodCode(Long.valueOf(category.getCategoryId().toString() + product.getId().toString()));
            product.setImagePath(listexProduct.getImagePath());

            for (NutritionFact nutritionFact : NutritionFact.NutritionFacts.getNutritionFacts()) {
                String nutritionValue = productNutritions.get(nutritionFact.getId().toString());

                if (nutritionValue != null) {
                    relationProductNutritionList.add(new RelationProductNutrition(product, nutritionFact, nutritionValue));
                }
            }
        }

        return relationProductNutritionList;
    }

    @Override
    public void importCategories() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(SCHEME).setHost(LISTEX_HOST).setPath(GET_CATEGORIES_URL)
                .setParameter(API_KEY_PARAM, apiKey);

        ListexCategoriesResponse categoriesResponse = sendGetHttpRequest(builder, ListexCategoriesResponse.class).get();

        List<ListexCategory> categoryList = categoriesResponse.getResult();

        List<Category> thirdLevelCategories = new ArrayList<>();

        long idCounter = 10;
        for (ListexCategory category : categoryList) {
            if (category.getParentCategoryId().equals(ROOT_CATEGORY_ID)) {
                thirdLevelCategories.add(new Category(category.getCategoryId(),
                        ++idCounter,
                        category.getCategoryName(),
                        null,
                        new ArrayList<>()));
            }
        }

        idCounter = 10;
        for (Category category : thirdLevelCategories) {
            for (ListexCategory listexCategory : categoryList) {
                if (listexCategory.getParentCategoryId().equals(category.getCategoryNumber())) {
                    category.getSubCategories().add(new Category(listexCategory.getCategoryId(),
                            Long.valueOf(category.getCategoryId().toString() + ++idCounter),
                            listexCategory.getCategoryName(),
                            category,
                            new ArrayList<>()));
                }
            }

            idCounter = 10;
        }

        categoryRepository.saveAll(thirdLevelCategories);
    }
}
