package com.drools.controller;

import com.drools.model.Question;
import com.drools.model.QuestionModel;
import com.drools.model.entity.Diet;
import com.drools.model.entity.Item;
import com.drools.repository.DietRepository;
import com.drools.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping("/getQuestions")
    public List<QuestionModel> getQuestions() {
        QuestionModel diets = new QuestionModel(Question.DIETS_QUESTION.getText(),
                ((List<Diet>) dietRepository.findAll())
                        .stream()
                        .map(Diet::getName)
                        .collect(Collectors.toList()));

        QuestionModel forbiddenProducts = new QuestionModel(Question.FORBIDDEN_PRODUCTS_QUESTION.getText(),
                itemRepository.findAllByRareIsTrue()
                        .stream()
                        .map(Item::getName)
                        .collect(Collectors.toList()));

        return List.of(diets, forbiddenProducts);
    }
}
