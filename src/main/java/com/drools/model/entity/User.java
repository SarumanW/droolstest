package com.drools.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@SequenceGenerator(name = "user_seq", allocationSize = 1)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    private Long id;

    private String login;

    private String password;

    @ManyToMany
    @JoinTable(
            name = "user_item",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Item> forbiddenItems;

    @ManyToMany
    @JoinTable(
            name = "user_diet",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "diet_id"))
    private List<Diet> followedDiets;

    private boolean supportsSeasonality;

    @OneToMany(mappedBy = "user")
    @Where(clause = "shown = true")
    private Set<RelationUserProduct> userProducts;
}
