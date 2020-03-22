package com.drools;

import com.drools.model.Diet;
import com.drools.model.Question;
import com.drools.repository.DietRepository;
import com.drools.repository.QuestionRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringDroolsApplication {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private DietRepository dietRepository;

    public static void main(String[] args) {
        SpringApplication.run(SpringDroolsApplication.class, args);
    }

    @Bean
    InitializingBean sendDatabase() {
        return () -> {
            List<Question> questions = new ArrayList<>();

            questions.add(new Question(1L, "Оберіть пункти, які найкраще вас описують"));
            questions.add(new Question(2L, "Оберіть продукти, яких ви намагаєтесь уникати"));

            questionRepository.saveAll(questions);

            dietRepository.saveAll(Diet.DietType.getDiets());
        };
    }
}

