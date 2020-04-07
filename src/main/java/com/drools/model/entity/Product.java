package com.drools.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "product_seq", allocationSize = 1)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "product_item",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Product> composition;

    @ManyToMany
    @JoinTable(
            name = "product_item",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    @Transient
    private List<Product> isInProduct;

    private String name;

    private String nameTranslated;

    @OneToMany(mappedBy = "product")
    private List<RelationProductNutrition> nutritionFacts;

    private boolean rare;

    public Product(String name, List<Product> composition) {
        this.name = name;
        this.composition = composition;
    }

    public enum ProductType {
        FOUNDATION, BRANDED
    }
}
