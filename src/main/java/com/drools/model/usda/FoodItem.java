package com.drools.model.usda;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FoodItem {

    private String fdcId;

    private String description;

    private String foodCode;

    private List<FoodNutrient> foodNutrients;

    private List<FoodIngredient> inputFoods;
}
