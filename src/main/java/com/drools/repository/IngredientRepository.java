package com.drools.repository;

import com.drools.model.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    public List<Ingredient> findAllByRareIsTrue();
}
