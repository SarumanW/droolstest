package com.drools.model.entity;

import com.drools.utils.RestrictionImport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "diet_seq", allocationSize = 1)
public class Diet {

    @Id
    private Long id;

    private String name;

    //TODO: Add diet explanation

    public enum DietType {

        FAT_LOSS(1, "Втрачаю вагу"),
        PALEO(2, "Палео дієта"),
        MEDITERRANEAN(3, "Середземнороська дієта"),
        KETO(4, "Кето дієта"),
        DIABETES(5, "Діабетик"),
        VEGAN(6, "Веган"),
        VEGETERIAN(7, "Вегетаріанець"),
        PESKATARIAN(8, "Пескатаріанець"),
        GLUTEN_FREE(9, "Їжа без вмісту глютену"),
        DAIRY_FREE(10, "Уникаю молочних продуктів"),
        LOW_SODIUM(11, "Їжа з низьким вмістом солі");

        private int id;

        private String dietName;

        private DietRestrictionModel dietRestrictionModel;

        DietType(int id, String dietName) {
            this.id = id;
            this.dietName = dietName;

            //get details from file by diet id
            this.dietRestrictionModel = RestrictionImport.getRestrictionsByDietId(id);
        }

        public String getDietName() {
            return dietName;
        }

        public int getId() {
            return id;
        }

        public DietRestrictionModel getDietRestrictionModel() {
            return dietRestrictionModel;
        }

        public static List<Diet> getDiets() {
            return Arrays.stream(DietType.values())
                    .map(diet -> new Diet((long) diet.getId(), diet.getDietName()))
                    .collect(Collectors.toList());
        }
    }
}

