package responsemodel;

import lavka.drools.model.entity.Product;
import lavka.drools.model.entity.RelationProductNutrition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String imagePath;

    private Long categoryId;

    private String name;

    private String shortName;

    private String expiration;

    private String composition;

    private Map<String, String> nutritionFacts;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.imagePath = product.getImagePath();
        this.name = product.getName();
        this.shortName = product.getShortName();

        if (product.getExpirationMonths() != null) {
            this.expiration = product.getExpirationMonths() + " m";
        } else if (product.getExpirationDays() != null) {
            this.expiration = product.getExpirationDays() + " d";
        } else {
            this.expiration = "-";
        }

        this.composition = product.getComposition();
        this.categoryId = product.getFoodCode();

        this.nutritionFacts = new HashMap<>();
        for (RelationProductNutrition relationProductNutrition : product.getNutritionFacts()) {
            this.nutritionFacts.put(relationProductNutrition.getNutritionFact().getName(), relationProductNutrition.getValue());
        }
    }
}
