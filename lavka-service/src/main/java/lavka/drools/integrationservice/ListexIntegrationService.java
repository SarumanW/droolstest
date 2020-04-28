package lavka.drools.integrationservice;

import lavka.drools.model.entity.Category;
import lavka.drools.model.integration.listex.ListexCategory;
import lavka.drools.model.integration.listex.ListexResponse;
import lavka.drools.repository.CategoryRepository;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@PropertySource("keys.properties")
public class ListexIntegrationService implements IntegrationService {

    private static final String SCHEME = "https";
    private static final String LISTEX_HOST = "api.listex.info/v3";
    private static final String GET_CATEGORIES_URL = "/categories";
    private static final String API_KEY_PARAM = "apikey";

    private static final Long ROOT_CATEGORY_ID = 14001L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Value("${listex.key}")
    private String apiKey;

    @Override
    public void importProductBase() {

    }

    @Override
    public void importCategories() {
        URIBuilder builder = new URIBuilder();
        builder.setScheme(SCHEME).setHost(LISTEX_HOST).setPath(GET_CATEGORIES_URL)
                .setParameter(API_KEY_PARAM, apiKey);

        ListexResponse categoriesResponse = sendGetHttpRequest(builder, ListexResponse.class).get();

        List<ListexCategory> categoryList = categoriesResponse.getResult();

        List<Category> thirdLevelCategories = new ArrayList<>();

        long idCounter = 0;
        for (ListexCategory category : categoryList) {
            if (category.getParentCategoryId().equals(ROOT_CATEGORY_ID)) {
                thirdLevelCategories.add(new Category(category.getCategoryId(),
                        ++idCounter,
                        category.getCategoryName(),
                        null,
                        new ArrayList<>()));
            }
        }

        idCounter = 0;
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

            idCounter = 0;
        }

        categoryRepository.saveAll(thirdLevelCategories);
    }
}
