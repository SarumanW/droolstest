package com.drools.utils;

import com.drools.model.entity.DietRestrictionModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestrictionImport {
    private static Gson gson = new Gson();

    private static List<DietRestrictionModel> restrictions = new ArrayList<>();

    public static DietRestrictionModel getRestrictionsByDietId(int id) {
        if (restrictions.isEmpty()) {
            try (InputStream resource = new ClassPathResource(
                    "files/diets_restrictions.json").getInputStream();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(resource))) {
                String json = reader.lines()
                        .collect(Collectors.joining());

                Type dietRestrictionType = new TypeToken<ArrayList<DietRestrictionModel>>() {
                }.getType();

                restrictions.addAll(gson.fromJson(json, dietRestrictionType));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return restrictions.stream()
                .filter(dietRestrictionModel -> dietRestrictionModel.getDietId() == id)
                .findFirst()
                .orElse(null);
    }
}
