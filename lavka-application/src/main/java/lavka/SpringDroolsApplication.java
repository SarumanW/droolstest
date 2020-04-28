package lavka;

import lavka.drools.model.entity.Diet;
import lavka.drools.model.entity.NutritionFact;
import lavka.drools.repository.DietRepository;
import lavka.drools.repository.NutritionFactRepository;
import lavka.functionalmodel.RuleEngine;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SpringDroolsApplication {

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private NutritionFactRepository nutritionFactRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDroolsApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            dietRepository.saveAll(Diet.DietType.getDiets());
            nutritionFactRepository.saveAll(NutritionFact.NutritionFacts.getNutritionFacts());
        };
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RuleEngine ruleEngine() {
        return new RuleEngine();
    }
}

