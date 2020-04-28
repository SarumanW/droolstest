package lavka.drools.model.integration.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsdaFoodItem {

    private String fdcId;

    private String description;

    private String foodCode;

    private List<UsdaFoodNutrient> usdaFoodNutrients;

    private List<UsdaFoodIngredient> inputFoods;
}
