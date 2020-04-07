package com.drools.controller;

import com.drools.model.survey.Question;
import com.drools.model.survey.QuestionModel;
import com.drools.model.entity.Diet;
import com.drools.model.entity.Product;
import com.drools.repository.DietRepository;
import com.drools.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/getQuestions")
    public List<QuestionModel> getQuestions() {
        QuestionModel diets = new QuestionModel(Question.DIETS_QUESTION.getText(),
                ((List<Diet>) dietRepository.findAll())
                        .stream()
                        .map(Diet::getName)
                        .collect(Collectors.toList()));

        QuestionModel forbiddenProducts = new QuestionModel(Question.FORBIDDEN_PRODUCTS_QUESTION.getText(),
                productRepository.findAllByRareIsTrue()
                        .stream()
                        .map(Product::getName)
                        .collect(Collectors.toList()));

        return List.of(diets, forbiddenProducts);
    }
}
