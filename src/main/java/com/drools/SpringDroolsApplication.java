package com.drools;

import com.drools.model.entity.Diet;
import com.drools.model.entity.NutritionFact;
import com.drools.repository.CategoryRepository;
import com.drools.repository.DietRepository;
import com.drools.repository.NutritionFactRepository;
import com.drools.utils.CategoriesImport;
import com.functionalmodel.RuleEngine;
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

    @Autowired
    private CategoryRepository categoryRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDroolsApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            dietRepository.saveAll(Diet.DietType.getDiets());
            nutritionFactRepository.saveAll(NutritionFact.NutritionFacts.getNutritionFacts());
            categoryRepository.saveAll(CategoriesImport.retrieveCategoriesList());
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

