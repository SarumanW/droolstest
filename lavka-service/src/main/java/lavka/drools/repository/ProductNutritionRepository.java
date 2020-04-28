package lavka.drools.repository;

import lavka.drools.model.entity.RelationProductNutrition;
import org.springframework.data.repository.CrudRepository;

public interface ProductNutritionRepository extends CrudRepository<RelationProductNutrition, Long> {
}
