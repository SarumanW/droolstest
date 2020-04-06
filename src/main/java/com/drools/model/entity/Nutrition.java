package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "nutrition_seq", allocationSize = 1)
public class Nutrition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nutrition_seq")
    private Long id;

    private double calories;

    private double protein;

    private double carbs;

    private double fat;

    private double sugar;

}
