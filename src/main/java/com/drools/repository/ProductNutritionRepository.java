package com.drools.repository;

import com.drools.model.entity.RelationProductNutrition;
import org.springframework.data.repository.CrudRepository;

public interface ProductNutritionRepository extends CrudRepository<RelationProductNutrition, Long> {
}
