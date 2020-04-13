package com.drools.utils;

import com.drools.model.entity.Category;
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

public class CategoriesImport {
    private static Gson gson = new Gson();

    public static List<Category> retrieveCategoriesList() {
        List<Category> categories = new ArrayList<>();

        try (InputStream resource = new ClassPathResource(
                "files/categories.json").getInputStream();
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(resource))) {
            String json = reader.lines()
                    .collect(Collectors.joining());

            Type categoryType = new TypeToken<ArrayList<Category>>() {
            }.getType();

            categories = gson.fromJson(json, categoryType);
        } catch (IOException e) {
            e.printStackTrace();
        }

        loopCategories(categories, null);

        return categories;
    }

    private static void loopCategories(List<Category> categories, Category parentCategory) {
        for (Category category : categories) {
            category.setParentCategory(parentCategory);

            if (category.getSubCategories().size() > 0) {
                loopCategories(category.getSubCategories(), category);
            }
        }
    }
}
