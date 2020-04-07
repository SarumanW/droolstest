package com.drools.repository;

import com.drools.model.entity.NutritionFact;
import org.springframework.data.repository.CrudRepository;

public interface NutritionFactRepository extends CrudRepository<NutritionFact, Long> {
}
