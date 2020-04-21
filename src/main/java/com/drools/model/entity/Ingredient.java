package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ingredient {

    @Id
    private Long id;

    private String name;

    private boolean rare;

//    @ManyToMany(mappedBy = "composition")
//    private List<Product> products;

    public Ingredient(Long id, String name) {
        this.id = id;
        this.name = name;
        this.rare = false;
    }
}
