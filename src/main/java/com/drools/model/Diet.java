package com.drools.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Diet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Diet(String name) {
        this.name = name;
    }

    //TODO: Add diet explanation


    public enum DietType {

        FAT_LOSS("Втрачаю вагу"),
        PALEO("Палео дієта"),
        MEDITERRANEAN("Середземнороська дієта"),
        KETO("Кето дієта"),
        DIABETES("Діабетик"),
        VEGAN("Веган"),
        VEGETERIAN("Вегетаріанець"),
        PESKATARIAN("Пескатаріанець"),
        GLUTEN_FREE("Їжа без вмісту глютену"),
        DAIRY_FREE("Уникаю молочних продуктів"),
        LOW_SODIUM("Їжа з низьким вмістом солі");

        private String dietName;

        DietType(String dietName) {
            this.dietName = dietName;
        }

        public String getDietName() {
            return dietName;
        }

        public static List<Diet> getDiets() {
            return Arrays.stream(DietType.values())
                    .map(dietName -> new Diet(dietName.getDietName()))
                    .collect(Collectors.toList());
        }
    }
}

