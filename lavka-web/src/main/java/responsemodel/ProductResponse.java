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
        this.expiration = product.getExpirationMonths() + " m " + product.getExpirationDays() + " d";
        this.composition = product.getComposition();

        this.nutritionFacts = new HashMap<>();
        for (RelationProductNutrition relationProductNutrition : product.getNutritionFacts()) {
            this.nutritionFacts.put(relationProductNutrition.getNutritionFact().getName(), relationProductNutrition.getValue());
        }
    }
}
