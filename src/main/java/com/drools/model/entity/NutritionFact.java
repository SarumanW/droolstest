package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NutritionFact {

    @Id
    private Long id;

    private String name;

    public enum NutritionFacts {
        PROTEIN("203", "Protein"),
        FAT("204", "Fat"),
        CARBS("205", "Carbs"),
        ENERGY("208", "Energy");

        private String code;
        private String name;

        NutritionFacts(String code, String name) {
            this.code = code;
            this.name = name;
        }

        public static List<NutritionFact> getNutritionFacts() {
            return Arrays.stream(NutritionFacts.values())
                    .map(facts -> new NutritionFact(Long.valueOf(facts.code), facts.name))
                    .collect(Collectors.toList());
        }
    }

}
