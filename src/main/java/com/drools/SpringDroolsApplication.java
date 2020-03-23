package com.drools;

import com.drools.model.entity.Diet;
import com.drools.repository.DietRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDroolsApplication {

    @Autowired
    private DietRepository dietRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDroolsApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            dietRepository.saveAll(Diet.DietType.getDiets());
        };
    }
}

