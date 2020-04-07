package com.drools.controller;

import com.drools.model.usda.FoodItem;
import com.drools.utils.Translator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/usda")
public class UsdaController {

    @GetMapping("/foods")
    public List<String> getFoodsList() throws IOException, InterruptedException, URISyntaxException {

        HttpClient client = HttpClient.newHttpClient();

        URIBuilder builder = new URIBuilder();
        builder.setScheme("https").setHost("api.nal.usda.gov").setPath("/fdc/v1/foods/list")
                .setParameter("dataType", "Foundation")
                .setParameter("pageSize", "200")
                .setParameter("api_key", "8ym093tKGYh1r2iecUggVFyU1zveNHcr9eRE6yWZ");
        URI uri = builder.build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        String res = client.send(request,
                HttpResponse.BodyHandlers.ofString())
                .body();
        ObjectMapper mapper = new ObjectMapper();

        List<FoodItem> asList = mapper.readValue(res, new TypeReference<>() {
        });

        String collectString = asList.stream()
                .map(FoodItem::getDescription)
                .collect(Collectors.joining("/"));

        String[] foodItemsNamesTranslated =
                Translator.translate("en", "ru", collectString)
                .split("/");




        return asList.stream()
                //.map(FoodItem::getDescription)
                .map(i -> Translator.translate("en", "ru", i.getDescription()))
                .collect(Collectors.toList());
    }
}

//TODO: map fooditem to entity food item and save to database with two languages included
