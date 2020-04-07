package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "prod_nutr_seq", allocationSize = 1)
public class RelationProductNutrition {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prod_nutr_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "nutrition_fact_id")
    private NutritionFact nutritionFact;

    private double value;
}
