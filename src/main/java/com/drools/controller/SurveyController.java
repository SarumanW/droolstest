package com.drools.controller;

import com.drools.model.entity.Diet;
import com.drools.model.entity.Ingredient;
import com.drools.model.entity.Product;
import com.drools.model.entity.User;
import com.drools.model.survey.Question;
import com.drools.model.survey.QuestionModel;
import com.drools.model.survey.SurveyResponse;
import com.drools.repository.DietRepository;
import com.drools.repository.IngredientRepository;
import com.drools.repository.ProductRepository;
import com.drools.repository.UserRepository;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/survey")
public class SurveyController {

    @Autowired
    private DietRepository dietRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KieSession session;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/getQuestions")
    public ResponseEntity<List<QuestionModel>> getQuestions() {
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

        return new ResponseEntity<>(List.of(diets, forbiddenProducts), HttpStatus.OK);
    }

    @PostMapping("/sendAnswers")
    public ResponseEntity<User> sendAnswers(@RequestBody SurveyResponse surveyResponse) {
        Iterable<Product> all = productRepository.findAll();

        for(Product product : all) {
            session.insert(product);
        }

        User user = userRepository.findById(surveyResponse.getUserId()).orElse(null);

        if (user != null) {
            FactHandle factHandle = session.getFactHandle(user);
            if (factHandle != null) {
                session.delete(factHandle);
            }

            surveyResponse.getQuestionAnswers().forEach((k, v) -> {
                if (k.equals(Question.DIETS_QUESTION.getText())) {

                    List<Diet> userDiets = new ArrayList<>();

                    for (String dietName : v) {
                        Diet.DietType.getDiets()
                                .stream()
                                .filter(d -> d.getName().equals(dietName))
                                .findFirst()
                                .ifPresent(userDiets::add);
                    }

                    user.getFollowedDiets().clear();
                    user.getFollowedDiets().addAll(userDiets);
                }
            });

            session.insert(user);
            session.fireAllRules();

            userRepository.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
