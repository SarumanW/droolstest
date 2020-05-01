package lavka.drools.model.entity;

import com.sun.istack.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id
    private Long id;

    private Long foodCode;

    @Nullable
    private Long categoryId;

    @Nullable
    private String imagePath;

    private String name;

    @Nullable
    private String shortName;

    @Nullable
    private String expirationMonths;

    @Nullable
    private String expirationDays;

    @Lob
    private String composition;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<RelationProductNutrition> nutritionFacts = new ArrayList<>();

    public Product(String id, String foodCode, String name) {
        this.id = Long.valueOf(id);
        this.foodCode = Long.valueOf(foodCode);
        this.name = name;
    }

    public Product(Long id) {
        this.id = id;
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

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public List<RelationProductNutrition> getNutritionFacts() {
        return nutritionFacts;
    }

    public void setNutritionFacts(List<RelationProductNutrition> nutritionFacts) {
        this.nutritionFacts = nutritionFacts;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getExpirationMonths() {
        return expirationMonths;
    }

    public void setExpirationMonths(String expirationMonths) {
        this.expirationMonths = expirationMonths;
    }

    public String getExpirationDays() {
        return expirationDays;
    }

    public void setExpirationDays(String expirationDays) {
        this.expirationDays = expirationDays;
    }
}
