package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "user_ingr_seq", allocationSize = 1)
public class RelationUserIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_ingr_seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean forbidden;
}
