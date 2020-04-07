package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    }

}
