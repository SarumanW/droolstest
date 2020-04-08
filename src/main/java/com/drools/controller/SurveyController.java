package com.drools.controller;

import com.drools.model.entity.Diet;
import com.drools.model.entity.Ingredient;
import com.drools.model.survey.Question;
import com.drools.model.survey.QuestionModel;
import com.drools.repository.DietRepository;
import com.drools.repository.IngredientRepository;
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
    private IngredientRepository ingredientRepository;

    @GetMapping("/getQuestions")
    public List<QuestionModel> getQuestions() {
        QuestionModel diets = new QuestionModel(Question.DIETS_QUESTION.getText(),
                ((List<Diet>) dietRepository.findAll())
                        .stream()
                        .map(Diet::getName)
                        .collect(Collectors.toList()));

        QuestionModel forbiddenProducts = new QuestionModel(Question.FORBIDDEN_PRODUCTS_QUESTION.getText(),
                ingredientRepository.findAllByRareIsTrue()
                        .stream()
                        .map(Ingredient::getName)
                        .collect(Collectors.toList()));

        return List.of(diets, forbiddenProducts);
    }
}
