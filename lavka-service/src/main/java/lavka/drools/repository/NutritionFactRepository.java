package lavka.drools.repository;

import lavka.drools.model.entity.NutritionFact;
import org.springframework.data.repository.CrudRepository;

public interface NutritionFactRepository extends CrudRepository<NutritionFact, Long> {
}
