package lavka.drools.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    private Long id;

    private Long foodCode;

    private String name;

    @ManyToMany
    @JoinTable(
            name = "product_ingredient",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id"))
    private List<Ingredient> composition = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<RelationProductNutrition> nutritionFacts = new ArrayList<>();

    public Product(String id, String foodCode, String name) {
        this.id = Long.valueOf(id);
        this.foodCode = Long.valueOf(foodCode);
        this.name = name;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFoodCode() {
        return foodCode;
    }

    public void setFoodCode(Long foodCode) {
        this.foodCode = foodCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getComposition() {
        return composition;
    }

    public void setComposition(List<Ingredient> composition) {
        this.composition = composition;
    }

    public List<RelationProductNutrition> getNutritionFacts() {
        return nutritionFacts;
    }

    public void setNutritionFacts(List<RelationProductNutrition> nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }
}
