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
//@SequenceGenerator(name = "product_seq", allocationSize = 1)
public class Product {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_item",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Product> composition;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "product_item",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    //@Transient
    private List<Product> isInProduct;

    private String name;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<RelationProductNutrition> nutritionFacts;

    private boolean rare;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    public Product(String name, List<Product> composition) {
        this.name = name;
        this.composition = composition;
    }

    public Product(String id, String name, ProductType productType) {
        this.id = Long.valueOf(id);
        this.name = name;
        this.productType = productType;
    }

    public enum ProductType {
        FOUNDATION, BRANDED
    }
}
